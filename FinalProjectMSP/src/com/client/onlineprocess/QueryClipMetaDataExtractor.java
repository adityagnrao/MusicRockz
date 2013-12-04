package com.client.onlineprocess;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.common.videoAudioutility.GraphDatatracker;
import com.common.videoAudioutility.Pixel;
import com.common.videoAudioutility.VideoAudioShot;
import com.common.videoAudioutility.VideoUtility;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HIST_ARRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCreateHist;

import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvFileStorage;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram;
import com.musicg.wave.Wave;

public class QueryClipMetaDataExtractor {

	public static void main(String[] args) throws IOException {
		int width = 352;
		int height = 288;
		VideoAudioShot QueryVideo = new VideoAudioShot(0);
		VideoUtility util = new VideoUtility();
		List<VideoAudioShot>ExtractedMetadata = VideoMetadataReader("C:\\DataExtracted\\Metadata.msp");
		OpenCvReadFromFile(ExtractedMetadata);
		int N = Integer.parseInt(args[0]);
		try {
			File file = new File(args[1]+".rgb");
			InputStream is = new FileInputStream(file);
			long len = file.length();
			byte[] bytes = new byte[(int)len];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			int ind = 0;
			int bytesread = (height*width*3); int frameindex = 0;
			while((frameindex*bytesread) < len){
				BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for(int y = 0; y < height; y++){

					for(int x = 0; x < width; x++){

						byte a = 0;
						byte r = bytes[(frameindex*bytesread)+ind];
						byte g = bytes[(frameindex*bytesread)+ind+height*width];
						byte b = bytes[(frameindex*bytesread)+ind+height*width*2]; 

						int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
						//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
						img.setRGB(x,y,pix);
						ind++;
					}
				}

				frameindex++;
				ind = 0;
				QueryVideo.getListofFrames().add(img);
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}



		/*
		 * Perform MetaData extraction on Query
		 */
		QueryVideo = util.scenechangedetector.FindScenechangedFrames(QueryVideo, width, height);
		//feature extraction ---> Color and feature
		QueryVideo.featureBasedExtractor();
		QueryVideo.HSVHistorgramExtractor();
		String filename = args[1];
		Wave sampleWave = new Wave(filename+".wav");
		QueryVideo.ExtractAudioFingerPrint(sampleWave);
		
		List<Double> FinalListBuddy = new ArrayList<Double>();
		GraphDatatracker []g = new GraphDatatracker[ExtractedMetadata.size()];
		for(int i = 0; i < ExtractedMetadata.size(); i++)
		{
			//List<CvHistogram>HistogramValue = new ArrayList<CvHistogram>();
			//ExtractedMetadata.get(i).setHistogramValue(HistogramValue);
			//ExtractedMetadata.get(i).HSVHistorgramExtractor();
			 g[i] = util.library.MatchVideoAudio(QueryVideo, ExtractedMetadata.get(i));
			System.out.println(" ------------------gologolo------------"+ g[i].getMatchSum());
			FinalListBuddy.add(g[i].getMatchSum());
		}
		System.out.println("Audiomatchresults"+g[0].getAudioMatchSum());
		System.out.println("appa guruve shambo shankara"+IndexMatch(g,ExtractedMetadata.size(),findKthMax(FinalListBuddy, 1))+" "+IndexMatch(g,ExtractedMetadata.size(),findKthMax(FinalListBuddy, 2)));
		
	}

	private static int IndexMatch(GraphDatatracker g[], int Ssize, double tobeMatchedNo)
	{
		for(int i = 0; i < Ssize; i++)
		{
			if(g[i].getMatchSum() == tobeMatchedNo)
				return i;
		}
		return -1;
	}
	
	private static double findKthMax(List<Double> input, int k) {		

		if (input != null && input.size() > 0) {

			double element = input.get(0);
			List<Double> largerThanElement = new ArrayList<Double>();
			List<Double> smallerThanElement = new ArrayList<Double>();

			for (int i = 1; i < input.size(); i++) {


				if (element < input.get(i)) {
					largerThanElement.add(input.get(i));
				} else {
					smallerThanElement.add(input.get(i));
				}
			}

			if (largerThanElement.size() == k - 1) {
				return element;
			}
			else if (largerThanElement.size() < k - 1) {
				return findKthMax(smallerThanElement, k - largerThanElement.size() - 1);
			} else if (largerThanElement.size() >= k) {       
				return findKthMax(largerThanElement, k);
			}
			return element; // To satisfy the crazy compiler
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static List<VideoAudioShot> OpenCvReadFromFile(List<VideoAudioShot> extractedMetadata) {


		System.out.println(extractedMetadata.size());
		for (int index = 0; index < extractedMetadata.size(); index++) {
			List<CvMat>DescriptorList = new ArrayList<CvMat>();
			List<CvHistogram>Histograms = new ArrayList<CvHistogram>();
			CvFileStorage cvfile, cvfile2;
			cvfile = opencv_core.cvOpenFileStorage(
					"C:\\DataExtracted\\FeatColorMetadata"+index+".xml", // filename
					null, // memstorage
					CV_STORAGE_READ, // flags
					null); // encoding

			cvfile2 = opencv_core.cvOpenFileStorage(
					"C:\\DataExtracted\\HistrMetadata"+index+".xml", // filename
					null, // memstorage
					CV_STORAGE_READ, // flags
					null); // encoding

			int ssize = cvReadIntByName(cvfile, null, "size");
			System.out.println(ssize);
			for(int dindex = 0; dindex < ssize; dindex++){
				Pointer pointer1 = cvReadByName(cvfile, null, "Descriptors"+dindex);
				Pointer pointer2 = cvReadByName(cvfile2, null, "Histogram"+dindex);
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
				hist.mat(new CvMatND(pointer2));
				DescriptorList.add(new CvMat(pointer1));
				Histograms.add(hist);

			}
			extractedMetadata.get(index).setDescriptorList(DescriptorList);
			extractedMetadata.get(index).setHistogramValue(Histograms);
			cvReleaseFileStorage(cvfile);
			cvReleaseFileStorage(cvfile2);
		}


		return extractedMetadata;

	}

	private static List<VideoAudioShot> VideoMetadataReader(String string) throws IOException {
		ObjectInputStream objectinputstream = null;
		FileInputStream streamIn = null;
		List<VideoAudioShot>readCase = null;
		try {
			streamIn = new FileInputStream(string);
			objectinputstream = new ObjectInputStream(streamIn);
			readCase = (List<VideoAudioShot>) objectinputstream.readObject();

		} catch (Exception e) {

			e.printStackTrace();
		}finally {
			if(objectinputstream != null){
				objectinputstream .close();
			} 
		}
		return readCase;
	}

}
