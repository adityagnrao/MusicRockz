package com.common.videoutility;



import java.util.ArrayList;
import java.util.List;

public class ImageSearchEngine {

	static int M = 50;
	Debugger TODEBUG = new Debugger();

	double w[][] = {{5.00,0.83,1.01, 0.52, 0.47, 0.30},
			{19.21,1.26, 0.44, 0.53, 0.28, 0.14},
			{34.37,0.36, 0.45, 0.14, 0.18, 0.27}
	};

	public ImageSearchEngine() {

	}

	private Pixel [] DecomposeImageArray(Pixel [] RGBSrcimg, int lenght){

		Pixel RGBfinalimg [] = new Pixel[lenght];

		for(int i = 0; i < lenght; i++)
			RGBfinalimg[i] = new Pixel(-1, -1, -1, RGBSrcimg[i].i, RGBSrcimg[i].j);

		for(int i = 0; i < lenght; i++)
		{
			RGBfinalimg[i].X = RGBSrcimg[i].X/Math.sqrt(lenght);
			RGBfinalimg[i].Y = RGBSrcimg[i].Y/Math.sqrt(lenght);
			RGBfinalimg[i].Z = RGBSrcimg[i].Z/Math.sqrt(lenght);
		}

		while(lenght > 1)
		{
			lenght = lenght/2;
			for( int i = 0; i < lenght; i++)
			{

				RGBfinalimg[i].X = (RGBSrcimg[2*i].X + RGBSrcimg[2*i+1].X)/Math.sqrt(2);
				RGBfinalimg[i+lenght].X = (RGBSrcimg[2*i].X - RGBSrcimg[2*i+1].X)/Math.sqrt(2);

				RGBfinalimg[i].Y = (RGBSrcimg[2*i].Y + RGBSrcimg[2*i+1].Y)/Math.sqrt(2);
				RGBfinalimg[i+lenght].Y = (RGBSrcimg[2*i].Y - RGBSrcimg[2*i+1].Y)/Math.sqrt(2);

				RGBfinalimg[i].Z = (RGBSrcimg[2*i].Z + RGBSrcimg[2*i+1].Z)/Math.sqrt(2);
				RGBfinalimg[i+lenght].Z = (RGBSrcimg[2*i].Z - RGBSrcimg[2*i+1].Z)/Math.sqrt(2);
			}
		}

		return RGBfinalimg;
	}

	public Pixel [][] DecomposeImage(Pixel [][] RGBSrcimg, int width, int height)
	{
		Pixel [][] rGBTransposedimg = new Pixel[width][height];
		for(int i = 0; i < height; i++)
			RGBSrcimg[i] = DecomposeImageArray(RGBSrcimg[i], width);

		rGBTransposedimg = TrasnposeMatrix(RGBSrcimg, width, height);

		for(int j = 0; j < width; j++)
			rGBTransposedimg[j] = DecomposeImageArray(rGBTransposedimg[j], height);

		RGBSrcimg = TrasnposeMatrix(rGBTransposedimg, height, width);

		return RGBSrcimg;
	}

	private Pixel[][] TrasnposeMatrix(Pixel[][] rGBSrcimg, int width, int height) {

		Pixel [][] rGBFinalimg = new Pixel[width][height];

		for (int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				rGBFinalimg[i][j] = rGBSrcimg[j][i];
			}
		return rGBFinalimg;
	}


	public SearchStructure [][] BuildSearchStructure(SearchStructure s[][], Pixel[][] rGBimg, int width, int height, int TargetImageIndex) {
		rGBimg = DecomposeImage(rGBimg, width, height);
		s[0][0].AvgIntensityX.add(rGBimg[0][0].X);
		s[0][0].AvgIntensityY.add(rGBimg[0][0].Y);
		s[0][0].AvgIntensityZ.add(rGBimg[0][0].Z);
		return AddMaxElements(s, rGBimg, width, height, TargetImageIndex);
	}

	private SearchStructure[][] AddMaxElements(SearchStructure[][] s, Pixel[][] rGBimg, int width, int height, int TargetImageIndex) {

		List<Pixel>input = new ArrayList<Pixel>();
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				input.add(rGBimg[i][j]);

		Pixel element = new Pixel(-1,-1,-1,-1,-1);
		for(int i = 1; i <= M; i++)
		{
			element = findKthMax(input, i, 0);
			TODEBUG.DEBUG_PRINTLN(TODEBUG.DEBUG, element.X+" "+i);
			s[element.i][element.j].PosXarray.add(TargetImageIndex);

			element = findKthMax(input, i, 1);
			s[element.i][element.j].PosYarray.add(TargetImageIndex);

			element = findKthMax(input, i, 2);
			s[element.i][element.j].PosZarray.add(TargetImageIndex);
		}

		TODEBUG.DEBUG_PRINTLN(TODEBUG.DEBUG, "-----------------------------");
		for(int j = 0;  j < input.size(); j++)
		{
			Pixel temp = input.get(j);
			input.remove(j);
			temp.X = -temp.X;
			temp.Y = -temp.Y;
			temp.Z = -temp.Z;
			input.add(j, temp);
		}
		for(int i = 1; i <= M; i++)
		{
			element = findKthMax(input, i, 0);
			TODEBUG.DEBUG_PRINTLN(TODEBUG.DEBUG, element.X+" "+i);
			s[element.i][element.j].NegXarray.add(TargetImageIndex);

			element = findKthMax(input, i, 1);
			s[element.i][element.j].NegYarray.add(TargetImageIndex);

			element = findKthMax(input, i, 2);
			s[element.i][element.j].NegZarray.add(TargetImageIndex);
		}

		return s;
	}


