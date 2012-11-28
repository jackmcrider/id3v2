package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Folder extends DefaultMutableTreeNode {
	private int amountOfChildren = 0;

	public Folder(String path) {
		if (path == null)
			path = "resources" + File.separator + "mp3s";
		
		this.setUserObject(new File(path));

		for (File f : ((File) this.getUserObject()).listFiles()) {
			if (f.isDirectory()) {
				this.add(new Folder(f.getPath()));
			} else if (f.getPath().endsWith("mp3")){
				this.add(new MP3File(f.getPath()));
			} else {
				this.add(new OrdinaryFile(f.getPath()));
			}
			this.amountOfChildren++;
		}
	}

	public String toString() {
		return ((File) this.getUserObject()).getName() + " (" + this.amountOfChildren + ")";
	}
}