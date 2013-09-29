package painter.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;

public class Fill extends Draw{

	BufferedImage bufferedImage;

	Queue<Point> pointsQueue;
	
	Point p0;
	int	  rgb0;
	int	  rgb;
	
	public Fill()
	{}

	@Override
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e) 
	{ 
		//it must have been painted so that it has a non-null p0.
		//the circumstance may be it is dragging, not pressing.
		if (p0 != null){
			return bufferedImage;
		}
		
		p0 = new Point(e.getPoint());
		int x = p0.x;
		int y = p0.y;
		
		bufferedImage = bi;
		rgb0 = bufferedImage.getRGB(x, y);  // the color to be replaced

		Color c = g.getColor();
		rgb = c.getRGB();    // the color to replace rgb0
	
		if (rgb0 == rgb){
			return bufferedImage;
		}
		
		pointsQueue = new LinkedList<Point>();
		pointsQueue.offer(p0);
		

		fillPointsIter();

		g.drawImage(bufferedImage, 0, 0, canvas);
		return bufferedImage;
		
	}


	protected void fillPointsIter()
	{
		while(!pointsQueue.isEmpty()){
			
			Point fp = pointsQueue.poll();

			
			int[] xs = {fp.x, fp.x+1, fp.x, fp.x-1};
			int[] ys = {fp.y-1, fp.y, fp.y+1, fp.y};
			
			for (int i = 0; i < 4; i++){
				
				//outside the bufferedimage
				if ((xs[i] < 0 || xs[i] >= bufferedImage.getWidth()) ||
					(ys[i] < 0 || ys[i] >= bufferedImage.getHeight()))
				{
					continue;
				}
				
				//the color is different
				if (bufferedImage.getRGB(xs[i], ys[i])!=rgb0)
				{
					continue;
				}
				
				
				bufferedImage.setRGB(xs[i], ys[i], rgb);
				pointsQueue.offer(new Point(xs[i], ys[i]));
			}
			
			
			
		}
		
	}

	
}
