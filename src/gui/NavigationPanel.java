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

import model.Folder;
import model.MP3File;
import model.Tree;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	private Tree tree;
	private JTree visualTree;
	private JButton directoryChooser;

	public NavigationPanel(final EditorPanel ep) {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());
		
		// Create tree of folders and files
		this.tree = new Tree(new Folder(null));

		// Initialize the visual tree
		this.visualTree = new JTree(this.tree);
		this.visualTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selected = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				
				if (selected instanceof model.MP3File) {
					ep.load((MP3File) selected);
				}
			}
		});
		
		this.directoryChooser = new JButton("Verzeichnis wechseln");
		this.directoryChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton source = (JButton) arg0.getSource();
				NavigationPanel navigationPanel = (NavigationPanel) source.getParent();
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(navigationPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            navigationPanel.replaceTree(file.getAbsolutePath());
		        }
			}
			
		});
		
		this.add(new JLabel("Verzeichnisbaum"), BorderLayout.NORTH);
		this.add(this.visualTree, BorderLayout.CENTER);
		this.add(this.directoryChooser, BorderLayout.SOUTH);
	}
	
	public void replaceTree(String path) {
		Folder newRoot = new Folder(path);
		
		this.tree.replaceRoot(newRoot);
		this.visualTree.updateUI();
	}
	
}