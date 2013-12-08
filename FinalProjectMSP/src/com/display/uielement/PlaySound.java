package com.display.uielement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JLabel;

class PlaySound extends VideoQueryUI implements Runnable {

	public PlaySound(InputStream waveStream) {
		this.waveStream = waveStream;

	}

	public void run() {
		try {
			if (query_video_stop == true) {
				// query_video_stop = false;
				replay();
			}
//			} else if (query_video_stop == true) {
//				// query_video_stop = false;
//				this.notifyAll();
//			} 
				else {
//				 query_video_stop = false;
				this.play();
			}
		} catch (PlayWaveException e) {
			e.printStackTrace();
			return;
		}
	}

	public void play() throws PlayWaveException {

		try {
			query_video_stop = false;
			query_video_pause = false;
			InputStream bufferedIn = new BufferedInputStream(this.waveStream);
			audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
		} catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		audioFormat = audioInputStream.getFormat();
		Info info = new Info(SourceDataLine.class, audioFormat);
		dataLine = null;

		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
			throw new PlayWaveException(e1);
		}
		qissrgetable = true;
		// Starts the music :P
		dataLine.start();

		int readBytes = 0;
		byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];

		try {
			while (readBytes != -1) {

				if (query_video_stop == true) {
					dataLine.stop();
					dataLine.flush();
					inputStream.close();
					return;
				} else if (query_video_pause == true) {
					dataLine.stop();
					synchronized(this)
					{
						while(result_video_pause == true)
						{
							System.out.print("1");
						}
					}
						dataLine.start();
				} else {
					readBytes = audioInputStream.read(audioBuffer, 0,
							audioBuffer.length);
					if (readBytes >= 0) {
						dataLine.write(audioBuffer, 0, readBytes);

					}
				}

			}

		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();
			dataLine.close();
			query_video_stop = true;
		}
	}

	private void replay() throws PlayWaveException {
		System.out.println("I can come here too");
		query_video_stop = false;
		query_video_pause = false;
		replay=true;

		try {

			InputStream bufferedIn = new BufferedInputStream(this.waveStream);
			audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
		} catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}
		int readBytes = 0;
		byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];
		qissrgetable = true;
		audioFormat = audioInputStream.getFormat();
		Info info = new Info(SourceDataLine.class, audioFormat);
		dataLine = null;

		try {
			dataLine = (SourceDataLine) AudioSystem.getLine(info);
			dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
		} catch (LineUnavailableException e1) {
			throw new PlayWaveException(e1);
		}
		// Starts the music :P
//		query_video_stop = false;
//		query_video_pause = false;
		dataLine.start();
		
//		while (qissrgetable == true) {
//
//		}
//		System.out.println("replaying audio");
		try {
			while (readBytes != -1) {
				if (query_video_stop == true) {
					System.out.println("not replaying audio ");
					dataLine.stop();
					dataLine.flush();
					inputStream.close();

					return;
				} else if (query_video_pause == true) {
					dataLine.stop();
					try {
						this.wait();
						dataLine.start();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("replaying audio");
					readBytes = audioInputStream.read(audioBuffer, 0,
							audioBuffer.length);
					if (readBytes >= 0) {
						dataLine.write(audioBuffer, 0, readBytes);

					}
				}

			}

		} catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		finally {
			// plays what's left and and closes the audioChannel
			dataLine.drain();

			dataLine.stop();
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataLine.flush();

			dataLine.close();
			query_video_stop = true;

		}

	}

	public long getPosition() {
		return dataLine.getLongFramePosition();
			}
	public boolean getActive() {
		return dataLine.isActive();
			}
	public float getSampleRate() {
		// if (this == null)
		// System.out.print("null=====");
		return audioFormat.getFrameRate();
	}

	public SourceDataLine dataLine;
	private AudioFormat audioFormat;
	private InputStream waveStream;
	private final int EXTERNAL_BUFFER_SIZE = 524288;
	static AudioInputStream audioInputStream = null;

	// private boolean Flag;
	// public boolean isFlag() {
	// return Flag;
	// }
	//
	// public void setFlag(boolean flag) {
	// Flag = flag;
	// }
	//
	// private boolean pFLag;
	// public boolean ispFLag() {
	// return pFLag;
	// }
	//
	// public void setpFLag(boolean pFLag) {
	// this.pFLag = pFLag;
	// }
	// }

}
	class PlayWaveException extends Exception {

		public PlayWaveException(String message) {
			super(message);
		}

		public PlayWaveException(Throwable cause) {
			super(cause);
		}

		public PlayWaveException(IOException cause) {
			super(cause);
		}

		public PlayWaveException(String message, Throwable cause) {
			super(message, cause);
		}

	}
