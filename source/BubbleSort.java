package code;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import javax.swing.*;

public class BubbleSort extends JInternalFrame implements Runnable
{
	Executor executor;

	float[] randArray;
	int numElements;

	public BubbleSort()
	{
		super("Bubble Sort", true, true, true, true);
		numElements = 10;
		randArray = new float[numElements];
		for(int i = 0; i < numElements; i++)
			randArray[i] = (i+1) * (1.0f / (float)numElements);

		//randomize elements in the array so we have something to sort
		Random rand = new Random();
		for(int i = 0; i < numElements*2; i++)
			swap(rand.nextInt(randArray.length), rand.nextInt(randArray.length));

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

	public void run()
	{

	}

	public void start()
	{
		executor.execute(this);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawOval(0,0,10,10);
	}
}
