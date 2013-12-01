package com.common.videoutility;

import static com.googlecode.javacv.cpp.opencv_imgproc.CV_COMP_INTERSECT;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCompareHist;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_features2d.BFMatcher;
import com.googlecode.javacv.cpp.opencv_features2d.DMatchVectorVector;

public class VisualFeatureLibrary {
	
	double FeatureWeight = 0.8;
	double ColorbasedMatch = 0.2;
	Debugger VFLDebug = new Debugger();
	
	public VisualFeatureLibrary() {
		// TODO Auto-generated constructor stub
	}

	public GraphDatatracker MatchVideos(VideoShot Query, VideoShot DatabaseVideo)
	{
		BFMatcher matcher = new BFMatcher(4, false);
		DMatchVectorVector matchesvector1 = new DMatchVectorVector();
		DMatchVectorVector matchesvector2 = new DMatchVectorVector();
		double matchSum = 0;
		double temp = 0;
		GraphDatatracker Graphdata = new GraphDatatracker(DatabaseVideo.TotalNoofFrames);
		
		for(int queryimage = 0; queryimage < Query.DescriptorList.size(); queryimage++)
		{
			CvMat descriptors1 = Query.DescriptorList.get(queryimage);
			for(int databasevideo = 0; databasevideo < DatabaseVideo.DescriptorList.size(); databasevideo++)
			{
				CvMat descriptors2 = DatabaseVideo.DescriptorList.get(databasevideo);
				matcher.knnMatch(descriptors1, descriptors2, matchesvector1, 2, null, true);
				matcher.knnMatch(descriptors2, descriptors1, matchesvector2, 2, null, true);
				double m1 = ratioTest(matchesvector1);
				double m2 = ratioTest(matchesvector2);
				VFLDebug.DEBUG_PRINTLN(true, " ratiotest---------- "+m1+" "+m2);
				if(m1 < m2)
					temp = FeatureWeight*m2/Query.KeypointCapacity.get(queryimage);
				else
					temp = FeatureWeight*m1/DatabaseVideo.KeypointCapacity.get(databasevideo);
				matchSum += temp;
				 double matchValue=cvCompareHist(Query.HistogramValue.get(queryimage), DatabaseVideo.HistogramValue.get(databasevideo), CV_COMP_INTERSECT);
				 matchSum += ColorbasedMatch*matchValue;
				 VFLDebug.DEBUG_PRINTLN(true, " feature match "+temp);
				 Graphdata.SetMatchdata(DatabaseVideo.Frametracker.get(databasevideo), temp + ColorbasedMatch*matchValue );
				 
			}
		}
		Graphdata.SetMatchSum(matchSum);
		return Graphdata;
	}
	
	private int ratioTest(DMatchVectorVector matches){
		int j = 0;
		
		for (int i =0; i < matches.size(); i++) {
            // if 2 NN has been identified
			if (matches.size(i) > 0) 
			{
				if(matches.get(i, 0).distance() < 0.6*matches.get(i, 1).distance())
					{
						j++;
					}
			}
		}
		return j;
	}
	
	public void BuildLibrary() {
		// TODO Auto-generated method stub
		
	}

}
