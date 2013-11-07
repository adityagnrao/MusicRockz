package com.common.videoutility;

public class VideoUtility {
	
	public MPEG4encoder encoder;
	public SceneChangeDetector scenechangedetector;
	public VideoObjectSegmentation videoobjects;
	public VisualFeatureLibrary library;
	public ParameterTracker trackerelems;
	
	
	public VideoUtility() {
		
		encoder = new MPEG4encoder();
		scenechangedetector = new SceneChangeDetector();
		videoobjects = new VideoObjectSegmentation();
		library = new VisualFeatureLibrary();
		trackerelems = new ParameterTracker();
	}
}
