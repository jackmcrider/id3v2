package gui;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.DirectoryTree;
import model.Folder;
import model.MP3File;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	private DirectoryTree tree;
	private JTree visualTree;
	private JButton directoryChooser;

	public NavigationPanel(final EditorPanel ep) {
		// Set layout of NavigationPanel
		this.setLayout(new BorderLayout());

		// Create tree of folders and files
		this.tree = new DirectoryTree(new Folder(null));

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

		this.visualTree.setCellRenderer(new FileTreeCellRenderer());

		// Logic for changing directory
		this.directoryChooser = new JButton("Verzeichnis wechseln");
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
		this.add(new JLabel("Verzeichnisbaum"), BorderLayout.NORTH);
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
			System.out
					.println("replaceTree() was not called with the path of a directory!");
		}
	}
	

}

@SuppressWarnings("serial")
class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	private FileSystemView fileSystemView;

	private JLabel label;

	FileTreeCellRenderer() {
		label = new JLabel();
		label.setOpaque(true);
		fileSystemView = FileSystemView.getFileSystemView();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		File file = (File) node.getUserObject();
		label.setIcon(fileSystemView.getSystemIcon(file));
		label.setText(fileSystemView.getSystemDisplayName(file));
		label.setToolTipText(file.getPath());

		if (selected) {
			label.setBackground(backgroundSelectionColor);
			label.setForeground(this.textSelectionColor);
		} else {
			label.setBackground(backgroundNonSelectionColor);
			label.setForeground(this.textNonSelectionColor);
		}

		return label;
	}
}