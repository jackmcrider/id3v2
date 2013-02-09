package view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Folder;
import model.XMLReader;
import control.Program;
import control.handlers.ClickedOnFileInTree;
import control.handlers.DirectoryChooser;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	private DefaultTreeModel tree;
	private JTree visualTree;
	private JButton directoryChooser;
	private JScrollPane scrollTree;
	private Folder folder;
	private String standardPath = ".";
	File xml = null;
	XMLReader reader;

	public NavigationPanel() {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());

		// Create tree of folders and files
		xml = searchForCache(new File(standardPath));
		if (xml == null) {
			this.tree = new DefaultTreeModel(new Folder(standardPath, true));
		} else {
			reader = new XMLReader(xml);
			this.tree = new DefaultTreeModel(reader.readXML());
		}

		// Initialize the visual tree
		this.visualTree = new JTree(this.tree);
		this.visualTree.addTreeSelectionListener(new ClickedOnFileInTree());

		// Initialize the Scrollpane of the Tree
		scrollTree = new JScrollPane(this.visualTree);

		// Logic for changing directory
		this.directoryChooser = new JButton("Change directory");
		this.directoryChooser.addActionListener(new DirectoryChooser());

		// Add components
		this.add(new JLabel("Directory tree"), BorderLayout.NORTH);

		this.add(scrollTree, BorderLayout.CENTER);
		this.add(this.directoryChooser, BorderLayout.SOUTH);
	}

	public Object getRoot() {
		return this.tree.getRoot();
	}

	/**
	 * Replace the tree in the navigation panel with a new root directory
	 * 
	 * @param path
	 */
	public void replaceTree(String path) {
		xml = searchForCache(new File(path));
		if (xml == null) {
			folder = new Folder(path, true);
			File newRoot = new File(path);
			if (newRoot.exists() && newRoot.isDirectory()) {
				this.tree.setRoot(folder);
			} else {
				Program.getControl().setStatus(
						"The thing that you selected was not a directory.");
			}
		} else {
			XMLReader reader = new XMLReader(xml);
			this.tree.setRoot(reader.readXML());
		}
	}

	public File searchForCache(File root) {
		if(root.listFiles() != null){
		for (File f : root.listFiles()) {
			if (f.toString().endsWith(".xml")) {
				return f;
			}
		}
		}
		return null;
	}

	public void setTree(DefaultMutableTreeNode n) {
		this.tree.setRoot(n);
	}
}