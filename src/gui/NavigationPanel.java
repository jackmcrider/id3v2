package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Folder;
import model.MP3File;

public class NavigationPanel extends JScrollPane {
	private static final long serialVersionUID = -7186448742695743778L;
	private JTree tree;
	private JPanel panel;

	public NavigationPanel(final EditorPanel ep) {

		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));

		tree = new JTree(new Folder(null));

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				if (node.isLeaf()) {
					
					ep.load((MP3File) node);
				}
			}
		});

		panel.add(tree);
		this.getViewport().add(panel);
	}
}
