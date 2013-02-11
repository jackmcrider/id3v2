package control.handlers;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.MP3File;
import control.Program;

/**
 * This class handles events when something is clicked in the file tree in the
 * navigation panel
 * 
 * @author Karl
 * 
 */
public class ClickedOnFileInTree implements TreeSelectionListener {
	public void valueChanged(TreeSelectionEvent e) {
		
		// Get the selected node from the tree
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) e.getPath()
				.getLastPathComponent();

		// Check if it is an mp3 file
		if (selected instanceof MP3File) {
			MP3File current = (MP3File) selected;
			if (!current.isParsed() || !current.isCached()){
				current.parse();
			}
			if (current.isID3v2Tag()) {
				Program.getControl().loadMP3File(current);
			} else {
				Program.getControl().updateCurrentlyOpenedMP3File();
				Program.getControl().setCurrentyMP3(null);
				Program.getControl().getMainWindow()
						.setStatus("This is not an MP3 file with ID3v2 tags.");
				Program.getControl().getMainWindow().getEditorPanel()
						.setCover(null);
				Program.getControl().getMainWindow().getEditorPanel()
						.setTitle("broken file");
				Program.getControl().getMainWindow().getEditorPanel()
						.setArtist("broken file");
				Program.getControl().getMainWindow().getEditorPanel()
						.setAlbum("broken file");
				Program.getControl().getMainWindow().getEditorPanel()
						.setYear("broken file");
			}
		}
	}
}
