package control.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import control.Program;

/**
 * This KeyListener is for detecting changes on a mp3-file and starts parsing.
 * @author Karl
 *
 */
public class ChangedMP3Tags implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Handles the event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (!Program.getControl().getCurrentlyOpenedMP3File().isParsed())
			Program.getControl().getCurrentlyOpenedMP3File().parse();

		Program.getControl().updateCurrentlyOpenedMP3File();
		Program.getControl().addChangedFile();
		Program.getControl().setStatus(
				Program.getControl().getCurrentlyOpenedMP3File()
						+ " is changed.");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}