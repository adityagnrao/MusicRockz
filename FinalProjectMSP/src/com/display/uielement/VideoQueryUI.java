package com.display.uielement;

import com.common.*;

import java.awt.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JFrame;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;

import com.common.videoAudioutility.*;

import javax.swing.JLayeredPane;
import javax.swing.JSlider;

public class VideoQueryUI {

        private JFrame frame;
        private JTextField textField;
        
        static java.util.List<VideoAudioShot> vlist;
        static java.util.List<String> res_val=new ArrayList<String>();
        private JList list_2;
        static boolean qissrgetable = false;
        static boolean rissrgetable = false;
        static boolean query_video_stop = false;
        static boolean query_video_pause = false;
        static boolean replay=false;
        static boolean Rreplay=false;
        static DrawGraph graphUtility;
        static VideoAudioShot QueryVideo;
        static VideoAudioShot ResultVideo;
    	static protected int video_start;
        JSlider scrollBar;

	// static FileInputStream inputStream;

        public static Thread t1, t2,t3,t4;
        JPanel graphPanel;
        JPanel panel_3;
        JPanel panel;
        JPanel panel_1;
        JPanel panel_2=new JPanel();
        GroupLayout gl_panel_1;
        static protected int video_index;


    static protected int audiostartbyte=0;
	static  boolean result_video_stop=false;
	static boolean result_video_pause=false;
	static VideoQueryUI window;
	static PlaySound playSound;
	static Playvideo playvideo;
	static RPlaySound rplaySound;
	static RPlayvideo rplayvideo;
	static String vfilename;
	static String afilename;
	static FileInputStream inputStream;
	static File resultvfile;
	static FileInputStream outputstream;

