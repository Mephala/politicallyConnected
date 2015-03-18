package mainApp.utils;

public class AppUtils {

	public static String getResourcePath(String resourceName) {
		return AppUtils.class.getClassLoader().getResource(resourceName).getPath();
	}

}
