/**
 * @author Matthew Lindner, Holly Busken, Robert Dunn
 * @version 1.0
 * This Class allows our project to be executed as either an application, or an applet. 
 */

package code;

import javax.swing.JApplet;

public class Demo extends JApplet
{
	/**
	 * init() will be executed if Demo.class is opened in a web-browser.
	 * Calls the "Driver" constructor, which creates JFrame, sorts, etc.
	 */
	public void init()
	{
		new Driver();
	}
	
	/**
	 * main() will be executed if Demo.class is opened in a desktop environment. 
	 * Calls the "Driver" constructor, which creates JFrame, sorts, etc.
	 */
	public static void main(String [] args)
	{
		new Driver();
	}
}