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

public class MainWindow {
	private JFrame MainWindow;
	private JSplitPane MainSplitter;
	private JTree MainNavigation;
	private JPanel MainEditor;
	private GridLayout MainEditorStructure;
	
	private int width = 800, height = 600;

	/**
	 * Create a new main window.
	 */
	public MainWindow() {
		// Create window
		MainWindow = new JFrame();
		InitializeComponents();

		// Define stuff
		MainWindow.setSize(new Dimension(width, height));
		MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainWindow.add(MainSplitter);

		// Make window visible
		MainWindow.setVisible(true);
	}
	
	private void InitializeComponents() {
		MainNavigation();
		MainEditor();
		MainSplitter();
		
		MainSplitter.setTopComponent(MainNavigation);
		MainSplitter.setBottomComponent(MainEditor);
	}
	
	private void MainSplitter() {
		MainSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		// beg jmcr 8.11.12
		MainSplitter.setDividerLocation(width / 3);
		// end jmcr 8.11.12
		// lutsch meinen schwanz
	}
	
	private void MainNavigation() {
		MainNavigation = new JTree();
	}
	
	private void MainEditor() {
		MainEditor = new JPanel();
		MainEditorStructure = new GridLayout(5, 1, 5, 5);
		MainEditor.setLayout(MainEditorStructure);
		
		//MainEditor.add();
	}

	/**
	 * Get the JFrame instance of the main window.
	 * @return
	 */
	public JFrame get() {
		return MainWindow;
	}
}