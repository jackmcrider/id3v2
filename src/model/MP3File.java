package model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class MP3File extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 6499167243937478419L;
	private File representedFile;
	
	private String title;
	private String artist;
	private String album;
	private String year;

	public MP3File(String path) {
		representedFile = new File(path);
		
		this.title = representedFile.hashCode() + " title";
		this.artist = representedFile.hashCode() + " artist";
		this.album = representedFile.hashCode() + " album";
		this.year = representedFile.hashCode() + " year";
	}

	public String toString() {
		return representedFile.getName();
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