	static String video_fname;
	static String audio_fname;
	static String queryname;
	/**
	 * Launch the application.
	 */
	public void querystart() {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new VideoQueryUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		

		try {			

			inputStream = new FileInputStream(afilename);

			playSound = new PlaySound(inputStream);
			playvideo = new Playvideo(vfilename, playSound,57,40,window.panel_2);

			t1 = new Thread(playSound);
			 t2 = new Thread(playvideo);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		
	}

	/**
	 * Create the application.
	 * @param tempstr 
	 * @param args 
	 * @param extractedMetadata 
	 */
	public VideoQueryUI() {
		initialize();
	}


	public VideoQueryUI(java.util.List<VideoAudioShot> extractedMetadata, VideoAudioShot queryVideo2, String query_name, String[] res,DrawGraph GraphUtil) {
		// TODO Auto-generated constructor stub
		
			vfilename = query_name+".rgb";
			queryname=query_name;
		afilename =  query_name+".wav";
		res_val.add(res[0]+" %");
		res_val.add(res[1]+" %");
		res_val.add(res[2]+" %");
		
		vlist=extractedMetadata;
		QueryVideo=queryVideo2;
 graphUtility = GraphUtil;
		System.out.println("called");
		System.out.println("frames "+QueryVideo.getFrameSize());
	
	}

        /**
         * Initialize the contents of the frame.
         */
        private void initialize() {
                frame = new JFrame();
                frame.setResizable(false);
                frame.getContentPane().setBackground(Color.WHITE);
                frame.setBounds(100, 100, 1024, 576);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBackground(Color.WHITE);

                panel_1 = new JPanel();
                panel_1.setBounds(5, 5, 515, 70);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textField.setBackground(Color.WHITE);
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setText(queryname);

		// list_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                String rlist[]={vlist.get(0).getFileName(),vlist.get(1).getFileName(),vlist.get(2).getFileName()};
                list_2 = new JList(rlist); // data has type Object[]
		list_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		list_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 video_fname = "Rsrc_d/"+list_2.getSelectedValue().toString()
						+ ".rgb";
//				 PlayvideoComponent component1 = new PlayvideoComponent();
//				 component1.setBounds(57, 40, 352, 288 + 22);
//				 component1.setImg(null);
//				 window.panel_3.add(component1);
				 video_index=list_2.getSelectedIndex();
                                int index=vlist.get(video_index).getVideoID();
                                graphUtility.createAndShowGui(index, window.graphPanel, vlist.get(video_index).getListofFrames().get(0));
				 audio_fname ="Rsrc_d/"+list_2.getSelectedValue().toString() + ".wav";
				
				try {
					outputstream = new FileInputStream(audio_fname);
					rplaySound = new RPlaySound(outputstream);
					rplayvideo = new RPlayvideo(video_fname, rplaySound,57,40,window.panel_3);
					
                 System.out.println("t3 is being set up");
					t3 = new Thread(rplaySound);
					 t4 = new Thread(rplayvideo);
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		list_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		list_2.setLayoutOrientation(JList.VERTICAL);
		list_2.setVisibleRowCount(3);
		String rlist1[]={res_val.get(0),res_val.get(1),res_val.get(2)};
		JList list_1 = new JList(rlist1);
		list_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.setVisibleRowCount(3);
		
		gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(list_2, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(498, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(list_2, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
        scrollBar = new JSlider();
        scrollBar.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent arg0) {
        		
        		result_video_stop=true;
        	     		
        	}
        	@SuppressWarnings("static-access")
			@Override
        	public void mouseReleased(MouseEvent e) {
        		try {
        			audiostartbyte=(int) ((scrollBar.getValue()/24)*rplaySound.getSampleRate());
        			
        			video_start=scrollBar.getValue();
//        			audiostartbyte=(int) (((window.scrollBar.getValue()-5)/24)*(5+rplaySound.getSampleRate())*rplaySound.getSampleSize());
//					result_video_stop=rplayvideo.isFlag();
        			
        			replay=true;
					outputstream = new FileInputStream(audio_fname);
					rplaySound = new RPlaySound(outputstream);
					rplayvideo = new RPlayvideo(video_fname, rplaySound,57,40,window.panel_3);
					t3 = new Thread(rplaySound);
					t4 = new Thread(rplayvideo);
					t3.start();
					t3.sleep(500);
					t4.start();
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
        		
        	}
        });
		scrollBar.setBounds(579, 86, 363, 15);
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setMinimum(0);
		scrollBar.setValue(0);

		panel_3 = new JPanel();
		panel_3.setBounds(527, 99, 459, 383);
		panel_3.setOpaque(true);
                graphPanel = new JPanel();
                graphPanel.setBounds(579, 5, 459, 70);
                graphPanel.setVisible(true);

		JButton button_1 = new JButton("Play ");
		button_1.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if(result_video_pause==true)
					{
						synchronized(rplaySound)
						{
						result_video_pause=false;
						rplaySound.notifyAll();
						t3.sleep(100);
						synchronized(rplayvideo)
						{
							rplayvideo.notify();
						}
//						System.out.println("changing to false");
//						rplaySound.dataLine.start();
//						t3.resume();
//						t4.resume();
						}
					}
					else
					if(list_2.isSelectionEmpty())
					{
					
						JOptionPane.showMessageDialog(frame,
							    "please select a result video to play",
							    "Video not selected",
							    JOptionPane.PLAIN_MESSAGE);
					}
					else
					{
					if (t3.isAlive()) {
						
					}
					// if(t1.isInterrupted()|| t2.isInterrupted())
					// {
					//
					// t1 = new Thread(playSound);
					// t2 = new Thread(window.playvideo);
					// }
					else {

						if (result_video_stop==true) {
							try {
//								result_video_stop=rplayvideo.isFlag();
								outputstream = new FileInputStream(audio_fname);
								rplaySound = new RPlaySound(outputstream);
								rplayvideo = new RPlayvideo(video_fname, rplaySound,57,40,window.panel_3);
								
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							t3 = new Thread(rplaySound);
							t4 = new Thread(rplayvideo);
							t3.start();
							t3.sleep(500);
							t4.start();
						}
						else {
							t3.start();
System.out.println("i have come here");
							t3.sleep(500);
							t4.start();
						}
						
					}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		button_1.setBounds(565, 493, 91, 17);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});

//		JButton button_2 = new JButton("Pause");
//		button_2.addMouseListener(new MouseAdapter() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				try {
//					result_video_pause=true;
////					rplaySound.dataLine.stop();
////					t4.suspend();
//					
//					
////					t3.suspend();
//					
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		});
//		button_2.setBounds(728, 493, 97, 17);

		JButton button_3 = new JButton("Stop");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				result_video_stop=true;
				
			}
		});
		button_3.setBounds(899, 493, 68, 17);

		JButton button_4 = new JButton("Play ");
		button_4.setBounds(41, 493, 91, 17);
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					if (t1.isAlive()) {
						
					}
					// if(t1.isInterrupted()|| t2.isInterrupted())
					// {
					//
					// t1 = new Thread(playSound);
					// t2 = new Thread(window.playvideo);
					// }
					else {

//						t1 = new Thread(playSound);
//						t2 = new Thread(window.playvideo);
						if (query_video_stop==true) {
							try {
//								query_video_stop=playvideo.isFlag();
								
								inputStream = new FileInputStream(afilename);
								playSound = new PlaySound(inputStream);
								playvideo = new Playvideo(vfilename, playSound,57,40,window.panel_2);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							t1 = new Thread(playSound);
							t2 = new Thread(playvideo);
							System.out.println("I am replaying "+query_video_stop);
							t1.start();
							t1.sleep(300);
							t2.start();
						}
							
							
//						} else if (query_video_pause == true) {
//							t1.run();
//							t1.sleep(200);
//							t2.run();
//						} 
							else {
							t1.start();

							t1.sleep(300);
							t2.start();
						}
						
//						t1.start();
//
//						t1.sleep(500);
//						t2.start();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		JButton button_5 = new JButton("Stop");
		button_5.setBounds(386, 493, 68, 17);
		button_5.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent arg0) {

                                //                                t1.stop();
                                //                                // t1.sleep(10);
                                //                                t2.stop();
                                //                                query_video_stop = true;


                                query_video_stop=true;
                        }
                });
                panel_3.setLayout(null);
                graphPanel.setLayout(null);
                GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
                groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
                                Alignment.LEADING).addGroup(
                                                groupLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 988,
                                                                Short.MAX_VALUE).addContainerGap()));
                groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
                                Alignment.LEADING).addGroup(
                                                groupLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 516,
                                                                Short.MAX_VALUE).addContainerGap()));
                panel.setLayout(null);
                panel.add(panel_1);
                panel.add(button_4);
                panel.add(button_5);
                panel.add(button_1);
//                panel.add(button_2);
                panel.add(button_3);
                panel.add(scrollBar);
                panel.add(panel_3);
                panel.add(graphPanel);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setOpaque(true);
		panel_2.setBounds(5, 99, 482, 383);
		panel.add(panel_2);
		frame.getContentPane().setLayout(groupLayout);
	}
}
