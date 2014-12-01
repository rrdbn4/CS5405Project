/**
@author Matthew Lindner, Holly Busken, Robert Dunn
@version 1.0
*/
package code;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
The Driver class creates a frame to manage all the sorts and demo information.
*/
public class Driver extends JFrame implements ActionListener, ChangeListener
{
    /** desktop is pane to hold all JInternalFrames. */
	private JDesktopPane desktop = new JDesktopPane();
	/** author is the menu item for displaying author information. */
	private JMenuItem author;
	/** problemDescription is the menu item for displaying information about the problem. */
	private JMenuItem problemDescription;
	/** help is the menu item for displaying help information. */
	private JMenuItem help;
	/** references is the menu item for displaying reference information. */
	private JMenuItem references;
	
	/** bubbleSortMenuItem is the menu item for displaying the bubble sort visualization. */
	private JMenuItem bubbleSortMenuItem;
	/** selectionSortMenuItem is the menu item for displaying the selection sort visualization.*/
	private JMenuItem selectionSortMenuItem;
	/** mergeSortMenuItem is the menu item for displaying the merge sort visualization. */
	private JMenuItem mergeSortMenuItem;
	/** quickSortMenuItem is the menu item for displaying the quick sort visualization.*/
	private JMenuItem quickSortMenuItem;
	/** heapSortMenuItem is the menu item for displaying the heap sort visualization. */
	private JMenuItem heapSortMenuItem;
	/** shellSortMenuItem is the menu item for displaying the shell sort visualization. */
	private JMenuItem shellSortMenuItem;
	
	/** bubbleSortItem is the check box for displaying the bubble sort visualization. */
	private JCheckBox bubbleSortItem;
	/** selectionSortItem is the check box for displaying the selection sort visualization. */
	private JCheckBox selectionSortItem;
	/** mergeSortItem is the check box for displaying the merge sort visualization. */
	private JCheckBox mergeSortItem; 
	/** quickSortItem is the check box for displaying the quick sort visualization. */
	private JCheckBox quickSortItem; 
	/** heapSortItem is the check box for displaying the heap sort visualization.*/
	private JCheckBox heapSortItem; 
	/** shellSortItem is the check box for displaying the shell sort visualization. */
	private JCheckBox shellSortItem;
	
	/** authors is a frame for displaying author information. */
	private Authors authors = new Authors();
	/** problem is a frame for displaying information about the problem. */ 
	private ProblemDescription problem = new ProblemDescription();
	/** helpWindow is a frame for displaying help information. */
	private Help helpWindow = new Help();
	/** refWindow is a frame for displaying reference information. */
	private References refWindow = new References();
	
	/** ss is a frame for visualizing the selection sort algorithm.*/
	private SelectionSort ss;
	/** hs is a frame for visualizing the heap sort algorithm.*/
	private HeapSort hs;
	/** bs is a frame for visualizing the bubble sort algorithm.*/
	private BubbleSort bs;
	/** shs is a frame for visualizing the shell sort algorithm. */
	private ShellSort shs;
	/** ms is a frame for visualizing the merge sort algorithm.*/
	private MergeSort ms;
	/** qs is a frame for visualizing the quick sort algorithm.*/
	private QuickSort qs;
	
	/** sleepTime is the speed for the sorting algorithms.*/
	private int sleepTime = Control.DEFAULT_SPEED;
	/** arraySize is the number of elements to sort. */
	private int arraySize = Control.DEFUALT_NUM_OF_ELEMENTS;
	
	/** container is a panel for holding the buttons and sliders for the sorting algorithms.*/
	private JPanel container;
	/** startStop is a button for starting or stopping all sorting algorithms checked.*/
	private JButton startStop;
	/** pauseResume is a button for pausing or resuming all sorting algorithms checked.*/
	private JButton pauseResume;
	/** speed is a slider for changing the speed of sorting.*/
	private JSlider speed;
	/** numElements is a slider for changing the number of elements to sort.*/
	private JSlider numElements;

	/**
	Creates a frame, menu bar, and buttons to use with all sorts.
	*/
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

	/**
	Detects button and check box clicks and takes the corresponding actions for them.
	*/
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
					ms.start();
					
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
					qs.start();
					
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
				if (ms != null && !ms.isClosed())
				{ 
					if (ms.isRunning() == true)
					{
						if(ms.isPaused())
						{
							ms.resume();
						}
						ms.stop();
					}
					else
					{
						ms.start();
					}
				}
				if (qs != null && !qs.isClosed())
				{ 
					if (qs.isRunning() == true)
					{
						if(qs.isPaused())
						{
							qs.resume();
						}
						qs.stop();
					}
					else
					{
						qs.start();
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
				if (ms != null && !ms.isClosed())
				{ 
					if (ms.isPaused() == true)
					{
						if (ms.isRunning() == true)
						{
							ms.resume();
						}
					}
					else
					{
						if (ms.isRunning() == true)
						{
							ms.resume();
						}
					}
				}
				if (qs != null && !qs.isClosed())
				{ 
					if (qs.isPaused() == true)
					{
						if (qs.isRunning() == true)
						{
							qs.resume();
						}
					}
					else
					{
						if (qs.isRunning() == true)
						{
							qs.resume();
						}
					}
				}				
			}
		}
	}
	
	/**
	Creates a menu bar for information about the authors, the problem,
	references, and for selecting sorting algorithms.
	*/
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

	/**
	Detects changes in both the speed slider and delay slider 
	and does the corresponding actions for the changes.
	*/
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
			if (ms != null && ms.isClosed() == false)
			{
				ms.setDelay(speed.getValue());
			}
			if (qs != null && qs.isClosed() == false)
			{
				qs.setDelay(speed.getValue());
			}			
			
			
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
			if (ms != null && ms.isClosed() == false)
			{
				ms.setNumberOfElements(arraySize);
			}
			if (qs != null && qs.isClosed() == false)
			{
				qs.setNumberOfElements(arraySize);
			}			
			
		}
	}
}	
