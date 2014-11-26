/**
 * @author Matthew Lindner
 * @version 1.0
 * Heap Sort implementation.
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

public class HeapSort extends JInternalFrame implements Runnable, ChangeListener, ActionListener
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
	 * The current size of the heap, which is the remaining data needing to be sorted.
	 */
	private int heapSize;
	
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
	 * The current index of numsToSort being operated on. This element will be colored red in the paint() function.
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
	
	public HeapSort()
	{
		super("Heap Sort", true, true, true, true);
		setBounds(400, 400, 500, 400);
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
	public void sort()
	{
		/**
		 * Sort should currently be unpaused, running, and not complete.
		 */
		isPaused = false;
		isRunning = true;
		doneSorting = false;
		
		/**
		 * First converts numsToSort into a heap.
		 */
		buildMaxHeap();
		/**
		 * For each element of the array, recreate the heap.
		 */
		for (int i = numsToSort.length - 1; i >= 1 && isRunning == true; i--)
		{
			swap(0, i);
			heapSize--;
			maxHeapify(0);
		}
		/**
		 * If the sort has not been stopped (by the stop button, or by the numElements slider...
		 * indicate that the sort is done (doneSorting = true), and redraw the array.
		 */
		if (isRunning == true)
		{
			/**
			 * When we're done sorting, we don't want any elements colored red.
			 */
			currentIndex = -1; 
			doneSorting = true;
			repaint();
		}
	}
	
	/**
	 * For the unsorted data (size of unsorted data = heapSize), recreate the max-heap.
	 */
	private void buildMaxHeap()
	{
		heapSize = numsToSort.length -1;
		for (int i = (numsToSort.length)/2; i >= 0 && isRunning == true; i--)
		{
			maxHeapify(i);
		}
	}
	
	/**
	 * A recursive function. For set of elements in numsToSort (parent, 2 children), maxHeapify ensures that the parent is larger than both of its children.
	 * @param i The current element being "heapified". For any "node" 'i' in the heap, its children are in index 2i and 2i+1. 
	 */
	private void maxHeapify(final int i)
	{
		int largest;
		int left = 2*i;
		int right = 2*i + 1;
		
		if ((left <= heapSize) && (numsToSort[left] > numsToSort[i]))
		{
			largest = left;
		}
		else
		{
			largest = i;
		}
		if ((right <= heapSize) && (numsToSort[right] > numsToSort[largest]))
		{
			largest = right;
		}
		
		/**
		 * Recursively calls itself until the parent node is larger than both of its children.
		 */
		if (largest != i)
		{
			swap(i, largest);
			maxHeapify(largest);
		}
	}
	
	/**
	 * Swaps 2 elements in numsToSort, and sleeps.
	 * @param i Index of the element in numsToSort to be swapped with element j
	 * @param j Index of the element in numsToSort to be swapped with element i
	 */
	private void swap(final int i, final int j)
	{
		/**
		 * Swap the elements
		 */
		float temp = numsToSort[i];
		numsToSort[i] = numsToSort[j];
		numsToSort[j] = temp;
		
		currentIndex = j;
		
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
		if(isPaused == true)
		{
			mutex.lock();
			try
			{
				condition.await();
			} catch (InterruptedException e){}
			mutex.unlock();
		}
	}
	
	/**
	 * Sets the numsToSort array. The data being sorted.
	 * @param array The new array to sort.
	 */
	public void setArray(final float[] array)
	{
		numsToSort = array;
		arraySize = array.length;
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
		
		/**
		 * The width and height of each element. Takes into account the size of the container, and JInternalFrame insets.
		 */
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
		/**
		 * We only pause the sort if it is currently running.
		 */
		if (isRunning == true)
		{
			mutex.lock();
			isPaused = false;
			/**
			 * Signal the sort to "wake up". Resumes execution from await().
			 */
			condition.signal();
			pauseResume.setText("Pause");
			mutex.unlock();
		}
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
			 * Stop sorting the array.
			 */
			stop();
			
			/**
			 * Set numsToSort to a new random array of the size returned by the slider.
			 */
			arraySize = numElements.getValue();
			setArray(generateRandomArray(arraySize));
			
			startStop.setText("Start");
			pauseResume.setText("Pause");
			
			doneSorting = false;
			currentIndex = -1;
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
				stop();
				startStop.setText("Start");
				pauseResume.setEnabled(false);
			}
			else
			{
				start();
				startStop.setText("Stop");
				pauseResume.setEnabled(true);
			}
		}
		/**
		 * When the pauseResume button is pressed, the thread is either paused, or resumed.
		 */
		else if (e.getSource() == pauseResume)
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