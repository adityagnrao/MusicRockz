package com.server.offlineprocess;

import com.common.videoutility.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class VideoShotMetaDataExtractor {

	public static void main(String[] args) {
		
		VideoUtility util =  new VideoUtility();
		
		//RGB stream to MPEG-4 encoder
		util.encoder.ConvertRGBtoMP4();
		
		//Scene change detection algorithm
		util.scenechangedetector.FindScenechangedFrames();
		
		//Video-object Segmentation
		util.videoobjects.SegmenttoVideoObjects();
		
		//Parameter tracking
		util.trackerelems.TrackVideoObjects();
		
		//Visual Library Feature
		util.library.BuildLibrary();
		
		
		
	}
	
}
