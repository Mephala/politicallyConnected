package serviceprovider.util;

import java.io.InputStream;

public class SPSFileUtils {

	public static InputStream getFileAsInputStream(String fileName) {
		return SPSFileUtils.class.getClassLoader().getResourceAsStream(fileName);
	}

}
