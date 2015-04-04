package mainApp.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
import service.provider.common.core.RequestApplication;
import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.ManagementJobDto;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SavePcaPersonListRequestDto;
import service.provider.common.response.SavePcaPersonListResponseDto;

public class CloudManager {

	private static CloudManager instance;
	private final Log logger = LogFactory.getLog(getClass());

	private CloudManager() {
		logger.info("Initializing cloud manager...");
	}

	public static synchronized CloudManager getInstance() {
		if (instance == null)
			instance = new CloudManager();
		return instance;
	}

	public void saveToCloud(Set<Manager> readWordData) {
		long start = System.currentTimeMillis();
		logger.info("Serializing read data to save it over cloud...");
		List<PcaPersonDto> personDtoList = convertReadDataToSerializableDto(readWordData);
		logger.info("Creating save request for serialized data...");
		SavePcaPersonListRequestDto savePersonRequest = RequestDtoFactory.createSavePcaPersonListRequestDto(RequestApplication.WEB);
		savePersonRequest.setPersonListToSave(personDtoList);
		logger.info("Sending save request to server...");
		SavePcaPersonListResponseDto response = ServiceClient.savePcaPersonList(savePersonRequest);
		logger.info("Save request is finished with server response. Response:" + response + " total elapsed time to save data to cloud :" + (System.currentTimeMillis() - start)
				+ " ms.");
		if (response == null || !ResponseStatus.OK.equals(response.getResponseStatus())) {
			String errorMsg = "Server is not responding!";
			if (response != null)
				errorMsg = response.getError();
			JOptionPane.showMessageDialog(null, "Saving read data to database is failed!! Reason:" + errorMsg, "ERROR!", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Read data is saved to database successfully!", "SUCCESS!!! Wuhu", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private List<PcaPersonDto> convertReadDataToSerializableDto(Set<Manager> readWordData) {
		List<PcaPersonDto> convertedList = new ArrayList<PcaPersonDto>();
		for (Manager manager : readWordData) {
			convertedList.add(convertManagerToPcaPerson(manager));
		}
		return convertedList;
	}

	private PcaPersonDto convertManagerToPcaPerson(Manager manager) {
		PcaPersonDto person = new PcaPersonDto();
		person.setName(manager.getName());
		Set<ManagementJob> jobs = manager.getJobs();
		List<ManagementJobDto> managementJobDto = new ArrayList<ManagementJobDto>();
		for (ManagementJob managementJob : jobs) {
			ManagementJobDto mDto = new ManagementJobDto();
			mDto.setName(managementJob.getName());
			mDto.setYear(managementJob.getYear());
			managementJobDto.add(mDto);
		}
		person.setManagementJobs(managementJobDto);
		// TODO implement political job.
		return person;
	}

}
