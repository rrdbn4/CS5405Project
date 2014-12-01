/**
 * @author Matthew Lindner 
 * @version 1.0
 * Class that acts as a clock. Updates every second, writing the time to a JLablel
 */

package code;

import java.util.Date;
import java.util.concurrent.*;
import javax.swing.JLabel;

public class Clock extends JLabel implements Runnable
{
	/**
	 * Constructor for clock. Creates a fixed thread pool of size 1, and immediatly executes that thread.
	 */
	public Clock()
	{
		Executor executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
	}
	
	/**
	 * Function called by execute(), runs in a thraed.
	 * Every second, updates the JLabel with the new time
	 */
	public void run()
	{
		while(true)
		{
			setText(new Date().toString());
			try
			{
				/**
				 * Wait 1 second
				 */
				Thread.sleep(100);
			}
			catch(InterruptedException e){}
		}
	}
	
}
