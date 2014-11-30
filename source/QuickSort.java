/**
 * @author Holly Busken
 * @version 1.0
 * Quick sort frame.
 * Visualizes the quick sort algorithm in a JInternalFrame. 
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
import javax.swing.border.TitledBorder;
import static java.lang.Math.*;
import java.util.Random;

/**
The QuickSort class creates a visualization and uses controls 
for the QuickThread class.
*/
public class QuickSort extends JInternalFrame implements ActionListener, ChangeListener, Runnable
{
  /** size controls the number of items sorted. */
  int size;
  /** delay controls the speed of sorting. */
  int delay;
  /** pause is a flag for halting QuickThread execution. */
  boolean pause;
  /** running is a flag for determining if QuickThread is running. */
  boolean running;
  /** startButton is the button for starting QuickThread execution. */
  JButton startButton;
  /** stop is the button for stopping QuickThread execution. */
  JButton stop;
  /** pauseButton is the button for halting QuickThread execution. */
  JButton pauseButton;
  /** sizeSelector is the slider for changing the number of items to sort. */
  JSlider sizeSelector;
  /** delaySelector is the slider for changing the speed of sorting. */
  JSlider delaySelector;
  /** mThread is the QuickThread being controlled and visualized. */
  QuickThread qThread;
  /** executor is the thread service for QuickSort. */
  private final ExecutorService executor;
  /** container holds the buttons for controlling execution of merge sort. */
  JPanel container;
  
    /** 
  The constructor creates buttons and sliders for controlling the QuickThread execution.
  The variables size and delay are set to the default values in the Control class.
  */
  	public QuickSort()
  	{
		super("Quick Sort Demo", true, true, true, true);
		size=Control.DEFUALT_NUM_OF_ELEMENTS;
		pause=false;
		delay=Control.DEFAULT_SPEED;
		
		startButton=new JButton("Start");
		startButton.addActionListener(this);
		stop=new JButton("Stop");
		stop.addActionListener(this);
		pauseButton=new JButton("Pause");
		pauseButton.addActionListener(this);
	
		sizeSelector= new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS,10);
		sizeSelector.setBorder(new TitledBorder("Number of Elements"));
		sizeSelector.addChangeListener(this);

			
		delaySelector= new JSlider(Control.MIN_SPEED, Control.MAX_SPEED,300);
		delaySelector.setBorder(new TitledBorder("Speed"));
		delaySelector.addChangeListener(this);
		
		container = new JPanel();
		container.add(startButton);
        container.add(stop);
		container.add(pauseButton);
		container.add(delaySelector);
		container.add(sizeSelector);
		add(container, BorderLayout.SOUTH);
		
		executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
        
		setSize(650,300);
		setVisible(true);
		setOpaque(true);
        qThread=new QuickThread();

  	}
	
  /**
  Sets the speed for sorting.
  @param value is the speed to set for the delay
  */ 
  public void setDelay(int value)
  {
    delay = Control.MAX_SPEED - value;
	delaySelector.setValue(value);
    qThread.setDelay(delay);
  }
  
  /**
  Sets the number of elements to sort. QuickThread is reset.
  @param value is the new number of elements to sort.
  */  
  public void setNumberOfElements(int value)
  {
    sizeSelector.setValue(value);
    size=value;
	qThread= new QuickThread();
    qThread.start(size,delay);
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
	  qThread= new QuickThread();
	  qThread.start(size,delay);
	  running=true;	  
	}
	if(event.getSource()==delaySelector)
	{
      int x = Control.MAX_SPEED - delaySelector.getValue();
	  qThread.setDelay(x);
	  delay=x;
	}
	
  }
  
  /**
  Stops the QuickThread then starts a new QuickThread
  and sets running to true.
  */  
  public void start()
  {
	  qThread.stop();
	  qThread= new QuickThread();
	  qThread.start(size,delay);
	  running=true;
  }
  
  /**
  Stops the QuickThread and sets running to false.
  */  
  public void stop()
  {
    qThread.stop();
	running=false;
  }
  
  /**
  Determines if QuickThread is running.
  @return true if QuickThread is running, false otherwise.
  */  
  public boolean isRunning()
  {
    return running;
  }
  
  /**
  Determines if QuickThread's sort execution is paused.
  @return true is execution is paused, false otherwise.
  */  
  public boolean isPaused()
  {
    return pause;
  }

  /**
  Detects when the buttons are pressed and starts, stops, or pauses
  the QuickThread execution when appropiate.
  */  
  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource()==startButton)
	{
	  qThread.stop();
	  qThread= new QuickThread();
	  qThread.start(size,delay);
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
  
  /**
  Halts or resumes the execution of QuickThread.
  */  
  public void resume()
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

  /**
  Paints a series of bars in the frame representing the numbers being sorted.
  */  
  public void paint(Graphics g)
  {
 	super.paint(g);
    float[] data=qThread.getData();
	if(data!=null)
	{
    int width = getWidth() - getInsets().left - getInsets().right;
	int height = getHeight() - getInsets().top - getInsets().bottom - container.getHeight(); 
	for(int i=0;i<data.length;i++)
	{
	  if(qThread.isFinished()==true)
	    g.setColor(Color.GREEN);
	  else if(i==qThread.getHighlight())
	    g.setColor(Color.RED);
	  else
	    g.setColor(Color.BLUE);

	  g.fillRect(getInsets().left + round(i * (width / (float)data.length)), getInsets().top + round(height - (height * data[i])), round(width / (float)data.length), round(height * data[i]));
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
