package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.handlers.ApplicationCloser;
import control.handlers.ChangedMP3Tags;
import control.handlers.CoverChooser;
import control.handlers.CoverDeleter;
import control.handlers.SaveChangedMP3Files;

@SuppressWarnings("serial")
/**
 * The right side of the window, the editor panel
 * @author Karl
 *
 */
public class EditorPanel extends JPanel {

	private GridBagLayout editorStructure;

	private JTextField titleField, albumField, artistField, yearField;
	private JLabel titleLabel, albumLabel, artistLabel, yearLabel, cover;
	private JPanel titlePanel, albumPanel, artistPanel, yearPanel, coverPanel,
			buttonsPanel;

	private JButton saveButton, closeButton, deleteButton;

	private BufferedImage image;
	private ImageIcon icon;

	public EditorPanel() {
		//contains  the changed tags
		ChangedMP3Tags addFileToChangedFiles = new ChangedMP3Tags();

		//the panel where the JTextField and JLabel for the title is placed
		this.titlePanel = new JPanel();
		this.titlePanel.setLayout(new GridLayout(2, 0));
		this.titleLabel = new JLabel("Title");
		this.titleField = new JTextField("Title");
		this.titlePanel.add(titleLabel);
		this.titlePanel.add(titleField);
		this.titleField.addKeyListener(addFileToChangedFiles);

		//the panel where the JTextField and JLabel for the album is placed
		this.albumPanel = new JPanel();
		this.albumPanel.setLayout(new GridLayout(2, 0));
		this.albumLabel = new JLabel("Album");
		this.albumField = new JTextField("Album");
		this.albumPanel.add(albumLabel);
		this.albumPanel.add(albumField);
		this.albumField.addKeyListener(addFileToChangedFiles);

		//the panel where the JTextField and JLabel for the artist is placed
		this.artistPanel = new JPanel();
		this.artistPanel.setLayout(new GridLayout(2, 0));
		this.artistLabel = new JLabel("Artist");
		this.artistField = new JTextField("Artist");
		this.artistPanel.add(artistLabel);
		this.artistPanel.add(artistField);
		this.artistField.addKeyListener(addFileToChangedFiles);

		//the panel where the JTextField and JLabel for the year is placed
		this.yearPanel = new JPanel();
		this.yearPanel.setLayout(new GridLayout(2, 0));
		this.yearLabel = new JLabel("Year");
		this.yearField = new JTextField("Year");
		this.yearPanel.add(yearLabel);
		this.yearPanel.add(yearField);
		this.yearField.addKeyListener(addFileToChangedFiles);

		//the panel which contains the two buttons
		this.closeButton = new JButton("Close");
		//logic for clicking on close-Button
		this.closeButton.addActionListener(new ApplicationCloser());
		this.saveButton = new JButton("Save");
		//logic for clicking on the save-Button
		this.saveButton.addActionListener(new SaveChangedMP3Files());
		this.buttonsPanel = new JPanel();
		this.buttonsPanel.setLayout(new FlowLayout());
		this.saveButton.setSize(new Dimension(100, 40));
		this.closeButton.setSize(new Dimension(100, 40));
		this.buttonsPanel.add(this.saveButton);
		this.buttonsPanel.add(this.closeButton);

		//the coverpanel with the cover an the button to delete the cover
		this.coverPanel = new JPanel();
		this.coverPanel.setLayout(new FlowLayout());
		this.cover = new JLabel();
		this.cover.setBorder(BorderFactory.createLineBorder(Color.black));
		this.cover.addMouseListener(new CoverChooser());
		this.deleteButton = new JButton("x");
		this.deleteButton.addActionListener(new CoverDeleter());
		this.cover.setPreferredSize(new Dimension(100, 100));
		this.coverPanel.add(cover);
		this.coverPanel.add(this.deleteButton);

		this.editorStructure = new GridBagLayout();

		this.setLayout(this.editorStructure);

		addComponent(this, this.editorStructure, this.titlePanel, 0, 0, 2, 1,
				1.0, 0);
		addComponent(this, this.editorStructure, this.artistPanel, 0, 1, 1, 1,
				0.5, 0);
		addComponent(this, this.editorStructure, this.albumPanel, 1, 1, 1, 1,
				0.5, 0);
		addComponent(this, this.editorStructure, this.yearPanel, 1, 2, 1, 1,
				0.5, 0);
		addComponent(this, this.editorStructure, this.buttonsPanel, 1, 3, 1, 1,
				0.5, 1);
		addComponent(this, this.editorStructure, this.coverPanel, 0, 2, 1, 3,
				0, 0);
	}

	//sets Cover by a given BufferedImage
	public void setImage(BufferedImage newImage) {
		image = newImage;
		repaintCover();
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

	//is called every time when the mp3 or the cover is changed
	public void repaintCover() {
		int minSpace = (artistPanel.getSize().width < buttonsPanel.getSize().height) ? artistPanel
				.getSize().width : buttonsPanel.getSize().height;

		coverPanel.removeAll();
		if (image != null) {
			icon = new ImageIcon(image.getScaledInstance(minSpace, minSpace,
					Image.SCALE_SMOOTH));
			cover = new JLabel(icon);
			cover.setPreferredSize(new Dimension(100, 100));
			cover.addMouseListener(new CoverChooser());
			cover.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		coverPanel.add(cover);
		coverPanel.add(this.deleteButton);
	}


	//sets the value of the title-JTextField
	public void setTitle(String s) {
		titleField.setText(s);
	}

	//sets the value of the album-JTextField
	public void setAlbum(String s) {
		albumField.setText(s);
	}

	//sets the value of the artist-JTextField
	public void setArtist(String s) {
		artistField.setText(s);
	}

	//sets the value of the year-JTextField
	public void setYear(String s) {
		yearField.setText(s);
	}

	//changes the shown cover on the panel
	public void setCover(ImageIcon i) {
		if (i != null) {
			cover.setIcon(new ImageIcon(i.getImage().getScaledInstance(100,
					100, Image.SCALE_SMOOTH)));
			icon = i;
		} else {
			icon = null;
			cover.setIcon(icon);
			this.repaint();
		}
	}

	//returns the cover shown on the panel
	public ImageIcon getCover() {
		return this.icon;
	}

	//return a String with title
	public String getTitle() {
		return titleField.getText();
	}
	//return a String with album
	public String getAlbum() {
		return albumField.getText();
	}

	//return a String with artist
	public String getArtist() {
		return artistField.getText();
	}

	//return a String with year
	public String getYear() {
		return yearField.getText();
	}

	public Point getFrameLoc() {
		return this.getLocationOnScreen();
	}

	public int getFrameX() {
		return this.getWidth();
	}

	public int getFrameY() {
		return this.getHeight();
	}
}
