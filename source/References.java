/**
 * @author Matthew Lindner, Holly Busken, Robert Dunn
 * @version 1.0
 * Class that stores references to code that helped group members complete this project.
 * Text area is contained in jScrollPane, which is placed inside JInternal Frame.
 */
package code;

import javax.swing.*;

public class References extends JInternalFrame
{
	/**
	 * Text box that will contain a lit of references.
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
	
	public References()
	{
		super("References", true, true, true, true);
		setBounds(20, 20, width, height);
		String output ="Heap sort was adapted from pseduo-code \"Introduction To Algorithms\", Third Edition, pages 154, 157, and 160.\n";
		output+="Quick sort was adapted from pseudo-code from Wikipedia, http://en.wikipedia.org/wiki/Quicksort\n";
		output+= "Clock class was adapted from Dr. Sabharwals notes http://web.mst.edu/~chaman/courses/CS342/Demos/14MultiTasking/03ClockInMenu/\n";
		textArea = new JTextArea(output);
		
		/**
		 * The references text is read only.
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
