package code;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;


public class SelectionSort extends JInternalFrame implements Runnable, ChangeListener
{
	private int[] numsToSort;
	private int arraySize = 50;
	
	Executor executor;
	private Lock mutex;
	private int sleepTime = 50;
	
	private final Random rand;	
	private int currentIndex;
	
	private JPanel sliderContainer;
	private JSlider speed;
	private int maxSpeed = 100, minSpeed = 1;
	private JSlider numElements;
	private int maxNumElements = 100, minNumElements = 20;
	
	public SelectionSort()
	{
		super("Selection Sort", true, true, true, true);
		setBounds(0, 0, 450, 400);
		setMinimumSize(new Dimension(450, 200));
	
		mutex = new ReentrantLock();
		executor = Executors.newFixedThreadPool(1);
		
		rand = new Random();
		numsToSort = generateRandomArray(arraySize);
		currentIndex = -1; //Before the sort has started, we don't want any of the elements colored red
		
		speed = new JSlider(minSpeed, maxSpeed, sleepTime);
		speed.setBorder(new TitledBorder("Speed"));
		speed.addChangeListener(this);
		numElements = new JSlider(minNumElements, maxNumElements, arraySize);
		numElements.setBorder(new TitledBorder("Number of Elements"));
		numElements.addChangeListener(this);
		sliderContainer = new JPanel();
		sliderContainer.add(speed);
		sliderContainer.add(numElements);
		add(sliderContainer, BorderLayout.SOUTH);
	}
	
	public void start()
	{
		executor.execute(this);
	}
	
	public void run()
	{
		sort();
	}
	
	private void sort()
	{
		int min;
		for (int i = 0; i < numsToSort.length - 1; i++)
		{
			mutex.lock();
			min = i;
			for (currentIndex = i+1; currentIndex < numsToSort.length; currentIndex++)
			{
				if (numsToSort[currentIndex] < numsToSort[min])
				{
					min = currentIndex;
				}	
				
				repaint();
				try
				{
					Thread.sleep(sleepTime);
				} catch (InterruptedException e){}	
				
			}
			if (min != i)
			{
				swap(i, min);
			}
			mutex.unlock();
		}
	}
	
	private void swap(final int i, final int j)
	{
		int temp = numsToSort[i];
		numsToSort[i] = numsToSort[j];
		numsToSort[j] = temp;		
	}
	
	private int[] generateRandomArray(final int size)
	{
		int[] array = new int[size];	
		for (int i = 0; i < arraySize; i++)
		{
			array[i] = rand.nextInt(arraySize) + 1;
		}
		return array;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		int width, height;
		for (int i = 0; i < numsToSort.length - 1; i++)
		{
			width = getWidth()/(numsToSort.length - 1);
			height = getHeight() - getInsets().top - sliderContainer.getHeight(); 
			
			g.setColor(Color.BLUE);
			if (i == currentIndex)
			{
				g.setColor(Color.RED);
			}
			g.fillRect(getInsets().left + width*i, height - numsToSort[i]*(height/numsToSort.length), width, numsToSort[i]*(height/numsToSort.length));
		}
	}

	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == speed)
		{
			sleepTime = maxSpeed - speed.getValue();
		}
		else if (e.getSource() == numElements)
		{
			//???
		}
		repaint();
	}
}