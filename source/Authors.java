/**
 * @author Matthew Lindner, Holly Busken, Robert Dunn
 * @version 1.0
 * Class that displays group member's names and emails. Also gives basic instructions on how to load and execute our project.
 */
package code;

import java.awt.*;
import javax.swing.*;

public class Authors extends JInternalFrame
{
	/**
	 * The default width and height of the JInternalFrame
	 */
	private int width = 700;
	private int height = 400;
	
	public Authors()
	{
		super("Authors", true, true, true, true);
		setBounds(20, 20, width, height);
		repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		/**
		 * The initial y coordinate is 100;
		 */
		int y = 100;
		/**
		 * The string that will be written to the screen.
		 */
		String str;
		
		Font boldText = new Font("Helvetica", Font.BOLD, 25);
		FontMetrics metric = g.getFontMetrics(boldText);
		g.setFont(boldText);
		
		str = "Authors";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
		
		str="Matthew Lindner, Holly Buskin, Robert Dunn";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
		
		str="Email:";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
		
		str="mdlkwd@mst.edu, hmbwbc@mst.edu, rrdbn4@mst.edu";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
		
		str="To execute:";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
		
		str="\"java -jar Demo.jar\", or open \"Demoj.html\"";
		g.drawString(str, width/2-(metric.stringWidth(str)/2), y);
		y+=30;
	}
}
