/****************************************
 * 
 * reference source: http://zetcode.com/tutorials/javaswingtutorial/resizablecomponent/
 * 
 * ����O�@�ӳz�����e���B�M�e���������|�����O�C
 * �u�bø�e����Shapes�ɡA�ƹ���}��X�{�C�ƹ����N�I���O�B�N�����C
 * �]���A���ݭn�վ�j�p�B���ʪ��\��C
 * ���focus�]�P�q����resizer��focus�C
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
