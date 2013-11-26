package com.server.offlineprocess;

import com.common.videoutility.*;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.highgui.VideoCapture;
import org.opencv.video.Video;

public class VideoShotMetaDataExtractor {

	int framerate = 25;

	public static void main(String[] args) throws IOException {

		int width = 352;
		int height = 288;
		List<VideoShot>VideoList = new ArrayList<VideoShot>();
		/*
		 * FOR each video extract rgb framebyframe data
		 */
		int N = Integer.parseInt(args[0]);
		for(int i = 1; i <= N; i++){
			try {
				File file = new File(args[i]);
				InputStream is = new FileInputStream(file);
				VideoShot newVideo =  new VideoShot(i);
				

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
					newVideo.Frames.add(img);
				
				
				}
				VideoList.add(newVideo);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Video Utility Object created
		
		
		
		VideoUtility util = new VideoUtility();
		List<BufferedImage> ProcessedFrames = new ArrayList<BufferedImage>();
		util.TODEBUGS.DEBUG_PRINTLN(true, "yappax ayetho maga video reading");
		
		/*
		 * Perform MetaData extraction on every video
		 */
		//for(int Vindex = 0; Vindex < N; Vindex++)
		{
			//ProcessedFrames = util.scenechangedetector.FindScenechangedFrames(VideoList.get(Vindex), width, height);
			//Video-object Segmentation
			ProcessedFrames.add(VideoList.get(0).Frames.get(2));
			ProcessedFrames.add(VideoList.get(1).Frames.get(200));
			ProcessedFrames.add(VideoList.get(2).Frames.get(135));
			util.videoobjects.HSVHistorgramMatcher(ProcessedFrames);
		}
		
	

		//Parameter tracking
		//util.trackerelems.TrackVideoObjects();

		//Visual Library Feature
		//util.library.BuildLibrary();
/*

			Pixel [][] Queryimg = new Pixel[height][width];
		int index = 0;
		Queryimg = util.Converter.ConverttoPixelArray(VideoList.get(0).Frames.get(5), width, height);
		Queryimg = util.Converter.ConvertRGBtoYUV(Queryimg, width, height);
		Queryimg = util.Converter.Normalizeby256(Queryimg, width, height);
		N = VideoList.get(1).Frames.size();
		Pixel [][][] Targetimg = new Pixel[N][height][width];
		util.TODEBUGS.DEBUG_PRINT(true, "Value of N"+N);
		int ti,tj = 0;
		int countframes = 0;
		for( ti = 0; ti < 55; ti++)
		{
			Targetimg[ti] = util.Converter.ConverttoPixelArray(VideoList.get(1).Frames.get(tj), width, height);
			Targetimg[ti] = util.Converter.ConvertRGBtoYUV(Targetimg[ti], width, height);
			Targetimg[ti] = util.Converter.Normalizeby256(Targetimg[ti], width, height);
			countframes++;
			tj+=5;
		}
		/*for(tj = ti; tj < VideoList.get(1).Frames.size(); tj+=10)
		{
			Targetimg[tj] = util.Converter.ConverttoPixelArray(VideoList.get(2).Frames.get(tj), width, height);
			Targetimg[tj] = util.Converter.ConvertRGBtoYUV(Targetimg[tj], width, height);
			Targetimg[tj] = util.Converter.Normalizeby256(Targetimg[tj], width, height);
		}
		N = countframes-1;
		util.TODEBUGS.DEBUG_PRINT(true, "Value of N"+N);


		Pixel [][] FinalImage = new Pixel[height][width];


		SearchStructure s[][] = new SearchStructure[height][width];

		//Initialize Search Structure for target images
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				s[i][j] = new SearchStructure();

		for(int i = 0; i < N; i++){
			util.TODEBUGS.DEBUG_PRINT(util.TODEBUGS.DEBUG, "Target Image 1\n");
			s = util.SearchEngine.BuildSearchStructure(s,Targetimg[i], width, height, i);
		}

		Queryimg = util.SearchEngine.DecomposeImage(Queryimg, width, height);
		double Score[] = new double[N];
		Score = util.SearchEngine.MatchSearchStructures(Queryimg, s, N, width, height);

		for(int i = 0; i < N; i++)
			util.TODEBUGS.DEBUG_PRINT(true, Score[i]+"\n");
		 
*/

		/*************************************OUTPUT**************************************************/
		/*BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage Outputimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			File file = new File(args[0]);
			InputStream is = new FileInputStream(file);

			long len = file.length();
			byte[] bytes = new byte[(int)len];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}


			int ind = 0;
			for(int y = 0; y < height; y++){

				for(int x = 0; x < width; x++){

					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
			{
				int pix = 0xff000000 | (((int)(FinalImage[y][x].getX()) & 0xff) << 16) | (((int)(FinalImage[y][x].getY()) & 0xff) << 8) | ((int)(FinalImage[y][x].getZ()) & 0xff);
				Outputimg.setRGB(x, y, pix);
			}

		// Use a label to display the image
		JFrame Inputframe = new JFrame();
		JLabel Inputlabel = new JLabel(new ImageIcon(img));
		Inputframe.getContentPane().add(Inputlabel, BorderLayout.CENTER);
		Inputframe.pack();
		Inputframe.setVisible(true);

		//Output the image after transition
		JFrame OutputFrame = new JFrame();
		JLabel OutputLabel = new JLabel(new ImageIcon(Outputimg));
		OutputFrame.getContentPane().add(OutputLabel, BorderLayout.CENTER);
		OutputFrame.pack();
		OutputFrame.setVisible(true);
		 */

	}

}
