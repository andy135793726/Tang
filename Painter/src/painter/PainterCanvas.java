package painter;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import painter.Main.WORK;
import painter.draw.Eraser;
import painter.draw.Fill;
import painter.draw.Pencil;
import painter.resizablecomponent.ShapesResizer;
import painter.shape.Line;
import painter.shape.Oval;
import painter.shape.Rect;
import painter.shape.RightTri;
import painter.shape.RoundedRect;
import painter.shape.Shapes;
import painter.shape.Tri;


public class PainterCanvas extends JPanel implements MouseListener, MouseMotionListener{
	
	private static int undo_step_num = Main.getUndoStepNum();
	
	private ArrayList<Main.WORK> workList;  //每個影響繪圖的動作都存入
	private ArrayList<Main.WORK> undoList;  //復原時將取消的動作存入
	private ArrayList<BufferedImage>  graphicsList;     //每個繪圖完成的畫面都存入
	private ArrayList<BufferedImage>  undoGraphicsList; //復原時將畫面存入
	private ArrayList<Dimension> sizeList;
	private ArrayList<Dimension> undoSizeList;
	
	private BufferedImage lastBufferedImage;          //save the last graphics. When mouse-released, savd as tha last graphics.
	private Graphics      lastGraphics;
	
	
	private Graph graph;
	MouseEvent me;
	
	Integer defaultWidth = null;
	Integer defaultHeight = null;

	
	private BufferedImage biBeforeResize;
	
	private boolean isShapesResizing = false;       //正在調整"Shapes"子類別的話為真
	private boolean hasMouseDragged = false;		//滑鼠拖曳時為真、放鬆後為假
	
	public PainterCanvas()
	{
	
		initialMemoryLists();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				if (defaultHeight == null && defaultWidth == null){
					defaultHeight = getHeight();
					defaultWidth  = getWidth();
				}
				
				biBeforeResize = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				instanceMyGraphics(getWidth(), getHeight());
			}
			
