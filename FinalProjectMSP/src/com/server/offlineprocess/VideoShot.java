package com.server.offlineprocess;

import com.common.videoutility.Pixel;

public class VideoShot {

	int frameNo;
	Pixel [][]RGBstream;
	
	public VideoShot(int width, int height) {
		RGBstream = new Pixel[height][width];
	}
}
