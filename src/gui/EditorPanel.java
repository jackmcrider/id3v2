package gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
//import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class EditorPanel extends JPanel {
	
	private GridBagLayout ediStructure;
	private JTextField titlef, albumf, artistf, jahrf;
	private JLabel titlel, albuml,artistl,jahrl;
	private GridBagConstraints constr;
	

	public EditorPanel(Container win){
		/*ediStructure = new GridLayout(2, 3, 5, 5);
		setLayout(ediStructure);*/
		titlel = new JLabel("Titel");
		titlef  = new JTextField("Titel");
		
		albuml = new JLabel("Album");
		albumf  = new JTextField("Album");
		
		artistl = new JLabel("Artist");
		artistf = new JTextField("Artist");
		
		jahrl = new JLabel("Penis");
		jahrf   = new JTextField("Jahr");

		ediStructure = new GridBagLayout();
		constr = new GridBagConstraints();
		
		setLayout(ediStructure);
		
		//constr.fill = GridBagConstraints.HORIZONTAL;
		constr.ipadx = 0;
		constr.ipady = 0;
		
		//Labels:
		//-------
		constr.insets = new Insets(10, 10, 10, 10);
		constr.anchor = GridBagConstraints.LAST_LINE_START;
		constr.weightx = 1.0;
		constr.weighty = 0.1;
		constr.gridwidth = 2;
		addC(titlel, 0,0);
		
		constr.gridwidth = 1;
	  addC(albuml, 2, 0);
	  addC(artistl, 2, 1);
	  addC(jahrl, 4, 1);
	  
	  
	  //Felder:
	  //-------
	  constr.fill = GridBagConstraints.HORIZONTAL;
	  constr.anchor = GridBagConstraints.FIRST_LINE_START;
		constr.gridwidth = 2;
		addC(titlef, 1, 0);
		
		constr.gridwidth = 1;
		addC(albumf, 3, 0);
	  addC(artistf, 3, 1);
		
	  //add bild
	  
	  addC(jahrf, 5, 1);
		}
	

	
	private void addC(Container c, int y, int x)
	{
		constr.gridx = x;
		constr.gridy = y;
		this.add(c, constr);
	}
	
	public void refresh(DefaultMutableTreeNode n){
		titlef.setText(n.toString()+"title");
		albumf.setText(n.toString()+"album");
		artistf.setText(n.toString()+"artist");
		jahrf.setText(n.toString()+"jahr");
	}
	
	public void setTitle(String s)
	{
		titlef.setText(s);
	}
	public void setAlbum(String s)
	{
		albumf.setText(s);
	}
	public void setArtist(String s)
	{
		artistf.setText(s);
	}
	public void setJahr(String s)
	{
		jahrf.setText(s);
	}
	
	public String getTitle()
	{
		return titlef.getText();
	}
	public String getAlbum()
	{
		return albumf.getText();
	}
	public String getArtist()
	{
		return artistf.getText();
	}
	public String getJahr()
	{
		return jahrf.getText();
	}
	
}
