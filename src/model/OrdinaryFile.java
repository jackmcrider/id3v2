package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
/**
 * This represents an ordinary file (obsolete)
 * @author Karl
 *
 */
public class OrdinaryFile extends DefaultMutableTreeNode {
	public OrdinaryFile(String path) {
		this.setUserObject(new File(path));
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}
}
