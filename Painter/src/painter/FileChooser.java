package painter;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileChooser implements ActionListener {
	   static private final String newline = "\n";
	    JButton openButton, saveButton;
	    JFileChooser fc;

	    JFrame parentFrame;
	    
	    public FileChooser(JFrame pjf, JButton openBtn, JButton saveBtn)
	    {
	    	this();
	    	fc = new JFileChooser();
	    	parentFrame = pjf;
	    	openButton  = new JButton("open");
	    	openButton.addActionListener(this);
	    	saveButton  = new JButton("save");
	    	saveButton.addActionListener(this);
	    	
	    }
	    
	    public FileChooser() {

	        //Create a file chooser
	        fc = new JFileChooser();

	        //Uncomment one of the following lines to try a different
	        //file selection mode.  The first allows just directories
	        //to be selected (and, at least in the Java look and feel,
	        //shown).  The second allows both files and directories
	        //to be selected.  If you leave these lines commented out,
	        //then the default mode (FILES_ONLY) will be used.
	        //
	        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES).
	    }

	    public void actionPerformed(ActionEvent e) {

	        //Handle open button action.
	        if (e.getSource() == openButton) {
	            int returnVal = fc.showOpenDialog(parentFrame);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	                //log.append("Opening: " + file.getName() + "." + newline);
	            } else {
	                //log.append("Open command cancelled by user." + newline);
	            }
	       
	        //Handle save button action.
	        } else if (e.getSource() == saveButton) {
	            int returnVal = fc.showSaveDialog(parentFrame);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                
	                //log.append("Saving: " + file.getName() + "." + newline);
	            } else {
	                //log.append("Save command cancelled by user." + newline);
	            }
	        }
	    }

	    /** Returns an ImageIcon, or null if the path was invalid. */
	    protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = FileChooser.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }

	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event dispatch thread.
	     */
	    private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("FileChooser");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Add content to the window.
	       // frame.add(new FileChooser());

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }

	}

