//Holly Busken
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.*;
import java.util.Random;

public class MergeSort extends JInternalFrame implements ActionListener, Runnable
{

  private boolean resumed = false;
  int size;
  boolean pause;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  boolean start=false;
  MergeThread mThread;
  private final ExecutorService executor;
  public Lock lock=new ReentrantLock();
  public Condition condition=lock.newCondition();

  	public MergeSort()
  	{
		super("Merge Sort Demo", true, true, true, true);
		startButton=new JButton("Start");
		startButton.addActionListener(this);
		stop=new JButton("Stop");
		stop.addActionListener(this);
		pauseButton=new JButton("Pause");
		pauseButton.addActionListener(this);
		setLayout(new FlowLayout());
		add(startButton);
		add(stop);
		add(pauseButton);
		executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
		setSize(400,400);
		setVisible(true);
		setOpaque(true);
        mThread=new MergeThread();
		
  	}

  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource()==startButton)
	{
	  mThread.start();
	}
	else if(event.getSource()==pauseButton)
	{
	  mThread.pause();
	}
	else if(event.getSource()==stop)
	{
	  mThread.stop();
	}
  }

  public void paint(Graphics g)
  {
 	super.paint(g);
    string=mThread.output();
	//System.out.println(string);
	g.drawString(string,100,100);
    
  }
  
  public void run()
  {
    while(!isClosed())
	{
	  repaint();
	  	  try {Thread.sleep(10);}
      catch (InterruptedException ex){}
	 }
	 executor.shutdownNow();
  }

}
