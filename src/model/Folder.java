package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Folder extends DefaultMutableTreeNode {
	private File representedFolder;
	private int amountOfChildren = 0;

	public Folder(String path) {
		if (path == null)
			path = "resources" + File.separator + "mp3s";

		representedFolder = new File(path);

		for (File f : representedFolder.listFiles()) {
			if (f.isDirectory()) {
				this.add(new Folder(f.getPath()));
			} else {
				this.add(new MP3File(f.getPath()));
			}
			this.amountOfChildren++;
		}
	}

	public String toString() {
		return this.representedFolder.getName() + " (" + this.amountOfChildren + ")";
	}
}