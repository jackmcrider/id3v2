package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import control.Program;

public class DirectoryChooser implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Initialize file chooser
		final JFileChooser fc = new JFileChooser();

		// Show only directories
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Choose a directory
		int returnVal = fc.showOpenDialog(Program.getControl().getMainWindow().getNavigationPanel());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fc.getSelectedFile();
			Program.getControl().getMainWindow().getNavigationPanel().replaceTree(file.getAbsolutePath());
		}
	}

}
