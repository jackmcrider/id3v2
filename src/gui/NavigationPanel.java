package gui;

import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class NavigationPanel extends JScrollPane {
	private static final long serialVersionUID = -7186448742695743778L;
	private JTree tree;
	private File file;
	private JPanel panel;

	/*
	 * Left side of the window, contains a file tree Gets ep for accessing the
	 * editor panel
	 */
	public NavigationPanel(final EditorPanel ep) {

		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));

		file = new File("mp3s");
		tree = new JTree(createTree(null, file));

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				if (node.isLeaf()) {
					ep.refresh(node);
					// test(node.getParent()+file.separator+node.toString());
				}
			}
		});
		panel.add(tree);
		this.getViewport().add(panel);
	}

	/*
	 * DefaultMutableTreeNode greates the file tree by creating nodes from files
	 * and directories
	 */
	private DefaultMutableTreeNode createTree(DefaultMutableTreeNode curTop,
			File dir) {

		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);

		if (curTop != null) {
			curTop.add(curDir);
		}

		Vector vec = new Vector();
		String[] tmp = dir.list();

		for (int i = 0; i < tmp.length; i++)
			vec.addElement(tmp[i]);

		Collections.sort(vec, String.CASE_INSENSITIVE_ORDER);
		File file;
		Vector files = new Vector();

		for (int i = 0; i < vec.size(); i++) {
			String thisObject = (String) vec.elementAt(i);
			String newPath;

			if (curPath.equals("."))
				newPath = thisObject;
			else
				newPath = curPath + File.separator + thisObject;
			if ((file = new File(newPath)).isDirectory())
				createTree(curDir, file);
			else
				files.addElement(thisObject);
		}

		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));

		return curDir;
	}
}
