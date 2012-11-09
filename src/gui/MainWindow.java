package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;

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
		//win = new JFrame();
		InitializeComponents();
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainSplit);
		setVisible(true);
	}
	
	private void InitializeComponents() {
		
		ediPanel = new EditorPanel(this.getContentPane());
		navPanel = new NavigationPanel(ediPanel);
		
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