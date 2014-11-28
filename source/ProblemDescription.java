/**
 * @author Matthew Lindner, Holly Busken, Robert Dunn
 * @version 1.0
 * Class that shows group project problem description inside of a Text Area.
 * Text area is contained in jScrollPane, which is placed inside JInternal Frame.
 */
package code;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ProblemDescription extends JInternalFrame
{
	/**
	 * Text box that will contain group project problem description.
	 */
	private JTextArea textArea;
	/**
	 * Scroll Pane for the text area.
	 */
	JScrollPane scrollPane;
	/**
	 * The default width and height of the JInternalFrame
	 */
	private int width = 700;
	private int height = 200;
	
	public ProblemDescription()
	{
		super("Problem Description", true, true, true, true);
		setBounds(20, 20, width, height);
		
		textArea = new JTextArea("You will have seperate internal frame for each sorting algorithm. Each sorting algorithm should run in its own thread.\n\n"
				+ "Each sort can be controlled from both inside and outside of the sort.\n\n"
				+ "You should be able to start, stop, pause, and resume each sort.\n\n"
				+ "You should be able to control the speed, and the number of elements.\n\n"
				+ "Sorts: Bubble sort, Insertion Sort, Selection Sort, Merge Sort, Quick Sort, Heap Sort, Shell Sort.");
		
		/**
		 * The problem description is read only.
		 */
		textArea.setEditable(false);
		/**
		 * If the width of the text area is not big enough to accommodate each sentence, the words will wrap to the next line.
		 */
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		/**
		 * Create the scroll pane object, add the text area
		 */
		scrollPane = new JScrollPane(textArea);
		
		/**
		 * Add the scroll pane to the JPanel
		 */
		add(scrollPane);
	}
}
