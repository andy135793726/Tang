package painter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import painter.Main.WORK;
import painter.draw.Eraser;
import painter.draw.Pencil;
import painter.resizablecomponent.Resizable;
import painter.resizablecomponent.ResizableBorder;
import painter.shape.Line;
import painter.shape.Oval;
import painter.shape.Rect;
import painter.shape.RightTri;
import painter.shape.RoundedRect;
import painter.shape.Tri;
import painter.util.BMPFilter;
import painter.util.GIFFilter;
import painter.util.ImageFilter;
import painter.util.JPEGFilter;
import painter.util.JPGFilter;
import painter.util.PNGFilter;

public class PainterJFrame extends JFrame {

	private JPanel contentPane;
	private JPanel PainterCanvasPane;
	private Resizable	painterResizer;
	private PainterCanvas painterCanvas;
	
	
	private ArrayList<JToggleButton> toggleGroup_painting;
	private ArrayList<JMenuItem>  	 mnitemGroup_thickness;
	private ArrayList<JToggleButton> toggleGroup_color;
	private ArrayList<JMenuItem>	 mnitemGroup_shapes;
	private ArrayList<JMenuItem>	 mnitemGroup_fill;
	private ArrayList<JMenuItem>	 mnitemGroup_outline;
	
	private JButton btn_open, btn_save, btn_saveAs;
    private JFileChooser fc;
	private String filePath = null;
	
	/*
	 * action names for defining toggle buttons
	 */
	private final static String action_pencil_str = "action_pencil",
						  		action_fill_str   = "action_fill",
						  		action_eraser_str = "action_eraser";
	
	/*
	 * action names for defining shapes menuitems
	 */
	private final static String action_s_line_str  		  = "action_s_line",
								action_s_curve_str 		  = "action_s_curve",
								action_s_oval_str 		  = "action_s_oval",
								action_s_rect_str 		  = "action_s_rect",
								action_s_rounded_rect_str = "action_s_rounded_rect",
								action_s_polygon_str 	  = "action_s_polygon",
								action_s_tri_str 		  = "action_s_tri",
								action_s_r_tri_str		  = "action_s_r_tri";
	
	/*
	 *  actions names for defining shapes FILL menuitems
	 */
	private final static String action_s_fill_0_str = "action_s_fill_0",
								action_s_fill_1_str = "action_s_fill_1",
								action_s_fill_2_str = "action_s_fill_2",
								action_s_fill_3_str = "action_s_fill_3",
								action_s_fill_4_str = "action_s_fill_4",
								action_s_fill_5_str = "action_s_fill_5",
								action_s_fill_6_str = "action_s_fill_6";
	
	/*
	 *  actions names for defining shapes OUTLINE menuitems
	 */	
	private final static String action_s_outline_0_str = "action_s_outline_0",
								action_s_outline_1_str = "action_s_outline_1",
								action_s_outline_2_str = "action_s_outline_2",
								action_s_outline_3_str = "action_s_outline_3",
								action_s_outline_4_str = "action_s_outline_4",
								action_s_outline_5_str = "action_s_outline_5",
								action_s_outline_6_str = "action_s_outline_6";
								
	/*
	 *  action names for defining  thickness  menuitems
	 */
	private static String action_thn_1_str	= "action_thn_1",
						  action_thn_2_str	= "action_thn_2",
						  action_thn_3_str	= "action_thn_3",
						  action_thn_4_str	= "action_thn_4",
						  action_thn_5_str	= "action_thn_5";
	
