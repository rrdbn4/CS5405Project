import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import javax.swing.*;

public class HeapSort extends JInternalFrame implements Runnable
{
	private int[] numsToSort;
	private final int arraySize = 35;
	private int heapSize;
	
	Executor executor;
	private Lock mutex;
	private int sleepTime = 40;
	
	private final Random rand;
	private int currentIndex;
	
	public HeapSort()
	{
		super("Heap Sort", true, true, true, true);
		setBounds(400, 400, 400, 400);
		
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
	
	public void sort()
	{
		buildMaxHeap();
		for (int i = numsToSort.length - 1; i >= 1; i--)
		{
			swap(0, i);
			heapSize--;
			maxHeapify(0);
		}
		//when we're done sorting, we don't want any elements colored red
		currentIndex = -1; 
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
		int temp = numsToSort[i];
		numsToSort[i] = numsToSort[j];
		numsToSort[j] = temp;
		
		currentIndex = j;
		
		repaint();
		try
		{
			Thread.sleep(sleepTime);
		} catch (InterruptedException e){}	
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