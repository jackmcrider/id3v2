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
		Program.getControl().updateCurrentlyOpenedMP3File();
		Program.getControl().addChangedFile();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}