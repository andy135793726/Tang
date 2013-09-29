package painter;

import java.awt.Color;

import javax.swing.JFrame;


public class Main
{
	private static PainterJFrame painterJFrame;
	private static Color color1, color2;
	
	private static TAB  tab;
	private static WORK work;
	
	private static int undo_step_num;
	
	
	
	public static enum TAB { TAB_HOME, TAB_VIEW }
	
	public static enum WORK{
		W_NULL,
		W_CLEAR,
		W_CUT, 				W_COPY, 	W_PASTE,
		W_SELECT,   W_CROP/*µô´î*/, 	   W_RESIZE, 	W_ROTATE,
		W_PENCIL, 	W_FILL, W_WORD,    W_ERASER,  W_COLORSEL,   W_MAGNIFIER,
		W_PAINT,
		
		/*Shapes,     "_S_" means "shape" */
		W_S_RESIZE,
		W_S_LINE ,	 		W_S_CURVE, 	 		W_S_OVAL, 			W_S_RECT, 
				  	 W_S_ROUNDED_RECT, 		 W_S_POLYGON, 		 	 W_S_TRI,
		W_S_R_TRI, 		  W_S_DIAMOND, W_S_PENTAGON/*¤­Ãä§Î*/, 	 W_S_HEXAGON, 
						  W_S_R_ARROW, 	  	 W_S_L_ARROW,		 W_S_U_ARROW,
		W_S_D_ARROW,	W_S_FOUR_STAR, 	   W_S_FIVE_STAR,		W_S_SIX_STAR,
			  W_S_ROUNDED_RECT_DIALOG,   W_S_OVAL_DIALOG,   W_S_CLOUD_DIALOG,
		W_S_BORDER, W_S_FILL, 
		
		W_STROKE,
		W_COLOR,
		W_ZOOM_IN, W_ZOOM_OUT, W_ZOOM_100, W_FULL_SCREEN
	}

	public static final int THICKNESS_DEFAULT_PENCIL = 1;
	public static final int THICKNESS_DEFAULT_ERASER = 3;
	public static final int THICKNESS_DEFAULT_SHAPES = 3;
	
	
	public static void main(String[] args)
	{
		initConst();
		painterJFrame = new PainterJFrame();
		painterJFrame.setVisible(true);
	}
	
	public static void initConst()
	{
		color1 =   Color.black;
		color2 =   Color.white;
		tab	   =  TAB.TAB_HOME;
		work   = WORK.W_PENCIL;
		undo_step_num = 20;
	}
	
	
	public static void  setColor1(Color c) { color1 = c; }
	public static void  setColor2(Color c) { color2 = c; }
	public static Color getCurColor1()     { return color1; }
	public static Color getCurColor2()     { return color2; }
	
	public static void setWork(WORK w) { 
		work = w; 
		System.out.print("Current work is:" + work.toString()+'\n');
	}
	public static WORK getCurWork()    { return work; }
	
	public static void setTab(TAB t)   { tab = t; }
	public static TAB  getCurTab()	   { return tab; }
	
	public static boolean isPainting(){
		return (//work == WORK.W_SELECT||   
				//work == WORK.W_CROP|| 	   
				work == WORK.W_RESIZE|| 	
				work == WORK.W_ROTATE||
				work == WORK.W_PENCIL|| 	
				work == WORK.W_FILL|| 
				work == WORK.W_WORD||    
				work == WORK.W_ERASER||  
				work == WORK.W_COLORSEL||   
				work == WORK.W_MAGNIFIER||
				work == WORK.W_PAINT||
				work == WORK.W_S_RESIZE||
				work == WORK.W_S_LINE ||	 		
				work == WORK.W_S_CURVE|| 	 		
				work == WORK.W_S_OVAL|| 			
				work == WORK.W_S_RECT|| 
				work == WORK.W_S_ROUNDED_RECT|| 		 
				work == WORK.W_S_POLYGON|| 		 	 
				work == WORK.W_S_TRI||
				work == WORK.W_S_R_TRI|| 		  
				work == WORK.W_S_DIAMOND|| 
				work == WORK.W_S_PENTAGON|| 	 
				work == WORK.W_S_HEXAGON|| 
				work == WORK.W_S_R_ARROW|| 	  	 
				work == WORK.W_S_L_ARROW||		 
				work == WORK.W_S_U_ARROW||
				work == WORK.W_S_D_ARROW||	
				work == WORK.W_S_FOUR_STAR|| 	   
				work == WORK.W_S_FIVE_STAR||		
				work == WORK.W_S_SIX_STAR||
				work == WORK.W_S_ROUNDED_RECT_DIALOG||   
				work == WORK.W_S_OVAL_DIALOG||   
				work == WORK.W_S_CLOUD_DIALOG||
			  	work == WORK.W_S_BORDER|| 
			  	work == WORK.W_S_FILL );
	}
	
	
	public static void setUndoStepNum(int num)
	{
		undo_step_num = num;
	}
	
	public static int getUndoStepNum(){
		return undo_step_num;
	}
	
	
	public static boolean workIsShapes()
	{
		return (
				work == WORK.W_S_RESIZE||
				work == WORK.W_S_LINE ||	 		
				work == WORK.W_S_CURVE|| 	 		
				work == WORK.W_S_OVAL|| 			
				work == WORK.W_S_RECT|| 
				work == WORK.W_S_ROUNDED_RECT|| 		 
				work == WORK.W_S_POLYGON|| 		 	 
				work == WORK.W_S_TRI||
				work == WORK.W_S_R_TRI|| 		  
				work == WORK.W_S_DIAMOND|| 
				work == WORK.W_S_PENTAGON|| 	 
				work == WORK.W_S_HEXAGON|| 
				work == WORK.W_S_R_ARROW|| 	  	 
				work == WORK.W_S_L_ARROW||		 
				work == WORK.W_S_U_ARROW||
				work == WORK.W_S_D_ARROW||	
				work == WORK.W_S_FOUR_STAR|| 	   
				work == WORK.W_S_FIVE_STAR||		
				work == WORK.W_S_SIX_STAR||
				work == WORK.W_S_ROUNDED_RECT_DIALOG||   
				work == WORK.W_S_OVAL_DIALOG||   
				work == WORK.W_S_CLOUD_DIALOG||
			  	work == WORK.W_S_BORDER|| 
			  	work == WORK.W_S_FILL );
	}

}