package mainApp.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mainApp.model.CloudToExcelModel;
import mainApp.model.Manager;
import mainApp.utils.ExcelWritingUtils;
import mainApp.utils.MainAppUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
import service.provider.common.CommonUtils;
import service.provider.common.core.RequestApplication;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.request.GetAllPcaDataRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.response.GetAllPcaDataResponseDto;

public class CloudToExcelManager {

	private static CloudToExcelManager instance;
	private final Log logger = LogFactory.getLog(getClass());
	private final String companyFirstExcelName = "CompanyFirst(ALLDATA).xls";
	private final String companyFirstSheetName = companyFirstExcelName;
	private final String personFirstExcelName = "PersonFirst(ALLDATA).xls";
	private final String personFirstSheetName = personFirstExcelName;
	private final CloudManager cloudManager = CloudManager.getInstance();

	private CloudToExcelManager() {
		logger.info("Cloud to excel manager has been initialized.");
	}

	public static synchronized CloudToExcelManager getInstance() {
		if (instance == null)
			instance = new CloudToExcelManager();
		return instance;
	}

	public void writeExcelFileFromCloud(CloudToExcelModel c2eModel) {
		logger.info("Getting cloud data for creating Excel files.");
		long start = System.currentTimeMillis();
		GetAllPcaDataRequestDto request = RequestDtoFactory.createGetAllPcaDataRequestDto(RequestApplication.PCA);
		GetAllPcaDataResponseDto response = ServiceClient.getAllPcaDataDto(request);
		logger.info("All cloud data has been received after " + (System.currentTimeMillis() - start) + " ,ms.");
		List<PcaPersonDto> pcaPersonDtoList = response.getAllPersonDtoList();
		Set<Manager> managerList = MainAppUtils.createManagerList(pcaPersonDtoList);
		ExcelWritingUtils.createCompanyFirstExcel(companyFirstExcelName, companyFirstSheetName, managerList);
		ExcelWritingUtils.createPersonFirstExcel(personFirstExcelName, personFirstSheetName, MainAppUtils.convertSetToList(managerList));
	}

	public void writeExcelFileFromCloudForMergedData() {
		logger.info("Creating excel file for merged data...");
		Set<Manager> managerList = getAllMergedPeopleAsSet();
		Set<Manager> nonCombinedManagers = new HashSet<Manager>();
		Set<Manager> combinedManagers = new HashSet<Manager>();
		divideManagerListToCombinedAndNonCombined(managerList, nonCombinedManagers, combinedManagers);
		ExcelWritingUtils.writeMergedDataToExcel(nonCombinedManagers, "Direct-Relations.xls");
		ExcelWritingUtils.writeMergedDataToExcel(combinedManagers, "InDirect-Relations.xls");
		ExcelWritingUtils.writeMergedDataToExcel(managerList, "AllMergeList.xls");

	}

	/**
	 * Ismi uzun olan ve merged olma ihtimali yuksek olan elemanlari digerlerinden ayird eden metod. Side effectli metod, verilen listleri modifiye ediyor, beware.
	 * 
	 * @param combinedManagers2
	 */
	private void divideManagerListToCombinedAndNonCombined(Set<Manager> managerList, Set<Manager> nonCombinedManagers, Set<Manager> combinedManagers) {
		if (managerList == null || nonCombinedManagers == null || combinedManagers == null)
			return;
		for (Manager manager : managerList) {
			if (manager == null)
				continue;
			String mName = manager.getName();
			if (mName == null)
				continue;
			if (CommonUtils.wordCount(mName) < 4)
				nonCombinedManagers.add(manager);
			else
				combinedManagers.add(manager);
		}

	}

	private Set<Manager> getAllMergedPeopleAsSet() {
		return cloudManager.getAllMergedPeopleAsSet();
	}
}
