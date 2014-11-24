package code;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class BubbleSort extends JInternalFrame implements Runnable, ChangeListener, ActionListener
{
	ExecutorService executor;

	float[] randArray;
	int numElements = 30;
	int sleepTime = 200;
	int highlightIndex = 0;
	boolean doneSorting = false;
	boolean isPaused = false;
  boolean isRunning = true;

	JPanel container;
	JButton startStop, pauseResume;
	JSlider speedSlider, numElSlider;

	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

	public BubbleSort()
	{
		super("Bubble Sort", true, true, true, true);
		setBounds(0, 0, 500, 400);
		setLayout(new BorderLayout());

		pauseResume = new JButton("Pause / Resume");
		pauseResume.addActionListener(this);
    startStop = new JButton("Start / Stop");
    startStop.addActionListener(this);

    speedSlider = new JSlider(Control.MIN_SPEED, Control.MAX_SPEED, sleepTime);
		speedSlider.setBorder(new TitledBorder("Speed"));
		speedSlider.addChangeListener(this);
		numElSlider = new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS, numElements);
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

	//swap element i with element j in randArray
	public void swap(int i, int j)
	{
		if(i >= randArray.length || j >= randArray.length)
			return;

		float temp = randArray[i];
		randArray[i] = randArray[j];
		randArray[j] = temp;
	}

	public int round(float input)
	{
		return (int)(input + 0.5f);
	}

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
	}

	public void start()
	{
    randArray = new float[numElements];
    for(int i = 0; i < numElements; i++)
      randArray[i] = (i+1) * (1.0f / (float)numElements);

    //randomize elements in the array so we have something to sort
    Random rand = new Random();
    for(int i = 0; i < numElements*2; i++)
      swap(rand.nextInt(randArray.length), rand.nextInt(randArray.length));
    isRunning = true;
		executor.execute(this);
	}

  public void stop()
  {
    isRunning = false;
    isPaused = false;
  }

	public void pause()
	{
    if(isRunning)
		  isPaused = true;
	}

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

  public void setNumberOfElements(int numEl)
  {
    if(isRunning)
      stop();
    numElements = numEl;
    start();
  }

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
