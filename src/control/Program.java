package control;

public class Program {
	private static MainControl mainControl;
	
	private static String workingPath;
	
	public static void main(String[] args) {
		workingPath = System.getProperty("user.dir");
		mainControl = new MainControl();
	}
	
	public static MainControl getControl() {
		return mainControl;
	}
	
	public static String getPath() {
		return workingPath;
	}

}