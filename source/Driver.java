package code;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Driver extends JFrame implements ActionListener, ChangeListener
{
	private JDesktopPane desktop = new JDesktopPane();
	private JMenuItem author, problemDescription, help, references, bubbleSortMenuItem, selectionSortMenuItem, mergeSortMenuItem, quickSortMenuItem, heapSortMenuItem, shellSortMenuItem;
	private JCheckBox bubbleSortItem, selectionSortItem, mergeSortItem, quickSortItem, heapSortItem, shellSortItem;
	
	private Authors authors = new Authors();
	private ProblemDescription problem = new ProblemDescription();
	private Help helpWindow = new Help();
	private References refWindow = new References();
	
	private SelectionSort ss;
	private HeapSort hs;
	private BubbleSort bs;
	private ShellSort shs;
	private MergeSort ms;
	private QuickSort qs;
	
	private int sleepTime = Control.DEFAULT_SPEED;
	private int arraySize = Control.DEFUALT_NUM_OF_ELEMENTS;
	
	private JPanel container;
	private JButton startStop;
	private JButton pauseResume;
	private JSlider speed;
	private JSlider numElements;

	public Driver()
	{
		super("Sort");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,1000);
		setJMenuBar(setupMenuBar());
		add(desktop);
		
		startStop = new JButton("Start/Stop");
		startStop.addActionListener(this);
		pauseResume = new JButton("Pause/Resume");
		pauseResume.addActionListener(this);
		speed = new JSlider(Control.MIN_SPEED, Control.MAX_SPEED, Control.DEFAULT_SPEED);
		speed.setBorder(new TitledBorder("Speed"));
		speed.addChangeListener(this);
		numElements = new JSlider(Control.MIN_NUM_OF_ELEMENTS, Control.MAX_NUM_OF_ELEMENTS, Control.DEFUALT_NUM_OF_ELEMENTS);
		numElements.setBorder(new TitledBorder("Number of Elements"));
		numElements.addChangeListener(this);
		container = new JPanel();
		container.add(startStop);
		container.add(pauseResume);
		container.add(speed);
		container.add(numElements);
		add(container, BorderLayout.SOUTH);
		setVisible(true);
		
		desktop.add(authors);
		desktop.add(problem);
		desktop.add(helpWindow);
		desktop.add(refWindow);
	}

	public void actionPerformed(ActionEvent e)
	{
	    if(e.getSource() instanceof JCheckBox)
		{
		  //need to have a code in here for putting the same array in all 
		  if(bubbleSortItem.isSelected())
		  {
		  	if (bs == null || bs.isClosed())		
			{		
				bs = new BubbleSort();		
				desktop.add(bs);  //add bs to the desktop		
				bs.start();		
			}		
			bs.setVisible(true);		
			bs.toFront();	
		  }
		  if(selectionSortItem.isSelected())
		  {
			if (ss == null || ss.isClosed())
			{ 
				ss = new SelectionSort();
				desktop.add(ss);
			}
			ss.setVisible(true);
			ss.toFront();		  
		  }
		  if(mergeSortItem.isSelected())
		  {
			if (ms == null || ms.isClosed() == true)
			{ 
				ms = new MergeSort();
				desktop.add(ms);
			}
			ms.setVisible(true);
			ms.toFront();		  
		  }
		  if(quickSortItem.isSelected())
		  {
			if (qs == null || qs.isClosed() == true)
			{ 
				qs = new QuickSort();
				desktop.add(qs);
			}
			qs.setVisible(true);
			qs.toFront();		  
		  }
		  if(heapSortItem.isSelected())
		  {
			if (hs == null || hs.isClosed())
			{ 
				hs = new HeapSort();
				desktop.add(hs);
			}
			hs.setVisible(true);
			hs.toFront();		  
		  }
		  if(shellSortItem.isSelected())
		  {
			if (shs == null || shs.isClosed())
			{
				shs = new ShellSort();
				desktop.add(shs);  //add bs to the desktop
				shs.start();
			}
			shs.setVisible(true);
			shs.toFront();		  
		  }
		}
		else if (e.getSource()  instanceof JMenuItem)
		{
			if (e.getSource() == author)
			{
				authors.setVisible(true);
				authors.toFront();
				
				if(authors.isClosed())
				{
					desktop.add(authors);
				}
			}
			else if (e.getSource() == problemDescription)
			{
				problem.setVisible(true);
				problem.toFront();
				
				if(problem.isClosed())
				{
					desktop.add(problem);
				}
			}
			else if (e.getSource() == help)
			{
				helpWindow.setVisible(true);
				helpWindow.toFront();
				
				if(helpWindow.isClosed())
				{
					desktop.add(helpWindow);
				}
			}
			else if (e.getSource() == references)
			{
				refWindow.setVisible(true);
				refWindow.toFront();
				
				if(refWindow.isClosed())
				{
					desktop.add(refWindow);
				}
			}
			else if (e.getSource() == selectionSortMenuItem)
			{
				if (ss == null || ss.isClosed())
				{ 
					ss = new SelectionSort();
					desktop.add(ss);
					ss.start();
				}
				ss.setVisible(true);
				ss.toFront();
			}
			else if (e.getSource() == heapSortMenuItem)
			{
				if (hs == null || hs.isClosed())
				{ 
					hs = new HeapSort();
					desktop.add(hs);
					hs.start();
				}
				hs.setVisible(true);
				hs.toFront();
			}
			else if (e.getSource() == bubbleSortMenuItem)		
			{		
				if (bs == null || bs.isClosed())		
				{		
					bs = new BubbleSort();		
					desktop.add(bs);  //add bs to the desktop		
					bs.start();		
				}		
				bs.setVisible(true);		
				bs.toFront();		
			}
			else if (e.getSource() == shellSortMenuItem)
			{
				if (shs == null || shs.isClosed())
				{
					shs = new ShellSort();
					desktop.add(shs);  //add bs to the desktop
					shs.start();
				}
				shs.setVisible(true);
				shs.toFront();
			}
			else if (e.getSource() == mergeSortMenuItem)
			{
				if (ms == null || ms.isClosed() == true)
				{ 
					ms = new MergeSort();
					desktop.add(ms);
					
				}
				ms.setVisible(true);
				ms.toFront();
			}
			else if (e.getSource() == quickSortMenuItem)
			{
				if (qs == null || qs.isClosed() == true)
				{ 
					qs = new QuickSort();
					desktop.add(qs);
					
				}
				qs.setVisible(true);
				qs.toFront();
			}
		}
		else if (e.getSource()  instanceof JButton)
		{
			if (e.getSource() == startStop)
			{
				if (ss != null && !ss.isClosed())
				{ 
					if (ss.isRunning() == true)
					{
						if(ss.isPaused())
						{
							ss.resume();
						}
						ss.stop();
					}
					else
					{
						ss.start();
					}
				}
				if (hs != null && !hs.isClosed())
				{ 
					if (hs.isRunning() == true)
					{
						if(hs.isPaused())
						{
							hs.resume();
						}
						hs.stop();
					}
					else
					{
						hs.start();
					}
				}
				if (bs != null && !bs.isClosed())
				{ 
					if (bs.isRunning() == true)
					{
						if(bs.isPaused())
						{
							bs.resume();
						}
						bs.stop();
					}
					else
					{
						bs.start();
					}
				}
				if (shs != null && !shs.isClosed())
				{ 
					if (shs.isRunning() == true)
					{
						if(shs.isPaused())
						{
							shs.resume();
						}
						shs.stop();
					}
					else
					{
						shs.start();
					}
				}
			}
			else if (e.getSource() == pauseResume)
			{
				if (ss != null && !ss.isClosed())
				{ 
					if (ss.isPaused() == true)
					{
						if (ss.isRunning() == true)
						{
							ss.resume();
						}
					}
					else
					{
						if (ss.isRunning() == true)
						{
							ss.pause();
						}
					}
				}
				if (hs != null && !hs.isClosed())
				{ 
					if (hs.isPaused() == true)
					{
						if (hs.isRunning() == true)
						{
							hs.resume();
						}
					}
					else
					{
						if (hs.isRunning() == true)
						{
							hs.pause();
						}
					}
				}
				if (bs != null && !bs.isClosed())
				{ 
					if (bs.isPaused() == true)
					{
						if (bs.isRunning() == true)
						{
							bs.resume();
						}
					}
					else
					{
						if (bs.isRunning() == true)
						{
							bs.pause();
						}
					}
				}
				if (shs != null && !shs.isClosed())
				{ 
					if (shs.isPaused() == true)
					{
						if (shs.isRunning() == true)
						{
							shs.resume();
						}
					}
					else
					{
						if (shs.isRunning() == true)
						{
							shs.pause();
						}
					}
				}
			}
		}
	}
	
	private JMenuBar setupMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu aboutMenu = new JMenu("About");
		author = new JMenuItem("Authors");
		problemDescription = new JMenuItem("Problem Description");
		help = new JMenuItem("Help");
		references = new JMenuItem("References");
		author.addActionListener(this);
		problemDescription.addActionListener(this);
		help.addActionListener(this);
		references.addActionListener(this);
		aboutMenu.add(author);
		aboutMenu.add(problemDescription);
		aboutMenu.add(help);
		aboutMenu.add(references);
		
		JMenu demosMenu = new JMenu("Demos");
		bubbleSortMenuItem = new JMenuItem("Bubble Sort");
		selectionSortMenuItem = new JMenuItem("Selection Sort");
		mergeSortMenuItem = new JMenuItem("Merge Sort");
		quickSortMenuItem = new JMenuItem("Quick Sort");
		heapSortMenuItem = new JMenuItem("Heap Sort");
		shellSortMenuItem = new JMenuItem("Shell Sort");
		bubbleSortMenuItem.addActionListener(this);
		selectionSortMenuItem.addActionListener(this);
		mergeSortMenuItem.addActionListener(this);
		quickSortMenuItem.addActionListener(this);
		heapSortMenuItem.addActionListener(this);
		shellSortMenuItem.addActionListener(this);
		demosMenu.add(bubbleSortMenuItem);
		demosMenu.add(selectionSortMenuItem);
		demosMenu.add(mergeSortMenuItem);
		demosMenu.add(quickSortMenuItem);
		demosMenu.add(heapSortMenuItem);
		demosMenu.add(shellSortMenuItem);
		
		JMenu multiTaskingMenu = new JMenu("MultiTasking");
		bubbleSortItem = new JCheckBox("Bubble Sort",false);
		selectionSortItem = new JCheckBox("Selection Sort",false);
		mergeSortItem = new JCheckBox("Merge Sort",false);
		quickSortItem = new JCheckBox("Quick Sort",false);
		heapSortItem = new JCheckBox("Heap Sort",false);
		shellSortItem = new JCheckBox("Shell Sort",false);
		
		multiTaskingMenu.add(bubbleSortItem);		
		multiTaskingMenu.add(selectionSortItem);		
		multiTaskingMenu.add(mergeSortItem);		
		multiTaskingMenu.add(quickSortItem);		
		multiTaskingMenu.add(heapSortItem);		
		multiTaskingMenu.add(shellSortItem);
		
		bubbleSortItem.addActionListener(this);
		selectionSortItem.addActionListener(this);
		mergeSortItem.addActionListener(this);
		quickSortItem.addActionListener(this);
		heapSortItem.addActionListener(this);
		shellSortItem.addActionListener(this);
		
		JMenu clockMenu = new JMenu("Clock");
		clockMenu.add(new Clock());
		
		menuBar.add(aboutMenu);
		menuBar.add(demosMenu);
		menuBar.add(multiTaskingMenu);
		menuBar.add(clockMenu);
		return menuBar;
	}

	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == speed)
		{
			sleepTime = Control.MAX_SPEED - speed.getValue();
			
			
			if (ss != null && !ss.isClosed())
			{ 
				ss.setDelay(speed.getValue());
			}
			if (hs != null && !hs.isClosed())
			{ 
				hs.setDelay(speed.getValue());
			}
			if (bs != null && bs.isClosed() == false)
			{
				bs.setDelay(speed.getValue());
			}
			if (shs != null && shs.isClosed() == false)
			{
				shs.setDelay(speed.getValue());
			}
			
			/*if (bs != null && bs.isClosed() == false)
			{
				bs.setDelay(speed.getValue());
			}
			...
			...
			...
			*/
			
		}
		else if (e.getSource() == numElements)
		{
			arraySize = numElements.getValue();
			
			if (ss != null && !ss.isClosed())
			{ 
				ss.setNumberOfElements(arraySize);
			}
			if (hs != null && !hs.isClosed())
			{ 
				hs.setNumberOfElements(arraySize);
			}
			if (bs != null && bs.isClosed() == false)
			{
				bs.setNumberOfElements(arraySize);
			}
			if (shs != null && shs.isClosed() == false)
			{
				shs.setNumberOfElements(arraySize);
			}
			
			/*if (qs != null && qs.isClosed() == false)
			{
				setArray(arraySize);
			}
			...
			...
			...
			*/
		}
	}
}	
