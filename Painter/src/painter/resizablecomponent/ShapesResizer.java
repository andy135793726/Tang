/****************************************
 * 
 * reference source: http://zetcode.com/tutorials/javaswingtutorial/resizablecomponent/
 * 
 * 本質是一個透明全畫面、和畫布完全重疊的底板。
 * 只在繪畫物件為Shapes時，滑鼠放開後出現。滑鼠任意點擊別處就消失。
 * 因此，不需要調整大小、移動的功能。
 * 獲取focus也同義為給resizer的focus。
 * 
 * 
 * 
 ****************************************/



package painter.resizablecomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ShapesResizer extends JPanel {
	
	public JPanel area;
	public Resizable resizer;
	
	public ShapesResizer()
	{
		super(null);
		setOpaque(false);
		
		area = new JPanel();
		area.setBackground(Color.yellow);
		area.setOpaque(false);
		resizer = new Resizable(area);
		add(resizer);
		
		setVisible(false);

		
		setBackground(Color.red);
		
	}
	
	@Override
	public boolean hasFocus()
	{
		return resizer.hasFocus();
	}
	
	@Override
	public void grabFocus()
	{	
		resizer.grabFocus();
	}

	
	public void setAreaBounds(int x, int y, int w, int h)
	{
		area.setBounds(x, y, w, h);
	}
	
}
