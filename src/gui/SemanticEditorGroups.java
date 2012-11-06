package gui;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SemanticEditorGroups {
	private ArrayList<JPanel> SemanticGroups;
	
	public void addGroup(String label) {
		JPanel ID3Title = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel ID3TitleLabel = new JLabel(label);
		JTextField ID3TitleField = new JTextField(50);
		ID3Title.add(ID3TitleLabel);
		ID3Title.add(ID3TitleField);
	}
}
