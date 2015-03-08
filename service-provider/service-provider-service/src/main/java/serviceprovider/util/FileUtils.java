package serviceprovider.util;

import java.io.InputStream;

public class FileUtils {

	public static InputStream getFileAsInputStream(String fileName) {
		return FileUtils.class.getClassLoader().getResourceAsStream(fileName);
	}

}
