package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Folder extends DefaultMutableTreeNode {
	public Folder(String path, boolean recursive) {
		if (path == null)
			path = "resources" + File.separator + "mp3s";
		
		this.setUserObject(new File(path));

		
		if(recursive){
			for (File f : ((File) this.getUserObject()).listFiles()) {
				if (f.isDirectory()) {
					this.add(new Folder(f.getPath(), recursive));
				} else if (f.getPath().endsWith("mp3")) {
					MP3File mp3File = new MP3File(f.getPath());
					this.add(mp3File);
				} else {
					// this.add(new OrdinaryFile(f.getPath()));
				}
			}
		}
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}
}