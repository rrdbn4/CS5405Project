/**
 * @author Holly Busken
 * @version 1.0
 * Merge sort frame.
 * Visualizes merge sort algorithm in a JInternalFrame. 
*/
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

/**
The MergeSort class creates a visualization and uses controls 
for the MergeThread class.
*/
public class MergeSort extends JInternalFrame implements ActionListener, ChangeListener, Runnable
{
  /** size controls the number of items sorted. */
  int size;
  /** delay controls the speed of sorting. */
  int delay;
  /** pause is a flag for halting MergeThread execution. */
  boolean pause;
  /** startButton is the button for starting MergeThread execution. */
  JButton startButton;
  /** stop is the button for stopping MergeThread execution. */
  JButton stop;
  /** pauseButton is the button for halting MergeThread execution. */
  JButton pauseButton;
  /** sizeSelector is the slider for changing the number of items to sort. */
  JSlider sizeSelector;
  /** delaySelector is the slider for changing the speed of sorting. */
  JSlider delaySelector;
  /** mThread is the MergeThread being controlled and visualized. */
  MergeThread mThread;
  /** running is a flag for determining if MergeThread is running. */
  boolean running;
  /** executor is the thread service for MergeSort. */
  private final ExecutorService executor;

  /** 
  The constructor creates buttons and sliders for controlling the MergeThread execution.
  The variables size and delay are set to the default values in the Control class.
  */
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
        size=Control.DEFUALT_NUM_OF_ELEMENTS;
		setSize(650,300);
		setVisible(true);
		setOpaque(true);
        mThread=new MergeThread();
		pause=false;
		delay=Control.DEFAULT_SPEED;
  	}
	
  /**
  Sets the speed for sorting.
  @param value is the speed to set for the delay
  */  
  public void setDelay(int value)
  {
    delay = Control.MAX_SPEED - value;
	delaySelector.setValue(value);
    mThread.setDelay(delay);
  }
  
  /**
  Sets the number of elements to sort. MergeThread is reset.
  @param value is the new number of elements to sort.
  */
  public void setNumberOfElements(int value)
  {
    sizeSelector.setValue(value);
    size=value;
	mThread.stop();
	mThread= new MergeThread();
	mThread.start(size,delay);
	running=true;
  }
	
  /**
  Detects changes in the sliders and changes the values for size and delay as needed.
  */
  public void stateChanged(ChangeEvent event)
  {
    if(event.getSource()==sizeSelector)
	{
      int x = sizeSelector.getValue();
	  size=x;
	  mThread.stop();
	  mThread= new MergeThread();
	  mThread.start(size,delay);
	  running=true;	  
	}
	if(event.getSource()==delaySelector)
	{
      delay = Control.MAX_SPEED - delaySelector.getValue();
	  mThread.setDelay(delay);
	}
	
  }

  /**
  Detects when the buttons are pressed and starts, stops, or pauses
  the MergeThread execution when appropiate.
  */
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
  
  /**
  Stops the MergeThread then starts a new MergeThread
  and sets running to true.
  */
  public void start()
  {
	  mThread.stop();
	  mThread= new MergeThread();
	  mThread.start(size,delay);
	  running=true;
  }
  
  /**
  Stops the MergeThread and sets running to false.
  */
  public void stop()
  {
    mThread.stop();
	running=false;
  }
  
  /**
  Determines if MergeThread is running.
  @return true if MergeThread is running, false otherwise.
  */
  public boolean isRunning()
  {
    return running;
  }
  
  /**
  Determines if MergeThread's sort execution is paused.
  @return true is execution is paused, false otherwise.
  */
  public boolean isPaused()
  {
    return pause;
  }
  
  /**
  Halts or resumes the execution of MergeThread.
  */
  public void resume()
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
  
  /**
  Paints a series of bars in the frame representing the numbers being sorted.
  */
  public void paint(Graphics g)
  {
 	super.paint(g);
    int[] data=mThread.getData();
	if(data!=null)
	{
	int offset=0;
	int x=10;
	int y=getHeight()-(getHeight()/size);
	int width=(getWidth()-10)/(size+1);
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
  
  /**
  The paint method is continually called to update the visualization until the frame is closed.
  */
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
