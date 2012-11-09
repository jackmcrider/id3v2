package gui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;


public class MainWindow extends JFrame{
	
	private JSplitPane mainSplit;
	private NavigationPanel navPanel;
	private EditorPanel ediPanel;
	private int width = 800, height = 600;
	private JFrame win;

	/**
	 * Create a new main window.
	 */
	public MainWindow()  {
		InitializeComponents();
		setSize(new Dimension(width, height));
		setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - height/2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainSplit);
		setVisible(true);
	}
	
	private void InitializeComponents() {
		
		ediPanel = new EditorPanel(this.getContentPane());
		navPanel = new NavigationPanel(ediPanel);
    
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, ediPanel);
		mainSplit.setDividerLocation(width / 3);
	}	

	/**
	 * Get the JFrame instance of the main window.
	 * @return
	 */
	public JFrame get() {
		return this;
	}
}
