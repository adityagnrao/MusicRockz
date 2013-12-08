package com.display.uielement;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.common.videoAudioutility.VideoAudioShot;

class Playvideo extends VideoQueryUI implements Runnable {

	static long  numFrames=QueryVideo.getFrameSize() ;

	public void run() {
		System.out.println("checking "+query_video_stop);
		
		if (query_video_stop== true) {
//			query_video_stop = false;
//			query_video_pause = false;
//			replay(0, this.numFrames);
			play(0,numFrames);
//			return;
		}
//		} else if (query_video_pause == true) {
////			query_video_stop = false;
////			query_video_pause = false;
//			replay(frameCounter, numFrames);
//			return;
//		}
		else {
//			query_video_stop = false;
//			query_video_pause = false;
//			System.out.println("playing");
			play(0,numFrames);
		}

	}


	public Playvideo(String fileName, PlaySound pSound,int x,int y,JPanel panel) {
		this.fileName = fileName;
		this.QplaySound = pSound;
		this.X=x;
		this.Y=y;
		this.pane=panel;
		
	}

	
   
	private void play(long framenum, long numframes) {
		PlayvideoComponent component = new PlayvideoComponent();
		
		try {

			query_video_stop = false;
			query_video_pause = false;
			
			component.setBounds(X, Y, WIDTH, HEIGHT + 22);
			// component.setSize(WIDTH, HEIGHT+22);
			double spf = 0;
			while (qissrgetable == false) {

			}
			double sr = QplaySound.getSampleRate();
			// double sr=44100.0;
			if (sr != 0)
				spf = sr / FPS;

			int offset = 5; // only seems to work for Sample 2

			int j = (int) framenum;
//			System.out.println("naan illu bande nodu");
			while (j < Math.round(QplaySound.getPosition() / spf)) {
				// if(query_video_stop==true)
				// {
				// // t1.stop();
				// return;
				// }
				
					System.out.println("vlist is null"+video_index);
							System.out.println("vlist.size"+vlist.size());
				img=QueryVideo.getListofFrames().get(j);
				component.setImg(img);
				pane.add(component);
				pane.repaint();
				pane.setVisible(true);
System.out.println(j);
				j++;
			}

			// add the play video component to the UI Panel
			// panel_2.add(component);

			// Video ahead of audio, wait for audio to catch up
			while (j > Math.round(offset + QplaySound.getPosition() / spf)) {
				// Do Nothing
			}

			for (int i = j; i < numFrames; i++) {
				System.out.println(i);
				if (query_video_stop == true) {
					// t1.stop();
					QplaySound.dataLine.stop();
					QplaySound.dataLine.flush();
					
					component.setImg(null);
//					setframes(i, numFrames);
					frameCounter=0;
					return;
				}
				// Video ahead of audio, wait for audio to catch up
				while (i > Math.round(offset + QplaySound.getPosition() / spf)) {
					// Do Nothing
					if(!QplaySound.getActive())
						return;
				}

				// Audio ahead of video, roll video forward to catch up
				while (i < Math.round(QplaySound.getPosition() / spf)) {
					if(!QplaySound.getActive())
						return;
					img=QueryVideo.getListofFrames().get(i);
					component.setImg(img);

					pane.add(component);

					pane.repaint();
					pane.setVisible(true);
					i++;
					
				}
				img=QueryVideo.getListofFrames().get(i);
				component.setImg(img);

				pane.add(component);

				pane.repaint();
				pane.setVisible(true);
//				System.out.println(i);
			}
			query_video_stop=true;
			
			frameCounter=0;
			component.setImg(null);
			System.out.println("rePlay"+frameCounter);
			System.out.println(query_video_stop);

		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			query_video_stop=true;	
			component.setImg(null);
			audiostartbyte=0;
			pane.add(component);
			pane.repaint();
//			int k=QueryVideo.getListofFrames().size();
			
		}

	}
//

	

	private long frameCounter;
	private JPanel pane;
	private VideoAudioShot vs;
	private PlaySound QplaySound;
	private String fileName;
	private final int WIDTH = 352;
	private final int HEIGHT = 288;
	// private final double FPS = 23.976; // Frames Per Second
	private final double FPS = 24; // Frames Per Second
	private InputStream is;
	private BufferedImage img;
	private byte[] bytes;
	private int X,Y;
//	private boolean Flag;
//	public boolean isFlag() {
//		return Flag;
//	}
//
//	public void setFlag(boolean flag) {
//		Flag = flag;
//	}
//
//	private boolean pFLag;
//
//	public boolean ispFLag() {
//		return pFLag;
//	}
//
//	public void setpFLag(boolean pFLag) {
//		this.pFLag = pFLag;
//	}
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