			@Override
			public void componentMoved(ComponentEvent e){}
			@Override
			public void componentHidden(ComponentEvent e){}
		});
		
		setBackground(Color.white);
		setLayout(null);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		System.out.print("bi size " + lastBufferedImage.getWidth() + ", " + lastBufferedImage.getHeight() + '\n');
		g.drawImage(lastBufferedImage, 0, 0, this);
	}
	
	
	private  void MyPaintOffScreen()
	{
		if (isShapesResizing){
			lastBufferedImage = copyBufferedImage(biBeforeResize);
		}
		
		else{
			//always reset lastBufferedImage to be the last saved bufferedimage
			if (graphicsList.size() != 0){
				lastBufferedImage = copyBufferedImage(graphicsList.get(graphicsList.size()-1));
			}
			else {
				lastBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				lastBufferedImage.getGraphics().fillRect(0, 0, lastBufferedImage.getWidth(), lastBufferedImage.getHeight());
			}
			//this can avoid the problem:
			//if after saving the last bi, you changed the canvas size, especially larger,
			//the current bi can't be directly the last saved one,
			//but you need to renew one whose size is correct and the content is the same.
		}
		
		instanceMyGraphics(getWidth(), getHeight());
		
		Graphics g = lastGraphics;
		
		
		// set color
		if (getWork() != WORK.W_ERASER)
			g.setColor(getColor1());
		else 
			g.setColor(getColor2());
		
		// painting
		if (isGraphWorking()){
			lastBufferedImage = graph.paint(lastBufferedImage, g, this, me);
		}
		
		repaint();
	}
	
	
	//called at first and anytime when canvas size is changed
	private void instanceMyGraphics(int width, int height)
	{
		System.out.print("instanceMyGraphics called\n");
		
		if (width <= 0) width = 1;
		if (height <= 0) height = 1;
		
		//just change size
		if (lastBufferedImage != null){
			BufferedImage lastLastBI = lastBufferedImage;
			lastBufferedImage 		 = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			
			int llBiWidth  = lastLastBI.getWidth();
			int llBiHeight = lastLastBI.getHeight();
			
			if (width  < llBiWidth)  llBiWidth  = width;
			if (height < llBiHeight) llBiHeight = height;
			
			lastBufferedImage.getGraphics().drawImage(lastLastBI.getSubimage(0, 0, llBiWidth, llBiHeight), 0, 0, this);	
		}
		
		//first time instancing
		else {
			System.out.print("instanceMyGraphics called 2\n");
			lastBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = lastBufferedImage.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, lastBufferedImage.getWidth(), lastBufferedImage.getHeight());
		}
			
			
		lastGraphics     = lastBufferedImage.getGraphics();
	}
	
	
	private Main.WORK getWork() { return Main.getCurWork(); }
	
	private Color	  getColor1() { return Main.getCurColor1(); }
	private Color	  getColor2() { return Main.getCurColor2(); }
	
	
	//called due to size-changing
	public void saveWorkByParent()
	{
		saveWork();
	}
	
	private void saveWork()
	{
		System.out.print("saveWork() called\n");

		// if full, need to delete the earliest WORK to add the newest one
		if (workList.size() == undo_step_num){ 
			
			ArrayList<WORK> 	new_wl =
						new ArrayList<WORK>( workList.subList(1, workList.size()-1) );
			ArrayList<BufferedImage> new_gl = 
						new ArrayList<BufferedImage>( graphicsList.subList(1, graphicsList.size()-1) );
			
			workList 	 = new_wl;
			graphicsList = new_gl;
		}
		
		workList.add(getWork());
		graphicsList.add(lastBufferedImage);
		sizeList.add(new Dimension(lastBufferedImage.getWidth(), lastBufferedImage.getHeight()));
	
		//if undo button was used but you want to keep painting or clear(->savework()),
		//no redo can be done anymore
		undoGraphicsList.clear();
		undoList.clear();
		undoSizeList.clear();
		
		
		System.out.print("graphics list size : " + graphicsList.size()+ '\n');
	}

	public void undo()
	{	
		graph = null;
		
		if (graphicsList.isEmpty()) 
			return;
		
		undoGraphicsList.add(graphicsList.remove(graphicsList.size()-1));
		undoList.add(workList.remove(workList.size()-1));
		undoSizeList.add(sizeList.remove(sizeList.size()-1));
		
		
		if (graphicsList.isEmpty()){
			lastBufferedImage = null;
			instanceMyGraphics(defaultWidth, defaultHeight);	
		}
		else{
			lastBufferedImage = copyBufferedImage(graphicsList.get(graphicsList.size()-1));
			lastGraphics = lastBufferedImage.getGraphics();
		}
			
		//call PainterJFrame.setPainterResizerSize
		((PainterJFrame)getParent().getParent().
						getParent().getParent().
						getParent().getParent().
						getParent().getParent()).
						setPainterResizerSize(
								new Dimension(lastBufferedImage.getWidth(), 
										  lastBufferedImage.getHeight()));	
		
		System.out.print("graphics List size: " + graphicsList.size() + '\n');
		System.out.print("size List size:     " + sizeList.size() + '\n');
		System.out.print("bufferedImage size: " + lastBufferedImage.getWidth() + ", " + lastBufferedImage.getHeight() + '\n');
		System.out.print("canvas size		: " + getWidth() + ", " + getHeight() + '\n');
		MyPaintOffScreen();
	}
	
	public void redo()
	{
		graph = null;
		
		if (undoGraphicsList.isEmpty()) 
			return;
		
		graphicsList.add(undoGraphicsList.remove(undoGraphicsList.size()-1));
		workList.	 add(undoList.		  remove(undoList.		  size()-1));
		sizeList.	 add(undoSizeList.	  remove(undoSizeList.	  size()-1));
		
		int lastWidth  = graphicsList.get(graphicsList.size()-1).getWidth();
		int lastHeight = graphicsList.get(graphicsList.size()-1).getHeight();
		
		instanceMyGraphics(lastWidth, lastHeight);
		
		//call PainterJFrame.setPainterResizerSize
				((PainterJFrame)getParent().getParent().
								getParent().getParent().
								getParent().getParent().
								getParent().getParent()).
								setPainterResizerSize(new 
										Dimension(lastBufferedImage.getWidth(), 
												  lastBufferedImage.getHeight()));		
		
		MyPaintOffScreen();
	}
	
	
	public void clear()
	{
		lastBufferedImage = null;
		instanceMyGraphics(getWidth(), getHeight());
		
		saveWork();
		MyPaintOffScreen();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		me = e;
		
		hasMouseDragged = true;
		MyPaintOffScreen();
	}

	@Override
	public void mouseMoved(MouseEvent e) {me = e;}

	@Override
	public void mouseClicked(MouseEvent e) {
		me = e;
	}

	@Override
	public void mouseEntered(MouseEvent e) {me = e;}

	@Override
	public void mouseExited(MouseEvent e) {me = e;}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		System.out.print("mousePressed called\n");
		requestFocus();
		
		me = e;
		isShapesResizing = false;
		biBeforeResize = null;
		hasMouseDragged = false;
		
		Point point0 = e.getPoint();
		
		if (isPainting()){
			instanceGraph(getWork(), point0);
			System.out.print("is painting \n");
		}
		
		if (isShapesWorking()){
			setShapesResizer();
		}
		
		MyPaintOffScreen();
		System.out.print("graph's class is :" + graph.getClass().getName() + '\n');
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		System.out.print("mouseReleased() called\n");
		MouseEvent lastMe = me;
		me = e;
		
		if (isGraphWorking()){
				
			//如果沒有drag的話，形狀是不會被拉出來的哦! 所以如果是shapes又沒動的話就不save。
			if ((hasMouseDragged || !isShapesWorking()))
				saveWork();
			
				
			if(!Main.workIsShapes()){
				graph = null;
			}
			else {
				Shapes shapes = (Shapes)graph;
				shapes.sResizer.setVisible(true);
				shapes.sResizer.resizer.requestFocus();
				
				shapes.sResizer.resizer.addComponentListener(new ComponentListener() {
					@Override
					public void componentResized(ComponentEvent e) {
						repaintShapes();
					}

					@Override
					public void componentShown(ComponentEvent e) {}
					@Override
					public void componentMoved(ComponentEvent e) {
						repaintShapes();
					}
					@Override
					public void componentHidden(ComponentEvent e) {}
				
				});
				
				shapes.sResizer.resizer.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent e) {
						System.out.print("savework due to resizer losing focus called\n");
						biBeforeResize = null;
						isShapesResizing = false;
					}					
					@Override
					public void focusGained(FocusEvent e) {
						isShapesResizing = true;
					}
				});
				
				shapes.sResizer.resizer.addMouseListener(resizerMouseListener);
				shapes.sResizer.resizer.addMouseMotionListener(resizerMouseListener);
				
				
			}
			
		}
	}
	
	
	private MouseInputListener resizerMouseListener = new MouseInputAdapter() {
		private boolean hasShapesChanged = false;
		
		public void mousePressed(MouseEvent e) {
			me = null;
			hasShapesChanged = false;
		}
		
		public void mouseDragged(MouseEvent e) {
			me = null;
			hasShapesChanged = true;
		}
		
		public void mouseReleased(MouseEvent e) {
			me = null;
			if (hasShapesChanged){
				saveWork();
			}
			hasShapesChanged = false;
		}
	};
			
	
	

	private void repaintShapes()
	{
		if (!isShapesWorking()) 
			return;
		
		
		if (biBeforeResize == null){
			if (graphicsList.size() >= 2)
				biBeforeResize = graphicsList.get(graphicsList.size()-2);
			else 
				biBeforeResize = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		
		MyPaintOffScreen();
	}
	
	
	public static boolean isPainting()
	{
		return Main.isPainting();
	}
	
	/* 若要開始繪圖，幫Graph類型的graph物件產生適當的繪圖型態*/
	private void instanceGraph(WORK w, Point p0)  
	{
		graph = getGraphInstanceByWork(w, p0);
	}
	
	
	private Graph getGraphInstanceByWork(WORK w, Point p0)
	{
		switch(w){
		case W_PENCIL:  return new Pencil(); 
		case W_FILL	 :	return new Fill();
		case W_ERASER:  return new Eraser();
		case W_S_LINE:  return new Line(p0);
		case W_S_RECT: 	return new Rect(p0);
		case W_S_ROUNDED_RECT: return new RoundedRect(p0);
		case W_S_OVAL: 	return new Oval(p0);
		case W_S_TRI :  return new Tri(p0);
		case W_S_R_TRI: return new RightTri(p0);
		default: 		return new Graph();
		}
	}
	
	
	private boolean isGraphWorking()
	{
		return ((graph != null) && (graph.getClass() != (Graph.class)));
	}
	
	
	
	private void initialMemoryLists(){
		workList		 = null;
		undoList 		 = null;
		graphicsList 	 = null;
		undoGraphicsList = null;
		sizeList 		 = null;
		undoSizeList 	 = null;
		resizeMemoryLists(undo_step_num);
	}
	
	public void resizeMemoryLists(int capacity)
	{
		if (workList == null || undoList == null || graphicsList == null || undoGraphicsList == null){
			workList 		 = new ArrayList<WORK>(capacity);
			undoList 		 = new ArrayList<WORK>(capacity);
			graphicsList 	 = new ArrayList<BufferedImage>(capacity);
			undoGraphicsList = new ArrayList<BufferedImage>(capacity);
			sizeList		 = new ArrayList<Dimension>(capacity);
			undoSizeList	 = new ArrayList<Dimension>(capacity);
		}
		
		else if (capacity > workList.size()){
			workList.ensureCapacity(capacity);
			undoList.ensureCapacity(capacity);
			graphicsList.ensureCapacity(capacity);
			undoGraphicsList.ensureCapacity(capacity);
			sizeList.ensureCapacity(capacity);
			undoSizeList.ensureCapacity(capacity);
		}
		
		else if (capacity < workList.size()){
			workList     = 
					new ArrayList<WORK>(workList.subList(workList.size()-capacity, workList.size()-1));
			graphicsList = 
					new ArrayList<BufferedImage>(graphicsList.subList(graphicsList.size()-capacity, graphicsList.size()-1));
			sizeList	 =
					new ArrayList<Dimension>(sizeList.subList(sizeList.size()-capacity, sizeList.size()-1));
		}
		else {/*do nothing*/}
	}
	
	
	
	private void setShapesResizer()
	{
		if (!isShapesWorking()){
			return;
		}
		
		ShapesResizer sResizer = ((Shapes)graph).sResizer;
		add(sResizer);
		
		sResizer.setBounds(0, 0, getWidth(), getHeight());
	}
	
	private boolean isShapesWorking()
	{
		if (Main.workIsShapes() && graph != null)
			return true;
		return false;
	}

	
	private BufferedImage copyBufferedImage(BufferedImage oriBi)
	{
		BufferedImage newBi;
		ColorModel cm = oriBi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = oriBi.copyData(null);
		newBi = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		return newBi;
	}
	
	
	public BufferedImage getCurrentImage()
	{
		return lastBufferedImage;
	}
	
}
