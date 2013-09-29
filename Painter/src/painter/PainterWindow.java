
/* 放棄此文件 */

package painter;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class PainterWindow extends JFrame
{
	private PainterTabbedPane 	painterTabbedPane;
	private PainterCanvas 		painterCanvas;
	
	
	public PainterWindow()
	{
		setSize(400, 300);
		
		
		painterTabbedPane = new PainterTabbedPane();
		painterCanvas = new PainterCanvas();
		
		add(BorderLayout.NORTH, painterTabbedPane);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setVisible(true);
	}
	
	
	
}



class PainterTabbedPane extends JTabbedPane
{
	private HomePanel 			homePanel;
	private ViewPanel 			viewPanel;
	
	public PainterTabbedPane()
	{
		homePanel = new HomePanel();
		viewPanel = new ViewPanel();
		
		addTab("Home", homePanel);
		addTab("View", viewPanel);
	}
	
}

class HomePanel extends JPanel
{
	public HomePanel()
	{
		
	}
	
}

class ViewPanel extends JPanel
{
	public ViewPanel()
	{
		
	}
}