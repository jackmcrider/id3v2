package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class MP3File extends DefaultMutableTreeNode {
	
	private String title;
	private String artist;
	private String album;
	private String year;

	public MP3File(String path) {
		this.setUserObject(new File(path));
		
		this.title = ((File) this.getUserObject()).hashCode() + " title";
		this.artist = ((File) this.getUserObject()).hashCode() + " artist";
		this.album = ((File) this.getUserObject()).hashCode() + " album";
		this.year = ((File) this.getUserObject()).hashCode() + " year";
	}

	public String toString() {
		return ((File) this.getUserObject()).getName();
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
}
