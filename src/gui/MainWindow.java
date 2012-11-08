package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;

public class MainWindow extends JFrame{
	
	private JSplitPane mainSplit;
	private NavigationPanel navPanel;
	private EditorPanel ediPanel;
	private int width = 800, height = 600;

	/**
	 * Create a new main window.
	 */
	public MainWindow()  {
		
		new JFrame();
		InitializeComponents();
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainSplit);
		setVisible(true);
		
	}
	
	private void InitializeComponents() {
		navPanel = new NavigationPanel();
		ediPanel = new EditorPanel();
		
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplit.setDividerLocation(width / 3);
		mainSplit.setTopComponent(navPanel);
		mainSplit.setBottomComponent(ediPanel);
	}	

	/**
	 * Get the JFrame instance of the main window.
	 * @return
	 */
	public JFrame get() {
		return this;
	}
}