package com.common.videoutility;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.server.offlineprocess.VideoShot;
import com.googlecode.javacv.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_video.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class SceneChangeDetector {

	PixelArray Converter = new PixelArray();
	Debugger SceneDebug = new Debugger();
	private static final int MAX_CORNERS = 500;
	int r = 2;
	double edgethreshold = 0.20;
	int edgewindow = 30;
	public SceneChangeDetector() {
		// TODO Auto-generated constructor stub
	}

	public List<BufferedImage> FindScenechangedFrames(VideoShot DatabaseVideo, int width, int height) {

		List<BufferedImage>Frames = new ArrayList<BufferedImage>();
		List<BufferedImage>DilatedFrames = new ArrayList<BufferedImage>();

		Frames = DatabaseVideo.getListofFrames();

		for(int i = 0; i < Frames.size(); i++)
		{

			CannyEdgeDetector detector = new CannyEdgeDetector();
			//adjust its parameters as desired
			detector.setLowThreshold(0.5f);
			detector.setHighThreshold(2.2f);
			//apply it to an image
			detector.setSourceImage(Frames.get(i));
			//SceneDebug.DEBUG_PRINTLN(true, "Processing"+i+" Image-----------------");
			detector.process();
			//SceneDebug.DEBUG_PRINTLN(true, "Processing"+i+" Image finished******************");
			BufferedImage edges = detector.getEdgesImage();
			Frames.remove(i);
			Frames.add(i, edges);
		}

		/*
		 * Frames contain edge detectedImages
		 */

		Pixel [][] FramedImage = new Pixel[height][width];
		for(int i = 0; i < Frames.size(); i++)
		{
			FramedImage = Converter.ConverttoPixelArray(Frames.get(i), width, height);
			FramedImage = EdgeDilation(FramedImage, width, height);

			BufferedImage edgeDilated = Converter.ConverttoBufferedImage(FramedImage, width, height);
			DilatedFrames.add(i, edgeDilated);
		}

		/*
		 * Edges are dilated in the frames now
		 */
		Pixel [][] FramedImage1 = new Pixel[height][width];
		Pixel [][] FramedImage2 = new Pixel[height][width];
		Pixel [][] DilatedFramedImage1 = new Pixel[height][width];
		Pixel [][] DilatedFramedImage2 = new Pixel[height][width];
		double ratio[] = new double[Frames.size()];
		for(int i = 0; i < Frames.size()-1; i++)
		{
			//FramedImage1 = OpticalFlowAnalysis(Frames.get(i), Frames.get(i+1));

			FramedImage1 = Converter.ConverttoPixelArray(Frames.get(i), width, height);
			FramedImage2 = Converter.ConverttoPixelArray(Frames.get(i+1), width, height);
			DilatedFramedImage1 = Converter.ConverttoPixelArray(DilatedFrames.get(i), width, height);
			DilatedFramedImage2 = Converter.ConverttoPixelArray(DilatedFrames.get(i+1), width, height);
			ratio[i] = ChangeinEdgePixels(FramedImage1, FramedImage2, DilatedFramedImage1, DilatedFramedImage2, width, height);
		}

		for(int i = 0; i < Frames.size()-1; i++)
			SceneDebug.DEBUG_PRINTLN(true, "Frame No "+i+" ----Ratio ---"+ratio[i]);

		List<BufferedImage>SceneChangedFrames = new ArrayList<BufferedImage>();
		int FrameCount = 0;
		int NoteId =  -1;
		int j = 1;
		double max = -1;
		double avg = 0;
		for(int i = 1; i <= ( Frames.size()/edgewindow); i++)
		{
			if(FrameCount > Frames.size() -1)
				break;
			max = ratio[FrameCount];
			avg = 0;
			while(FrameCount < edgewindow*i && FrameCount < Frames.size() -1)
			{
				avg+=ratio[FrameCount];
				if(ratio[FrameCount] >  max)
				{
					max = ratio[FrameCount];
					NoteId = FrameCount;
				}
				FrameCount++;
			}
			avg -= ratio[NoteId];
			avg /= edgewindow-1;

			if(ratio[NoteId] > (avg+edgethreshold))
			{
				SceneChangedFrames.add(DatabaseVideo.getListofFrames().get(NoteId));
				SceneChangedFrames.add(DatabaseVideo.getListofFrames().get(NoteId+1));
			}
		}



		return SceneChangedFrames;
	}


	private Pixel[][] OpticalFlowAnalysis(BufferedImage Image1, BufferedImage Image2)
	{

		Pixel [][] estimatedImage = new Pixel [Image1.getHeight()][Image1.getWidth()]; 
		double distanceX = 0, distanceY = 0;
		int count = 0;

		JFrame Inputframe = new JFrame();
		JLabel Inputlabel = new JLabel(new ImageIcon(Image1));
		Inputframe.getContentPane().add(Inputlabel, BorderLayout.CENTER);
		Inputframe.pack();
		Inputframe.setVisible(true);

		estimatedImage = Converter.ConverttoPixelArray(Image1, Image1.getWidth(), Image1.getHeight());

		// Load two images and allocate other structures
		IplImage imgA = IplImage.createFrom(Image1);
		IplImage imgB = IplImage.createFrom(Image2);

		CvSize img_sz = cvGetSize(imgA);
		int win_size = 15;
		IplImage simgA = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		IplImage simgB = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		cvCvtColor(imgA, simgA, CV_RGB2GRAY);
		cvCvtColor(imgB, simgB, CV_RGB2GRAY);
		//cvShowImage( "ImgA", simgA );
		//cvShowImage( "ImgB", simgB );

		IplImage imgC = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		// Get the features for tracking
		IplImage eig_image = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		IplImage tmp_image = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);

		int[] corner_count = { MAX_CORNERS };
		CvPoint2D32f cornersA = new CvPoint2D32f(MAX_CORNERS);

		CvArr mask = null;
		//Feature (Control-point structure tracking)
		cvGoodFeaturesToTrack(simgA, eig_image, tmp_image, cornersA,
				corner_count, 0.8, 3.0, mask, 3, 0, 0.04);

		cvFindCornerSubPix(simgA, cornersA, corner_count[0],
				cvSize(win_size, win_size), cvSize(-1, -1),
				cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.03));

		// Call Lucas Kanade algorithm
		byte[] features_found = new byte[MAX_CORNERS];
		float[] feature_errors = new float[MAX_CORNERS];

		CvSize pyr_sz = cvSize(simgA.width() + 8, simgB.height() / 3);

		IplImage pyrA = cvCreateImage(pyr_sz, IPL_DEPTH_8U, 1);
		IplImage pyrB = cvCreateImage(pyr_sz, IPL_DEPTH_8U, 1);

		CvPoint2D32f cornersB = new CvPoint2D32f(MAX_CORNERS);
		cvCalcOpticalFlowPyrLK(simgA, simgB, pyrA, pyrB, cornersA, cornersB,
				corner_count[0], cvSize(win_size, win_size), 5,
				features_found, feature_errors,
				cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.3), 0);

		// Make an image of the results
		for (int i = 0; i < corner_count[0]; i++) {
			if (features_found[i] == 0 || feature_errors[i] > 550) {
				System.out.println("Error is " + feature_errors[i] + "/n");
				continue;
			}
			//System.out.println("Got it/n");
			cornersA.position(i);
			cornersB.position(i);
			CvPoint p0 = cvPoint(Math.round(cornersA.x()),
					Math.round(cornersA.y()));

			CvPoint p1 = cvPoint(Math.round(cornersB.x()),
					Math.round(cornersB.y()));

			SceneDebug.DEBUG_PRINTLN(true, "cornersA.x() and cornersA.y()"+cornersA.x()+" "+cornersA.y());
			SceneDebug.DEBUG_PRINTLN(true, "cornersB.x() and cornersB.y()"+cornersB.x()+" "+cornersB.y());

			distanceX += (double)(cornersA.x() - cornersB.x());
			distanceY += (double)(cornersA.y() - cornersB.y());
			count++;
			//estimatedImage.setRGB((int)cornersA.x(), (int)cornersA.y(), 0);
			//estimatedImage.setRGB((int)cornersB.x(), (int)cornersB.y(), pix);

			cvLine(imgC, p0, p1, CV_RGB(255, 0, 0), 
					2, 8, 0);
		}

		cvSaveImage(
				"image0-1.png",
				imgC);
		cvNamedWindow( "LKpyr_OpticalFlow", 0 );
		cvShowImage( "LKpyr_OpticalFlow", imgC );
		distanceX /= count;
		distanceY/= count;
		SceneDebug.DEBUG_PRINTLN(true, "distanceX & distanceY"+distanceX+" "+distanceY);


		Image2 = Converter.ConverttoBufferedImage(estimatedImage, 352, 288);
		Inputframe = new JFrame();
		Inputlabel = new JLabel(new ImageIcon(Image2));
		Inputframe.getContentPane().add(Inputlabel, BorderLayout.CENTER);
		Inputframe.pack();
		Inputframe.setVisible(true);
		cvWaitKey(0);

		return estimatedImage;
	}

	private double ChangeinEdgePixels(Pixel[][] framedImage1,
			Pixel[][] framedImage2, Pixel[][] DilatedFrame1, Pixel[][] DilatedFrame2, int width, int height) {

		int FirstImagecount = TotalNoofEdgePixels(framedImage1, width, height);
		int SecondImagecount = TotalNoofEdgePixels(framedImage2, width, height);
		double densityIn = 0;
		double densityOut = 0;
		for(int i = 0; i < height; i+=1)
		{
			for(int j = 0; j < width; j+=1)
			{
				if(i-r < 0 || j-r < 0 || i+r >= height || j+r >=width);
				else	
				{
					if(framedImage1[i][j].X ==  255.0f)
					{
						if(DilatedFrame2[i][j].X == 255.0f)
							densityIn+=1;
					}

					if(framedImage2[i][j].X ==  255.0f)
					{
						if(DilatedFrame1[i][j].X == 255.0f)
							densityOut+=1;
					}
				}
			}

		}
		densityIn /= SecondImagecount;
		densityOut /= FirstImagecount;
		densityIn = 1 - densityIn;
		densityOut = 1 - densityOut;

		return max(densityIn, densityOut);
	}

	private double max(double a, double b){
		if(a < b)
			return b;

		return a;

	}

	private int TotalNoofEdgePixels(Pixel[][] srcImage, int width, int height)
	{

		int count = 0;
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(srcImage[i][j].X == 255)
					count++;
			}
		}
		return count;
	}

	private Pixel [][] EdgeDilation(Pixel [][]srcImage, int width, int height){

		Pixel [][] newImage = new Pixel[height][width];
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				newImage[i][j] = new Pixel(0,0,0);
			}
		}

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(i-r < 0 || j-r < 0 || i+r >= height || j+r >=width);

				else
				{
					if(srcImage[i][j].X == 255.0)
					{
						for(int indexX = i-r; indexX <= i+r; indexX++)
							for(int indexY = j-r; indexY <= j+r; indexY++)
							{
								if(Converter.EuclideanDistance(srcImage[i][j], srcImage[indexX][indexY]) <= r)
								{
									newImage[indexX][indexY].X = 255.0;
									newImage[indexX][indexY].Y = 255.0;
									newImage[indexX][indexY].Z = 255.0;
								}

							}
					}
					else
						newImage[i][j] = srcImage[i][j];
				}
			}

		}


		return newImage;
	}



}
