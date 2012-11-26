package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class Folder extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -9065207958966277512L;
	private File representedFolder;

	public Folder(String path) {
		if (path == null)
			path = "mp3s";

		representedFolder = new File(path);

		for (File f : representedFolder.listFiles()) {
			if (f.isDirectory()) {
				this.add(new Folder(f.getPath()));
			} else {
				this.add(new MP3File(f.getPath()));
			}
		}
	}

	public String toString() {
		return this.representedFolder.getName();
	}
}