/**
 * @author Matthew Lindner, Holly Busken, Robert Dunn
 * @version 1.0
 * Class that provides some useful help about using our program.
 * Text area is contained in jScrollPane, which is placed inside JInternal Frame.
 */
package code;

import javax.swing.*;

public class Help extends JInternalFrame
{
	/**
	 * Text box that will contain the help information.
	 */
	private JTextArea textArea;
	/**
	 * Scroll Pane for the text area.
	 */
	JScrollPane scrollPane;
	/**
	 * The default width and height of the JInternalFrame
	 */
	private int width = 980;
	private int height = 500;
	
	public Help()
	{
		super("Help", true, true, true, true);
		setBounds(0, 10, width, height);
		
		textArea = new JTextArea("About Menu:\n"
				+ "\tAuthors - The names of each group member, their emails, and instructions on how to execute the application.\n"
				+ "\tProblem Description - Condensed text description of the assignment.\n"
				+ "\tHelp - This window, explains the functionality of this program.\n"
				+ "\tReferences - Citations of any outside help any group memeber may have used to complete this project.\n\n"
				+ "Demos:\n"
				+ "\tSelect a sort, and it will immediatly begin sorting. Helpful if the user does not understand what a sort does, or how various sorts operate differntly.\n\n"
				+ "MultiTasking:\n"
				+ "\tAllows user to select multiple sorts. Once the user has picked their sorts, he or she should click the \"Start\" button. This allows the user to compare the speed of differnt sorts.\n\n"
				+ "Clock:\n"
				+ "\tThe current time. Helpful if the user wants to time a sort.\n\n"
				+ "Start/Stop - Either starts or stops all open sorts.\n"
				+ "Pause/Resume - Either pauses or resumes all open sorts.\n"
				+ "Speed - adjust the speed of all open sorts.\n"
				+ "Number of Elements - Changes the number of elemnts all open sorts have to process. The user is required to press \"Start\" after changing the number of elemenmts.");
		
		/**
		 * The help text is read only.
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
