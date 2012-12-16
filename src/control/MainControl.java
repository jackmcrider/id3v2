package control;

import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import model.MP3File;
import view.MainWindow;

public class MainControl {
	private MainWindow mainWindow;

	// Reference to the currently opened mp3 file
	private MP3File currentlyOpenedMP3File = null;

	// Holds all mp3 files that have been changed
	private LinkedList<MP3File> changedFiles = new LinkedList<MP3File>();

	public MainControl() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mainWindow = new MainWindow();
		mainWindow.setTitle("ID3-Tag Editor");
		setStatus("Everything is fine!");
	}

	/**
	 * Get the main window
	 * 
	 * @return
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Set the status
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.mainWindow.setStatus(status);
	}

	/**
	 * Check if the currently opened mp3 file is in the list of changed files
	 * 
	 * @return
	 */
	public boolean currentlyOpenedMP3FileIsChanged() {
		if (currentlyOpenedMP3File == null)
			return false;

		return changedFiles.contains(currentlyOpenedMP3File);
	}

	/**
	 * Check if the currently opened mp3 file is parsed
	 * 
	 * @return
	 */
	public boolean currentlyOpenedMP3FileIsParsed() {
		if (currentlyOpenedMP3File == null)
			return false;

		return currentlyOpenedMP3File.isParsed();
	}

	/**
	 * Gets a list of changed mp3 files
	 * 
	 * @return
	 */
	public MP3File[] getChangedFiles() {
		return (MP3File[]) changedFiles.toArray();
	}

	/**
	 * Add a file to the list of changed files
	 * 
	 * @param changed
	 */
	public void addChangedFile() {
		if (!currentlyOpenedMP3FileIsChanged()){
			currentlyOpenedMP3File.changed();
			changedFiles.add(currentlyOpenedMP3File);
			mainWindow.getNavigationPanel().updateUI();
		}
	}

	/**
	 * Update the tags of the currently opened mp3 file
	 * 
	 * @param triggeringField
	 */
	public void updateCurrentlyOpenedMP3File() {
		// Update tags when edited
		if (currentlyOpenedMP3FileIsParsed()) {
			currentlyOpenedMP3File.setTitle(mainWindow.getEditorPanel()
					.getTitle());
			currentlyOpenedMP3File.setArtist(mainWindow.getEditorPanel()
					.getArtist());
			currentlyOpenedMP3File.setAlbum(mainWindow.getEditorPanel()
					.getAlbum());
			currentlyOpenedMP3File.setYear(mainWindow.getEditorPanel()
					.getYear());
			if(mainWindow.getEditorPanel().getCover() != null)
			currentlyOpenedMP3File.setCover(mainWindow.getEditorPanel()
					.getCover());
			
			addChangedFile();
			setStatus(currentlyOpenedMP3File + " was changed.");
		}
	}

	/**
	 * Load a file into the editor
	 * 
	 * @param loadFile
	 */
	public void loadMP3File(MP3File loadFile) {
		updateCurrentlyOpenedMP3File();

		currentlyOpenedMP3File = loadFile;

		mainWindow.getEditorPanel().setTitle(currentlyOpenedMP3File.getTitle());
		mainWindow.getEditorPanel().setAlbum(currentlyOpenedMP3File.getAlbum());
		mainWindow.getEditorPanel().setArtist(currentlyOpenedMP3File.getArtist());
		mainWindow.getEditorPanel().setYear(currentlyOpenedMP3File.getYear());
		mainWindow.getEditorPanel().setCover(currentlyOpenedMP3File.getCover());
	}

	/**
	 * Save changed files
	 */
	public void saveChangedFiles() {
		if (changedFiles.size() > 0) {
			for (MP3File changed : changedFiles) {
				changed.write();
				//setStatus("Saved " + changed.getAbsolutePath());
			}

			changedFiles.clear();
			mainWindow.getNavigationPanel().updateUI();
		}else{
			setStatus("Nothing to do!");
		}
	}

	/**
	 * Handle the event that something was selected in the tree
	 * 
	 * @param e
	 */
	public void clickedOnFileInTree(TreeSelectionEvent e) {
		// Get the selected node from the tree
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) e.getPath()
				.getLastPathComponent();

		// Check if it is an mp3 file
		if (selected instanceof MP3File) {
			MP3File current = (MP3File) selected;

			if (!current.isParsed())
				current.parse();

			if (current.isID3v2Tag()) {
				loadMP3File(current);
			} else {
				mainWindow
						.setStatus("This is not an MP3 file with ID3v2 tags.");
				
				// TODO: does not work?!
				current.removeFromParent();
				mainWindow.getNavigationPanel().updateUI();
			}
		}
	}

}
