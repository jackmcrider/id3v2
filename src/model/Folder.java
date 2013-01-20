package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class Folder extends DefaultMutableTreeNode {
	public Folder(String path) {
		File xml = null;
		if (path == null) {
			path = "resources" + File.separator + "mp3s";
		}
		this.setUserObject(new File(path));
		xml = searchForCache();
		
		if (xml == null) {
			System.out.println("disk");
			for (File f : ((File) this.getUserObject()).listFiles()) {
				if (f.isDirectory()) {
					this.add(new Folder(f.getPath()));
				} else if (f.getPath().endsWith("mp3")) {
					MP3File mp3File = new MP3File(f.getPath());
					this.add(mp3File);
				} else {
					// this.add(new OrdinaryFile(f.getPath()));
				}
			}
		} else {
			System.out.println("xml");
			XMLReader reader = new XMLReader(xml);
			//this.userObject = reader.readXML();
			this.add(reader.readXML());
		}
	}

	public File searchForCache() {
		for (File f : ((File) this.getUserObject()).listFiles()) {
			if (f.toString().endsWith(".xml")) {
				return f;
			}
		}
		return null;
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}
}