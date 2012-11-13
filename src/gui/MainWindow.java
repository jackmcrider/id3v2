package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;


public class MainWindow extends JFrame{
	
	private JSplitPane mainSplit;
	private NavigationPanel navPanel;
	private EditorPanel ediPanel;
	private int width = 600, height = 350;

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
		//Erst nach Visible(true) wissen die Panels, wie gross sie sind
		ediPanel.addCover();
		setVisible(true);
	}
	
	private void InitializeComponents() {
		
		ediPanel = new EditorPanel(this.getContentPane());
		navPanel = new NavigationPanel(ediPanel);
    
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navPanel, ediPanel);
		mainSplit.setDividerLocation(width / 3);
		
  
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if(ediPanel.getCover() != null)
					ediPanel.repaintCover();
				setVisible(true);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}		
		});		
	}	

	/**
	 * Get the JFrame instance of the main window.
	 * @return
	 */
	public JFrame get() {
		return this;
	}
}
