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
				MP3File mp3File = new MP3File(f.getPath());
				if(mp3File.isID3v2Tag())
					this.add(mp3File);
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