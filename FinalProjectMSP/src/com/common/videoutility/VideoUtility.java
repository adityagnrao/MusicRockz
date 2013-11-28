package com.common.videoutility;

public class VideoUtility {
	
	public MPEG4encoder encoder;
	public SceneChangeDetector scenechangedetector;
	public VisualFeatureLibrary library;
	public GraphDatatracker trackerelems;
	public ImageSearchEngine SearchEngine;
	public PixelArray Converter;
	public Debugger TODEBUGS;
	
	public VideoUtility() {
		
		encoder = new MPEG4encoder();
		scenechangedetector = new SceneChangeDetector();
		library = new VisualFeatureLibrary();
		SearchEngine = new ImageSearchEngine();
		Converter = new PixelArray();
		TODEBUGS = new Debugger();
	}
}
