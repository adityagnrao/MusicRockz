package com.common.videoutility;


import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PixelArray {
	
	static double [] RGBtoYUV = {0.299, 0.587, 0.114,
		-0.147, -0.289, 0.436,
		0.615, -0.515, -0.100};
	static double [] YUVtoRGB = {0.999, 0.000, 1.140,
		1.000, -0.395, -0.581,
		1.000, 2.032, 0.000};
	
	public Pixel [][] ConvertRGBtoYUV(Pixel [][] srcImage, int width, int height)
	{
		Pixel YUVimg [][] = new Pixel[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
			{
				YUVimg[i][j] = MatrixMultiplication(srcImage[i][j], true);
				YUVimg[i][j].i = srcImage[i][j].i;
				YUVimg[i][j].j = srcImage[i][j].j;
			}
		return YUVimg;
	}
	
	
	/*
	 * Method is used to convert YUV to RGB values for all the pixels in the image
	 */
	public Pixel [][] ConvertYUVtoRGB(Pixel [][] srcImage, int width, int height)
	{

		Pixel RGBimg [][] = new Pixel[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
			{
				RGBimg[i][j] = MatrixMultiplication(srcImage[i][j], false);
				RGBimg[i][j].i = srcImage[i][j].i;
				RGBimg[i][j].j = srcImage[i][j].j;
			}
		return RGBimg;
	}
	
	
	public Pixel [][] Normalizeby256(Pixel [][] srcImage, int width, int height)
	{
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				{
					srcImage[i][j].X/=256;
					srcImage[i][j].Y/=256;
					srcImage[i][j].Z/=256;
				}
		
		return srcImage;
	}
	
	Pixel MatrixMultiplication(Pixel SrcImage, boolean RGB){

		Pixel DestImage = null;

		if(RGB == true)
		{

			double Y = SrcImage.X * RGBtoYUV[0] + SrcImage.Y * RGBtoYUV[1] + SrcImage.Z * RGBtoYUV[2];
			double U = SrcImage.X * RGBtoYUV[3] + SrcImage.Y * RGBtoYUV[4] + SrcImage.Z * RGBtoYUV[5];
			double V = SrcImage.X * RGBtoYUV[6] + SrcImage.Y * RGBtoYUV[7] + SrcImage.Z * RGBtoYUV[8];
			DestImage = new Pixel(Y, U, V);
		}
		else
		{

			double R = Math.abs(SrcImage.X * YUVtoRGB[0] + SrcImage.Y * YUVtoRGB[1] + SrcImage.Z * YUVtoRGB[2]);
			double G = Math.abs(SrcImage.X * YUVtoRGB[3] + SrcImage.Y * YUVtoRGB[4] + SrcImage.Z * YUVtoRGB[5]);
			double B = Math.abs(SrcImage.X * YUVtoRGB[6] + SrcImage.Y * YUVtoRGB[7] + SrcImage.Z * YUVtoRGB[8]);
			DestImage = new Pixel(R, G, B);
		}


		return DestImage;
	}
	
	public Pixel [][] ConverttoPixelArray(String fname, int width, int height) throws IOException
	{
		//BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		File file = new File(fname);
		BufferedImage img = ImageIO.read(file);

		int[] pixels = img.getRGB(0,0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

			int ind = 0;
			for(int y = 0; y < height; y++){

				for(int x = 0; x < width; x++){

					byte a = 0;
					byte r = (byte) pixels[ind];
					byte g = (byte) pixels[ind];
					byte b = (byte) pixels[ind];  

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}



		Pixel RGBimg[][] = new Pixel[height][width];

		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
			{
				int r = (img.getRGB(x, y) & 0x00ff0000)>>16;
			int g = (img.getRGB(x, y) & 0x0000ff00)>>8;
					int b = (img.getRGB(x, y) & 0x000000ff);
					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					RGBimg[y][x] = new Pixel(r,g,b,y,x);

			}
		
		JFrame Inputframe = new JFrame();
		JLabel Inputlabel = new JLabel(new ImageIcon(img));
		Inputframe.getContentPane().add(Inputlabel, BorderLayout.CENTER);
		Inputframe.pack();
		Inputframe.setVisible(true);

		return RGBimg;
	}

}
