package control;

import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import model.MP3File;
import model.XMLWriter;
import view.MainWindow;

/**
 * Holds all relevant references to needed classes
 * 
 * @author Karl
 * 
 */
public class MainControl {
	// Reference to the main window
	private MainWindow mainWindow;

	// Reference to the currently opened mp3 file
	private MP3File currentlyOpenedMP3File = null;

	// Holds all mp3 files that have been changed
	private LinkedList<MP3File> changedFiles = new LinkedList<MP3File>();

	/**
	 * Create a new main control
	 */
	public MainControl() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mainWindow = new MainWindow();
		mainWindow.setTitle("ID3-Tag Editor");
		@SuppressWarnings("unused")
		XMLWriter xw = new XMLWriter((DefaultMutableTreeNode) mainWindow
				.getNavigationPanel().getRoot());
		setStatus("Everything is fine!");
	}

	/**
	 * Get the currently opened mp3 file
	 * 
	 * @return
	 */
	public MP3File getCurrentlyOpenedMP3File() {
		return this.currentlyOpenedMP3File;
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

		return currentlyOpenedMP3File.isParsed()
				|| currentlyOpenedMP3File.isCached();
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
	 * Check if there are changed files
	 * 
	 * @return
	 */
	public boolean changedFiles() {
		if (this.changedFiles.size() > 0)
			return true;

		return false;
	}

	/**
	 * Add a file to the list of changed files
	 * 
	 * @param changed
	 */
	public void addChangedFile() {
		if (!currentlyOpenedMP3FileIsChanged()) {
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
		if (currentlyOpenedMP3FileIsParsed()) {
			if (mainWindow.getEditorPanel().getTitle().length() == 0)
				currentlyOpenedMP3File.setTitle(" ");
			else
				currentlyOpenedMP3File.setTitle(mainWindow.getEditorPanel()
						.getTitle());
			if (mainWindow.getEditorPanel().getArtist().length() == 0)
				currentlyOpenedMP3File.setArtist(" ");
			else
				currentlyOpenedMP3File.setArtist(mainWindow.getEditorPanel()
						.getArtist());
			if (mainWindow.getEditorPanel().getAlbum().length() == 0)
				currentlyOpenedMP3File.setAlbum(" ");
			else
				currentlyOpenedMP3File.setAlbum(mainWindow.getEditorPanel()
						.getAlbum());
			if (mainWindow.getEditorPanel().getYear().length() == 0)
				currentlyOpenedMP3File.setYear(" ");
			else
				currentlyOpenedMP3File.setYear(mainWindow.getEditorPanel()
						.getYear());
			if (mainWindow.getEditorPanel().getCover() != null) {
				currentlyOpenedMP3File.setCover(mainWindow.getEditorPanel()
						.getCover());
			} else {
				currentlyOpenedMP3File.setHasCover(false);
			}

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
		setStatus(currentlyOpenedMP3File + " is loaded");
		mainWindow.getEditorPanel().setTitle(currentlyOpenedMP3File.getTitle());
		mainWindow.getEditorPanel().setAlbum(currentlyOpenedMP3File.getAlbum());
		mainWindow.getEditorPanel().setArtist(
				currentlyOpenedMP3File.getArtist());
		mainWindow.getEditorPanel().setYear(currentlyOpenedMP3File.getYear());
		if (currentlyOpenedMP3File.hasCover())
			mainWindow.getEditorPanel().setCover(
					currentlyOpenedMP3File.getCover());
		else
			mainWindow.getEditorPanel().setCover(null);

	}

	/**
	 * Save changed files
	 */
	public void saveChangedFiles() {
		if (changedFiles.size() > 0) {
			for (MP3File changed : changedFiles) {
				changed.write();
				// setStatus("Saved " + changed.getAbsolutePath());
			}

			changedFiles.clear();
			mainWindow.getNavigationPanel().updateUI();
		} else {
			setStatus("Nothing to do!");
		}
	}

	/**
	 * Save changed files and write xml
	 */
	public void saveAll() {
		Program.getControl().saveChangedFiles();

		@SuppressWarnings("unused")
		XMLWriter xw = new XMLWriter((DefaultMutableTreeNode) mainWindow
				.getNavigationPanel().getRoot());
	}

}
