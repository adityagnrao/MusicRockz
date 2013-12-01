package com.common.audioutility;

import com.musicg.wave.Wave;

public class AudioSample {
	public float audio_rating;
	public float getAudio_rating() {
		return audio_rating;
	}
	public void setAudio_rating(float audio_rating) {
		this.audio_rating = audio_rating;
	}
	public Wave getAudio_wave() {
		return audio_wave;
	}
	public void setAudio_wave(Wave audio_wave) {
		this.audio_wave = audio_wave;
	}
	public Wave audio_wave;
	public AudioSample()
	{
		audio_rating=0;
		audio_wave=new Wave();
	}
	public AudioSample(Wave newwave)
	{
		audio_rating=0;
		audio_wave=newwave;
	}
	
}
