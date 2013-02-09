package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
/*
 * recursive representation of the folder
 */
public class Folder extends DefaultMutableTreeNode {
	
	/*
	 * Creates a new DefaultMutableTreeNode for the NavigaionPanel
	 */
	public Folder(String path, boolean recursive) {
		if (path == null)
			path = ".";

		this.setUserObject(new File(path));

		if (recursive) {
			if(((File)this.getUserObject()).listFiles() != null){
			for (File f : ((File) this.getUserObject()).listFiles()) {
				if (f.isDirectory()) {
					this.add(new Folder(f.getPath(), recursive));
				} else if (f.getPath().endsWith(".mp3")) {
					MP3File mp3File = new MP3File(f.getPath());
					this.add(mp3File);
				} else {
					// this.add(new OrdinaryFile(f.getPath()));
				}
			}
			}
		}
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}
}