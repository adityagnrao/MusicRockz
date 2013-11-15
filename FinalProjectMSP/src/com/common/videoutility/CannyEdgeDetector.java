package com.common.videoutility;



public class CannyEdgeDetector {

	double Gaussianfilter[][] =
		{
			{2, 4, 5, 4, 2},
		    {4, 9, 12, 9, 4},
		    {5, 12, 15, 12, 5},
		    {4,  9,  12,  9, 4},
		    {2, 4, 5, 4, 2},
		};
	double factor = 1.0 / 159.0;
	int filterWidth = 5;
	int filterHeight = 5;
	
	double SobelKernelX[][] =
		{
			{1, 0, -1},
		    {2, 0, -2},
		    {1, 0, -1},
		};
	
	double SobelKernelY[][] =
		{
			{1, 2, 1},
		    {0, 0, 0},
		    {-1, -2, -1},
		};
	
	int KernelWidth = 3;
	int KernelHeight = 3;
	
	public CannyEdgeDetector() {
		
	}
	public Pixel [][] EdgeDetectorProcess(Pixel [][]srcImPixels, int width, int height){
		
		srcImPixels = NoiseReduction(srcImPixels, width, height);
		srcImPixels = IntensityGradient(srcImPixels, width, height);
		//srcImPixels = NonMaximaSuppression(srcImPixels, width, height);
		//srcImPixels = HysterisThreshold(srcImPixels, width, height);
		
		return srcImPixels;
	}
	
	private Pixel [][] NoiseReduction(Pixel [][] srcImage, int width, int height){
		
		Pixel [][] result = new Pixel[height][width];
		for(int j = 0; j < height; j++)
			for(int i = 0; i < width; i++)
				result[j][i] = new Pixel(0,0,0);
		for(int x = 0; x < height; x++) 
			for(int y = 0; y < width; y++) 
			{ 
				double luminance = 0.0;

				//multiply every value of the filter with corresponding image pixel 
				for(int filterX = 0; filterX < filterWidth; filterX++) 
					for(int filterY = 0; filterY < filterHeight; filterY++) 
					{ 
						int imageX = (x - filterWidth / 2 + filterX + height) % height; 
						int imageY = (y - filterHeight / 2 + filterY + width) % width; 
						luminance += srcImage[imageX][imageY].X * Gaussianfilter[filterX][filterY]; 
					} 



				//truncate values smaller than zero and larger than 255 
				result[x][y].X = min(max((factor * luminance), 0.0), 255); 
			}    
		return result;
	}
	
	private double max(double a, double b){
		if(a < b)
			return b;

		return a;

	}
	
	private double min(double a, double b){
		if(a < b)
			return a;

		return b;

	}
	
	private Pixel [][] IntensityGradient(Pixel [][] srcImage, int width, int height){
		
		Pixel [][] result = new Pixel[height][width];
		for(int j = 0; j < height; j++)
			for(int i = 0; i < width; i++)
				result[j][i] = new Pixel(0,0,0);
		for(int x = 0; x < height; x++) 
			for(int y = 0; y < width; y++) 
			{ 
				double luminance = 0.0;

				//multiply every value of the filter with corresponding image pixel 
				for(int filterX = 0; filterX < KernelWidth; filterX++) 
					for(int filterY = 0; filterY < KernelHeight; filterY++) 
					{ 
						int imageX = (x - KernelWidth / 2 + filterX + height) % height; 
						int imageY = (y - KernelHeight / 2 + filterY + width) % width; 
						luminance += srcImage[imageX][imageY].X * SobelKernelX[filterX][filterY]; 
					} 
		
				//truncate values smaller than zero and larger than 255 
				result[x][y].X = min(max((luminance), 0.0), 255); 

			}    
		
		for(int x = 0; x < height; x++) 
			for(int y = 0; y < width; y++) 
			{ 
				double luminance = 0.0;

				//multiply every value of the filter with corresponding image pixel 
				for(int filterX = 0; filterX < KernelWidth; filterX++) 
					for(int filterY = 0; filterY < KernelHeight; filterY++) 
					{ 
						int imageX = (x - KernelWidth / 2 + filterX + height) % height; 
						int imageY = (y - KernelHeight / 2 + filterY + width) % width; 
						luminance += srcImage[imageX][imageY].X * SobelKernelY[filterX][filterY]; 
					} 
		
				//truncate values smaller than zero and larger than 255 
				result[x][y].X = min(max((luminance), 0.0), 255); 

			}  
		
		return result;
	}
	

	private Pixel [][] NonMaximaSuppression(Pixel [][] srcImage, int width, int height){
		
		return srcImage;
	}
	
	private Pixel [][] HysterisThreshold(Pixel [][] srcImage, int width, int height){
		
		return srcImage;
	}
}
