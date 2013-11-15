package com.common.videoutility;

import java.util.ArrayList;
import java.util.List;

public class SearchStructure {
	
	List<Double> AvgIntensityX = new ArrayList<Double>();
	List<Double> AvgIntensityY = new ArrayList<Double>();
	List<Double> AvgIntensityZ = new ArrayList<Double>();
	
	List<Integer> PosXarray = new ArrayList<Integer>();
	List<Integer> NegXarray = new ArrayList<Integer>();
	List<Integer> PosYarray = new ArrayList<Integer>();
	List<Integer> NegYarray = new ArrayList<Integer>();
	List<Integer> PosZarray = new ArrayList<Integer>();
	List<Integer> NegZarray = new ArrayList<Integer>();
	
	
	public SearchStructure() {
	
	}
	
	public List<Integer> getPosXarray(){
		return this.PosXarray;
	}
	
	public List<Integer> getNegXarray(){
		return this.NegXarray;
	}
	public List<Integer> getPosYarray(){
		return this.PosYarray;
	}
	public List<Integer> getNegYarray(){
		return this.NegYarray;
	}
	public List<Integer> getPosZarray(){
		return this.PosZarray;
	}
	public List<Integer> getNegZarray(){
		return this.NegZarray;
	}
}
