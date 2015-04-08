package mainApp.manager;

import javax.swing.JOptionPane;

import mainApp.model.CloudToExcelModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
import service.provider.common.core.RequestApplication;
import service.provider.common.request.GetAllPcaDataRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.response.GetAllPcaDataResponseDto;

public class CloudToExcelManager {

	private static CloudToExcelManager instance;
	private final Log logger = LogFactory.getLog(getClass());

	private CloudToExcelManager() {
		logger.info("Cloud to excel manager has been initialized.");
	}

	public static synchronized CloudToExcelManager getInstance() {
		if (instance == null)
			instance = new CloudToExcelManager();
		return instance;
	}

	public void writeExcelFileFromCloud(CloudToExcelModel c2eModel) {
		long start = System.currentTimeMillis();
		GetAllPcaDataRequestDto request = RequestDtoFactory.createGetAllPcaDataRequestDto(RequestApplication.PCA);
		GetAllPcaDataResponseDto response = ServiceClient.getAllPcaDataDto(request);
		JOptionPane.showMessageDialog(null, "Tum veriler cekildi! " + (System.currentTimeMillis() - start), "Oldu bu is", JOptionPane.INFORMATION_MESSAGE);

	}

}
