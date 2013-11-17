package com.common.videoutility;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.server.offlineprocess.VideoShot;


public class SceneChangeDetector {

	PixelArray Converter = new PixelArray();
	Debugger SceneDebug = new Debugger();

	public SceneChangeDetector() {
		// TODO Auto-generated constructor stub
	}

	public List<BufferedImage> FindScenechangedFrames(VideoShot DatabaseVideo, int width, int height) {

		List<BufferedImage>Frames = new ArrayList<BufferedImage>();

		Frames = DatabaseVideo.getListofFrames();
		for(int i = 0; i < Frames.size(); i++)
		{
			CannyEdgeDetector detector = new CannyEdgeDetector();
			//adjust its parameters as desired
			detector.setLowThreshold(0.5f);
			detector.setHighThreshold(1f);
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
			Frames.remove(i);
			Frames.add(i, edgeDilated);
		}

		/*
		 * Edges are dilated in the frames now
		 */
		Pixel [][] FramedImage1 = new Pixel[height][width];
		Pixel [][] FramedImage2 = new Pixel[height][width];
		double ratio[] = new double[Frames.size()];
		for(int i = 0; i < Frames.size()-1; i++)
		{
			FramedImage1 = Converter.ConverttoPixelArray(Frames.get(i), width, height);
			FramedImage2 = Converter.ConverttoPixelArray(Frames.get(56), width, height);
			ratio[i] = ChangeinEdgePixels(FramedImage1, FramedImage2, width, height);
		}
		
		for(int i = 0; i < Frames.size()-1; i++)
		SceneDebug.DEBUG_PRINTLN(true, "----Ratio ---"+ratio[i]);
		return Frames;
	}




	private double ChangeinEdgePixels(Pixel[][] framedImage1,
			Pixel[][] framedImage2, int width, int height) {

		int FirstImagecount = TotalNoofEdgePixels(framedImage1, width, height);
		int SecondImagecount = TotalNoofEdgePixels(framedImage2, width, height);
		double densityIn = 0;
		double densityOut = 0;
		for(int i = 0; i < height; i+=4)
		{
			for(int j = 0; j < width; j+=4)
			{
				if(i == 0 || j == 0 || i == 1 || j == 1 || i == height-1 || j == width-1 || i == height-2 || j == width-2);
				else	
				{
					if(framedImage1[i][j].X ==  255)
					{
						densityIn += CountEdgePixels(framedImage2, i,j);
					}
					
					if(framedImage2[i][j].X ==  255)
					{
						densityOut += CountEdgePixels(framedImage1, i,j);
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
	
	private int CountEdgePixels(Pixel[][] framedImage, int i, int j) {
		int count = 0;
		for(int indexX = i-2; indexX <= i+2; indexX++)
			for(int indexY = j-2; indexY <= j+2; indexY++)
			{
				if(framedImage[indexX][indexY].X == 255)
				{
					if(Converter.EuclideanDistance(framedImage[i][j], framedImage[indexX][indexY]) < 2)
						return 1;
				}
					
			}
		return 0;
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
				if(i == 0 || j == 0 || i == height-1 || j == width-1)
					newImage[i][j] = srcImage[i][j];
				
				else if(srcImage[i][j].X != 0)			
				{
					//Set all the surrounding pixels to white as well
					for(int pindex = -1; pindex <=1; pindex++ )
					{
						newImage[i+pindex][j] = new Pixel(255.0, 255.0, 255.0);
						newImage[i][j+pindex] = new Pixel(255.0, 255.0, 255.0);
					}
					for(int pindex = -1; pindex <=1; pindex++ )
					{
						newImage[i+pindex][j-pindex] = new Pixel(255.0, 255.0, 255.0);
						newImage[i-pindex][j+pindex] = new Pixel(255.0, 255.0, 255.0);
					}
					newImage[i+1][j+1] = new Pixel(255.0, 255.0, 255.0);

				}
				else
					newImage[i][j] = srcImage[i][j];
			}

		}


		return newImage;
	}



}
