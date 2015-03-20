package mainApp.utils;

import javax.swing.ImageIcon;

public class MainAppUtils {

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path, String description) {
		if (path != null) {
			java.net.URL imgURL = MainAppUtils.class.getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL, description);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
		return null;
	}

}
