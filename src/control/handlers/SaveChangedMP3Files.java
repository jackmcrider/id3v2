package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;

import model.XMLWriter;
import control.Program;

public class SaveChangedMP3Files implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		Program.getControl().saveChangedFiles();
		
		@SuppressWarnings("unused")
		XMLWriter xw = new XMLWriter((DefaultMutableTreeNode) Program
				.getControl().getMainWindow().getNavigationPanel().getRoot());
	}
}