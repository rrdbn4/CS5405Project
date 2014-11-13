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

public class QuickSort extends JInternalFrame implements ActionListener, Runnable
{

  private boolean resumed = false;
  int size;
  boolean pause;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  boolean start=false;
  QuickThread qThread;
  private final ExecutorService executor;
  public Lock lock=new ReentrantLock();
  public Condition condition=lock.newCondition();

  	public QuickSort()
  	{
		super("Quick Sort Demo", true, true, true, true);
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
        qThread=new QuickThread();
		pause=false;
		size=10;
  	}

  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource()==startButton)
	{
	  qThread.stop();
	  qThread= new QuickThread();
	  qThread.start(size);
	}
	else if(event.getSource()==pauseButton)
	{
	  pause=!pause;
	  if(pause==true)
	  {
	    pauseButton.setText("Unpause");
	    qThread.pause();
	  }
	  if(pause==false)
	  {
	    pauseButton.setText("Pause");
		qThread.unpause();
	  }
	}
	else if(event.getSource()==stop)
	{
	  qThread.stop();
	}
  }

  public void paint(Graphics g)
  {
 	super.paint(g);
    int[] data=qThread.getData();
	if(data!=null)
	{
	int offset=0;
	int x=10;
	int y=200;
	int width=(getWidth()-20)/size;
	for(int i=0;i<data.length;i++)
	{
	  g.drawRect(x+offset,y,width,-data[i]);
	  offset+=width;
	}
	}
    
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
