package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class EditorPanel extends JPanel {
	
	private GridLayout ediStructure;

	public EditorPanel(){
		
		ediStructure = new GridLayout(5, 1, 5, 5);
		setLayout(ediStructure);
		
	}
}
