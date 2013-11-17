package com.server.offlineprocess;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.common.videoutility.Pixel;

public class VideoShot {

	int VideoID;
	List<BufferedImage>Frames = new ArrayList<BufferedImage>();
	
	
	public VideoShot(int index) {
		VideoID = index;
	}
	
	public int getVideoID()
	{
		return VideoID;
	}
	
	public List<BufferedImage> getListofFrames(){
		return this.Frames;
	}
}
