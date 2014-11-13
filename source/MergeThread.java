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
  	}
	
	public void stop()
	{
	  resumed=false;
	  executor.shutdownNow();
	}
	
	public void pause()
	{
	
	  //need to change to lock
	  while(pause==true)
	  {
	 try {Thread.sleep(10);}
      catch (InterruptedException ex){}
	  }	  
	}
	
	public void unpause()
	{
      pause=false;
	}

  	public void start()
	{	        
	    size=10;
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

  public String output()
  {
    //System.out.println("output()");
    string="";
	for(int i=0;i<size;i++)
	  string+=data[i]+" ";
	return string;

  }

  void merge(int p, int q, int r)
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
     try 
	  {Thread.sleep(1000);}
      catch (InterruptedException ex){}

  }
  
  void mSort(int p, int r)
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