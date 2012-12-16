package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Folder;
import model.MP3File;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	private DefaultTreeModel tree;
	private JTree visualTree;
	private JButton directoryChooser;

	public NavigationPanel(final EditorPanel ep) {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());

		// Create tree of folders and files
		this.tree = new DefaultTreeModel(new Folder(null));

		// Initialize the visual tree
		this.visualTree = new JTree(this.tree);
		this.visualTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selected = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (selected instanceof MP3File) {
					MP3File current = (MP3File) selected;
					if(!current.isParsed())
						current.parse();
					ep.load(current);
				}
			}
		});

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
		this.add(this.visualTree, BorderLayout.CENTER);
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
			System.out.println("replaceTree() was not called with the path of a directory!");
		}
	}
}