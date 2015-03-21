package mainApp.utils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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

	public static boolean isCollectionEmpty(Collection collection) {
		boolean isEmpty = true;
		if (collection != null) {
			Iterator iterator = collection.iterator();
			if (iterator.hasNext())
				isEmpty = false;
		}
		return isEmpty;
	}

	public static boolean fileCreationSuccess(File file) {
		boolean isSuccess = false;
		if (file != null)
			isSuccess = file.length() > 0;
		return isSuccess;
	}

}
