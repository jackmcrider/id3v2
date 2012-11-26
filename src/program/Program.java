package program;

import javax.swing.UIManager;

import gui.MainWindow;

public class Program {
	public MainWindow window;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainWindow window = new MainWindow();
		window.setTitle("ID3-Tag Editor");
	}

}