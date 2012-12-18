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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.handlers.ChangedMP3Tags;
import control.handlers.CoverChooser;
import control.handlers.SaveChangedMP3Files;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

	private GridBagLayout editorStructure;

	private JTextField titleField, albumField, artistField, yearField;
	private JLabel titleLabel, albumLabel, artistLabel, yearLabel, cover;
	private JPanel titlePanel, albumPanel, artistPanel, yearPanel, coverPanel,
			buttonsPanel;

	private JButton saveButton, closeButton;

	private BufferedImage image;
	private ImageIcon icon;

	public EditorPanel() {
		ChangedMP3Tags addFileToChangedFiles = new ChangedMP3Tags();

		this.titlePanel = new JPanel();
		this.titlePanel.setLayout(new GridLayout(2, 0));
		this.titleLabel = new JLabel("Title");
		this.titleField = new JTextField("Title");
		this.titlePanel.add(titleLabel);
		this.titlePanel.add(titleField);
		this.titleField.addKeyListener(addFileToChangedFiles);

		this.albumPanel = new JPanel();
		this.albumPanel.setLayout(new GridLayout(2, 0));
		this.albumLabel = new JLabel("Album");
		this.albumField = new JTextField("Album");
		this.albumPanel.add(albumLabel);
		this.albumPanel.add(albumField);
		this.albumField.addKeyListener(addFileToChangedFiles);

		this.artistPanel = new JPanel();
		this.artistPanel.setLayout(new GridLayout(2, 0));
		this.artistLabel = new JLabel("Artist");
		this.artistField = new JTextField("Artist");
		this.artistPanel.add(artistLabel);
		this.artistPanel.add(artistField);
		this.artistField.addKeyListener(addFileToChangedFiles);

		this.yearPanel = new JPanel();
		this.yearPanel.setLayout(new GridLayout(2, 0));
		this.yearLabel = new JLabel("Year");
		this.yearField = new JTextField("Year");
		this.yearPanel.add(yearLabel);
		this.yearPanel.add(yearField);
		this.yearField.addKeyListener(addFileToChangedFiles);

		this.closeButton = new JButton("Close");
		this.closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.saveButton = new JButton("Save");
		this.saveButton.addActionListener(new SaveChangedMP3Files());

		this.buttonsPanel = new JPanel();
		this.buttonsPanel.setLayout(new FlowLayout());
		this.saveButton.setSize(new Dimension(100, 40));
		this.closeButton.setSize(new Dimension(100, 40));
		this.buttonsPanel.add(this.saveButton);
		this.buttonsPanel.add(this.closeButton);

		this.coverPanel = new JPanel();
		this.coverPanel.setLayout(new FlowLayout());
		this.cover = new JLabel();
		this.cover.setBorder(BorderFactory.createLineBorder(Color.black));
		this.cover.addMouseListener(new CoverChooser());

		this.cover.setPreferredSize(new Dimension(100, 100));
		this.coverPanel.add(cover);

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

	public void repaintCover() {
		int minSpace = (artistPanel.getSize().width < buttonsPanel.getSize().height) ? artistPanel
				.getSize().width : buttonsPanel.getSize().height;

		coverPanel.removeAll();
		icon = new ImageIcon(image.getScaledInstance(minSpace, minSpace,
				Image.SCALE_SMOOTH));
		cover = new JLabel(icon);
		cover.setPreferredSize(new Dimension(100, 100));
		coverPanel.add(cover);
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

	public void setCover(ImageIcon i) {
		if (i != null)
			cover.setIcon(i);
	}

	public ImageIcon getCover() {
		return this.icon;
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
