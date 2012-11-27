package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.MP3File;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

	private GridBagLayout editorStructure;

	private JTextField titleField, albumField, artistField, yearField;
	private JLabel titleLabel, albumLabel, artistLabel, jahrLabel, cover;
	private JPanel titlePanel, albumPanel, artistPanel, jahrPanel, coverPanel,
			buttonsPanel;

	private JButton saveButton, closeButton;

	private File imageFile;
	private BufferedImage image;
	private ImageIcon icon;

	private MP3File currentlyOpenedMP3File = null;

	public EditorPanel() {

		this.titlePanel = new JPanel();
		this.titlePanel.setLayout(new GridLayout(2, 0));
		this.titleLabel = new JLabel("Titel");
		this.titleField = new JTextField("Titel");
		this.titlePanel.add(titleLabel);
		this.titlePanel.add(titleField);

		this.albumPanel = new JPanel();
		this.albumPanel.setLayout(new GridLayout(2, 0));
		this.albumLabel = new JLabel("Album");
		this.albumField = new JTextField("Album");
		this.albumPanel.add(albumLabel);
		this.albumPanel.add(albumField);

		this.artistPanel = new JPanel();
		this.artistPanel.setLayout(new GridLayout(2, 0));
		this.artistLabel = new JLabel("Künstler");
		this.artistField = new JTextField("Künstler");
		this.artistPanel.add(artistLabel);
		this.artistPanel.add(artistField);

		this.jahrPanel = new JPanel();
		this.jahrPanel.setLayout(new GridLayout(2, 0));
		this.jahrLabel = new JLabel("Jahr");
		this.yearField = new JTextField("Jahr");
		this.jahrPanel.add(jahrLabel);
		this.jahrPanel.add(yearField);

		this.closeButton = new JButton("Schließen");
		this.closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.saveButton = new JButton("Speichern");
		this.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("noe saving bro");
			}
		});
		
		this.buttonsPanel = new JPanel();
		this.buttonsPanel.setLayout(new FlowLayout());
		this.saveButton.setSize(new Dimension(100, 40));
		this.closeButton.setSize(new Dimension(100, 40));
		this.buttonsPanel.add(this.saveButton);
		this.buttonsPanel.add(this.closeButton);

		this.editorStructure = new GridBagLayout();

		this.setLayout(this.editorStructure);

		addComponent(this, this.editorStructure, this.titlePanel, 0, 0, 2, 1, 1.0, 0);
		addComponent(this, this.editorStructure, this.artistPanel, 0, 1, 1, 1, 0.5, 0);
		addComponent(this, this.editorStructure, this.albumPanel, 1, 1, 1, 1, 0.5, 0);
		addComponent(this, this.editorStructure, this.jahrPanel, 1, 2, 1, 1, 0.5, 0);
		addComponent(this, this.editorStructure, this.buttonsPanel, 1, 3, 1, 1, 0.5, 1);
	}

	private void addComponent(Container cont, GridBagLayout gbl, Component c,
			int x, int y, int width, int height, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	public void addCover() {
		coverPanel = new JPanel();
		coverPanel.setLayout(new GridLayout(1, 1));
		imageFile = new File("testbild/bild.jpg");
		image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException ioex) {
			System.exit(1);
		}

		int minSpace = (artistPanel.getSize().width < buttonsPanel.getSize().height) ? artistPanel
				.getSize().width : buttonsPanel.getSize().height;
		icon = new ImageIcon(image.getScaledInstance(minSpace, minSpace,
				Image.SCALE_SMOOTH));
		cover = new JLabel(icon);
		coverPanel.addMouseListener(new mListener());
		cover.setPreferredSize(new Dimension(10, 10));
		coverPanel.add(cover);
		addComponent(this, editorStructure, coverPanel, 0, 2, 1, 3, 0, 0);
	}

	public void load(MP3File n) {
		if (this.currentlyOpenedMP3File != null) {
			this.currentlyOpenedMP3File.setTitle(this.getTitle());
			this.currentlyOpenedMP3File.setAlbum(this.getAlbum());
			this.currentlyOpenedMP3File.setArtist(this.getArtist());
			this.currentlyOpenedMP3File.setYear(this.getYear());
		}

		this.currentlyOpenedMP3File = n;
		this.setTitle(n.getTitle());
		this.setAlbum(n.getAlbum());
		this.setArtist(n.getArtist());
		this.setYear(n.getYear());
		// TODO: n.getCover()
	}

	public void repaintCover() {
		// get the min space for cover, set it as size
		int minSpace = (artistPanel.getSize().width < buttonsPanel.getSize().height) ? artistPanel
				.getSize().width : buttonsPanel.getSize().height;

		coverPanel.removeAll();
		icon = new ImageIcon(image.getScaledInstance(minSpace, minSpace,
				Image.SCALE_SMOOTH));
		cover = new JLabel(icon);
		cover.setPreferredSize(new Dimension(10, 10));
		coverPanel.add(cover);
	}

	private class mListener implements MouseListener {
		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			CoverDialog dialog = new CoverDialog();
			dialog.setVisible(true);
		}
	}

	public JLabel getCover() {
		return cover;
	}

	public void setTitle(String s) {
		titleField.setText(s);
	}

	public void setAlbum(String s) {
		albumField.setText(s);
	}

	public void setArtist(String s) {
		artistField.setText(s);
	}

	public void setYear(String s) {
		yearField.setText(s);
	}

	public String getTitle() {
		return titleField.getText();
	}

	public String getAlbum() {
		return albumField.getText();
	}

	public String getArtist() {
		return artistField.getText();
	}

	public String getYear() {
		return yearField.getText();
	}

}
