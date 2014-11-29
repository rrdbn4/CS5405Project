package code;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

/**
The internal frame visualizer for bubble sort.
The sort runs in its own thread and the speed and number of elements in the array are adjustable.
The visualization is a simple colorized bar graph
@author Robert Dunn
*/
public class BubbleSort extends JInternalFrame implements Runnable, ChangeListener, ActionListener
{
  /** 
  The executor service for the sorting thread 
  */
	ExecutorService executor;

  /**
  The randomized array of elements being sorted for the visual
  */
	float[] randArray;
  /**
  The size of randArray. This defaults to the global default constant
  */
	int numElements = Control.DEFUALT_NUM_OF_ELEMENTS;

  /**
  The sleep time in milliseconds for the sorting thread sleep interruption
  */
	int sleepTime = Control.DEFAULT_SPEED;
  /**
  The current index of the sort to highlight in the visual
  */
	int highlightIndex = 0;
  /**
  The bool flag to tell if the sort is done
  */
	boolean doneSorting = false;
  /**
  The bool flag to tell if the thread is paused
  */
	boolean isPaused = false;
  /**
  The bool flag to tell if the thread is stopped
  */
  boolean isRunning = true;

  /**
  The container for the sliders, buttons, and gridlayout
  */
	JPanel container;
  /**
  The buttons to control the visual
  */
	JButton startStop, pauseResume;
  /**
  The sliders to controls the speed of the sort and number of elements in the array
  */
	JSlider speedSlider, numElSlider;

  /**
  The lock and mutex for the thread stuff
  */
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

  /**
  Sets up the graphical elements and stes up the executor service
  */
	public BubbleSort()
	{
		super("Bubble Sort", true, true, true, true);
		setBounds(0, 0, 500, 400);
		setLayout(new BorderLayout());

		pauseResume = new JButton("Pause / Resume");
		pauseResume.addActionListener(this);
    startStop = new JButton("Start / Stop");
    startStop.addActionListener(this);

    speedSlider = new JSlider(Control.MIN_SPEED, Control.MAX_SPEED, Control.DEFAULT_SPEED);
		speedSlider.setBorder(new TitledBorder("Speed"));
		speedSlider.addChangeListener(this);
		numElSlider = new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS, Control.DEFUALT_NUM_OF_ELEMENTS);
		numElSlider.setBorder(new TitledBorder("No. Elements"));
		numElSlider.addChangeListener(this);

		container = new JPanel();
    container.setLayout(new GridLayout(1, 4));
		container.add(startStop);
		container.add(pauseResume);
		container.add(speedSlider);
		container.add(numElSlider);
		add(container, BorderLayout.SOUTH);

