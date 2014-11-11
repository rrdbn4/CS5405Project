package code;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import javax.swing.*;

public class ShellSort extends JInternalFrame implements Runnable
{
	Executor executor;

	float[] randArray;
	int numElements = 30;
	int sleepTime = 200;
	int highlightIndex = 0;

	public ShellSort()
	{
		super("Shell Sort", true, true, true, true);
		setBounds(0, 0, 500, 400);

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

	public int round(float input)
	{
		return (int)(input + 0.5f);
	}

	public void run()
	{
		int increment = randArray.length / 2;
		while (increment > 0) 
		{
			for (int i = increment; i < randArray.length; i++) 
			{
				int j = i;
				float temp = randArray[i];
				while (j >= increment && randArray[j - increment] > temp) 
				{
					randArray[j] = randArray[j - increment];
					j = j - increment;
					highlightIndex = j;
					try
					{
						Thread.sleep(sleepTime);
					} catch(InterruptedException e) {}
					repaint();
				}
				randArray[j] = temp;
			}
			if (increment == 2) {
				increment = 1;
			} else {
				increment *= (5.0 / 11);
			}
		}
	}

	public void start()
	{
		executor.execute(this);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		float width = getWidth() - getInsets().left - getInsets().right;
		float height = getHeight() - getInsets().top - getInsets().bottom;

		for(int i = 0; i < randArray.length; i++)
		{
			if(i == highlightIndex)
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLUE);
			g.fillRect(getInsets().left + round(i * (width / (float)numElements)), getInsets().top + round(height - (height * randArray[i])), round(width / (float)numElements), round(height * randArray[i]));
		}
	}
}

