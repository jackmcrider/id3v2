package model;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class Tree extends DefaultTreeModel {

	public Tree(TreeNode node) {
		super(node);
	}
	
	public void replaceRoot(TreeNode node) {
		this.setRoot(node);
	}
	
}