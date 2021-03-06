/**
 * @author Matthew Lindner
 * @version 1.0
 * Selection sort implementation.
 * Visualizes sorting algorithm in JInternalFrame. Sort is executed in a separate thread.
 * Sort can be paused/resume and started/stopped by 2 JButtons. Speed of sort, and the number of elements being sorted can be controlled by 2 JSliders.
 * These properties can also be controlled outside of the JInternal frame (ie. in containing JFrame) by using various public functions.
 */

package code;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class SelectionSort extends JInternalFrame implements Runnable, ChangeListener, ActionListener
{
	/**
	 * The data being sorted.
	 */
	private float[] numsToSort;
	/**
	 * The size of the array being sorted. Default value is set to default static value in Control class.
	 */
	private int arraySize = Control.DEFUALT_NUM_OF_ELEMENTS;
	
	/**
	 * Manages sorting thread.
	 */
	ExecutorService executor;
	/**
	 * Used to ensure mutual exclusion of some resources that could potentially have ill-effects on execution due to race-condition.
	 */
	private Lock mutex;
	/**
	 * How long (in milliseconds) the thread should sleep. The smaller the value, the quicker the sort will complete, and vice-versa.
	 * Default value is set to default static value in Control class.
	 */
	private int sleepTime = Control.DEFAULT_SPEED;
	
	/**
	 * Random number variable. Used to generate indexes to randomize the numsToSort array.
	 */
	private final Random rand;	
	/**
	 * Used to pause the sort. When the pause button is pressed, isPaused is set to true, and the thread waits until it is signaled (by the resume button).
	 */
	private final Condition condition;
	/**
	 * When sort is still going, doneSorting = false. When sort is complete, doneSorting = true.
	 */
	private int currentIndex;
	/**
	 * When sort is still going, doneSorting = false. When sort is complete, doneSorting = true.
	 */
	private boolean doneSorting;
	/**
	 * When sort is paused, isPaused = true. When pause is resumed, isPaused = false;
	 */
	private boolean isPaused;
	/**
	 * When Start button is pressed, isRunning = true. When Stop button is pressed, isRunning = false.
	 */
	private boolean isRunning;
	
	/**
	 * JPanel, located at bottom of the JInternalFrame, contains start/stop and pause/resume buttons, speed, and numElements sliders.
	 */
	private JPanel container;
	/**
	 * Button that alternates between "Start" and "Stop". Executes said functions.
	 */
	private JButton startStop;
	/**
	 * Button that alternates between "Pause" and "Resume". Executes said functions.
	 */
	private JButton pauseResume;
	/**
	 * Slider that sets the execution speed (sleep time) of thread.
	 */
	private JSlider speed;
	/**
	 * Slider that sets the size (arraySize) of the array being sorted (numsToSort).
	 */
	private JSlider numElements;
	
	public SelectionSort()
	{
		super("Selection Sort", true, true, true, true);
		setBounds(0, 0, 600, 400);
		setMinimumSize(new Dimension(450, 200));
	
		mutex = new ReentrantLock();
		condition = mutex.newCondition();
		/**
		 * The thread pool only contains 1 thread, the one doing to sorting.
		 */
		executor = Executors.newFixedThreadPool(1);
		
		rand = new Random();
		numsToSort = generateRandomArray(arraySize);
		/**
		 * Before the sort has started, we don't want any of the elements colored red
		 */
		currentIndex = -1;
		
		/**
		 * Create the JButtons, Sliders, add them to the container, and add the container to the JInternalFrame.
		 */
		startStop = new JButton("Stop");
		startStop.addActionListener(this);
		pauseResume = new JButton("Pause");
		pauseResume.addActionListener(this);
		speed = new JSlider(Control.MIN_SPEED, Control.MAX_SPEED, Control.DEFAULT_SPEED);
		speed.setBorder(new TitledBorder("Speed"));
		speed.addChangeListener(this);
		numElements = new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS, Control.DEFUALT_NUM_OF_ELEMENTS);
		numElements.setBorder(new TitledBorder("Number of Elements"));
		numElements.addChangeListener(this);
		container = new JPanel();
		container.add(startStop);
		container.add(pauseResume);
		container.add(speed);
		container.add(numElements);
		add(container, BorderLayout.SOUTH);
	}
	
	/**
	 * Called when "Start" button is pressed, or externally from Driver.class.
	 * Caused the thread to begin execution.
	 */
	public void start()
	{
		executor.execute(this);
	}
	
	/**
	 * The function called when execute() is called.
	 * Calls the sort() function, begins sorting data.
	 */
	public void run()
	{
		sort();
	}
	
	/**
	 * Heap Sort implementation.
	 * Creates a heap, places the top of the heap (the largest element) at the end of numsToSort, repeat.
	 */
	private void sort()
	{
		/**
		 * Sort should currently be unpaused, running, and not complete.
		 */
		isPaused = false;
		isRunning = true;
		doneSorting = false;
		
		startStop.setText("Stop");
		pauseResume.setText("Pause");
		
		/**
		 * For each iteration of the loop, the smallest element is swapped to the left most unsorted index of the array.
		 */
		int min;
		for (int i = 0; i < numsToSort.length - 1 && isRunning == true && doneSorting == false; i++)
		{
			min = i;
			for (currentIndex = i+1; currentIndex < numsToSort.length && isRunning == true && doneSorting == false; currentIndex++)
			{
				if (numsToSort[currentIndex] < numsToSort[min])
				{
					min = currentIndex;
				}	
				
				mutex.lock();
				repaint();
				/**
				 * Put the thread to sleep.
				 */
				try
				{
					Thread.sleep(sleepTime);
				} catch (InterruptedException e){}	
				
				/**
				 * If the Pause button was pressed, the thread waits here until signalled to continue.
				 */
				if(isPaused == true && doneSorting == false)
				{
					try
					{
						condition.await();
					} catch (InterruptedException e){}	
				}
				mutex.unlock();
			}
			if (min != i && isRunning == true)
			{
				swap(i, min);
			}
		}
		if (isRunning == true)
		{
			/**
			 * When we're done sorting, we don't want any elements colored red.
			 */
			doneSorting = true;
			repaint();
		}
	}
	
	/**
	 * Swaps 2 elements in numsToSort, and sleeps.
	 * @param i Index of the element in numsToSort to be swapped with element j
	 * @param j Index of the element in numsToSort to be swapped with element i
	 */
	private void swap(final int i, final int j)
	{
		float temp = numsToSort[i];
		numsToSort[i] = numsToSort[j];
		numsToSort[j] = temp;		
	}
	
	/**
	 * Sets the numsToSort array. The data being sorted.
	 * @param size The size of the new array to sort.
	 */
	public void setNumberOfElements(final int size)
	{
		/**
		 * Stop sorting the array.
		 */
		stop();
		
		numElements.setValue(size);
		numsToSort = generateRandomArray(size);
		
		startStop.setText("Start");
		pauseResume.setText("Pause");
		
		doneSorting = false;
		currentIndex = -1;
	}
	
	/**
	 * Generates an ordered array, then randomizes it. This way, the sorted data is of the set {0, 1, 2.... arraySize - 1}
	 * @param size the size of the random array to generate.
	 * @return The randomly generated array.
	 */
	private float[] generateRandomArray(final int size)
	{
		float[] array = new float[size];	
		int rand1, rand2;
		float temp;

		/**
		 * Produce the ordered array.
		 */
		for(int i = 0; i < array.length; i++)
		{
			array[i] = (i+1) * (1.0f / (float)size);
		}

		/**
		 * Randomize the ordered array.
		 */
		for(int i = 0; i < array.length*2; i++)
		{
			rand1 = rand.nextInt(array.length);
			rand2 = rand.nextInt(array.length);
			
			temp = array[rand1];
			array[rand1] = array[rand2];
			array[rand2] = temp;		
		}
		
		return array;
	}
	
	/**
	 * Rounds a number to the next whole number integer.
	 * @param input Floating point number to round.
	 * @return the next whole number integer.
	 */
	private int round(float input)
	{
		return (int)(input + 0.5f);
	}
	
	/**
	 * Draw the array. Elements are blue, the current element being worked on is red.
	 * Once the array is sorted, every element is green.
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		
		int width = getWidth() - getInsets().left - getInsets().right;
		int height = getHeight() - getInsets().top - getInsets().bottom - container.getHeight(); 
		for (int i = 0; i < numsToSort.length; i++)
		{
			if (doneSorting == false)
			{
				g.setColor(Color.BLUE);
				if (i == currentIndex)
				{
					g.setColor(Color.RED);
				}
			}
			else
			{
				g.setColor(Color.GREEN);
			}
			g.fillRect(getInsets().left + round(i * (width / (float)numsToSort.length)), getInsets().top + round(height - (height * numsToSort[i])), round(width / (float)numsToSort.length), round(height * numsToSort[i]));
		}
	}

	/**
	 * Stops the execution of the sorting thread.
	 * Once isRunning is set to false, the sort leaves the loop.
	 */
	public void stop()
	{
		isRunning = false;
		isPaused = false;
		startStop.setText("Start");
	}
	
	/**
	 * Sets isPuased = true. In the sort, when isPaused = true, await() is executed.
	 * Now, the thread waits until it is signaled to continue.
	 */
	public void pause()
	{
		if (isRunning == true)
		{
			isPaused = true;
			pauseResume.setText("Unpause");
		}
	}
	
	/**
	 * Changes the sort from a "Paused" state, to an actively sorting ("Resumed") state.
	 */
	public void resume()
	{
		if (isRunning == true)
		{
			mutex.lock();
			isPaused = false;
			condition.signal();
			pauseResume.setText("Pause");
			mutex.unlock();
		}
	}
	
	/**
	 * Sets sleepTime, and changes the value of the sleep slider
	 * @param delay The new sleep value
	 */
	public void setDelay(final int delay)
	{
		sleepTime =  Control.MAX_SPEED - delay;
		speed.setValue(delay);
	}
	
	/**
	 * Returns whether or not the sort is currently running.
	 * @return Whether or not the sort is currently running.
	 */
	public boolean isRunning()
	{
		return isRunning;
	}
	
	/**
	 * Returns whether or not the sort is currently paused.
	 * @return Whether or not the sort is currently paused.
	 */
	public boolean isPaused()
	{
		return isPaused;
	}
	
	/**
	 * Event driven programming.
	 */
	public void stateChanged(ChangeEvent e)
	{
		/**
		 * The value of the speed slider is translated into the sleepTime of the sort thread.
		 */
		if (e.getSource() == speed)
		{
			sleepTime = Control.MAX_SPEED - speed.getValue();
		}
		/**
		 * The value of the numsElements slider is translated into the size of the array being sorted.
		 */
		else if (e.getSource() == numElements)
		{
			/**
			 * Set numsToSort to a new random array of the size returned by the slider.
			 */
			arraySize = numElements.getValue();
			setNumberOfElements(arraySize);
		}
		repaint();
	}

	/**
	 * Event driven programming.
	 */
	public void actionPerformed(ActionEvent e)
	{
		/**
		 * When the startStop button is pressed, the thread is either stopped, or restarted.
		 */
		if (e.getSource() == startStop)
		{
			if (isRunning == true)
			{
				startStop.setText("Start");
				pauseResume.setEnabled(false);
				stop();
				
			}
			else
			{
				startStop.setText("Stop");
				pauseResume.setEnabled(true);
				start();
			}
		}
		/**
		 * When the pauseResume button is pressed, the thread is either paused, or resumed.
		 */
		else if (e.getSource() == pauseResume)
		{
			if (isRunning == true)
			{
				if (isPaused == true)
				{
					resume();
				}
				else
				{
					pause();
				}
			}
		}
	}
}
