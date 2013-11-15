package com.common.videoutility;


public class SceneChangeDetector {
	
	public SceneChangeDetector() {
		// TODO Auto-generated constructor stub
	}

	public Pixel[][] FindScenechangedFrames(Pixel [][] sPixels, int width, int height) {
		CannyEdgeDetector cobj = new CannyEdgeDetector();
		sPixels = cobj.EdgeDetectorProcess(sPixels, width, height);
		//TODO - Rest of Scene detection process
		return sPixels;
	}

}
