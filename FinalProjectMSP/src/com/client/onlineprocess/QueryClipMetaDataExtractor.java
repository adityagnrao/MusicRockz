package com.client.onlineprocess;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.common.videoutility.GraphDatatracker;
import com.common.videoutility.VideoShot;
import com.common.videoutility.VideoUtility;

public class QueryClipMetaDataExtractor {

	public static void main(String[] args) throws IOException {
	int width = 352;
	int height = 288;
	VideoShot QueryVideo = new VideoShot(0);
	VideoUtility util = new VideoUtility();
	List<VideoShot>ExtractedMetadata = VideoMetadataReader("C:\\Metadata.msp");
	
	int N = Integer.parseInt(args[0]);
	try {
			File file = new File(args[1]);
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
	
	for(int i = 1; i < N; i++)
	{
		GraphDatatracker g = util.library.MatchVideos(QueryVideo, ExtractedMetadata.get(i));
		util.TODEBUGS.DEBUG_PRINTLN(true, " "+ g.getMatchSum());
	}
	System.out.print("appa guruve shambo shankara");
	JFrame Inputframe = new JFrame();
	JLabel Inputlabel = new JLabel(new ImageIcon(ExtractedMetadata.get(0).getListofProcessedFrames().get(0)));
	Inputframe.getContentPane().add(Inputlabel, BorderLayout.CENTER);
	Inputframe.pack();
	Inputframe.setVisible(true);
	
	}

	private static List<VideoShot> VideoMetadataReader(String string) throws IOException {
		ObjectInputStream objectinputstream = null;
		FileInputStream streamIn = null;
		List<VideoShot>readCase = null;
		 try {
		        streamIn = new FileInputStream(string);
		        objectinputstream = new ObjectInputStream(streamIn);
		        readCase = (List<VideoShot>) objectinputstream.readObject();

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
