package mainApp.manager;

import java.util.List;
import java.util.Set;

import mainApp.model.CloudToExcelModel;
import mainApp.model.Manager;
import mainApp.utils.ExcelWritingUtils;
import mainApp.utils.MainAppUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
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
}
