package com.common.videoAudioutility;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HIST_ARRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCalcHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCompareHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCreateHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetMinMaxHistValue;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvNormalizeHist;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_nonfree;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.IplImageArray;
import com.googlecode.javacv.cpp.opencv_features2d.DescriptorExtractor;
import com.googlecode.javacv.cpp.opencv_features2d.FeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint;
import com.googlecode.javacv.cpp.opencv_highgui.CvOpenGLCallback;
import com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram;
import com.googlecode.javacv.cpp.opencv_nonfree.SIFT;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;

public class VideoAudioShot implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int VideoID;
	int TotalNoofFrames;
	List<Pixel [][]>capturedframes = new ArrayList<Pixel [][]>();
	transient List<BufferedImage>Frames = new ArrayList<BufferedImage>();
	transient List<CvMat>DescriptorList = new ArrayList<CvMat>();
	List<Integer>KeypointCapacity = new ArrayList<Integer>();
	transient List<CvHistogram>HistogramValue = new ArrayList<CvHistogram>();
	transient Debugger VideoDebug = new Debugger();
	transient PixelArray converter = new PixelArray();
	transient List<BufferedImage>ProcessedFrames = new ArrayList<BufferedImage>();
	List<Integer>Frametracker = new ArrayList<Integer>();
	//Audio Contents
	byte[] audioFeatureTracker;
	

	public VideoAudioShot(int index) {
		VideoID = index;
		HistogramValue = new ArrayList<CvHistogram>();
	}

	public int getVideoID()
	{
		return VideoID;
	}
	
	public void SetFrameSize(int TotalNoofFrames)
	{
		this.TotalNoofFrames = TotalNoofFrames;
	}
	
	public int getFrameSize()
	{
		return this.TotalNoofFrames;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeInt(ProcessedFrames.size()); // how many images are serialized?
		
		int i = 0;
		for (BufferedImage eachImage : ProcessedFrames) {
			ImageIO.write(eachImage, "png", out); // png is lossless
		}
		
		
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final int imageCount = in.readInt();
		CvFileStorage cvfile,cvfile2;
		/*cvfile = opencv_core.cvOpenFileStorage(
	            "C:\\DescriptorMetadata.xml", // filename
	            null, // memstorage
	            CV_STORAGE_READ, // flags
	            null); // encoding
		
		cvfile2 = opencv_core.cvOpenFileStorage(
	            "C:\\HistogramMetadata.xml", // filename
	            null, // memstorage
	            CV_STORAGE_READ, // flags
	            null); // encoding
	            */
		int i1 = 0;
		ProcessedFrames = new ArrayList<BufferedImage>(imageCount);
		ReadableByteChannel channel = Channels.newChannel(in);
		for (int i=0; i<imageCount; i++) {
			ProcessedFrames.add(ImageIO.read(in));
			//DescriptorList.add((CvMat)cvReadByName(cvfile, null, "Descriptors"+i1));
			//HistogramValue.add((CvHistogram)cvReadByName(cvfile2, null, "Histogram"+i1));
		}
		
	}

	public List<BufferedImage> getListofFrames(){
		return this.Frames;
	}



	public List<BufferedImage> getListofProcessedFrames(){
		return this.ProcessedFrames;
	}

	public List<CvMat> getListofDescriptors(){
		return this.DescriptorList;
	}

	public List<CvHistogram> getListofHistograms(){
		return this.HistogramValue;
	}

	public List<Integer> getFrameTrackerdata(){
		return this.Frametracker;
	}

	public void setListofProcessedFrames(List<BufferedImage> ProcessedFrames){
		this.ProcessedFrames =  ProcessedFrames;
	}
	
	public void setListofFrames(List<BufferedImage> Frames){
		this.Frames =  Frames;
	}
	
	public void setListofCapturedFrames(List<Pixel[][]>Frames){
		this.capturedframes =  Frames;
	}

	public void setFrameTracker(List<Integer> Frametracker){
		this.Frametracker =  Frametracker;
	}
	
	public void setDescriptorList(List<CvMat> Descriptorlist){
		this.DescriptorList =  Descriptorlist;
	}
	
	public void setHistogramValue(List<CvHistogram> histogramsval){
		this.HistogramValue =  histogramsval;
	}

	public void HSVHistorgramExtractor() {

		for(int i = 0; i < this.ProcessedFrames.size(); i++)
		{
			IplImage basImage = IplImage.createFrom(this.ProcessedFrames.get(i));
			CvSize img_sz = cvGetSize(basImage);
			//cvShowImage("first imag", basImage);
			IplImage hsvImage= cvCreateImage(img_sz, IPL_DEPTH_8U, 3);    
			cvCvtColor(basImage, hsvImage, CV_RGB2HSV);   
			IplImageArray hsvChannels = splitChannels(hsvImage);
			CvHistogram hist=getHueHistogram(hsvChannels);
			this.HistogramValue.add(hist);
		}

	}

	public void featureBasedExtractor() {

		List<CvMat>DescriptorList = new ArrayList<CvMat>();
		for(int i = 0; i < this.ProcessedFrames.size(); i++)
		{
			BufferedImage frame1 = this.ProcessedFrames.get(i);
			IplImage simg = IplImage.createFrom(frame1);
			CvSize img_sz = cvGetSize(simg);
			IplImage gray = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
			cvCvtColor(simg, gray, CV_RGB2GRAY);
			opencv_nonfree.SIFT siftoperator = new SIFT();
			FeatureDetector detector = siftoperator.getFeatureDetector();
			DescriptorExtractor extractor = siftoperator.getDescriptorExtractor();
			KeyPoint keypoints = new KeyPoint();

			detector.detect(gray, keypoints, null);
			CvMat descriptors = new CvMat(null);
			extractor.compute(gray,
					keypoints, descriptors);
			DescriptorList.add(descriptors);
			this.DescriptorList.add(descriptors);
			this.KeypointCapacity.add(keypoints.capacity());
		}

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
		//VideoDebug.DEBUG_PRINTLN(false, "Min="+minMax[0]); //Less than 0.01
		//VideoDebug.DEBUG_PRINTLN(false, "Max="+minMax[1]); //255
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
	
	
	public void ExtractAudioFingerPrint(Wave sampleWave)
	{
		this.audioFeatureTracker = sampleWave.getFingerprint();
	}
	

}
