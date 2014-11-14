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

public class MergeThread implements Runnable
{

  private boolean resumed = false;
  private Thread sort = null;
  private int[] data;
  private boolean listChange=false;
  int size;
  boolean pause;
  int delay;
  private final ExecutorService executor;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  public Lock lock=new ReentrantLock();
  public Condition condition=lock.newCondition();
  boolean start=false;
  

  	public MergeThread()
  	{
		executor = Executors.newFixedThreadPool(1);
		resumed=true;
		pause=false;
		executor.execute(this);
		data = null;
		delay=1000;
  	}
	
	public void stop()
	{
	  resumed=false;
	}
	
	public void pause()
	{
      pause=true;
	}
	
	public void setDelay(int time)
	{
	  delay=time;
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
       mSort(0,data.length-1);	  
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
	/*
    string="";
	for(int i=0;i<size;i++)
	  string+=data[i]+" ";
	return string;*/
	return data;

  }

  void merge(int p, int q, int r)
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
  
  
    if(resumed==true)
	{
    int left=q-p+1;
	int right=r-q;
	int L[]= new int[left+2];
	int R[]=new int[right+2];
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
	}
  }

}
