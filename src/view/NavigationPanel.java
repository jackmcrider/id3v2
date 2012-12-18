package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import model.Folder;
import control.Program;
import control.handlers.ClickedOnFileInTree;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	private DefaultTreeModel tree;
	private JTree visualTree;
	private JButton directoryChooser;
	private JScrollPane scrollTree;

	public NavigationPanel() {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());

		// Create tree of folders and files
		this.tree = new DefaultTreeModel(new Folder(null));

		// Initialize the visual tree
		this.visualTree = new JTree(this.tree);
		this.visualTree.addTreeSelectionListener(new ClickedOnFileInTree());
		
		// Initialize the Scrollpane of the Tree
		scrollTree = new JScrollPane(this.visualTree);
		
		// Logic for changing directory
		this.directoryChooser = new JButton("Change directory");
		this.directoryChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Get source of action
				JButton source = (JButton) arg0.getSource();

				// Get navigation panel
				NavigationPanel navigationPanel = (NavigationPanel) source
						.getParent();

				// Initialize file chooser
				final JFileChooser fc = new JFileChooser();

				// Show only directories
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Choose a directory
				int returnVal = fc.showOpenDialog(navigationPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					navigationPanel.replaceTree(file.getAbsolutePath());
				}
			}
		});

		// Add components
		this.add(new JLabel("Directory tree"), BorderLayout.NORTH);
		
		this.add(scrollTree, BorderLayout.CENTER);
		this.add(this.directoryChooser, BorderLayout.SOUTH);
	}

	/**
	 * Replace the tree in the navigation panel with a new root directory
	 * 
	 * @param path
	 */
	public void replaceTree(String path) {
		File newRoot = new File(path);
		if (newRoot.exists() && newRoot.isDirectory()) {
			this.tree.setRoot(new Folder(path));
		} else {
			Program.getControl().setStatus("The thing that you selected was not a directory.");
		}
	}
}