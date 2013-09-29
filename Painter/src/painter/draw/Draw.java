package painter.draw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import painter.Graph;

public abstract class Draw  extends Graph {
	
	private ArrayList<Point> pointList;
	private static final int[] THICKNESS_LIST = {0, 0, 0, 0, 0};
	private static BasicStroke 	 b;
	
	/*--duplicated--
	 * 
	 *private static ArrayList< ArrayList<Point> > paintList;
	 *private static ArrayList< ArrayList<Point> > undoPointList;
	 */
	
	@Override
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e) { return bi; }
	
	
	
}
