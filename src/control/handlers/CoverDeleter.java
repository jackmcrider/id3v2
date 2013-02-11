package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.Program;

/**
 * Adds functionality to remove a cover from a file
 * @author Karl
 *
 */
public class CoverDeleter implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Program.getControl().getCurrentlyOpenedMP3File() == null)
			return;

		if (!Program.getControl().currentlyOpenedMP3FileIsParsed()
				|| Program.getControl().getCurrentlyOpenedMP3File().isCached())
			Program.getControl().getCurrentlyOpenedMP3File().parse();
		Program.getControl().getMainWindow().getEditorPanel().setCover(null);
		Program.getControl().updateCurrentlyOpenedMP3File();
		Program.getControl().addChangedFile();
	}

}
