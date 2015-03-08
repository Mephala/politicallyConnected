package serviceprovider.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import serviceprovider.constant.ApplicationConstants;

public class ServiceStringUtils extends StringUtils {

	public static String normalizeInputStringFromHtml(String s) {
		String retVal = s;
		if (s != null) {
			List<Integer> charArray = new ArrayList();
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				int hex = c;
				charArray.add(hex);
			}
			for (int i = 0; i < charArray.size() - 1; i++) {
				List<Integer> tmpList = new ArrayList<Integer>();
				tmpList.add(charArray.get(i));
				tmpList.add(charArray.get(i + 1));
				String convertedString = ApplicationConstants.integerDupleToCharMap.get(tmpList);
				if (convertedString != null) {
					String prev = "";
					String next = "";
					if (i > 0) {
						prev = s.substring(0, i);
					}
					if (i < s.length() - 2) {
						next = s.substring(i + 2);
					}
					return normalizeInputStringFromHtml(prev + convertedString + next);
				}
			}
		}
		return retVal;
	}

}