		executor = Executors.newFixedThreadPool(1);
	}

  /**
	swap element i with element j in randArray
  @param i   the index of the the element to swap with element j
  @param i   the index of the the element to swap with element i
  */
	public void swap(int i, int j)
	{
		if(i >= randArray.length || j >= randArray.length)
			return;

		float temp = randArray[i];
		randArray[i] = randArray[j];
		randArray[j] = temp;
	}

  /**
  rounds the input to the nearest integer
  @param input   the float to be rounded
  @return   the rounded float returned as an int
  */
	public int round(float input)
	{
		return (int)(input + 0.5f);
	}

  /**
  the sorting thread's run method
  this is where bubble sort is performed and where the painting is controlled
  */
	public void run()
	{
		doneSorting = false;
    isPaused = false;
    isRunning = true;
		for(int i = 0; i < randArray.length; i++)
		{
			for(int j = 0; j < randArray.length - i - 1; j++)
			{
        if(!isRunning)  //break out of loop
        {
          j = 9999999;
          i = 9999999;
          break;
        }
				if(isPaused)
				{
					lock.lock();
					try
					{
						condition.await();
					}catch(InterruptedException e){}
					lock.unlock();
				}
				if(randArray[j + 1] < randArray[j])
					swap(j, j+1);
				highlightIndex = j + 1;
				try
				{
					Thread.sleep(sleepTime);
				} catch(InterruptedException e){}

				repaint();
			}
		}
    if(isRunning) //only show as done if it wasn't killed
    {
  		doneSorting = true;
  		repaint();
    }
    isRunning = false;
	}

  /**
  starts the visualization thread
  */
	public void start()
	{
    if(isRunning)
      stop();
    if(randArray == null || isSorted())
      createRandArray();
    isRunning = true;
		executor.execute(this);
	}

  /**
  fills randArray with evenly stepped floats, then randomizes the elements
  */
  public void createRandArray()
  {
    randArray = new float[numElements];
    for(int i = 0; i < numElements; i++)
      randArray[i] = (i+1) * (1.0f / (float)numElements);

    //randomize elements in the array so we have something to sort
    Random rand = new Random();
    for(int i = 0; i < numElements*2; i++)
      swap(rand.nextInt(randArray.length), rand.nextInt(randArray.length));
  }

  /**
  is the array sorted?
  @return true if randArray is sorted, false if not
  */
  public boolean isSorted()
  {
    for(int i = 0; i < randArray.length - 1; i++)
      if(randArray[i] >= randArray[i + 1])
        return false;

    return true;
  }

  /**
  stops the visualization thread
  */
  public void stop()
  {
    isRunning = false;
    isPaused = false;
  }

  /**
  pauses the visual thread and waits for the condition signal
  */
	public void pause()
	{
    if(isRunning)
		  isPaused = true;
	}

  /**
  is the visual running?
  @return true if the thread is running, false if not
  */
  public boolean isRunning()
  {
    return isRunning;
  }

  /**
  is the visual paused?
  @return true if the thread is paused, false if not
  */
  public boolean isPaused()
  {
    return isPaused;
  }

  /**
  resumes the thread from a paused state if it is running in the first place
  */
	public void resume()
  {
    if(isRunning)
    {
  		isPaused = false;
  		lock.lock();  
  		condition.signal();
  		lock.unlock();
    }
	}
  /**
  stops the visual if it's running, sets the number of elements in the array, then randomizes the array 
  @param numEl   the number of elements in randArray
  */
  public void setNumberOfElements(int numEl)
  {
    if(isRunning)
      stop();
    numElements = numEl;
    createRandArray();
    highlightIndex = -1;
    repaint();
  }

  /**
  set the thread sleep time
  @param millisecondDelay   sleep time
  */
  public void setDelay(int millisecondDelay)
  {
    speedSlider.setValue(millisecondDelay);
  }

  /**
  action event for the buttons
  controls thread pausing, resuming, stopping, and starting
  */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == pauseResume)
		{
			if(isPaused)
				resume();
			else
				pause();
		}
		else if(e.getSource() == startStop)
		{
      if(isRunning)
        stop();
      else
        start();
		}
	}

  /**
  change listener for the sliders
  controls the speed of the sort and number of elements in the array
  */
	public void stateChanged(ChangeEvent e)
	{
		if(e.getSource() == speedSlider)
		{
			sleepTime = Control.MAX_SPEED - speedSlider.getValue();
		}
		else
		{
			setNumberOfElements(numElSlider.getValue());
		}
	}

  /**
  paint the bar graph visual of the sort
  */
	public void paint(Graphics g)
	{
		super.paint(g);
		float width = getWidth() - getInsets().left - getInsets().right;
		float height = getHeight() - getInsets().top - getInsets().bottom - container.getHeight();

		for(int i = 0; i < randArray.length; i++)
		{
			if(!doneSorting)
			{
				if(i == highlightIndex)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLUE);
			}
			else
				g.setColor(Color.GREEN);
			g.fillRect(getInsets().left + round(i * (width / (float)numElements)), getInsets().top + round(height - (height * randArray[i])), round(width / (float)numElements), round(height * randArray[i]));
		}
	}
}
