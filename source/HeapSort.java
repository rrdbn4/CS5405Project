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
	private float[] numsToSort;
	private int arraySize = Control.DEFUALT_NUM_OF_ELEMENTS;
	private int heapSize;
	
	Executor executor;
	private Lock mutex;
	private int sleepTime = Control.DEFAULT_SPEED;
	
	private final Random rand;
	private final Condition condition;
	private int currentIndex;
	private boolean doneSorting, isPaused, isRunning;
	
	private JPanel container;
	private JButton startStop, pauseResume;
	private JSlider speed, numElements;
	
	public HeapSort()
	{
		super("Heap Sort", true, true, true, true);
		setBounds(400, 400, 450, 400);
		setMinimumSize(new Dimension(450, 200));

		mutex = new ReentrantLock();
		condition = mutex.newCondition();
		executor = Executors.newFixedThreadPool(1);
		
		rand = new Random();
		numsToSort = generateRandomArray(arraySize);
		currentIndex = -1; //Before the sort has started, we don't want any of the elements colored red
		
		startStop = new JButton("Start");
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
	
	public void start()
	{
		executor.execute(this);
	}
	
	public void run()
	{
		sort();
	}
	
	public void sort()
	{
		isPaused = false;
		isRunning = true;
		doneSorting = false;
		buildMaxHeap();
		for (int i = numsToSort.length - 1; i >= 1; i--)
		{
			swap(0, i);
			heapSize--;
			maxHeapify(0);
		}
		//when we're done sorting, we don't want any elements colored red
		currentIndex = -1; 
		doneSorting = true;
		repaint();
	}
	
	private void buildMaxHeap()
	{
		heapSize = numsToSort.length -1;
		for (int i = (numsToSort.length)/2; i >= 0; i--)
		{
			maxHeapify(i);
		}
	}
	
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
		
		if (largest != i)
		{
			swap(i, largest);
			maxHeapify(largest);
		}
	}
	
	private void swap(final int i, final int j)
	{
		float temp = numsToSort[i];
		numsToSort[i] = numsToSort[j];
		numsToSort[j] = temp;
		
		currentIndex = j;
		
		repaint();
		try
		{
			Thread.sleep(sleepTime);
		} catch (InterruptedException e){}	
		
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
	
	private float[] generateRandomArray(final int size)
	{
		float[] array = new float[size];	
		int rand1, rand2;
		float temp;

		for(int i = 0; i < array.length; i++)
		{
			array[i] = (i+1) * (1.0f / (float)size);
		}

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
	
	private int round(float input)
	{
		return (int)(input + 0.5f);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		int width = getWidth() - getInsets().left - getInsets().right;
		int height = getHeight() - getInsets().top - getInsets().bottom - container.getHeight(); 
		for (int i = 0; i < numsToSort.length - 1; i++)
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
	
	public void stop()
	{
		
	}
	
	public void pause()
	{
		if (isRunning == true)
		{
			isPaused = true;
			pauseResume.setText("Unpause");
		}
	}
	
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
	
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == speed)
		{
			sleepTime = Control.MAX_SPEED - speed.getValue();
		}
		else if (e.getSource() == numElements)
		{
			//???
		}
		repaint();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == startStop)
		{
			
		}
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