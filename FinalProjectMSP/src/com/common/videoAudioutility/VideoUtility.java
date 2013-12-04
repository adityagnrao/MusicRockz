package com.common.videoAudioutility;

public class VideoUtility {
	
	public MPEG4encoder encoder;
	public SceneChangeDetector scenechangedetector;
	public VisualAudioFeatureLibrary library;
	public GraphDatatracker trackerelems;
	public ImageSearchEngine SearchEngine;
	public PixelArray Converter;
	public Debugger TODEBUGS;
	
	public VideoUtility() {
		
		encoder = new MPEG4encoder();
		scenechangedetector = new SceneChangeDetector();
		library = new VisualAudioFeatureLibrary();
		SearchEngine = new ImageSearchEngine();
		Converter = new PixelArray();
		TODEBUGS = new Debugger();
	}
}
