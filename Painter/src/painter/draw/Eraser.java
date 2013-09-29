package painter.draw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import painter.Main;

public class Eraser extends Draw {

	private ArrayList<Point> pointList;
	
	private static int			thickness 		= Main.THICKNESS_DEFAULT_ERASER;
	private static final int[] 	THICKNESS_LIST 	= {0, 3, 7, 11, 15, 19};
	private static BasicStroke 	b 				= new BasicStroke(	THICKNESS_LIST[thickness], 
																	BasicStroke.CAP_ROUND, 
																	BasicStroke.JOIN_ROUND, 
																	0,
																	new float[] { 3, 0 },
																	0);
	
	public Eraser() 
	{
		pointList = new ArrayList<Point>();
		
		System.out.print("Eraser() constructor called \n");
	}


	@Override
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e) 
	{ 
		int x = e.getX();
		int y = e.getY();
		
		pointList.add(new Point(x, y));
		
		for (int i = 1; i < pointList.size(); i++){
			Point p1 = pointList.get(i-1);
			Point p2 = pointList.get(i);
			
			((Graphics2D)g).setStroke(b);
			
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		
		return bi; 
	}
	
	public static void setThickness(int t)
	{
		thickness = t;
		System.out.print("eraser's thickness is changed to " + THICKNESS_LIST[thickness]);
		b = new BasicStroke(THICKNESS_LIST[thickness], b.getEndCap(), b.getLineJoin(), b.getMiterLimit(),
				   b.getDashArray(), b.getDashPhase());
	}
	

}
