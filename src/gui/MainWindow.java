package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

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
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainSplit);
	}
	
	private void InitializeComponents() {
		
		navPanel = new NavigationPanel();
		ediPanel = new EditorPanel();
		
		// FIXME besser: scrollNavPanel in NavigationPanel() erzeugen
		JScrollPane scrollNavPanel = new JScrollPane();
    scrollNavPanel.getViewport().add(navPanel);
    // ^ das nach NavigationPanel.java ^
    
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollNavPanel, ediPanel);
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