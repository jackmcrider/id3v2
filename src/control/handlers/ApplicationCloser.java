package control.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ApplicationCloser implements ActionListener {
	private static int clicked;

	public void actionPerformed(ActionEvent e) {
		clicked = JOptionPane
				.showConfirmDialog(
						null,
						"You are about to close the application. You'll lose unsaved changes!",
						"Closing application", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);

		if (clicked == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}
}