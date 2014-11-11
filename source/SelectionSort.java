import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import javax.swing.*;

public class SelectionSort extends JInternalFrame implements Runnable
{
	private int[] numsToSort;
	private final int arraySize = 35;
	
	Executor executor;
	private Lock mutex;
	private int sleepTime = 15;
	
	private final Random rand;	
	private int currentIndex;
	
	public SelectionSort()
	{
		super("Selection Sort", true, true, true, true);
		setBounds(0, 0, 400, 400);
			
		rand = new Random();
		numsToSort = generateRandomArray(arraySize);
		
		mutex = new ReentrantLock();
		executor = Executors.newFixedThreadPool(1);
		
		currentIndex = -1; //Before the sort has started, we don't want any of the elements colored red
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
		
		for (int i = 0; i < numsToSort.length - 1; i++)
		{
			g.setColor(Color.BLUE);
			if (i == currentIndex)
			{
				g.setColor(Color.RED);
			}
			g.fillRect(5 + 5*i, 395 - numsToSort[i]*10 , 5, numsToSort[i]*10);
		}
	}
}
