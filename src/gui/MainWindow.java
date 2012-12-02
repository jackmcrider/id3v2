package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	// Components
	private JSplitPane mainSplitter;
	private NavigationPanel navigationPanel;
	private EditorPanel editorPanel;
	private int width = 640, height = 320;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Create a new main window and setup the basics.
	 */
	public MainWindow() {
		// Size and position
		this.setSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(600, 280));
		this.setLocation(this.screenSize.width / 2 - width / 2,	this.screenSize.height / 2 - height / 2);
		
		// Exit on close
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialize components and make them visible
		this.initializeComponents();
		this.setVisible(true);
		
		// Add the cover, when the panels know their size, after setVisible(true)
		
	}

	private void initializeComponents() {
		// Initialize editor and navigation panel
		this.editorPanel = new EditorPanel();
		this.navigationPanel = new NavigationPanel(editorPanel);

		// Initialize the splitter
		this.mainSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, editorPanel);
		this.mainSplitter.setDividerLocation(width / 3);
		this.add(mainSplitter);
		
		// Resize panels and cover
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// Get the source of the event
				MainWindow source = (MainWindow) arg0.getSource();
				
				// Resize cover
				if (source.getEditorPanel().getCover() != null)
					//source.getEditorPanel().repaintCover();
				
				// Resize splitter
				if (source.getNavigationPanel().getSize().width > 0)
					if (source.getNavigationPanel().getSize().width < width / 3)
						source.getMainSplitter().setDividerLocation(source.getSize().width / 3);
				
				// Repaint the source and set it visible again
				// source.setVisible(true);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}
	
	/**
	 * Get the MainSplitter instance of the MainWindow
	 * @return
	 */
	public JSplitPane getMainSplitter() {
		return this.mainSplitter;
	}
	
	/**
	 * Get the NavigationPanel instance of the MainWindow
	 * @return
	 */
	public NavigationPanel getNavigationPanel() {
		return this.navigationPanel;
	}
	
	/**
	 * Get the EditorPanel instance of the MainWindow
	 * @return
	 */
	public EditorPanel getEditorPanel() {
		return this.editorPanel;
	}

}
