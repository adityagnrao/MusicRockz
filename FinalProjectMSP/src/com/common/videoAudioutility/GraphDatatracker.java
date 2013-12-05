package com.common.videoAudioutility;

public class GraphDatatracker {
	
	double []MatchData;
	double MatchSum;
	double AudioMatchSum;
	int NoofElementstotrace;
	
	public GraphDatatracker(int TotalNoofFrames) {
		MatchData = new double[TotalNoofFrames];
		MatchSum = -1;
		AudioMatchSum = -1;
		NoofElementstotrace = TotalNoofFrames;
	}
	
	public int getSize(){
		return this.NoofElementstotrace;
	}
	
	public double[] getMatchdata() {
		// TODO Auto-generated method stub
		return this.MatchData;
	}
	
	public double getMatchSum() {
		// TODO Auto-generated method stub
		return this.MatchSum;
	}
	
	public double getAudioMatchSum() {
		// TODO Auto-generated method stub
		return this.AudioMatchSum;
	}
	
	public void SetMatchdata(int frameID, double matchsum) {
		// TODO Auto-generated method stub
		this.MatchData[frameID] += matchsum;
	}
	
	public void SetMatchSum(double matchsum) {
		// TODO Auto-generated method stub
		this.MatchSum = matchsum;
	}

	public void SetAudioMatch(float score) {
		// TODO Auto-generated method stub
		this.AudioMatchSum = score;
	}
	
}
