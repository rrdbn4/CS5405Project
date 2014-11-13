//Holly Busken
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

public class QuickThread implements Runnable
{

  private boolean resumed = false;
  private Thread sort = null;
  private int[] data;
  private boolean listChange=false;
  int size;
  boolean pause;
  private final ExecutorService executor;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  public Lock lock=new ReentrantLock();
  public Condition condition=lock.newCondition();
  boolean start=false;
  

  	public QuickThread()
  	{
		executor = Executors.newFixedThreadPool(1);
		resumed=true;
		pause=false;
		executor.execute(this);
		int[] data=null;
  	}
	
	public void stop()
	{
	  resumed=false;
	}
	
	public void pause()
	{
      pause=true;
	}
	
	public void unpause()
	{
	  pause=false;
	  //System.out.println("unpause");
	  lock.lock();
      condition.signal();
	  lock.unlock();
	}

  	public void start(int length)
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
	}
	
	
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

  public int[] getData()
  {
    //System.out.println("output()");
    /*string="";
	for(int i=0;i<size;i++)
	  string+=data[i]+" ";
	return string;*/
	return data;

  }

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
		storeIndex=storeIndex+1;
	  }
	}
	int holdValue=data[storeIndex];
	data[storeIndex]=data[right];
	data[right]=holdValue;
	}
     try 
	  {Thread.sleep(1000);}
      catch (InterruptedException ex){}
    return storeIndex;
  }
  
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
	}
  }

}
