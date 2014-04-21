package imageAnalysis;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class faceDetection {
	
	private static final String CASCADE_FILE = "/home/user/installs/haarcascade_frontalface_alt.xml";
	
	public static boolean faceDetect(IplImage originalImage)
	  { 
		    // We need a grayscale image in order to do the recognition, so we
		    // create a new image of the same size as the original one.
		    IplImage grayImage = IplImage.create(originalImage.width(),
		    originalImage.height(), IPL_DEPTH_8U, 1);
		 
		    // We convert the original image to grayscale.
		    cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
		    CvMemStorage storage = CvMemStorage.create();
		    // We instantiate a classifier cascade to be used for detection, using the cascade definition.
		    CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
		 
		    // We detect the faces.
		    CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);
		    
		    if(faces.total()>0)
		    	return true;
		    return false;	    
	  }
}
