package hadoop;



import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import xuggler.frameExtractor;

import InputFormats.VideoObject;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

public class mapper extends MapReduceBase implements Mapper<Text, VideoObject, Text, Text> {
	
	JobConf conf;
	frameExtractor frameExt;
	
	@Override
	public void configure(JobConf conf){
		this.conf=conf;
		frameExt = new frameExtractor();
	}
	
	public void map(Text key, VideoObject value,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		String format = key.toString().substring(key.toString().lastIndexOf("."));
		String filename = key.toString().substring(key.toString().lastIndexOf("/")+1);
		
		IContainerFormat containerFormat = IContainerFormat.make();
        containerFormat.setInputFormat(format);   
	    IContainer container = IContainer.make();
        ByteArrayInputStream bis = new ByteArrayInputStream(value.getVideoByteArray());       
        container.setInputBufferLength(bis.available());        
        container.open(bis,containerFormat);      
        try
        {
        	frameExt.Extractor(container,filename);
        }
        catch(Exception e){}
		container.close();
	}
	
	
}