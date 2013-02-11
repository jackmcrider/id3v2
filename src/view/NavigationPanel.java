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
/**
 * The left side of the main window, the navigation panel
 * @author Karl
 *
 */
public class NavigationPanel extends JPanel {
	private DefaultTreeModel tree = null;
	private JTree visualTree;
	private JButton directoryChooser;
	private JScrollPane scrollTree;
	private DefaultMutableTreeNode folder;
	private String standardPath = "resources/tests/";
	File xml = null;
	XMLReader reader;

	public NavigationPanel() {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());

		this.replaceTree(standardPath);

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
	/**
	 * returns the the root of the JTree
	 * 
	 */
	public Object getRoot() {
		return this.tree.getRoot();
	}

	/**
	 * Replace the tree in the navigation panel with a new root directory
	 * 
	 * @param path
	 */
	public void replaceTree(String path) {
		File dir = new File(path);

		if (!dir.exists() || !dir.isDirectory()) {
			Program.getControl().setStatus(
					"The thing that you selected was not a directory.");
			return;
		}

		xml = searchForCache(dir);

		boolean builtFromXML = false;
		try {
			//if a cache.xml in root exists
			if(xml != null){
				reader = new XMLReader(xml);
				this.folder = reader.readXML();
				if(this.tree == null)
					this.tree = new DefaultTreeModel(folder);
				else
					this.tree.setRoot(this.folder);
				
				builtFromXML = true;
			}
		} catch (Exception e) {
			System.out.println("XML korrumpiert!");
		}
		
		//if theres no cache.xml or the cache.xml is corrupt
		//build the tree by scanning the disk
		if (!builtFromXML) {
			this.folder = new Folder(path, true);
			if(this.tree == null)
				this.tree = new DefaultTreeModel(folder);
			else
				this.tree.setRoot(folder);
		}
	}
	/**
	 * searches for a cache.xml in the given root-directory and return the file if it exists. otherwise null.
	 * 
	 * @param root
	 */
	public File searchForCache(File root) {
		if (root.listFiles() != null) {
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

	public DefaultMutableTreeNode getFolder() {
		return this.folder;
	}
}