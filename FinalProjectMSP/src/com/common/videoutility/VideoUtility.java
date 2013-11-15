package com.common.videoutility;

public class VideoUtility {
	
	public MPEG4encoder encoder;
	public SceneChangeDetector scenechangedetector;
	public VideoObjectSegmentation videoobjects;
	public VisualFeatureLibrary library;
	public ParameterTracker trackerelems;
	public ImageSearchEngine SearchEngine;
	public PixelArray Converter;
	public Debugger TODEBUGS;
	
	public VideoUtility() {
		
		encoder = new MPEG4encoder();
		scenechangedetector = new SceneChangeDetector();
		videoobjects = new VideoObjectSegmentation();
		library = new VisualFeatureLibrary();
		trackerelems = new ParameterTracker();
		SearchEngine = new ImageSearchEngine();
		Converter = new PixelArray();
		TODEBUGS = new Debugger();
	}
}
