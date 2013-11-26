package com.common.videoutility;

import static com.googlecode.javacv.cpp.opencv_core.CV_C;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_NONE;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HIST_ARRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCalcHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCompareHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCreateHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.video.Video;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvMatArray;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import com.googlecode.javacv.cpp.opencv_features2d;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.CvWriteFunc;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.IplImageArray;
import com.googlecode.javacv.cpp.opencv_features2d.AdjusterAdapter;
import com.googlecode.javacv.cpp.opencv_features2d.BFMatcher;
import com.googlecode.javacv.cpp.opencv_features2d.DMatch;
import com.googlecode.javacv.cpp.opencv_features2d.DMatchVectorVector;
import com.googlecode.javacv.cpp.opencv_features2d.DescriptorExtractor;
import com.googlecode.javacv.cpp.opencv_features2d.DescriptorMatcher;
import com.googlecode.javacv.cpp.opencv_features2d.DrawMatchesFlags;
import com.googlecode.javacv.cpp.opencv_features2d.DynamicAdaptedFeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.FeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.FlannBasedMatcher;
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint;
import com.googlecode.javacv.cpp.opencv_features2d.SurfAdjuster;
import com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram;
import com.googlecode.javacv.cpp.opencv_nonfree;
import com.googlecode.javacv.cpp.opencv_nonfree.SIFT;
import com.googlecode.javacv.cpp.opencv_nonfree.SURF;
import com.googlecode.javacv.cpp.opencv_stitching.HomographyBasedEstimator;

public class ImageComparator {
	Debugger VideoDebug = new Debugger();
	PixelArray converter = new PixelArray();
	
	public ImageComparator() {
		// TODO Auto-generated constructor stub
	}
	public void HSVHistorgramMatcher(List<BufferedImage> BunchofImages) {
		 IplImage basImage = IplImage.createFrom(BunchofImages.get(0));
		 CvSize img_sz = cvGetSize(basImage);
		 cvShowImage("first imag", basImage);
		 IplImage hsvImage= cvCreateImage(img_sz, IPL_DEPTH_8U, 3);    
			cvCvtColor(basImage, hsvImage, CV_RGB2HSV);   
			 IplImageArray hsvChannels = splitChannels(hsvImage);
	    CvHistogram hist=getHueHistogram(hsvChannels);

	    IplImage contrastImage = IplImage.createFrom(BunchofImages.get(1));
	    cvShowImage("contrastImage", contrastImage);
		 IplImage hsvImage2= cvCreateImage(contrastImage.cvSize(), IPL_DEPTH_8U, 3);    
			cvCvtColor(contrastImage, hsvImage2, CV_RGB2HSV);   
			IplImageArray hsvChannels2 = splitChannels(hsvImage2);
	   CvHistogram hist1=getHueHistogram(hsvChannels2);
	  
	   double matchValue=cvCompareHist(hist, hist1, CV_COMP_INTERSECT);
	   VideoDebug.DEBUG_PRINTLN(true, " "+matchValue);
	   cvWaitKey(0);
	}
	
	public void featureBasedComparator(List<BufferedImage> BunchofImages) {

		BufferedImage frame1 = BunchofImages.get(0);
		BufferedImage frame2 = BunchofImages.get(1);
		IplImage simgA = IplImage.createFrom(frame1);
		IplImage simgB = IplImage.createFrom(frame2);
		
		CvSize img_sz = cvGetSize(simgA);
		IplImage first = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		IplImage second = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		cvCvtColor(simgA, first, CV_RGB2GRAY);
		cvCvtColor(simgB, second, CV_RGB2GRAY);
		cvShowImage("first", first);
		cvShowImage("second", second);
		
		opencv_nonfree.SIFT siftoperator = new SIFT();
		FeatureDetector detector = siftoperator.getFeatureDetector();
		DescriptorExtractor extractor = siftoperator.getDescriptorExtractor();
		KeyPoint keypoints1 = new KeyPoint();
		KeyPoint keypoints2 = new KeyPoint();
		
		detector.detect(first, keypoints1, null);
		detector.detect(second, keypoints2, null);
		
		VideoDebug.DEBUG_PRINT(true, "keypoint"+keypoints1.capacity());
		

		CvMat descriptors1 = new CvMat(null), descriptors2 = new CvMat(null);
		extractor.compute(first,
				keypoints1, descriptors1);
		extractor.compute(second, keypoints2, descriptors2);
		BFMatcher matcher = new BFMatcher(4, false);
		DMatchVectorVector matchesvector1 = new DMatchVectorVector();
		DMatchVectorVector matchesvector2 = new DMatchVectorVector();
		matcher.knnMatch(descriptors1, descriptors2, matchesvector1, 2, null, true);
		matcher.knnMatch(descriptors2, descriptors1, matchesvector2, 2, null, true);
		ratioTest(matchesvector1);
		ratioTest(matchesvector2);
		
		cvWaitKey(0);
		

	}
	
	private DMatch ratioTest(DMatchVectorVector matches){
		DMatch good_matches = new DMatch();
		int j = 0;
		VideoDebug.DEBUG_PRINTLN(true, " "+matches.size());
		for (int i =0; i < matches.size(); i++) {
            // if 2 NN has been identified
			VideoDebug.DEBUG_PRINTLN(true, matches.size(i)+" "+matches.get(i, 0).distance()/matches.get(i, 1).distance());
			if (matches.size(i) > 0) 
			{
				if(matches.get(i, 0).distance() < 0.6*matches.get(i, 1).distance())
					{
						j++;
					}
			}
		}
		VideoDebug.DEBUG_PRINTLN(true, "new matches "+good_matches.capacity()+" "+good_matches.sizeof()+" comeon give me "+j);
		return good_matches;
	}
	
	private CvHistogram getHueHistogram(IplImageArray image){
		
		int numberOfBins=256;
	    float minRange= 0f;
	    float maxRange= 180f;
	    // Allocate histogram object
	   int dims = 1;
	   int[]sizes = new int[]{numberOfBins};
	   int histType = CV_HIST_ARRAY;
	   float[] minMax = new  float[]{minRange, maxRange};
	   float[][] ranges = new float[][]{minMax};
	    int uniform = 1;
	    CvHistogram hist = cvCreateHist(dims, sizes, histType, ranges, uniform);
	    // Compute histogram
	    int accumulate = 1;
	    IplImage mask = null;
	   
	    cvCalcHist(image,hist, accumulate, null);
	    cvNormalizeHist(hist, 1);
	    cvGetMinMaxHistValue(hist, minMax, minMax, sizes, sizes);
	    VideoDebug.DEBUG_PRINTLN(true, "Min="+minMax[0]); //Less than 0.01
	    VideoDebug.DEBUG_PRINTLN(true, "Max="+minMax[1]); //255
		return hist;
		}
	
	private IplImageArray splitChannels(IplImage hsvImage) {
	    CvSize size = hsvImage.cvSize();
	    int depth=hsvImage.depth();
	    IplImage channel0 = cvCreateImage(size, depth, 1);
	    IplImage channel1 = cvCreateImage(size, depth, 1);
	    IplImage channel2 = cvCreateImage(size, depth, 1);
	    cvSplit(hsvImage, channel0, channel1, channel2, null);
	    return new IplImageArray(channel0, channel1, channel2);
	}

}
