/**
 * @author Holly Busken
 * @version 1.0
 * Quick sort implementation.
 * Executes the sorting algorithm on a list of numbers.
*/
package code;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.*;
import java.util.Random;

/**
The QuickThread class runs a quick sort implementation in it's own thread.
*/
public class QuickThread implements Runnable
{
  /** resumed is used to indicate if the quick sort should be running. */
  private boolean resumed = false;
  /** data is the elements to sort. */
  private int[] data;
  /** size is the number of elements to sort. */
  int size;
  /** delay is the number of milliseconds to wait between steps. */
  int delay;
  /** pause is a flag to determine if execution should be paused. */
  boolean pause;
  /** executor is the thread service for the quick sort. */
  private final ExecutorService executor;
  /** startButton is the button to use for starting the quick sort. */
  JButton startButton;
  /** stop is the button to use for stopping the quick sort. */
  JButton stop;
  /** pauseButton is the button to use for pausing the quick sort execution. */
  JButton pauseButton;
  /** lock is used for locking thread when paused. */
  public Lock lock=new ReentrantLock();
  /** condition is used for indicating when the thread should be unpaused or paused. */
  public Condition condition=lock.newCondition();
  /** start is a flag for indicating when the quick sort should start in the thread's execution. */
  boolean start=false;
  /** highlight is the index of the value to highlight when displayed. */
  int highlight=0;
  /** done is a flag for indicating when the quick sort has completed sorting the elements. */
  boolean done=false;
  
  /**
  The constructor creates the thread for the quick sort and starts it.
  */
  	public QuickThread()
  	{
		executor = Executors.newFixedThreadPool(1);
		resumed=true;
		pause=false;
		executor.execute(this);
		int[] data=null;
  	}
	
	/**
	Gets the index of the value to be highlighted when displayed.
	@return The index of the array to be highlighted when displayed.
	*/
	public int getHighlight()
	{
	  return highlight;
	}
	
	/**
	Determines if the quick sort is finished running.
	@return true if the quick sort has finished sorting, false otherwise.
	*/	
	public boolean isFinished()
	{
	  return done;
	}
	
	/**
	Signals the thread that the quick sort should stop.
	*/	
	public void stop()
	{
	  resumed=false;
	}
	
	/**
	Signals to the thread that the quick sort should be paused.
	*/	
	public void pause()
	{
      pause=true;
	}
	
	/**
	Signals the thread that it should resume execution.
	*/	
	public void unpause()
	{
	  pause=false;
	  //System.out.println("unpause");
	  lock.lock();
      condition.signal();
	  lock.unlock();
	}

	/**
	Sets the delay time to a specific value.
	@param time is the new delay time for execution.
	*/
	public void setDelay(int time)
	{
	  delay=time;
	}	

	/**
	Starts the execution of the quick sort algorithm with the specified
	number of elements, length, with the specified delay time, d.
	@param length is the number of elements to sort.
	@param d is the delay time to use for each step of the sort.
	*/	
  	public void start(int length, int d)
	{	        
	    size=length;
		data = new int[size];
		Random num= new Random();
		for(int i=0;i<size;i++)
		{
		  data[i]=num.nextInt(50);
		}
		resumed = true;
	    start=true;	
        pause=false;
        delay=d;	
	    done=false;
	}
	
  /**
  This method is what runs when the thread begins.
  After the quick sort is started, this method will start the quick sort.
  When the quick sort is stopped, this method shutdowns the thread.
  */ 	
  public void run()
  {
   while(resumed==true)
   {
    // System.out.println("here");
     if(start==true)
	 {
       qSort(0,data.length-1);	  
	   //System.out.println("STARTED");
	   start=false;
	 }
	 try {Thread.sleep(10);}
      catch (InterruptedException ex){}
	 
   }
   executor.shutdownNow();
  }

  /**
  Gets the data being sorted.
  @return an array of integers that is being sorted.
  */  
  public int[] getData()
  {
	return data;
  }

  /**
  Sorts the data in the partition created by the left and right index positions.
  @param left is the index of the leftmost element in the partition.
  @param right is the index of the rightmost element in the partition.
  @return the index to be used for the next partion.
  */
  int partition(int left, int right)
  {
    if(pause==true)
	{
	  lock.lock();  
	  //System.out.println("pause");
	  try
	  {
  	  condition.await();
	   }
	   catch(InterruptedException ex) {}
	   lock.unlock();
	}  
	int storeIndex=0;
    if(resumed==true)
	{
    int pivotIndex=left;
	int pivotValue=data[pivotIndex];
	data[pivotIndex]=data[right];
	data[right]=pivotValue;
	storeIndex=left;
	for(int i=left;i<=right-1;i++)
	{
	  if(data[i] < pivotValue)
	  {
	    int holdValue=data[storeIndex];
		data[storeIndex]=data[i];
		data[i]=holdValue;
		highlight=i;
		storeIndex=storeIndex+1;
	  }
	}
	int holdValue=data[storeIndex];
	highlight=storeIndex;
	data[storeIndex]=data[right];
	data[right]=holdValue;
	highlight=right;
	}
     try 
	  {Thread.sleep(delay);}
      catch (InterruptedException ex){}
    return storeIndex;
  }
  
  /**
  Using recursion and the partition method, it partitions the list of
  numbers into many partitions then sorts them until it is completely sorted.
  @param i is the index to start sorting.
  @param k is the index to end sorting.
  */
  void qSort(int i, int k)
  {
    if(resumed==true)
	{
	if(i<k)
	{
	  int p=partition(i,k);
	  qSort(i,p-1);
	  qSort(p+1,k);
	}
	if(i==0 && k==(data.length-1))
	  done=true;
	}
  }

}
