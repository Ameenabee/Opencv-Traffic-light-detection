package org.opencv.samples.tutorial1;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

public class YellowFilter {
	public static  void process(Mat mRgba)
	{
	 Mat mIntermediateMat = new Mat();
	  Mat  mIntermediateMat2 = new Mat();
	 Mat   mEmpty = new Mat();
	 Scalar  lo = new Scalar(20, 100, 100);
	  Scalar  hi = new Scalar(30, 255, 255);
	  
	  Scalar  bl = new Scalar(0, 0, 0, 255);
	   Scalar wh = new Scalar(255, 255, 255, 255);
	 //capture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
	Imgproc.cvtColor(mRgba, mIntermediateMat, Imgproc.COLOR_RGB2HSV_FULL);
	Core.inRange(mIntermediateMat,lo,hi,mIntermediateMat2); // green
	//Core.inRange(mIntermediateMat, new Scalar(0, 100, 30), new Scalar(5, 255, 255), mIntermediateMat2);
	//Core.inRange(mIntermediateMat, new Scalar(0, 100, 30), new Scalar(5, 255, 255), mIntermediateMat2); //red
	//Core.inRange(mIntermediateMat, new Scalar(20, 100, 100), new Scalar(30, 255, 255), mIntermediateMat2);//yellow
	//Core.bitwise_or(mIntermediateMat, mIntermediateMat2, mIntermediateMat);
	Imgproc.dilate(mIntermediateMat2, mIntermediateMat2, mEmpty);
	int maxAreaIdx = 0;
	MatOfPoint maxContour = new MatOfPoint();
   double maxContourArea = 0;
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	Mat hierarchy = new Mat();
	Imgproc.findContours(mIntermediateMat2, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	Log.d("processFrame", "contours.size()" + contours.size());
	mRgba.setTo(bl);
//	Imgproc.drawContours(mRgba, contours, -1, wh);//cut
	  for (int idx = 0; idx < contours.size(); idx++) 
     {
           MatOfPoint contour = contours.get(idx);
           double contourarea = Imgproc.contourArea(contour);
        
			if (contourarea > maxContourArea) 
           {
                     maxContour = contour;
                    maxContourArea = contourarea;
                    maxAreaIdx = idx;
                   
           }
     }
   
  //  Mat drawing = new Mat();//cut
   Scalar color = new Scalar(0,0,255);
     Point[] points_contour = maxContour.toArray();
     int nbPoints = points_contour.length; 
     for(int i=0; i< nbPoints;i++)
     {
             Point v=points_contour[i];      
     }
     //Imgproc.drawContours(mRgba, contours, maxAreaIdx, color);
// Imgproc.drawContours(mRgba, contours, maxAreaIdx, wh);
     int iCannyUpperThreshold = 100;
   
     iCannyUpperThreshold = 75; 
     Mat mGray = new Mat();
   //  Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);
     Imgproc.GaussianBlur(mIntermediateMat2, mIntermediateMat2, new Size(5, 5), 2, 2); 
     Imgproc.HoughCircles(mIntermediateMat2, mIntermediateMat, Imgproc.CV_HOUGH_GRADIENT, 2.0,100);
     int radius = 0; int iLineThickness = 50;Point pt = null;
 if (mIntermediateMat.cols() > 0)  
     for (int x = 0; x < Math.min(mIntermediateMat.cols(), 10); x++)   
         {  
         double vCircle[] = mIntermediateMat.get(0,x);  

         if (vCircle == null)  
             break;  
        // System.out.println("Yellow Light Detected!");
         pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));  
         radius = (int)Math.round(vCircle[2]);  
        
		// draw the found circle  
         
      
     
         Core.circle(mRgba, pt, radius, color, iLineThickness);  
         
		// draw a cross on the centre of the circle  
      
        
         }  
 Imgproc.drawContours(mRgba, contours,maxAreaIdx ,wh);
 
 if(maxAreaIdx != 0 && radius != 0 && iLineThickness == 50 && pt != null && nbPoints != 0)
 {
	 System.out.println("Yellow Light Detected!");
 }
  
      Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
      Utils.matToBitmap(mRgba, bmp);
   
   
}
}
