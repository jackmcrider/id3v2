package control;

import javax.swing.UIManager;

import view.MainWindow;

public class MainControl {
	private MainWindow window;

	public MainControl() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainWindow window = new MainWindow();
		window.setTitle("ID3-Tag Editor");
	}
}
