package InputFormats;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;

public class VideoRecordReader implements RecordReader<Text, VideoObject>{

    private String filename, fullName;
    private Configuration conf;
    
    boolean process = false;
    byte[] videoByteArray;
    
    public VideoRecordReader(FileSplit fileSplit, Configuration conf) throws IOException
    {

		this.conf = conf;
	    
		final Path file = fileSplit.getPath();        
	    	   
	    filename = file.toString();    
	    String format = filename.substring(filename.indexOf(".")+1);
	    fullName=file.toString();
	    
	    videoByteArray = new byte[(int)fileSplit.getLength()];

        FileSystem fs = file.getFileSystem(conf);
        FSDataInputStream fileIn = fs.open(file); 
    	
	    IContainerFormat containerFormat = IContainerFormat.make();
        containerFormat.setInputFormat(format);
	
        int offset = 0;
        int numRead = 0;
        while (offset < videoByteArray.length
                   && (numRead = fileIn.read(videoByteArray, offset, videoByteArray.length - offset)) >= 0) {
                                offset += numRead;                                
        }
        
    }
    
    @Override
	public Text createKey() {
		return new Text();
	}
    
    @Override
	public VideoObject createValue() {
		return new VideoObject(videoByteArray);
	}
    
      
	@Override
    public synchronized void close() throws IOException { }	

	@Override
	public long getPos() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean next(Text key, VideoObject value) throws IOException {
		// TODO Auto-generated method stub
		if (!process) 
		{
			 key.set(fullName);
			 process = true;
	         return true;
		}
		return false;
	}

	@Override
	public float getProgress() throws IOException {
		return 100.0f;
	}
	

}
