package code;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.event.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.*;
import java.util.Random;

public class QuickSort extends JInternalFrame implements ActionListener, ChangeListener, Runnable
{

  private boolean resumed = false;
  int size;
  int delay;
  boolean pause;
  String string;
  JButton startButton;
  JButton stop;
  JButton pauseButton;
  JSlider sizeSelector;
  JSlider delaySelector;
  boolean start=false;
  QuickThread qThread;
  private final ExecutorService executor;
  public Lock lock=new ReentrantLock();
  public Condition condition=lock.newCondition();

  	public QuickSort()
  	{
		super("Quick Sort Demo", true, true, true, true);
		startButton=new JButton("Start");
		startButton.addActionListener(this);
		stop=new JButton("Stop");
		stop.addActionListener(this);
		pauseButton=new JButton("Pause");
		pauseButton.addActionListener(this);
		setLayout(new FlowLayout());
		add(startButton);
		add(stop);
		add(pauseButton);
		
        JPanel sizePanel=new JPanel();		
		JLabel sizeLabel= new JLabel("Slide for Number of Elements to Sort");
		sizePanel.add(sizeLabel);
		sizeSelector= new JSlider(5,605,10);
		sizeSelector.setMajorTickSpacing(100);
		sizeSelector.setPaintTicks(true);
		sizeSelector.setPaintLabels(true);
		sizeSelector.addChangeListener(this);
		sizePanel.add(sizeSelector);
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.PAGE_AXIS));
		add(sizePanel);		
		
		JPanel delayPanel=new JPanel();		
		JLabel delayLabel= new JLabel("Slide for the Speed of Sorting");
		delayPanel.add(delayLabel);
		delaySelector= new JSlider(1,2000,1000);
		delaySelector.setMajorTickSpacing(500);
		delaySelector.setPaintTicks(true);
		delaySelector.setPaintLabels(true);
		delaySelector.addChangeListener(this);
		sizePanel.add(delaySelector);
		delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.PAGE_AXIS));
		add(delayPanel);		
		
		
		executor = Executors.newFixedThreadPool(1);
		executor.execute(this);
		setSize(600,600);
		setVisible(true);
		setOpaque(true);
        qThread=new QuickThread();
		pause=false;
		size=10;
		delay=1000;	
  	}
	
  public void stateChanged(ChangeEvent event)
  {
    if(event.getSource()==sizeSelector)
	{
      int x = sizeSelector.getValue();
	  size=x;
	}
	if(event.getSource()==delaySelector)
	{
      int x = delaySelector.getValue();
	  qThread.setDelay(x);
	  delay=x;
	}
	
  }

  public void actionPerformed(ActionEvent event)
  {
    if(event.getSource()==startButton)
	{
	  qThread.stop();
	  qThread= new QuickThread();
	  qThread.start(size,delay);
	}
	else if(event.getSource()==pauseButton)
	{
	  pause=!pause;
	  if(pause==true)
	  {
	    pauseButton.setText("Unpause");
	    qThread.pause();
	  }
	  if(pause==false)
	  {
	    pauseButton.setText("Pause");
		qThread.unpause();
	  }
	}
	else if(event.getSource()==stop)
	{
	  qThread.stop();
	}
  }

  public void paint(Graphics g)
  {
 	super.paint(g);
    int[] data=qThread.getData();
	if(data!=null)
	{
	int offset=0;
	int x=10;
	int y=500;
	int width=(getWidth()-20)/size;
	for(int i=0;i<data.length;i++)
	{
	  g.drawRect(x+offset,y-data[i],width,data[i]);
	  offset+=width;
	}
	}
    
  }
  
  public void run()
  {
    while(!isClosed())
	{
	  repaint();
	  	  try {Thread.sleep(10);}
      catch (InterruptedException ex){}
	 }
	 executor.shutdownNow();
  }

}
