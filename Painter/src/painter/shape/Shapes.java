package painter.shape;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import painter.Graph;
import painter.PainterCanvas;
import painter.resizablecomponent.ShapesResizer;

public abstract class Shapes extends Graph{

	protected Point point0;
	protected Point point1;
	
	
	public ShapesResizer sResizer;
	protected PainterCanvas parentCanvas;
	
	protected boolean resizerSet = false;
	
	protected boolean isPainting;
	
	public Shapes()
	{
		sResizer = new ShapesResizer();
	}
	
	public Shapes(Point p0)
	{
		this();
		point0 = p0;
	}
	
	
	public BufferedImage paint(BufferedImage bi, Graphics g, JComponent canvas, MouseEvent e)
	{
		System.out.print("shapes: paint called\n");
		
		if (parentCanvas == null)
			parentCanvas = (PainterCanvas)canvas;
		
		isPainting = (e != null);	
		
		if (!resizerSet){
			setResizer();
			resizerSet = true;
		}
		
		
		
		//if e is null, resizer is working. called by PainterCanvas
		if (isPainting){
			point1 = e.getPoint();
		
			int xlu = point0.x <= point1.x ? point0.x : point1.x;
			int ylu = point0.y <= point1.y ? point0.y : point1.y;
			int w   = Math.abs(point1.x - point0.x);
			int h   = Math.abs(point1.y - point0.y);
			sResizer.resizer.setBounds(xlu, ylu, w, h);
		}
		
		
		return bi;
	}
	
	
	

	
	
	/*
	protected JPanel testPanel = new JPanel();
	protected void addTestPanel()
	{
		
		
		int width = Math.abs(point1.x - point0.x);
		int height = Math.abs(point1.y - point0.y);
		testPanel.setSize(width, height);
		
		testPanel.setBackground(Color.blue);
		//testPanel.setVisible(true);
	}*/
	
	protected void setResizer()
	{
		//sResizer.setAreaBounds(point0.x, point0.y, parentCanvas.getWidth(), parentCanvas.getHeight());
	
		sResizer.resizer.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				if (!isPainting)
					setP0P1();
			}

			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {
				if (!isPainting)
					setP0P1();
			}
			@Override
			public void componentHidden(ComponentEvent e) {}
			
			private void setP0P1()
			{
				Rectangle rectangle = sResizer.resizer.getBounds();
				
				int width = rectangle.width;
				int height = rectangle.height;
				
				if (point0.x < point1.x){
					point0.x = rectangle.x;
					point1.x = rectangle.x + width;
				}
				else {
					point1.x = rectangle.x;
					point0.x = rectangle.x + width;
				}
				
				if (point0.y < point1.y){
					point0.y = rectangle.y;
					point1.y = rectangle.y + height;
				}
				else {
					point1.y = rectangle.y;
					point0.y = rectangle.y + height;
				}
				
			}
		});
	}
	
	
}
