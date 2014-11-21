package code;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.event.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.*;
import java.util.Random;

public class MergeSort extends JInternalFrame implements ActionListener, ChangeListener, Runnable
{

  private boolean resumed = false;
  int size;
  int delay;
  boolean pause;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  JSlider sizeSelector;
  JSlider delaySelector;
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
		JPanel sizePanel=new JPanel();		
		JLabel sizeLabel= new JLabel("Slide for Number of Elements to Sort");
		sizePanel.add(sizeLabel);
		sizeSelector= new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS,10);
		sizeSelector.setMajorTickSpacing(100);
		sizeSelector.setPaintTicks(true);
		sizeSelector.addChangeListener(this);
		sizePanel.add(sizeSelector);
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.PAGE_AXIS));
		add(sizePanel);		
		
		JPanel delayPanel=new JPanel();		
		JLabel delayLabel= new JLabel("Slide for the Speed of Sorting");
		delayPanel.add(delayLabel);
		delaySelector= new JSlider(Control.MIN_SPEED, Control.MAX_SPEED,300);
		delaySelector.setMajorTickSpacing(100);
		delaySelector.setPaintTicks(true);
		delaySelector.addChangeListener(this);
		sizePanel.add(delaySelector);
		delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.PAGE_AXIS));
		add(delayPanel);
		executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
        size=10;
		setSize(600,600);
		setVisible(true);
		setOpaque(true);
        mThread=new MergeThread();
		pause=false;
		delay=1000;
  	}
	
  public void stateChanged(ChangeEvent event)
  {
    if(event.getSource()==sizeSelector)
	{
      int x = sizeSelector.getValue();
	  size=x;
	}
	if(event.getSource()==delaySelector)
	{
      delay = Control.MAX_SPEED - delaySelector.getValue();
	  mThread.setDelay(delay);
	}
	
  }

  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource()==startButton)
	{
	  mThread.stop();
	  mThread= new MergeThread();
	  mThread.start(size,delay);
	}
	else if(event.getSource()==pauseButton)
	{
	  pause=!pause;
	  if(pause==true)
	  {
	    pauseButton.setText("Unpause");
	    mThread.pause();
	  }
	  if(pause==false)
	  {
	    pauseButton.setText("Pause");
		mThread.unpause();
	  }
	}
	else if(event.getSource()==stop)
	{
	  mThread.stop();
	}
  }
  
  public void universalStart()
  {
    mThread.start(size,delay);
  }
  
  public void universalStop()
  {
    mThread.stop();
  }

  public void universalPause()
  {
	  pause=!pause;
	  if(pause==true)
	  {
	    pauseButton.setText("Unpause");
	    mThread.pause();
	  }
	  if(pause==false)
	  {
	    pauseButton.setText("Pause");
		mThread.unpause();
	  }  
  }
  public void paint(Graphics g)
  {
 	super.paint(g);
    int[] data=mThread.getData();
	if(data!=null)
	{
	int offset=0;
	int x=10;
	int y=500;
	int width=(getWidth()-20)/size;
	for(int i=0;i<data.length;i++)
	{
	  if(mThread.isFinished()==true)
	    g.setColor(Color.GREEN);
	  else if(i==mThread.getHighlight())
	    g.setColor(Color.RED);
	  else
	    g.setColor(Color.BLUE);
	  g.fillRect(x+offset,y-data[i],width,data[i]);
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
