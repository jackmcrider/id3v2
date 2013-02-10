package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.Program;

/**
 * This class handles the event when someone presses the save button
 * 
 * @author Karl
 * 
 */
public class SaveChangedMP3Files implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		Program.getControl().saveAll();
	}
}