	/*
	 * action names for defining color buttons
	 */
	private static String action_clr_1_str	= "action_clr_1",
						  action_clr_2_str  = "action_clr_2";	
	
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PainterJFrame frame = new PainterJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public PainterJFrame() {
		setTitle("Painter");
		setSize(new Dimension(800, 700));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		addMouseListener(thisMouseListener);
		addMouseMotionListener(thisMouseListener);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		contentPane.add(tabbedPane, BorderLayout.NORTH);
			
		
		
		painterCanvas = new PainterCanvas();
		painterCanvas.setPreferredSize(new Dimension(640, 480));
		painterCanvas.setMaximumSize(painterCanvas.getPreferredSize());
		painterCanvas.setMinimumSize(painterCanvas.getPreferredSize());
		
		
		PainterCanvasPane = new JPanel();
		PainterCanvasPane.setBackground(Color.darkGray);
		
		//PainterCanvasPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		//PainterCanvasPane.add(painterCanvas);

		//PainterCanvasPane.setLayout(null);
		painterResizer = new Resizable(painterCanvas);
		
		int dist = ((ResizableBorder)painterResizer.getBorder()).getDist();
		painterResizer.setBounds(0, 0, 640 + 2*dist, 480+2*dist);
		PainterCanvasPane.add(painterResizer);
		painterResizer.removeFocusListener(painterResizer); //remove itself as a focuslistener,
											  				//which set unvisible itself when losing focus.
		painterResizer.addComponentListener(resizerComponentListener);
		painterResizer.addMouseListener(resizerMouseListener);
		painterResizer.addMouseMotionListener(resizerMouseListener);
		
		painterCanvas.setLayout(null);
		


		JScrollPane PainterScrollPane = new JScrollPane();
		
		contentPane.add(PainterScrollPane, BorderLayout.CENTER);
		PainterScrollPane.setViewportView(PainterCanvasPane);
		
			

		/*add toggles to the group*/
		toggleGroup_painting = new ArrayList<JToggleButton>();
		
		
		/*add menuitems (thickness) to the group*/
		mnitemGroup_thickness = new ArrayList<JMenuItem>();
		
		/* add the two buttons for colors */
		toggleGroup_color = new ArrayList<JToggleButton>(2);
		
		/* add menuitems (shapes) to the group*/
		mnitemGroup_shapes = new ArrayList<JMenuItem>();
		
		
		JPanel panel_file = new JPanel();
		tabbedPane.addTab("File", null, panel_file, null);
		panel_file.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JButton btn_newFile = new JButton("New");
		panel_file.add(btn_newFile);
		
		btn_open	= new JButton("Open");
		btn_open.addActionListener(fileSaveOpenListener);
		panel_file.add(btn_open);
		
		btn_save	= new JButton("Save");
		btn_save.addActionListener(fileSaveOpenListener);
		panel_file.add(btn_save);
		
		btn_saveAs  = new JButton("Save as..");
		btn_saveAs.addActionListener(fileSaveOpenListener);
		panel_file.add(btn_saveAs);
		
		
		setFileChooser();

		
		JPanel panel_home = new JPanel();
		tabbedPane.addTab("Home", null, panel_home, null);
		GridBagLayout gbl_panel_home = new GridBagLayout();
		gbl_panel_home.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_home.rowHeights = new int[]{0, 0};
		gbl_panel_home.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_home.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_home.setLayout(gbl_panel_home);
		
		JPanel panel_home_0 = new JPanel();
		GridBagConstraints gbc_panel_home_0 = new GridBagConstraints();
		gbc_panel_home_0.insets = new Insets(0, 0, 0, 5);
		gbc_panel_home_0.fill = GridBagConstraints.BOTH;
		gbc_panel_home_0.gridx = 0;
		gbc_panel_home_0.gridy = 0;
		panel_home.add(panel_home_0, gbc_panel_home_0);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel_home.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btn_undo = new JButton("un");
		btn_undo.setPreferredSize(new Dimension(20, 20));
		btn_undo.setMargin(new Insets(0, 0, 0, 0));
		btn_undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}
		});
		GridBagConstraints gbc_btn_undo = new GridBagConstraints();
		gbc_btn_undo.insets = new Insets(0, 0, 0, 5);
		gbc_btn_undo.gridx = 0;
		gbc_btn_undo.gridy = 0;
		panel_1.add(btn_undo, gbc_btn_undo);
		
		JButton btn_redo = new JButton("re");
		btn_redo.setPreferredSize(new Dimension(20, 20));
		btn_redo.setSize(new Dimension(20, 20));
		btn_redo.setMargin(new Insets(0, 0, 0, 0));
		btn_redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		GridBagConstraints gbc_btn_redo = new GridBagConstraints();
		gbc_btn_redo.insets = new Insets(0, 0, 0, 5);
		gbc_btn_redo.gridx = 1;
		gbc_btn_redo.gridy = 0;
		panel_1.add(btn_redo, gbc_btn_redo);
		
		JButton btn_clear = new JButton("clear");
		btn_clear.setSize(new Dimension(40, 20));
		btn_clear.setPreferredSize(new Dimension(40, 20));
		btn_clear.setMargin(new Insets(0, 0, 0, 0));
		btn_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
		});
		
		
		GridBagConstraints gbc_btn_save = new GridBagConstraints();
		gbc_btn_save.gridx = 2;
		gbc_btn_save.gridy = 0;
		panel_1.add(btn_clear, gbc_btn_save);
		
		JPanel panel_home_1 = new JPanel();
		GridBagConstraints gbc_panel_home_1 = new GridBagConstraints();
		gbc_panel_home_1.anchor = GridBagConstraints.WEST;
		gbc_panel_home_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_home_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_home_1.gridx = 2;
		gbc_panel_home_1.gridy = 0;
		panel_home.add(panel_home_1, gbc_panel_home_1);
		
		JPanel panel_home_2 = new JPanel();
		GridBagConstraints gbc_panel_home_2 = new GridBagConstraints();
		gbc_panel_home_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_home_2.fill = GridBagConstraints.BOTH;
		gbc_panel_home_2.gridx = 3;
		gbc_panel_home_2.gridy = 0;
		panel_home.add(panel_home_2, gbc_panel_home_2);
		
		

		JToggleButton toggle_pencil = new JToggleButton("pencil");
		toggle_pencil.setMargin(new Insets(2, 0, 0, 0));
		toggle_pencil.setActionCommand(action_pencil_str);
		panel_home_2.add(toggle_pencil);
		toggleGroup_painting.add(toggle_pencil);
		toggle_pencil.setSelected(true);
		
		JToggleButton toggle_fill = new JToggleButton("fill");
		toggle_fill.setMargin(new Insets(0, 0, 0, 0));
		toggle_fill.setActionCommand(action_fill_str);
		panel_home_2.add(toggle_fill);
		toggleGroup_painting.add(toggle_fill);
		
		JToggleButton toggle_eraser = new JToggleButton("eraser");
		toggle_eraser.setMargin(new Insets(0, 0, 0, 0));
		toggle_eraser.setActionCommand(action_eraser_str);
		panel_home_2.add(toggle_eraser);
		toggleGroup_painting.add(toggle_eraser);
		

		
		JPanel panel_home_4 = new JPanel();
		GridBagConstraints gbc_panel_home_4 = new GridBagConstraints();
		gbc_panel_home_4.gridwidth = 2;
		gbc_panel_home_4.insets = new Insets(0, 0, 0, 5);
		gbc_panel_home_4.fill = GridBagConstraints.BOTH;
		gbc_panel_home_4.gridx = 4;
		gbc_panel_home_4.gridy = 0;
		panel_home.add(panel_home_4, gbc_panel_home_4);
		GridBagLayout gbl_panel_home_4 = new GridBagLayout();
		gbl_panel_home_4.columnWidths = new int[]{0, 0};
		gbl_panel_home_4.rowHeights = new int[]{0, 0};
		gbl_panel_home_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_home_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_home_4.setLayout(gbl_panel_home_4);
		
		JMenuBar menuBar_shape = new JMenuBar();
		GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.gridx = 0;
		gbc_menuBar.gridy = 0;
		panel_home_4.add(menuBar_shape, gbc_menuBar);
		
		JMenu mn_shapes = new JMenu("shape");
		menuBar_shape.add(mn_shapes);
		
		JMenuItem mnitem_shapes_line = new JMenuItem("staight line");
		mnitem_shapes_line.setActionCommand(action_s_line_str);
		mn_shapes.add(mnitem_shapes_line);
		mnitemGroup_shapes.add(mnitem_shapes_line);
		
		JMenuItem mnitem_shapes_curve = new JMenuItem("curve");
		mnitem_shapes_curve.setActionCommand(action_s_curve_str);
		mn_shapes.add(mnitem_shapes_curve);
		mnitemGroup_shapes.add(mnitem_shapes_curve);
		
		JMenuItem mnitem_shapes_oval = new JMenuItem("oval");
		mnitem_shapes_oval.setActionCommand(action_s_oval_str);
		mn_shapes.add(mnitem_shapes_oval);
		mnitemGroup_shapes.add(mnitem_shapes_oval);
		
		JMenuItem mnitem_shapes_rect = new JMenuItem("rectangle");
		mnitem_shapes_rect.setActionCommand(action_s_rect_str);
		mn_shapes.add(mnitem_shapes_rect);
		mnitemGroup_shapes.add(mnitem_shapes_rect);
		
		JMenuItem mnitem_shapes_rounded_rect = new JMenuItem("rounded rectangle");
		mnitem_shapes_rounded_rect.setActionCommand(action_s_rounded_rect_str);
		mn_shapes.add(mnitem_shapes_rounded_rect);
		mnitemGroup_shapes.add(mnitem_shapes_rounded_rect);
		
		JMenuItem mnitem_shapes_polygon = new JMenuItem("polygon");
		mnitem_shapes_polygon.setActionCommand(action_s_polygon_str);
		mn_shapes.add(mnitem_shapes_polygon);
		mnitemGroup_shapes.add(mnitem_shapes_polygon);
		
		JMenuItem mnitem_shapes_tri = new JMenuItem("triangle");
		mnitem_shapes_tri.setActionCommand(action_s_tri_str);
		mn_shapes.add(mnitem_shapes_tri);
		mnitemGroup_shapes.add(mnitem_shapes_tri);
		
		JMenuItem mnitem_shapes_r_tri = new JMenuItem("right triangle");
		mnitem_shapes_r_tri.setActionCommand(action_s_r_tri_str);
		mn_shapes.add(mnitem_shapes_r_tri);
		mnitemGroup_shapes.add(mnitem_shapes_r_tri);
		
		
		JMenu mn_shape_fill = new JMenu("fill");
		menuBar_shape.add(mn_shape_fill);
		
		JMenu mn_shape_outline = new JMenu("outline");
		menuBar_shape.add(mn_shape_outline);
		
		JPanel panel_home_5 = new JPanel();
		GridBagConstraints gbc_panel_home_5 = new GridBagConstraints();
		gbc_panel_home_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_home_5.fill = GridBagConstraints.BOTH;
		gbc_panel_home_5.gridx = 6;
		gbc_panel_home_5.gridy = 0;
		panel_home.add(panel_home_5, gbc_panel_home_5);
		
		JMenuBar mnbar_thickness = new JMenuBar();
		panel_home_5.add(mnbar_thickness);
		
		JMenu mn_thickness = new JMenu("thickness");
		mnbar_thickness.add(mn_thickness);
		
		JMenuItem mnitem_thn_1 = new JMenuItem("1");
		mnitem_thn_1.setActionCommand(action_thn_1_str);
		mnitemGroup_thickness.add(mnitem_thn_1);
		mn_thickness.add(mnitem_thn_1);
		
		JMenuItem mnitem_thn_2 = new JMenuItem("2");
		mnitem_thn_2.setActionCommand(action_thn_2_str);
		mnitemGroup_thickness.add(mnitem_thn_2);
		mn_thickness.add(mnitem_thn_2);
		
		JMenuItem mnitem_thn_3 = new JMenuItem("3");
		mnitem_thn_3.setActionCommand(action_thn_3_str);
		mnitemGroup_thickness.add(mnitem_thn_3);
		mn_thickness.add(mnitem_thn_3);
		
		JMenuItem mnitem_thn_4 = new JMenuItem("4");
		mnitem_thn_4.setActionCommand(action_thn_4_str);
		mnitemGroup_thickness.add(mnitem_thn_4);
		mn_thickness.add(mnitem_thn_4);
		
		JMenuItem mnitem_thn_5 = new JMenuItem("5");
		mnitem_thn_5.setActionCommand(action_thn_5_str);
		mnitemGroup_thickness.add(mnitem_thn_5);
		mn_thickness.add(mnitem_thn_5);
		
		JPanel panel_home_6 = new JPanel();
		GridBagConstraints gbc_panel_home_6 = new GridBagConstraints();
		gbc_panel_home_6.fill = GridBagConstraints.BOTH;
		gbc_panel_home_6.gridx = 7;
		gbc_panel_home_6.gridy = 0;
		panel_home.add(panel_home_6, gbc_panel_home_6);
		panel_home_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		

		
		JToggleButton jtb_color1 = new JToggleButton("clr1");
		jtb_color1.setActionCommand(action_clr_1_str);
		jtb_color1.setSelected(true);
		toggleGroup_color.add(jtb_color1);
		panel_home_6.add(jtb_color1);
		
		JToggleButton jtb_color2 = new JToggleButton("clr2");
		jtb_color2.setActionCommand(action_clr_2_str);
		toggleGroup_color.add(jtb_color2);
		panel_home_6.add(jtb_color2);
		
		JPanel panel_view = new JPanel();
		tabbedPane.addTab("View", null, panel_view, null);
		
		
		for (JToggleButton jtb: toggleGroup_color){
			jtb.addActionListener(al_btn_group_color);
		}
		
		for(JToggleButton jtb : toggleGroup_painting){
			jtb.addActionListener(al_tg_group_paint);
		}
		
		for (JMenuItem mnitem: mnitemGroup_thickness){
			mnitem.addActionListener(al_mi_group_thickness);
		}
		
		for (JMenuItem mnitem: mnitemGroup_shapes){
			mnitem.addActionListener(al_mi_group_shapes);
		}
		
		tabbedPane.setSelectedIndex(1);

	}
	
	public void setWork(Main.WORK w)
	{
		Main.setWork(w);
	}
	
	public WORK getWork()
	{
		return Main.getCurWork();
	}
	
	public void setColor1(Color clr)
	{
		Main.setColor1(clr);
	}
	public void setColor2(Color clr)
	{
		Main.setColor2(clr);
	}
	public Color getColor1()
	{
		return Main.getCurColor1();
	}
	public Color getColor2()
	{
		return Main.getCurColor2();
	}
	
	public void undo()
	{
		painterCanvas.undo();
	}
	
	public void redo()
	{
		painterCanvas.redo();
	}
	public void clear()
	{
		WORK w = getWork();
		setWork(WORK.W_CLEAR);
		painterCanvas.clear();
		setWork(w);
	}
	

	public void setThickness(int thn)
	{
		switch(getWork()){
		case W_PENCIL: Pencil.setThickness(thn); System.out.print("call Pencil.setThickness\n"); break;
		case W_ERASER: Eraser.setThickness(thn); break;
		case W_S_LINE: Line.setThickness(thn); break;
		case W_S_RECT: Rect.setThickness(thn); break;
		case W_S_ROUNDED_RECT: RoundedRect.setThickness(thn); break;
		case W_S_OVAL: Oval.setThickness(thn); break;
		case W_S_TRI : Tri.setThickness(thn); break;
		case W_S_R_TRI: RightTri.setThickness(thn); break;
		
		default: break;
		}
	}
	
	
	//called by PainterCanvas when "undo&redo" are called with size-changing.
	//canvas size list is saved in PainterCanvas.
	public void setPainterResizerSize(Dimension size)
	{
		int dist = ((ResizableBorder)painterResizer.getBorder()).getDist();
		painterResizer.setBounds(0, 0, size.width + 2*dist, size.height + 2*dist);
		painterCanvas.setSize(size);
		painterResizer.requestFocus();
		System.out.print("resizer size: " + size.width + ", " + size.height + '\n'); 
	}
	
	
	
	private void setFileChooser()
	{
		fc = new JFileChooser();
		//Add a custom file filter and disable the default
        //(Accept All) file filter.
		PNGFilter pngFilter = new PNGFilter();
		
        fc.addChoosableFileFilter(new ImageFilter());
        fc.addChoosableFileFilter(pngFilter);
        fc.addChoosableFileFilter(new JPEGFilter());
        fc.addChoosableFileFilter(new JPGFilter());
        fc.addChoosableFileFilter(new GIFFilter());
        fc.addChoosableFileFilter(new BMPFilter());
        
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(pngFilter);
	}
	
	ActionListener al_tg_group_paint = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			WORK work = WORK.W_PENCIL;
			String strCommand = e.getActionCommand();
			
			//set all toggles false
			for (int i = 0; i < toggleGroup_painting.size(); i++){
				if (! strCommand.equals(toggleGroup_painting.get(i).getActionCommand()))
					toggleGroup_painting.get(i).setSelected(false);
			}
			
			
			if (strCommand.equals(action_pencil_str)){
				work = WORK.W_PENCIL;
			}
			else if (strCommand.equals(action_fill_str)){
				work = WORK.W_FILL;
			}
			else if (strCommand.equals(action_eraser_str)){
				work = WORK.W_ERASER;
			}

			else {
				work = WORK.W_NULL;
			}

			setWork(work);
		
		}
	};

	
	
	
	ActionListener al_mi_group_thickness = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.out.print("al_mi_group_thickness action performed\n");
			
			String strCommand = e.getActionCommand();
			if (strCommand.equals(action_thn_1_str)){
				setThickness(1);
			}
			else if (strCommand.equals(action_thn_2_str)){
				setThickness(2);
			}
			else if (strCommand.equals(action_thn_3_str)){
				setThickness(3);
			}
			else if (strCommand.equals(action_thn_4_str)){
				setThickness(4);
			}
			else if (strCommand.equals(action_thn_5_str)){
				setThickness(5);
			}
			
		}
	};
	
	
	
	
	
	/* listener of the two COLOR buttons*/
	ActionListener al_btn_group_color = new ActionListener(){
		
		Color colorChosen;
		ColorChooseDialog ccw;
		String strCommand;
		
		@Override
		public void actionPerformed(ActionEvent e) {

			strCommand = e.getActionCommand();
			if (strCommand == action_clr_1_str){
				toggleGroup_color.get(0).setSelected(true);
				toggleGroup_color.get(1).setSelected(false);
			}
			else if (strCommand == action_clr_2_str){
				toggleGroup_color.get(0).setSelected(false);
				toggleGroup_color.get(1).setSelected(true);
			}
			
			ccw = new ColorChooseDialog();
			ccw.setVisible(true);
		}
		
		protected void setColor(){
			if (strCommand == action_clr_1_str){
				System.out.print("setting color1 : " + colorChosen.toString() + '\n');
				setColor1(colorChosen);
			}
			else if (strCommand == action_clr_2_str){
				System.out.print("setting color2 : " + colorChosen.toString() + '\n');
				setColor2(colorChosen);
			}
		}
		
		
		/*listener of the ENTER button in the ColorChooser*/
		/*called when finishing choosing color*/
		ActionListener al_btn_colorChosen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorChosen = ccw.getColor();
				setColor();
			}
		};
		
		class ColorChooseDialog extends JDialog {

			private JColorChooser tcc;
			private JPanel        upperPanel;
			private Color 		  color;

		    public ColorChooseDialog() {

		        initUI();
		    }

		    public final void initUI() {

		        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		        add(Box.createRigidArea(new Dimension(0, 10)));

		        upperPanel = new JPanel();
		        upperPanel.setLayout(new FlowLayout());

		        
		        JButton enter = new JButton("OK");
		        enter.setFont(new Font("Serif", Font.BOLD, 13));
		        enter.setAlignmentX(0.5f);
		        enter.addActionListener(al_btn_colorChosen);
		        enter.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
		        upperPanel.add(enter);
		        
		        
		        
		        add(upperPanel);

		        
		        setModalityType(ModalityType.APPLICATION_MODAL);

		        setTitle("Choosing your color...");
		        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		        setLocationRelativeTo(contentPane);
		        setSize(500,338);
		        setResizable(false);
		        
		        
		        tcc = new JColorChooser();
		        tcc.getSelectionModel().addChangeListener(cl_tcc);
		        tcc.setBorder(BorderFactory.createTitledBorder("Choose your color"));

		        add(tcc, BorderLayout.PAGE_END);    
		    }
		    
		    public Color getColor(){ return color; }
		    
		    /*listener of the ColorChooser*/
		    private ChangeListener cl_tcc  = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					color = tcc.getColor();
				}
			};  
		}
	};
	
	
	ActionListener al_mi_group_shapes = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.print("al_mi_group_shapes called \n");
			
			/*set pencil, fill, eraser,etc. to unselected*/
			for (JToggleButton jtb: toggleGroup_painting){
				jtb.setSelected(false);
			}
			
			WORK work;
			String strCommand = e.getActionCommand();
			
			if (strCommand.equals(action_s_line_str)){
				work = WORK.W_S_LINE;
			}
			else if (strCommand.equals(action_s_curve_str)){
				work = WORK.W_S_CURVE;
			}
			else if (strCommand.equals(action_s_oval_str)){
				work = WORK.W_S_OVAL;
			}
			else if (strCommand.equals(action_s_rect_str)){
				work = WORK.W_S_RECT;
			}
			else if (strCommand.equals(action_s_rounded_rect_str)){
				work = WORK.W_S_ROUNDED_RECT;
			}
			else if (strCommand.equals(action_s_polygon_str)){
				work = WORK.W_S_POLYGON;
			}
			else if (strCommand.equals(action_s_tri_str)){
				work = WORK.W_S_TRI;
			}
			else if (strCommand.equals(action_s_r_tri_str)){
				work = WORK.W_S_R_TRI;
			}
			else{
				work = WORK.W_NULL;
			}
			
			setWork(work);
		}
		
	};
	
	
	MouseInputListener thisMouseListener = new MouseInputAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			requestFocus();
		}
	};
	
	
	ComponentListener resizerComponentListener = new ComponentListener() {
		@Override
		public void componentMoved(ComponentEvent e) {
			painterResizer.setBounds(0, 0, painterResizer.getWidth(), painterResizer.getHeight());
		}
		public void componentHidden(ComponentEvent e) {
			System.out.print("resizer hidden\n");
		}
		public void componentShown(ComponentEvent e) {}
		public void componentResized(ComponentEvent e) {
		
		}
	};
	
	MouseInputListener resizerMouseListener = new MouseInputAdapter() {
		private Dimension canvasSize;
		private boolean   cSizeChanged = false;
		
		@Override
		public void mousePressed(MouseEvent e){
			
			PainterCanvasPane.setLayout(null);
			
			canvasSize = painterCanvas.getSize();
			cSizeChanged = false;
		}
		
		@Override 
		public void mouseDragged(MouseEvent e){
			 cSizeChanged = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e){
			PainterCanvasPane.setLayout(new FlowLayout());
			
			if (cSizeChanged){
				painterCanvas.saveWorkByParent();
			}
			cSizeChanged = false;
			canvasSize = null;
		}
		
	};
	
	
	private ActionListener fileSaveOpenListener =  new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Handle open button action.
	        if (e.getSource() == btn_open) {
	            int returnVal = fc.showOpenDialog(getParent());

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                
	                
	                
	                
	                
	                
	                //log.append("Opening: " + file.getName() + "." + newline);
	            } else {
	                //log.append("Open command cancelled by user." + newline);
	            }
	       
	        //Handle save button action.
	        } else if (e.getSource() == btn_saveAs) {
	            int returnVal = fc.showSaveDialog(getParent());
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                try {
	                	File file = fc.getSelectedFile();
	                	String ext = getFileExt(file);
	                	if (ext == null){
	                		
	                		if (ImageFilter.class.isAssignableFrom(fc.getFileFilter().getClass()))
	                			ext  = ((ImageFilter)fc.getFileFilter()).getImageType();
	                		
                			if (ext == null){
                				ext = "png";
                			}
                
	                		file = new File(file.getPath() + '.' + ext);
	                	}
	                	
	                	filePath = file.getPath();
	                	
	                	//This is where a real application would save the file.
	                
	                	BufferedImage bi = painterCanvas.getCurrentImage();
	                	ImageIO.write(bi, "png", file);
	                	
	                	//log.append("Saving: " + file.getName() + "." + newline);
	                }
	                catch(IOException err){
	                	
	                }
	                
	            } else {
	            }
	        } else if (e.getSource() == btn_save) {
	        	if (isFileExisted()){
	        		try{
	        			File file = new File(filePath);
	        			String ext = getFileExt(file);
	        			BufferedImage bi = painterCanvas.getCurrentImage();
	                	ImageIO.write(bi, ext, file);
	        		} catch (IOException err){
	        			
	        		}
	        		
	        	}
	        	
	        	
	        	else{
		        	 int returnVal = fc.showSaveDialog(getParent());
			            if (returnVal == JFileChooser.APPROVE_OPTION) {
			                try {
			                	File file = fc.getSelectedFile();
	
			                	String ext = getFileExt(file);
			                	if (ext == null){
			                		
			                		if (ImageFilter.class.isAssignableFrom(fc.getFileFilter().getClass()))
			                			ext  = ((ImageFilter)fc.getFileFilter()).getImageType();
			                		
		                			if (ext == null){
		                				ext = "png";
		                			}
		                
			                		file = new File(file.getPath() + '.' + ext);
			                	}
			                	
			                	//This is where a real application would save the file.
			                
			                	BufferedImage bi = painterCanvas.getCurrentImage();
			                	ImageIO.write(bi, "png", file);
			                	
			                	//log.append("Saving: " + file.getName() + "." + newline);
			                }
			                catch(IOException err){
			                	
			                }
			                
			            } else {
			            }
	        	}
	        	
	        }
			
		}
	};
		
	private String getFileExt(File file)
	{
		String fileName = file.getName();
		String ext		= null;
		int i = fileName.lastIndexOf('.');
		if (i>0){
			ext = fileName.substring(i+1);
		}
		return ext;
	}
	
	private boolean isFileExisted()
	{
		if (filePath != null) 
			return true;
		return false;
	}
	
	
	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
