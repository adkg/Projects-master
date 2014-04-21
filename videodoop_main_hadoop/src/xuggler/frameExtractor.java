package xuggler;


import imageAnalysis.Initializer;

import java.awt.image.BufferedImage;
import java.util.Random;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import elasticSearch.indexGeneration;

public class frameExtractor {

	public void Extractor(IContainer container,String filename)
	{		  
        IStreamCoder videoCoder = null;		
		int numstreams = container.getNumStreams();
		int videostreamid=-1;
		for(int i=0;i<numstreams;i++)
		{
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
			{
				videoCoder = coder;
				videostreamid = i;
				break;
			}			
		}
		
		if(videostreamid==-1)throw new RuntimeException("No video stream");		
		if(videoCoder.open()<0)throw new RuntimeException("could not open video coder");
		
		IVideoResampler resampler = null;		
		
		if(videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
		{
			resampler = IVideoResampler.make(Initializer.IMAGE_WIDTH, Initializer.IMAGE_HEIGHT, IPixelFormat.Type.BGR24, videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
			if(resampler==null) throw new RuntimeException("Resampler error");
		}
		int frame=0;
		IPacket packet = IPacket.make();				
		indexGeneration indGen = new indexGeneration();
		Random rnd = new Random();
		
		//------------elastic-----------------		
		int comps[] = {201,206,207};
		Client client =null;
		
		ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
		settings.put("cluster.name","videodoop");
		TransportClient tmp = new TransportClient(settings);
		tmp.addTransportAddress(new InetSocketTransportAddress("10.222.250."+comps[rnd.nextInt(3)], 9300));
		client = tmp;
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		

		//----------------------------------------------------------
		
		while(container.readNextPacket(packet) >=0)
		{
			if(packet.getStreamIndex() == videostreamid)
			{
				IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),videoCoder.getWidth(),videoCoder.getHeight());
				int offset = 0;
				while(offset<packet.getSize() && packet.isKeyPacket())
				{
					int bytesDecoded = videoCoder.decodeVideo(picture, packet, 0);
					if (bytesDecoded < 0)
			            throw new RuntimeException("got error decoding video");
					
					offset+=bytesDecoded;
					
					if(picture.isComplete() && picture.isKeyFrame())
					{			
						frame++;
						IVideoPicture newPic = picture;
						//System.err.println(packet.getFormattedTimeStamp());
				        if (resampler != null)
				        {
				           newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), Initializer.IMAGE_WIDTH, Initializer.IMAGE_HEIGHT);
				              if (resampler.resample(newPic, picture) < 0)
				                throw new RuntimeException("could not resample video");
				        }
				        
				        if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
				              throw new RuntimeException("could not decode video as BGR 24 bit data");
				        
				        
				        BufferedImage image = new BufferedImage(Initializer.IMAGE_WIDTH,Initializer.IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR); 
				        IConverter converter = ConverterFactory.createConverter(image,IPixelFormat.Type.BGR24);
				        image = converter.toImage(newPic);
				        try
				        {
				        	indGen.generateIndex(image,filename,frame, client, bulkRequest);
				        }
				        catch(Exception e){}
				        /*
				        if(indGen.generateIndex(image,filename,frame, client,file))
				        {
					        File fl = new File(Initializer.VIDEO_FRAME_SOURCE_DIR+filename.substring(0,filename.lastIndexOf("."))+"/"+frame+".png");
					        fl.mkdirs();
					        try {
								ImageIO.write(image, "png", fl);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        image.flush();
	                     }*/		      
				       }
					}
				}			
			}		
		bulkRequest.execute().actionGet();
		client.close();
	}

}
