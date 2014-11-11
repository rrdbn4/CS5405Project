package code;

import java.awt.event.*;
import javax.swing.*;

public class Driver extends JFrame implements ActionListener
{
	private JDesktopPane desktop = new JDesktopPane();
	private JMenuItem author, problemDescription, help, references, bubbleSortMenuItem, selectionSortMenuItem, mergeSortMenuItem, quickSortMenuItem, heapSortMenuItem, shellSortMenuItem;
	
	private SelectionSort ss;
	private HeapSort hs;
	private BubbleSort bs;  //bs is a pretty fitting variable name
	private ShellSort shs;

	public Driver()
	{
		super("Sort");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000,1000);
		setJMenuBar(setupMenuBar());
		add(desktop);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == selectionSortMenuItem)
		{
			if (ss == null || ss.isClosed() == true)
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
			if (hs == null || hs.isClosed() == true)
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
		//Not sure what he wants here....
		
		JMenu clockMenu = new JMenu("Clock");
		clockMenu.add(new Clock());
		
		menuBar.add(aboutMenu);
		menuBar.add(demosMenu);
		menuBar.add(multiTaskingMenu);
		menuBar.add(clockMenu);
		return menuBar;
	}
}	