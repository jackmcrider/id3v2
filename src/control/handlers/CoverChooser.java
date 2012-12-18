package control.handlers;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.Program;

public class CoverChooser extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
		if (!Program.getControl().currentlyOpenedMP3FileIsParsed())
			return;

		FileFilter filter = new FileNameExtensionFilter("Image", "jpg", "jpeg",
				"png");

		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setApproveButtonText("Choose");
		fc.setDialogTitle("Choose new cover");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(Program.getControl().getMainWindow());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			BufferedImage image;
			ImageIcon icon;

			try {
				image = ImageIO.read(file);
				icon = new ImageIcon(image.getScaledInstance(100, 100,
						Image.SCALE_SMOOTH));

				Program.getControl().getMainWindow().getEditorPanel().setImage(image);
				Program.getControl().getMainWindow().getEditorPanel().setCover(icon);

				Program.getControl().updateCurrentlyOpenedMP3File();
				Program.getControl().addChangedFile();
			} catch (IOException ex) {
				Program.getControl().setStatus(ex.getMessage());
			}
		}
	}

}
