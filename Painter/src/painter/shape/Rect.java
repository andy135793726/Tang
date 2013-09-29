package painter.shape;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import painter.Main;
import painter.resizablecomponent.ResizableBorder;


public class Rect extends Shapes {


	private static int			thickness 		= Main.THICKNESS_DEFAULT_PENCIL;
	private static final int[] 	THICKNESS_LIST 	= {0, 1, 2, 3, 4, 5};
	private static BasicStroke 	b 				= new BasicStroke(THICKNESS_LIST[thickness],
																  BasicStroke.CAP_SQUARE, 
																  BasicStroke.JOIN_ROUND, 
																  0,
																  new float[] { 3, 1 }, 0);
	
	public Rect(){
		super();
	}
	
	public Rect(Point p0)
	{
		super(p0);
	}
	
	@Override
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e) {
		
		super.paint(bi, g, canvas, e);

		
		int x0 = point0.x;
		int y0 = point0.y;
		int x1 = point1.x;
		int y1 = point1.y;
		
		
		int width = (x1 >= x0) ? x1 - x0 : x0 - x1;
		int height = (y1 >= y0) ? y1 - y0 : y0 - y1;
		
		((Graphics2D)g).setStroke(b);
	
		g.drawRect( (x1 >= x0? x0 : x1), (y1 <= y0? y1 : y0), width, height);
		
		return bi;
		
	}
	
	
	
	public static void setThickness(int t)
	{
		thickness = t;
		
		b = new BasicStroke(THICKNESS_LIST[thickness], b.getEndCap(), b.getLineJoin(), b.getMiterLimit(),
				   b.getDashArray(), b.getDashPhase());
	}
	

}
