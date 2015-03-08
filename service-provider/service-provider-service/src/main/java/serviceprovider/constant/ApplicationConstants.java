package serviceprovider.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationConstants {

	public static final Map<List<Integer>, String> integerDupleToCharMap = new HashMap<List<Integer>, String>();
	static {
		List<Integer> dupleList = new ArrayList<Integer>();
		dupleList.add(196);
		dupleList.add(159);
		integerDupleToCharMap.put(dupleList, "ğ");
		dupleList = new ArrayList<Integer>();
		dupleList.add(196);
		dupleList.add(158);
		integerDupleToCharMap.put(dupleList, "Ğ");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(188);
		integerDupleToCharMap.put(dupleList, "ü");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(156);
		integerDupleToCharMap.put(dupleList, "Ü");
		dupleList = new ArrayList<Integer>();
		dupleList.add(197);
		dupleList.add(159);
		integerDupleToCharMap.put(dupleList, "ş");
		dupleList = new ArrayList<Integer>();
		dupleList.add(197);
		dupleList.add(158);
		integerDupleToCharMap.put(dupleList, "Ş");
		dupleList = new ArrayList<Integer>();
		dupleList.add(196);
		dupleList.add(176);
		integerDupleToCharMap.put(dupleList, "İ");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(182);
		integerDupleToCharMap.put(dupleList, "ö");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(150);
		integerDupleToCharMap.put(dupleList, "Ö");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(167);
		integerDupleToCharMap.put(dupleList, "ç");
		dupleList = new ArrayList<Integer>();
		dupleList.add(195);
		dupleList.add(135);
		integerDupleToCharMap.put(dupleList, "Ç");
		dupleList = new ArrayList<Integer>();
		dupleList.add(196);
		dupleList.add(177);
		integerDupleToCharMap.put(dupleList, "ı");
	}

}
