package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import control.Program;

/**
 * Handles the pressing of the close button
 * 
 * @author Karl
 * 
 */
public class ApplicationCloser implements ActionListener {
	private static int clicked;

	/**
	 * Handles the event
	 */
	public void actionPerformed(ActionEvent e) {
		if (Program.getControl().changedFiles()) {
			clicked = JOptionPane.showConfirmDialog(null,
					"Do you want to save before closing the application?",
					"Closing application", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (clicked == JOptionPane.YES_OPTION) {
				Program.getControl().saveAll();
				System.exit(0);
			} else if (clicked == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}

	}
}