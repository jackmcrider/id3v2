package control;

public class Program {
	private static MainControl mainControl;
	
	public static void main(String[] args) {
		mainControl = new MainControl();
	}
	
	public static MainControl getControl() {
		return mainControl;
	}

}