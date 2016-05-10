package org.opencv.samples.tutorial1;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    String cText;
    private Mat                  mRgba;
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = false;
    private boolean              mbool = false;
    private MenuItem             mItemSwitchCamera = null;
   // private Myname				 mDetector;
    private boolean              mIsColorSelected = false;
 //   Threads t = new Threads();
    //RedFilter r = new RedFilter();
    private int iCannyUpperThreshold = 100;
    private int iMinRadius = 20;
    private int iMaxRadius = 400;
    private int iAccumulator = 300;
    Button btn;
    
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
            
        }
    };

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
       
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial1_surface_view);
       	
        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    
  
    
	@Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;

            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
                toastMesage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
                toastMesage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }
    


    public void onCameraViewStarted(int width, int height) {
    	
    }

    public void onCameraViewStopped() {
    }

    
   
    	

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	 mRgba = inputFrame.rgba();
    	
    	 
    	 //mDetector.process(mRgba);
    	 new Thread() { public void run() { GreenFilter.process(mRgba); }}.start();
    	
         new Thread() { public void run() { RedFilter.process(mRgba); }}.start();
         
         new Thread() { public void run() { YellowFilter.process(mRgba); }}.start();
        
    	return mRgba;
	}
    	
   
    	 
	 private void redfilter(Mat mRgba) {
		// TODO Auto-generated method stub
		 Mat mIntermediateMat = new Mat();
   	  Mat  mIntermediateMat2 = new Mat();
   	 Mat   mEmpty = new Mat();
   	 Scalar  lo = new Scalar(0, 100, 30);
	  Scalar  hi = new Scalar(5, 255, 255);
   	  Scalar  bl = new Scalar(0, 0, 0, 255);
   	   Scalar wh = new Scalar(255, 255, 255, 255);
   	 //capture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
    	Imgproc.cvtColor(mRgba, mIntermediateMat, Imgproc.COLOR_RGB2HSV_FULL);
    	Core.inRange(mIntermediateMat, lo, hi, mIntermediateMat2); // green
    	//Core.inRange(mIntermediateMat, new Scalar(0, 100, 30), new Scalar(5, 255, 255), mIntermediateMat2); //red
    	//Core.inRange(mIntermediateMat, new Scalar(20, 100, 100), new Scalar(30, 255, 255), mIntermediateMat2);//yellow
    	Imgproc.dilate(mIntermediateMat2, mIntermediateMat2, mEmpty);
    	int maxAreaIdx = 0;
    	MatOfPoint maxContour = new MatOfPoint();
       double maxContourArea = 0;
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	Mat hierarchy = new Mat();
    	Imgproc.findContours(mIntermediateMat2, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    	Log.d("processFrame", "contours.size()" + contours.size());
    	mRgba.setTo(bl);
    //	Imgproc.drawContours(mRgba, contours, -1, wh);
    	 
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
      //  Mat drawing = new Mat();
        Scalar color = new Scalar(0,0,255);
         Point[] points_contour = maxContour.toArray();
         int nbPoints = points_contour.length; 
         for(int i=0; i< nbPoints;i++)
         {
                 Point v=points_contour[i];      
         }
         //Imgproc.drawContours(mRgba, contours, maxAreaIdx, color);
    // Imgproc.drawContours(mRgba, contours, maxAreaIdx, wh);
         iCannyUpperThreshold = 100;
       
         iCannyUpperThreshold = 75; 
         Mat mGray = new Mat();
         Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);
         Imgproc.GaussianBlur(mGray, mGray, new Size(5, 5), 2, 2); 
         Imgproc.HoughCircles(mGray, mIntermediateMat, Imgproc.CV_HOUGH_GRADIENT, 2.0, mGray.rows() / 8,   
                 iCannyUpperThreshold, iAccumulator, iMinRadius, iMaxRadius);  
                   
     if (mIntermediateMat.cols() > 0)  
         for (int x = 0; x < Math.min(mIntermediateMat.cols(), 10); x++)   
             {  
             double vCircle[] = mIntermediateMat.get(0,x);  
   
             if (vCircle == null)  
                 break;  
   
             Point pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));  
             int radius = (int)Math.round(vCircle[2]);  
             int iLineThickness = 100;
			// draw the found circle  
             Core.circle(mRgba, pt, radius, color, iLineThickness);  
                           
             
			// draw a cross on the centre of the circle  
          
            
             }  
 // Procedure 1

	     // Imgproc.drawContours(mRgba, contours, nbPoints, wh);
     Imgproc.drawContours(mRgba, contours, maxAreaIdx, color);
	       
          Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
          Utils.matToBitmap(mRgba, bmp);
       
	}
	 
	 

	
	
		// TODO Auto-generated method stub
		
	
}
