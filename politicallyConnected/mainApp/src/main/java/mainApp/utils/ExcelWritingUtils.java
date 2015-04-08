package mainApp.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mainApp.model.ManagementJob;
import mainApp.model.ManagementJobComparator;
import mainApp.model.Manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWritingUtils {

	private final static Log logger = LogFactory.getLog(ExcelWritingUtils.class);

	public static void writePersonFirstExcel(String excelName, String sheetName, List<Manager> sortedManagerList) {
		Workbook wb = new HSSFWorkbook();
		logger.info("Person first excel workbook has been created.");
		// Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet(sheetName);
		logger.info("Sheet of person first excel workbook has been created.");
		int rowCount = 0;
		logger.info("Reading data structure to create person first excel data.");
		for (Manager manager : sortedManagerList) {
			logger.info("Manager data is being created for person first excel data. Manager:" + manager.getName());
			Row row = sheet.createRow((short) rowCount);
			logger.info("Row is created for person first excel data. Manager:" + manager.getName());
			int rowCellCount = 0;
			Cell cell = row.createCell(rowCellCount);
			logger.info("Cell has been initialized for person first excel data. Manager:" + manager.getName());
			cell.setCellValue(createHelper.createRichTextString(manager.getName()));
			logger.info("Cell name has been written according to manager for person first excel data. Manager:" + manager.getName());
			rowCellCount++;
			Set<ManagementJob> managementJobs = manager.getJobs();
			for (ManagementJob managementJob : managementJobs) {
				Cell jobCell = row.createCell(rowCellCount);
				logger.info("Job cell has been initialized for job:" + managementJob.getName() + ", for person first excel data. Manager:" + manager.getName());
				jobCell.setCellValue(createHelper.createRichTextString(managementJob.getName()));
				logger.info("Job cell has been written for job:" + managementJob.getName() + ", for person first excel data. Manager:" + manager.getName());
				rowCellCount++;
			}
			rowCount++;
			logger.info("Manager row has been created for person first excel data. Manager:" + manager.getName());
		}
		logger.info("Data structure has been set to heap memory for person first excel.");
		// Write the output to a file
		FileOutputStream fileOut;
		try {
			logger.info("Writing data to excel file for person first excel");
			fileOut = new FileOutputStream(excelName);
			wb.write(fileOut);
			fileOut.close();
			logger.info("Completed writing person first excel file.");
		} catch (FileNotFoundException e) {
			System.err.println("Error during excel writing. Err:" + e.getMessage());
			logger.error("Error occured during creating person first excel document.", e);
		} catch (IOException e) {
			System.err.println("Error during excel writing. Err:" + e.getMessage());
			logger.error("Error occured during creating person first excel document.", e);
		}
	}

	public static void createCompanyFirstExcel(String excelName, String sheetName, Set<Manager> managerSet) {
		Map<ManagementJob, Set<Manager>> managementJobToManagerSetMap = createManagementJobToManagerMap(managerSet);
		Set<ManagementJob> companies = managementJobToManagerSetMap.keySet();
		List<ManagementJob> sortedCompanies = MainAppUtils.convertSetToSortedList(companies, ManagementJobComparator.COMPARATOR);
		Workbook wb = new HSSFWorkbook();
		// Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet(sheetName);
		int rowCount = 0;
		for (ManagementJob managementJob : sortedCompanies) {
			Row row = sheet.createRow((short) rowCount);
			int rowCellCount = 0;
			Cell cell = row.createCell(rowCellCount);
			cell.setCellValue(createHelper.createRichTextString(managementJob.getName()));
			rowCellCount++;
			Set<Manager> managers = managementJobToManagerSetMap.get(managementJob);
			for (Manager manager : managers) {
				Cell jobCell = row.createCell(rowCellCount);
				jobCell.setCellValue(createHelper.createRichTextString(manager.getName()));
				rowCellCount++;
			}
			rowCount++;
		}
		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(excelName);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error during excel writing. Err:" + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error during excel writing. Err:" + e.getMessage());
		}
	}

	private static Map<ManagementJob, Set<Manager>> createManagementJobToManagerMap(Set<Manager> managerSet) {
		Map<ManagementJob, Set<Manager>> jobToManagerMap = new HashMap<ManagementJob, Set<Manager>>();
		for (Manager manager : managerSet) {
			Set<ManagementJob> managerJobs = manager.getJobs();
			for (ManagementJob managementJob : managerJobs) {
				if (jobToManagerMap.containsKey(managementJob)) {
					Set<Manager> managerSetToJob = jobToManagerMap.get(managementJob);
					managerSetToJob.add(manager);
				} else {
					Set<Manager> managerSetToJob = new HashSet<Manager>();
					managerSetToJob.add(manager);
					jobToManagerMap.put(managementJob, managerSetToJob);
				}
			}
		}
		return jobToManagerMap;
	}

}