	private static Pixel findKthMax(List<Pixel> input, int k, int index) {		

		if (input != null && input.size() > 0) {

			Pixel element = input.get(0);
			List<Pixel> largerThanElement = new ArrayList<Pixel>();
			List<Pixel> smallerThanElement = new ArrayList<Pixel>();

			for (int i = 1; i < input.size(); i++) {

				if(index == 0)
				{
					if (element.X < input.get(i).X) {
						largerThanElement.add(input.get(i));
					} else {
						smallerThanElement.add(input.get(i));
					}
				}
				else if (index == 1)
				{
					if (element.Y < input.get(i).Y) {
						largerThanElement.add(input.get(i));
					} else {
						smallerThanElement.add(input.get(i));
					}
				}
				else if(index == 2)
				{
					if (element.Z < input.get(i).Z) {
						largerThanElement.add(input.get(i));
					} else {
						smallerThanElement.add(input.get(i));
					}
				}


			}

			if (largerThanElement.size() == k - 1) {
				return element;
			}
			else if (largerThanElement.size() < k - 1) {
				return findKthMax(smallerThanElement, k - largerThanElement.size() - 1, index);
			} else if (largerThanElement.size() >= k) {       
				return findKthMax(largerThanElement, k, index);
			}
			return element; // To satisfy the crazy compiler
		} else {
			throw new IllegalArgumentException();
		}
	}

	public double[] MatchSearchStructures(Pixel[][] queryimg, SearchStructure[][] s, int N, int width, int height) {
		// TODO Auto-generated method stub
		double Score[] = new double[N];

		//initialize the scores of each target Image
		for(int i = 0; i < N; i++)
		{
			Score[i] += w[0][0]*(Math.abs(queryimg[0][0].X - s[0][0].AvgIntensityX.get(i)));
			Score[i] += w[1][0]*(Math.abs(queryimg[0][0].Y - s[0][0].AvgIntensityY.get(i)));
			Score[i] += w[2][0]*(Math.abs(queryimg[0][0].Z - s[0][0].AvgIntensityZ.get(i)));
		}
		//Build the Query Image Search Structure
		List<Pixel>input = new ArrayList<Pixel>();
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				input.add(queryimg[i][j]);


		Pixel element = new Pixel(-1,-1,-1,-1,-1);
		for(int i = 1; i <= M; i++)
		{
			element = findKthMax(input, i, 0);
			queryimg[element.i][element.j].X = 1;

			element = findKthMax(input, i, 1);
			queryimg[element.i][element.j].Y = 1;

			element = findKthMax(input, i, 2);
			queryimg[element.i][element.j].Z = 1;
		}
		for(int j = 0;  j < input.size(); j++)
		{
			Pixel temp = input.get(j);
			input.remove(j);
			temp.X = -temp.X;
			temp.Y = -temp.Y;
			temp.Z = -temp.Z;
			input.add(j, temp);
		}

		for(int i = 1; i <= M; i++)
		{

			element = findKthMax(input, i, 0);
			queryimg[element.i][element.j].X = -1;

			element = findKthMax(input, i, 1);
			queryimg[element.i][element.j].Y = -1;

			element = findKthMax(input, i, 2);
			queryimg[element.i][element.j].Z = -1;
		}


		for (int i = 0; i < height; i++)
			for(int j = 1; j < width; j++)
			{
				if( queryimg[i][j].X != -1 && queryimg[i][j].X != 1)
					queryimg[i][j].X = 0;
				if( queryimg[i][j].Y != -1 && queryimg[i][j].Y != 1)
					queryimg[i][j].Y = 0;
				if( queryimg[i][j].Z != -1 && queryimg[i][j].Z != 1)
					queryimg[i][j].Z = 0;
			}


		for(int i = 0; i < N; i++)
		{
			TODEBUG.DEBUG_PRINTLN(true, "Scores initial vals"+Score[i]);
		}

		for (int i = 0; i < height; i++)
			for(int j = 1; j < width; j++)
			{
				if( queryimg[i][j].X > 0)
				{
					for(int k = 0; k < s[i][j].PosXarray.size(); k++)
						Score[s[i][j].PosXarray.get(k)] -= w[0][bin(i,j)];
				}
				else if(queryimg[i][j].X == -1)
				{
					for(int k = 0; k < s[i][j].NegXarray.size(); k++)
						Score[s[i][j].NegXarray.get(k)] -= w[0][bin(i,j)];
				}

				if( queryimg[i][j].Y > 0)
				{
					for(int k = 0; k < s[i][j].PosYarray.size(); k++)
						Score[s[i][j].PosYarray.get(k)] -= w[1][bin(i,j)];
				}
				else if(queryimg[i][j].Y == -1)
				{
					for(int k = 0; k < s[i][j].NegYarray.size(); k++)
						Score[s[i][j].NegYarray.get(k)] -= w[1][bin(i,j)];
				}
				if( queryimg[i][j].Z > 0)
				{
					for(int k = 0; k < s[i][j].PosZarray.size(); k++)
						Score[s[i][j].PosZarray.get(k)] -= w[2][bin(i,j)];
				}
				else if(queryimg[i][j].Z == -1)
				{
					for(int k = 0; k < s[i][j].NegZarray.size(); k++)
						Score[s[i][j].NegZarray.get(k)] -= w[2][bin(i,j)];
				}

			}
		return Score;
	}

	private int bin(int i, int j) {
		int max = i;
		if(i < j)
			max = j;

		if(max < 5)
			return max;

		return 5;
	}

}
