package com.common.videoutility;
/*
 * Author: Preetham M S
 * Class description: MulImage is a class consisting of 3 pixels
 * It can be used with YUV or RGB pixel
 */
public class Pixel {

	//3 pixels  -- can be RGB or YUV
	double X;
	double Y;
	double Z;
	int i;
	int j;
	double Angle;

	public Pixel(int r, int g, int b, int i, int j) {
		// TODO Auto-generated constructor stub
		this.X = r;
		this.Y = g;
		this.Z = b;
		this.i = i;
		this.j = j;
	}

	public Pixel(double r, double g, double b) {
		// TODO Auto-generated constructor stub
		this.X = r;
		this.Y = g;
		this.Z = b;
		this.i = -1;
		this.j = -1;
	}

	public double getX() {
		// TODO Auto-generated method stub
		return X;
	}
	
	public double getY() {
		// TODO Auto-generated method stub
		return Y;
	}
	
	public double getZ() {
		// TODO Auto-generated method stub
		return Z;
	}
	
	public double getAngle(){
		return Angle;
	}

}
