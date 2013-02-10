package control;

/**
 * Holds static references to the control and to the program path for
 * convenience
 * 
 * @author Karl
 * 
 */
public class Program {
	// Reference to the main control
	private static MainControl mainControl;

	// Reference to the working path
	private static String workingPath;

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		workingPath = System.getProperty("user.dir");
		mainControl = new MainControl();
	}

	/**
	 * Get the main control
	 * 
	 * @return
	 */
	public static MainControl getControl() {
		return mainControl;
	}

	/**
	 * Get the working path
	 * 
	 * @return
	 */
	public static String getPath() {
		return workingPath;
	}

}