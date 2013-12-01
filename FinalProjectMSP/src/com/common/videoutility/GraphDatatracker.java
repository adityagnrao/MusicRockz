package com.common.videoutility;

public class GraphDatatracker {
	
	double []MatchData;
	double MatchSum;
	
	public GraphDatatracker(int TotalNoofFrames) {
		MatchData = new double[TotalNoofFrames];
		MatchSum = -1;
		
	}
	
	public double[] getMatchdata() {
		// TODO Auto-generated method stub
		return this.MatchData;
	}
	
	public double getMatchSum() {
		// TODO Auto-generated method stub
		return this.MatchSum;
	}
	
	public void SetMatchdata(int frameID, double matchsum) {
		// TODO Auto-generated method stub
		this.MatchData[frameID] = matchsum;
	}
	
	public void SetMatchSum(double matchsum) {
		// TODO Auto-generated method stub
		this.MatchSum = matchsum;
	}
	
}
