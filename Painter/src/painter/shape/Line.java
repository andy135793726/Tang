package painter.shape;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;

import javax.swing.JComponent;

import painter.Main;


public class Line extends Shapes {

	private static int			thickness 		= Main.THICKNESS_DEFAULT_PENCIL;
	private static final int[] 	THICKNESS_LIST 	= {0, 1, 2, 3, 4, 5};
	private static BasicStroke 	b 				= new BasicStroke(THICKNESS_LIST[thickness],
																  BasicStroke.CAP_SQUARE, 
																  BasicStroke.JOIN_ROUND, 
																  0,
																  new float[] { 3, 0 }, 0);
	
	public Line(){
		super();
	}
	
	public Line(Point p0)
	{
		super(p0);
	}
	
	@Override
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e) {
		super.paint(bi, g, canvas, e);

		
		((Graphics2D)g).setStroke(b);
		g.drawLine(point0.x, point0.y, point1.x, point1.y);
		return bi;
		
	}
	
	
	
	public static void setThickness(int t)
	{
		thickness = t;
		
		b = new BasicStroke(THICKNESS_LIST[thickness], b.getEndCap(), b.getLineJoin(), b.getMiterLimit(),
				   b.getDashArray(), b.getDashPhase());
	}
	

}
