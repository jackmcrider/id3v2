package model;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class DirectoryTree extends DefaultTreeModel {

	public DirectoryTree(TreeNode node) {
		super(node);
	}
	
}