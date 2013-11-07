package com.common.videoutility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MPEG4encoder {
	
	public MPEG4encoder() {
		// TODO Auto-generated constructor stub
	}
	
	public String ConvertRGBtoMP4()
	{
		Process tr;
		try {
			tr = Runtime.getRuntime().exec( new String[]{ "cmd.exe" } );
			String Command_to_execute = "ffmpeg -f rawvideo -vcodec rawvideo -s 352x288 -pix_fmt rgb24 -i C:/USC/Fall-2013/Multimedia-Parag/Final-Project/talk1/talk1.rgb -c:v libx264 -preset ultrafast -qp 0 C:/outputs.mp4";
			Runtime.getRuntime().exec(Command_to_execute);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "C:/output3.mp4";
	}

}
