package com.client.onlineprocess;
import com.common.audioutility.*;
import com.musicg.wave.Wave;
public class AudioRatingDetector {
	AudioUtility audio=new AudioUtility();
	public void findaudio(Wave query_wave)
	{
		for(int i=0;i<audio.audio_list.size();i++)
		{
			AudioSample sample=audio.audio_list.get(i);
			if(sample.getAudio_wave().getFingerprintSimilarity(query_wave).getSimilarity()==1)
			{
				sample.setAudio_rating(1);
				break;
			}
			else 
			{
				audio.audio_list.get(i).audio_rating=audio.audio_list.get(i).getAudio_wave().getFingerprintSimilarity(query_wave).getSimilarity();
			}
		}
		
	}

}
