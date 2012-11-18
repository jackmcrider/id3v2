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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;


public class EditorPanel extends JPanel {

	private GridBagLayout ediStructure;
	private JTextField titlef, albumf, artistf, jahrf;
	private JLabel titlel, albuml, artistl, jahrl, cover;
	private JPanel titlep, albump, artistp, jahrp, coverp, buttonsp;
	private JTextArea infoArea;
	private JButton saveButton, closeButton;
	private GridBagConstraints constr;
	
	private File imageFile;
	private BufferedImage image;
	private ImageIcon icon;

	/*
	 * EditorPanel is the ID3v2-Editor, the right side of the window.
	 * It has a big gridlayout with jpanels for every field in it
	 * the cover is an ImageIcon 
	 */
	public EditorPanel() {

		titlep = new JPanel();
		titlep.setLayout(new GridLayout(2, 0));
		titlel = new JLabel("Titel");
		titlef = new JTextField("Titel");
		titlep.add(titlel);
		titlep.add(titlef);

		albump = new JPanel();
		albump.setLayout(new GridLayout(2, 0));
		albuml = new JLabel("Album");
		albumf = new JTextField("Album");
		albump.add(albuml);
		albump.add(albumf);

		artistp = new JPanel();
		artistp.setLayout(new GridLayout(2, 0));
		artistl = new JLabel("Artist");
		artistf = new JTextField("Artist");
		artistp.add(artistl);
		artistp.add(artistf);

		jahrp = new JPanel();
		jahrp.setLayout(new GridLayout(2, 0));
		jahrl = new JLabel("Jahr");
		jahrf = new JTextField("Jahr");
		jahrp.add(jahrl);
		jahrp.add(jahrf);

		
		closeButton = new JButton("Close");
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("noe saving bro");
			}
		});
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonsp = new JPanel();
		GridBagLayout bgl = new GridBagLayout();
		buttonsp.setLayout(new FlowLayout());
		saveButton.setSize(new Dimension(100,40));
		closeButton.setSize(new Dimension(100,40));
		buttonsp.add(saveButton);
		buttonsp.add(closeButton);
		

		ediStructure = new GridBagLayout();
		constr = new GridBagConstraints();

		setLayout(ediStructure);

		addComponent(this, ediStructure, titlep, 0, 0, 2, 1, 1.0, 0);
		addComponent(this, ediStructure, artistp, 0, 1, 1, 1, 0.5, 0);
		addComponent(this, ediStructure, albump, 1, 1, 1, 1, 0.5, 0);
		addComponent(this, ediStructure, jahrp, 1, 2, 1, 1, 0.5, 0);
		addComponent(this, ediStructure, buttonsp, 1, 3, 1, 1, 0.5, 1);
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
		coverp = new JPanel();
		coverp.setLayout(new GridLayout(1, 1));
		imageFile = new File("testbild/bild.jpg");
		image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException ioex) {
			System.exit(1);
		}
		
		int minSpace = (artistp.getSize().width < buttonsp.getSize().height) ? artistp.getSize().width : buttonsp.getSize().height;
		icon = new ImageIcon(image.getScaledInstance(minSpace,
				minSpace, Image.SCALE_SMOOTH));
		cover = new JLabel(icon);
		coverp.addMouseListener(new mListener());
		cover.setPreferredSize(new Dimension(10, 10));
		coverp.add(cover);
		addComponent(this, ediStructure, coverp, 0, 2, 1, 3, 0, 0);
	}



	public void refresh(DefaultMutableTreeNode n) {

		titlef.setText(n.toString() + "title");
		albumf.setText(n.toString() + "album");
		artistf.setText(n.toString() + "artist");
		jahrf.setText(n.toString() + "jahr");
		infoArea.setText(n.toString() + "info");
		//setCover
	}
	public void repaintCover() {
		// get the min space for cover, set it as size
		int minSpace = (artistp.getSize().width < buttonsp.getSize().height) ? artistp.getSize().width : buttonsp.getSize().height;
		
		coverp.removeAll();
		icon = new ImageIcon(image.getScaledInstance(minSpace, minSpace, Image.SCALE_SMOOTH));
		cover = new JLabel(icon);
		cover.setPreferredSize(new Dimension(10, 10));
		coverp.add(cover);
	}
	
	private class mListener implements MouseListener{
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
		titlef.setText(s);
	}

	public void setAlbum(String s) {
		albumf.setText(s);
	}

	public void setArtist(String s) {
		artistf.setText(s);
	}

	public void setJahr(String s) {
		jahrf.setText(s);
	}

	public String getTitle() {
		return titlef.getText();
	}

	public String getAlbum() {
		return albumf.getText();
	}

	public String getArtist() {
		return artistf.getText();
	}

	public String getJahr() {
		return jahrf.getText();
	}

}
