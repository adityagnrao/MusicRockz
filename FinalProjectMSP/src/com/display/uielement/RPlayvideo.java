package com.display.uielement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.common.videoAudioutility.*;

class RPlayvideo extends VideoQueryUI implements Runnable {

	static long  numFrames=vlist.get(video_index).getFrameSize();

	public void run() {
		
		
		if (result_video_stop== true) {
//			query_video_stop = false;
//			query_video_pause = false;
//			replay(0, numFrames);
			play(video_start,numFrames);
			return;
		}
//		else if (query_video_pause == true) {
////			query_video_stop = false;
////			query_video_pause = false;
//			play(frameCounter, numFrames);
//			return;
//		}
		else {
//			query_video_stop = false;
//			query_video_pause = false;
			System.out.println("playing");
			play(video_start,numFrames);
		}

	}


	public RPlayvideo(String fileName, RPlaySound pSound,int x,int y,JPanel panel) {
		this.fileName = fileName;
		this.RplaySound = pSound;
		this.X=x;
		this.Y=y;
		this.pane=panel;
		
	}

	
		@SuppressWarnings("deprecation")
		private void play(long framenum, long numframes) {
			PlayvideoComponent component = new PlayvideoComponent();
			System.out.println("playing video");
			try {
				window.scrollBar.setMaximum((int) numFrames);
				System.out.println("naan bande nodu"+vlist.get(video_index).getFrameSize()+" num Frames"+numFrames);
				result_video_stop = false;
				result_video_pause = false;
				
				component.setBounds(X, Y, WIDTH, HEIGHT + 22);
				// component.setSize(WIDTH, HEIGHT+22);
				double spf = 0;
//				while (rissrgetable == false) {
//
//				}
				double sr = rplaySound.getSampleRate();
				// double sr=44100.0;
				if (sr != 0)
					spf = sr / FPS;

				int offset = 5; // only seems to work for Sample 2

				int j = (int) framenum;
//				System.out.println("naan illu bande nodu");
				while (j < Math.round(rplaySound.getPosition() / spf)) {
					window.scrollBar.setValue(j);
					// if(query_video_stop==true)
					// {
					// // t1.stop();
					// return;
					// }
					
//						System.out.println("vlist is null"+video_index);
//								System.out.println("vlist.size"+vlist.size());
                    if (result_video_pause == true) {
						
//						rplaySound.dataLine.stop();
            			rplaySound.clip.stop();
						synchronized(this)
						{
							while(result_video_pause == true)
							{
								this.wait();
								
								
							}
						}
					
//							rplaySound.dataLine.start();
						rplaySound.clip.start();
					}
					img=vlist.get(video_index).getListofFrames().get(j);
					component.setImg(img);
					pane.add(component);
					pane.repaint();
					pane.setVisible(true);

					j++;
				}

				// add the play video component to the UI Panel
				// panel_2.add(component);

				// Video ahead of audio, wait for audio to catch up
				while (j > Math.round(offset + RplaySound.getPosition() / spf)) {
					// Do Nothing
				}

				for (int i = j; i < numFrames; i++) {
					window.scrollBar.setValue(i);
					
					if (result_video_stop == true) {
						// t1.stop();
						window.scrollBar.setValue(0);
//						rplaySound.dataLine.stop();
//						rplaySound.dataLine.flush();
						rplaySound.clip.stop();
						rplaySound.clip.flush();
						
						component.setImg(null);
//						setframes(i, numFrames);
						frameCounter=0;
						video_start=0;
//						audiostartbyte=(int) ((window.scrollBar.getValue()/FPS)*sr*rplaySound.getSampleSize());
						return;
					}
					if (result_video_pause == true) {
						video_start=i;
						component.setImg(null);
//						rplaySound.dataLine.stop();
						rplaySound.clip.stop();
						synchronized(this)
						{
							while(result_video_pause == true)
							{
								this.wait();
								
								
							}
						}
						
//							rplaySound.dataLine.start();
						rplaySound.clip.start();
					}
					
					// Video ahead of audio, wait for audio to catch up
					while (i > Math.round(offset + RplaySound.getPosition() / spf)) {
						if(!RplaySound.getActive())
							return;
						// Do Nothing
					}

					// Audio ahead of video, roll video forward to catch up
					while (i < Math.round(RplaySound.getPosition() / spf)) {
						if(!RplaySound.getActive())
							return;
						if (result_video_pause == true) {
							video_start=i;
//							rplaySound.dataLine.stop();
							rplaySound.clip.stop();
							
						
							synchronized(this)
							{
								while(result_video_pause == true)
								{
									this.wait();
									
									
								}
							}
						
//								rplaySound.dataLine.start();
							rplaySound.clip.start();
						}
						window.scrollBar.setValue(i);
						img=vlist.get(video_index).getListofFrames().get(i);
						component.setImg(img);
						if(!RplaySound.getActive())
							return;
						pane.add(component);

						pane.repaint();
						pane.setVisible(true);
						i++;
					}
					img=vlist.get(video_index).getListofFrames().get(i);
					component.setImg(img);

					pane.add(component);

					pane.repaint();
					pane.setVisible(true);
					System.out.println(i);
				}
				result_video_stop=true;
				
				frameCounter=0;
				component.setImg(null);
				System.out.println("rePlay"+frameCounter);
				System.out.println(result_video_stop);

			} catch (Exception e) {
				e.printStackTrace();
			}finally
			{
				result_video_stop=true;	
				component.setImg(null);
				
				pane.add(component);
				pane.repaint();
//				int k=QueryVideo.getListofFrames().size();
				video_start=0;
				
			}

		}
//	

	private long frameCounter;
	private JPanel pane;
	private VideoAudioShot vs;
	private RPlaySound RplaySound;
	private String fileName;
	private final int WIDTH = 352;
	private final int HEIGHT = 288;
	// private final double FPS = 23.976; // Frames Per Second
	private final double FPS = 24; // Frames Per Second
	private InputStream is;
	private BufferedImage img;
	private byte[] bytes;
	private int X,Y;

}
class PlayvideoComponent extends JLabel {

	public void paintComponent(Graphics g) {

		// Recover Graphics2D
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(img, 0, 0, this);
	}

	public void setImg(BufferedImage newimg) {
		this.img = newimg;
	}

	private BufferedImage img;
}