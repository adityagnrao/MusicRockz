package com.display.uielement;
import com.common.videoAudioutility.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawGraph {
DrawGraphComponent graphdraw;
   List<GraphDatatracker> gtracker;
   int Vindex;

   public DrawGraph(List<GraphDatatracker> g) {
   this.gtracker = g;
   }

   

   public void createAndShowGui(int VideoIndex, JPanel frame, BufferedImage img) {
   graphdraw = new DrawGraphComponent(this.gtracker, VideoIndex);
   frame.removeAll();
   frame.repaint();
  // graphdraw.setOpaque(true);
      graphdraw.setBounds(0, 0, 363, 70);
      graphdraw.setPreferredSize(new Dimension(363, 70));
      frame.add(graphdraw);
      frame.setOpaque(true);
      frame.repaint();
      frame.setVisible(true);
   }
}

class DrawGraphComponent extends JLabel{
   private static final int MAX_SCORE = 20;
   private static final int PREF_W = 363;
   private static final int PREF_H = 70;
   private static final int BORDER_GAP = 0;
   private static final Color GRAPH_COLOR = Color.green;
   private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
   private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
   private static final int GRAPH_POINT_WIDTH = 2;
   private static final int GRAPH_POINT_WIDTH_CODE = 6;
   private static final int Y_HATCH_CNT = 10;
   List<GraphDatatracker> gtracker;
   int Vindex;
   
   
public DrawGraphComponent(List<GraphDatatracker> gtracker, int vindex) {
this.gtracker = gtracker;
this.Vindex = vindex;
}

@Override
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
      double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (gtracker.get(Vindex).getSize() - 1);

      List<Point> graphPoints = new ArrayList<Point>();
      for (int i = 0; i < gtracker.get(Vindex).getSize(); i++) {
         double x1 = i*getWidth()/gtracker.get(Vindex).getSize();
         double y1 = (getHeight()-BORDER_GAP) - gtracker.get(Vindex).getMatchdata()[i]*100;
         graphPoints.add(new Point((int)x1, (int)y1));
      }

      System.out.println("****"+getHeight()+getWidth());
      // create x and y axes 
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth(), getHeight() - BORDER_GAP);

      // create hatch marks for y axis. 
      for (int i = 0; i < Y_HATCH_CNT; i+=1) {
         int x0 = BORDER_GAP;
         int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
         int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
         int y1 = y0;
         g2.drawLine(x0, y0, x1, y1);
      }

      Stroke oldStroke = g2.getStroke();
      g2.setStroke(GRAPH_STROKE);
      g2.setStroke(oldStroke);      
      g2.setColor(GRAPH_POINT_COLOR);
      int prevX = BORDER_GAP, prevY = getHeight() - BORDER_GAP;
      for (int i = 0; i < graphPoints.size(); i++) {
         int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2+BORDER_GAP;
         int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;
         int ovalW = GRAPH_POINT_WIDTH;
         int ovalH = GRAPH_POINT_WIDTH;
         g2.fillOval(x, y, ovalW, ovalH);
         g2.drawLine(prevX, prevY, x, y);
         prevX  = x;
         prevY = y;
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }
}
