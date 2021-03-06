/**
 * @author Holly Busken
 * @version 1.0
 * Merge sort implementation.
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
The MergeThread class runs a merge sort implementation in it's own thread.
*/
public class MergeThread implements Runnable
{

  /** resumed is used to indicate if the merge sort should be running. */
  private boolean resumed = false;
  /** data is the elements to sort. */
  private float[] data;
  /** size is the number of elements to sort. */
  int size;
  /** pause is a flag to determine if execution should be paused. */
  boolean pause;
  /** delay is the number of milliseconds to wait between steps. */
  int delay;
  /** highlight is the index of the value to highlight when displayed. */
  int highlight=-1;
  /** executor is the thread service for the merge sort. */
  private final ExecutorService executor;
  /** startButton is the button to use for starting the merge sort. */
  JButton startButton;
  /** stop is the button to use for stopping the merge sort. */
  JButton stop;
  /** pauseButton is the button to use for pausing the merge sort execution. */
  JButton pauseButton;
  /** lock is used for locking thread when paused. */
  public Lock lock=new ReentrantLock();
  /** condition is used for indicating when the thread should be unpaused or paused. */
  public Condition condition=lock.newCondition();
  /** start is a flag for indicating when the merge sort should start in the thread's execution. */
  boolean start=false;
  /** done is a flag for indicating when the merge sort has completed sorting the elements. */
  boolean done=false;
  
  /**
  The constructor creates the thread for the merge sort and starts it.
  */
  	public MergeThread()
  	{
		executor = Executors.newFixedThreadPool(1);
		resumed=true;
		pause=false;
		executor.execute(this);
		data = null;
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
	Determines if the merge sort is finished running.
	@return true if the merge sort has finished sorting, false otherwise.
	*/
	public boolean isFinished()
	{
	  return done;
	}
	
	/**
	Signals the thread that the merge sort should stop.
	*/
	public void stop()
	{
	  resumed=false;
	}
	
	/**
	Signals to the thread that the merge sort should be paused.
	*/
	public void pause()
	{
      pause=true;
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
	Signals the thread that it should resume execution.
	*/
	public void unpause()
	{
	  pause=false;
	  lock.lock();
      condition.signal();
	  lock.unlock();
	}

	/**
	Starts the execution of the merge sort algorithm with the specified
	number of elements, length, with the specified delay time, d.
	@param length is the number of elements to sort.
	@param d is the delay time to use for each step of the sort.
	*/
  	public void start(int length, int d)
	{	        
	    size=length;
		delay=d;
		data = new float[size];
		Random num= new Random();
		for(int i=0;i<size;i++)
		{
		  data[i]=num.nextInt(size-5) * (1.0f / (float)size);
		}
		resumed = true;
	    start=true;	
        pause=false;	
		done=false;
	}
	
  /**
  This method is what runs when the thread begins.
  After the merge sort is started, this method will start the merge sort.
  When the merge sort is stopped, this method shutdowns the thread.
  */  
  public void run()
  {
   while(resumed==true)
   {
     if(start==true)
	 {
       mSort(0,data.length-1);	  
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
  public float[] getData()
  {
	return data;
  }

  /**
  Merges the numbers of two sub groups and sorts them.
  @param p is the start index of the sub group.
  @param q is the midpoint index of the sub group.
  @param r is the end index of the sub group.
  */
  void merge(int p, int q, int r)
  {
    if(pause==true)
	{
	  lock.lock();  
	  try
	  {
  	  condition.await();
	   }
	   catch(InterruptedException ex) {}
	   lock.unlock();
	}  
  
    if(resumed==true)
	{
    int left=q-p+1;
	int right=r-q;
	float L[]= new float[left+2];
	float R[]=new float[right+2];
	for(int i=1;i<=left; i++)
	{
	  L[i]=data[p+i-1];
	}
	R[0]=data[q];
	for(int j=1;j<=right;j++)
	{
	  R[j]=data[q+j];
	}
	L[left+1]=Integer.MAX_VALUE;
	R[right+1]=Integer.MAX_VALUE;
	int a=1;
	int b=1;
	for(int k=p;k<=r;k++)
	{
	  if(L[a]<=R[b])
	  {
	    data[k]=L[a];
		highlight=k;
		a++;
	  }
	  else
	  {
	    data[k]=R[b];
		b++;
	  }
	}
	}
     try 
	  {Thread.sleep(delay);}
      catch (InterruptedException ex){}

  }
  
  /**
  Breaks the list of numbers into halves then merges them together.
  @param p is the starting index of the numbers to sort.
  @param r si the end index of the numbers to sort.
  */
  void mSort(int p, int r)
  {
    if(resumed==true)
	{
      int q; //midpoint
  	  if(r>p)
      {
	    q=(r+p)/2;
	    mSort(p,q);
	    mSort(q+1,r);
	    merge(p,q,r);
	  }
	  if(p==0 && r==(data.length-1))
	    done=true;
	}
  }

}
