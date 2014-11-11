import java.util.Date;
import java.util.concurrent.*;
import javax.swing.JLabel;

public class Clock extends JLabel implements Runnable
{
	public Clock()
	{
		Executor executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
	}
	
	public void run()
	{
		while(true)
		{
			setText(new Date().toString());
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e){}
		}
	}
	
}
