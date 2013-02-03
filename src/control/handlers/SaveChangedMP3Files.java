package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.Program;

public class SaveChangedMP3Files implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		Program.getControl().saveAll();
	}
}