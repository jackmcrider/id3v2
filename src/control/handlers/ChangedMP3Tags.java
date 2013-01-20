package control.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import control.Program;

public class ChangedMP3Tags implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!Program.getControl().getCurrentlyOpenedMP3File().isParsed())
			Program.getControl().getCurrentlyOpenedMP3File().parse();
		
		Program.getControl().updateCurrentlyOpenedMP3File();
		Program.getControl().addChangedFile();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}