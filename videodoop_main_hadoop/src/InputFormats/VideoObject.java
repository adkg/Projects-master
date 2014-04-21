package InputFormats;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.io.Writable;

public class VideoObject implements Writable {

		private byte[] videoByteArray= null;
        
        public VideoObject(byte[] video){
                videoByteArray = video;         
        }
        public void setVideoByteArray(byte[] videoByteArray)
        {
        	this.videoByteArray = videoByteArray;
        }
        
        public void write(DataOutput out) throws IOException{
                out.write(videoByteArray);
        }
        
        public void readFields(DataInput in) throws IOException{
                
                in.readFully(videoByteArray);
        }
        
        public byte[] getVideoByteArray(){
                return this.videoByteArray;
        }
     
        public int getLenght()
        {
        	return videoByteArray.length;
        }
}