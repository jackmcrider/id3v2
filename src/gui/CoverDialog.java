package gui;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CoverDialog extends JFrame {
	
	private static final long serialVersionUID = 2263996958694171526L;
	private BufferedImage image;
	private JButton saveButton, cancelButton;
	JLabel cover2 ,cover;

	public CoverDialog(JLabel currCover) {
		cover2 = currCover;
		this.setTitle("Change Cover");
		this.setLayout(new FlowLayout());
		this.getContentPane().add(currCover);
		
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");
		
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = fc.showOpenDialog(this.getContentPane());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			image = null;
			try {
				image = ImageIO.read(file);
				cover = new JLabel();
				cover.setIcon(new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				this.getContentPane().add(cover);
			} catch (IOException ioex) {
				System.exit(1);
			}
			
		}
		this.getContentPane().add(saveButton);
		this.getContentPane().add(cancelButton);
		
	}
	
	public void close(){
		//System.exit(1);
	}
	public BufferedImage getImage(){
		return image;
	}
	
	public JLabel getCover2(){
		return cover;
	}
	public JLabel getCover(){
		return cover2;
	}
	
	public JButton getSaveButton(){
		return saveButton;
	}
	public JButton getCancelButton(){
		return cancelButton;
	}
}
