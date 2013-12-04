package com.common.videoAudioutility;

public class Debugger {
	
	public boolean DEBUG = false;
	
	
	public void DEBUG_PRINTLN(boolean flag, String ToPrint)
	{
		if(flag)
		System.out.println(ToPrint);
	}
	
	public void DEBUG_PRINT(boolean flag, String ToPrint)
	{
		if(flag)
		System.out.print(ToPrint);
	}

}
