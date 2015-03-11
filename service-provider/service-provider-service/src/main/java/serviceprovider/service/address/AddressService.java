package serviceprovider.service.address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.CityDto;
import serviceprovider.util.HibernateUtil;

public class AddressService {

	private static AddressService instance;
	private static final String PTT_LIST_XLS_NAME = "pk_list_20.02.2015.xlsx";
	private Log logger = LogFactory.getLog(getClass());
	private List<City> turkishCities;

	private AddressService() {
		logger.info("Creating address service for the first time.");
		turkishCities = new ArrayList<>();
		try {
			logger.info("Reading city list from database...");
			List<City> persistedCities = getAllCitiesFromDB();
			if (CollectionUtils.isEmpty(persistedCities)) {
				logger.info("No persisted city information has been found! Creating DB records for the cities information from PTT documents...");
				List<City> createdCities = readAndFillValuesFromExcelFile(PTT_LIST_XLS_NAME);
				logger.info("Saving cities to database...");
				saveCitiesToDB(createdCities);
				turkishCities = createdCities;
			} else {
				turkishCities = persistedCities;
			}
		} catch (Exception ioe) {
			logger.fatal("Address Service could not be initialized. Addressing services will not work !!!!!! Immediate fix is required. ", ioe);
		}

	}

	public List<City> getTurkishCitiesList() {
		// Creating defensive copy of items to ensure immutability.
		logger.info("Returning cloned cities list.");
		long start = System.currentTimeMillis();
		List<City> cloneCities = new ArrayList<>();
		for (City city : turkishCities) {
			cloneCities.add(city.clone());
		}
		long end = System.currentTimeMillis();
		long differ = end - start;
		logger.info("Cities are cloned in " + differ + " miliseconds.");
		return cloneCities;
	}

	private List<City> getAllCitiesFromDB() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(City.class);
		List<City> persistedCities = criteria.list();
		initializePersistedData(persistedCities);
		session.getTransaction().commit();
		session.close();
		return persistedCities;
	}

	private void initializePersistedData(List<City> persistedCities) {
		for (City city : persistedCities) {
			Set<Ilce> ilceSet = city.getIlceSet();
			for (Ilce ilce : ilceSet) {
				Set<Semt> semtSet = ilce.getSemtSet();
				for (Semt semt : semtSet) {
					Set<Mahalle> mahalleSet = semt.getMahalleSet();
					for (Mahalle mahalle : mahalleSet) {
						Hibernate.initialize(mahalle);
					}
				}
			}
		}
	}

	private void saveCitiesToDB(List<City> createdCities) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		for (City city : createdCities) {
			logger.info("Saving city:" + city);
			session.save(city);
			logger.info("Saving city is successfull. City:" + city);
		}
		logger.info("Committing all saved city-ilce-semt-mahalle information...");
		session.getTransaction().commit();
		session.close();
		logger.info("Commit is successfull. Session is terminated.");
	}

	private List<City> readAndFillValuesFromExcelFile(String excelFileName) throws IOException, InvalidFormatException {
		logger.info("Reading excel file to create city-ilce-semt-mahalle data structure.");
		Workbook wb = WorkbookFactory.create(AddressService.class.getClassLoader().getResourceAsStream(excelFileName));
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
			logger.info("Processing data: " + cityName + "," + ilceName + "," + semtName + "," + mahalleName);
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
					savedCity.addIlce(ilce);
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
		logger.info("Data structure is ready to return read city values. Total number of cities:" + createdCities.size());
		return createdCities;
	}

	public static synchronized AddressService getInstance() {
		if (instance == null)
			instance = new AddressService();
		return instance;
	}

	public List<CityDto> getTurkishCitiesAsDto() {
		logger.info("Converting city list to Dto for transfer.");
		List<CityDto> cityDtoList = new ArrayList<>();
		List<City> cities = getTurkishCitiesList();
		for (City city : cities) {
			cityDtoList.add(city.convertToDto());
		}
		logger.info("CityDto list is ready to be returned.");
		return cityDtoList;
	}

}
