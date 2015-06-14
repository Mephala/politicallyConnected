package mainApp.manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.PoliticalJob;
import mainApp.utils.MainAppUtils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;

public class ReadFromExcel {

	private final String documentRootFolder;
	private final Map<String, List<Manager>> personFirstReadMap;
	private final Map<String, List<Manager>> personFirstPoliticalMap;

	public ReadFromExcel(String documentRootFolder) {
		super();
		this.documentRootFolder = documentRootFolder;
		this.personFirstReadMap = new HashMap<String, List<Manager>>();
		this.personFirstPoliticalMap = new HashMap<String, List<Manager>>();
	}

	public void readForCompany(String documentName) throws Exception {
		File document = validateAndGetFile(documentName);
		final String year = documentName.replace(".xls", "");
		Map<String, Manager> nameToManagerMap = new HashMap<String, Manager>();

		FileInputStream fis = new FileInputStream(document);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		readToMap(year, nameToManagerMap, rowIterator);
		List<Manager> readManagers = new ArrayList<Manager>();
		Set<String> managerName = nameToManagerMap.keySet();
		for (String mName : managerName) {
			readManagers.add(nameToManagerMap.get(mName));
		}
		personFirstReadMap.put(documentName, readManagers);
		workbook.close();
	}

	private File validateAndGetFile(String documentName) throws Exception {
		if (documentName == null)
			throw new Exception("Document name is null.");
		File document = new File(documentRootFolder + File.separator + documentName);
		if (document.exists() == false)
			throw new Exception("Document doesnt exist.");
		return document;
	}

	private void readToMap(final String year, Map<String, Manager> nameToManagerMap, Iterator<Row> rowIterator) {
		while (rowIterator.hasNext()) {
			ManagementJob mJob = null;
			Row currentRow = rowIterator.next();
			// Get iterator to all cells of current row
			Iterator<Cell> cellIterator = currentRow.cellIterator();
			String companyName = null;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell != null) {
					RichTextString cellString = cell.getRichStringCellValue();
					if (cellString != null) {
						String cellText = cellString.getString();
						if (companyName == null) {
							companyName = cellText;
							mJob = new ManagementJob();
							mJob.setName(companyName);
							mJob.setYear(year);
						} else {
							String managerName = cellText;
							if (managerName == null || managerName.length() == 0)
								continue;
							Manager manager = nameToManagerMap.get(managerName);
							if (manager == null) {
								manager = new Manager();
								manager.setName(managerName);
								Set<ManagementJob> mJobs = new HashSet<ManagementJob>();
								mJobs.add(mJob);
								manager.setJobs(mJobs);
								nameToManagerMap.put(managerName, manager);
							}
							manager.getJobs().add(mJob);
						}
					}
				}
			}
		}
	}

	public Map<String, List<Manager>> getPersonFirstMap() {
		return this.personFirstReadMap;
	}

	public Set<Manager> getReadManagers(String documentName) {
		List<Manager> readManagerList = personFirstReadMap.get(documentName);
		return MainAppUtils.convertListToSet(readManagerList);
	}

	public void readForPolitical(String documentName) throws Exception {
		File document = validateAndGetFile(documentName);
		final String year = documentName.replaceAll(".xls", "");
		FileInputStream fis = new FileInputStream(document);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		readForPoliticalJobs(year, rowIterator);

	}

	private void readForPoliticalJobs(final String year, Iterator<Row> rowIterator) {
		List<Manager> readManagers = new ArrayList<Manager>();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			Manager manager = null;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String cellString = richTextString.getString();
					if (manager == null) {
						manager = new Manager();
						manager.setName(cellString);
						Set<PoliticalJob> pjobs = new HashSet<PoliticalJob>();
						manager.setpJobs(pjobs);
					} else if (manager.getpJobs().size() == 1) {
						continue;
					} else {
						PoliticalJob pJob = new PoliticalJob(cellString, year);
						if (manager != null) {
							manager.getpJobs().add(pJob);
						}
					}
				}
			}
			if (manager != null)
				readManagers.add(manager);
		}
		this.personFirstPoliticalMap.put(year + ".xls", readManagers);
	}

	public Set<Manager> getReadPoliticians(String documentName) {
		List<Manager> politicalManagers = this.personFirstPoliticalMap.get(documentName);
		return MainAppUtils.convertListToSet(politicalManagers);
	}
}
