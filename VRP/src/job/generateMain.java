package job;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.MapFile;


public class generateMain {

	public static void main(String[] args) throws Exception {
		
		generateClass gp = new generateClass();
		gp.generate_pop(1);
		
		mapFileGenerator mpg = new mapFileGenerator();
		mpg.cust_generate();
		mpg.writeRouteTypes();
		mpg.speed();
		//mpg.print();
		
		
		
		
		/*
		Configuration conf = new Configuration();
		String uri = "hdfs://comp3/user/user/user/rohit/input/vrp_input/customerFile.map";
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		
		MapFile.Reader customerFile = new MapFile.Reader(fs, "/user/user/user/rohit/input/vrp_input/customerFile.map", conf);
		MapFile.Reader trafficFile = new MapFile.Reader(fs, "/user/user/user/rohit/input/vrp_input/trafficFile.map", conf);		
		crossover crs = new crossover(customerFile, trafficFile);
		System.err.print(crs.doCrossover("8,2,3,5,1,6,7,4","6,3,8,4,7,2,1,5"));*/
	}

}
