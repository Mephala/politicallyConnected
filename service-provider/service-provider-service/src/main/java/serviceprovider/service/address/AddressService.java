package serviceprovider.service.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class AddressService {

	private static AddressService instance;
	private static final String PTT_LIST_XLS_NAME = "pk_list_20.02.2015.xlsx";
	private Log logger = LogFactory.getLog(getClass());

	private AddressService() {
		logger.info("Initializing excel reading process.");
		try {
			Workbook wb = WorkbookFactory.create(AddressService.class.getClassLoader().getResourceAsStream(PTT_LIST_XLS_NAME));
			Sheet s = wb.getSheetAt(0);
			Row r = s.getRow(0);
			int dataIndex = 1;
			Set<String> savedCities = new HashSet<>();
			List<City> createdCities = new ArrayList<>();
			Map<String, City> cityNameToCityMap = new HashMap<>();
			Map<String, Ilce> ilceNameToIlceMap = new HashMap<>();
			Map<String, Semt> semtNameToSemtMap = new HashMap<>();
			r = s.getRow(dataIndex);
			while (r != null) {
				String cityName = r.getCell(0).toString().trim();
				String ilceName = r.getCell(1).toString().trim();
				String semtName = r.getCell(2).toString().trim();
				String mahalleName = r.getCell(3).toString().trim();
				logger.info(cityName + "," + ilceName + "," + semtName + "," + mahalleName);
				if (savedCities.contains(cityName)) {
					City savedCity = cityNameToCityMap.get(cityName);
					if (savedCity.containsIlceWithName(ilceName)) {
						Ilce savedIlce = ilceNameToIlceMap.get(ilceName);
						if (savedIlce.containsSemtWithName(semtName)) {
							Semt savedSemt = semtNameToSemtMap.get(semtName);
							if (savedSemt.containsMahalleWithName(mahalleName)) {
								logger.info("ERROR! Duplicate row! This should never happen?");
							} else {
								Mahalle mahalle = new Mahalle(mahalleName);
								savedSemt.addMahalle(mahalle);
							}
						} else {
							Semt semt = new Semt(semtName);
							semtNameToSemtMap.put(semtName, semt);
							Mahalle mahalle = new Mahalle(mahalleName);
							semt.addMahalle(mahalle);
							savedIlce.addSemt(semt);
						}
					} else {
						Ilce ilce = new Ilce(ilceName);
						ilceNameToIlceMap.put(ilceName, ilce);
						Semt semt = new Semt(semtName);
						semtNameToSemtMap.put(semtName, semt);
						Mahalle mahalle = new Mahalle(mahalleName);
						semt.addMahalle(mahalle);
						ilce.addSemt(semt);
					}
				} else {
					City city = new City(cityName);
					cityNameToCityMap.put(cityName, city);
					savedCities.add(cityName);
					createdCities.add(city);
					Ilce ilce = new Ilce(ilceName);
					ilceNameToIlceMap.put(ilceName, ilce);
					Semt semt = new Semt(semtName);
					semtNameToSemtMap.put(semtName, semt);
					Mahalle mahalle = new Mahalle(mahalleName);
					semt.addMahalle(mahalle);
					ilce.addSemt(semt);
					city.addIlce(ilce);
				}
				dataIndex++;
				r = s.getRow(dataIndex);
			}
			logger.info("Created cities:" + createdCities);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}

	}

	public static synchronized AddressService getInstance() {
		if (instance == null)
			instance = new AddressService();
		return instance;
	}

}
