package mainApp.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.PoliticalJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
import service.provider.common.core.RequestApplication;
import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.ManagementJobDto;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.dto.PoliticalJobDto;
import service.provider.common.request.GetAllPcaDataRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SavePcaPersonListRequestDto;
import service.provider.common.response.GetAllPcaDataResponseDto;
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
		logger.info("Save request is finished with server response. Response:" + response + " total elapsed time to save data to cloud :" + (System.currentTimeMillis() - start) + " ms.");
		if (response == null || !ResponseStatus.OK.equals(response.getResponseStatus())) {
			String errorMsg = "Server is not responding!";
			if (response != null)
				errorMsg = response.getError();
			JOptionPane.showMessageDialog(null, "Saving  data to database is failed!! Reason:" + errorMsg, "ERROR!", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Data is saved to database successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
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
		Set<PoliticalJob> pJobs = manager.getpJobs();
		List<PoliticalJobDto> pJobDtos = new ArrayList<PoliticalJobDto>();
		for (PoliticalJob politicalJob : pJobs) {
			pJobDtos.add(convertPoliticalJobToDto(politicalJob));
		}
		person.setPoliticalJobs(pJobDtos);
		return person;
	}

	private PoliticalJobDto convertPoliticalJobToDto(PoliticalJob politicalJob) {
		PoliticalJobDto pJobDto = new PoliticalJobDto();
		pJobDto.setName(politicalJob.getName());
		pJobDto.setYear(politicalJob.getYear());
		return pJobDto;
	}

	public List<Manager> getAllManagerDataFromCloud() {
		long start = System.currentTimeMillis();
		logger.info("Connecting cloud to download all pca data...");
		GetAllPcaDataRequestDto getAllPcaData = RequestDtoFactory.createGetAllPcaDataRequestDto(RequestApplication.PCA);
		GetAllPcaDataResponseDto response = ServiceClient.getAllPcaDataDto(getAllPcaData);
		List<PcaPersonDto> allPersons = response.getAllPersonDtoList();
		List<Manager> managerSet = convertPersonsToManager(allPersons);
		logger.info("All pca data has been collected from cloud in " + (System.currentTimeMillis() - start) + " ms.");
		return managerSet;
	}

	private List<Manager> convertPersonsToManager(List<PcaPersonDto> allPersons) {
		List<Manager> allManagers = new ArrayList<Manager>();
		if (allPersons != null) {
			for (PcaPersonDto pcaPerson : allPersons) {
				allManagers.add(convertPcaPersonToManager(pcaPerson));
			}
		}
		return allManagers;
	}

	private Manager convertPcaPersonToManager(PcaPersonDto pcaPerson) {
		Manager manager = null;
		if (pcaPerson != null) {
			manager = new Manager();
			manager.setName(pcaPerson.getName());
			manager.setJobs(convertPcaJobsToManagerJobs(pcaPerson.getManagementJobs()));
			manager.setpJobs(convertPcaPJobsTomanagerPJobs(pcaPerson.getPoliticalJobs()));
		}
		return manager;
	}

	private Set<PoliticalJob> convertPcaPJobsTomanagerPJobs(List<PoliticalJobDto> politicalJobs) {
		Set<PoliticalJob> pJobs = null;
		if (politicalJobs != null) {
			pJobs = new HashSet<PoliticalJob>();
			for (PoliticalJobDto pJobDto : politicalJobs) {
				PoliticalJob mjob = new PoliticalJob(pJobDto.getName(), pJobDto.getYear());
				pJobs.add(mjob);
			}
		}
		return pJobs;
	}

	private Set<ManagementJob> convertPcaJobsToManagerJobs(List<ManagementJobDto> managementJobs) {
		Set<ManagementJob> mJobs = null;
		if (managementJobs != null) {
			mJobs = new HashSet<ManagementJob>();
			for (ManagementJobDto mJobDto : managementJobs) {
				ManagementJob mjob = new ManagementJob();
				mjob.setName(mJobDto.getName());
				mjob.setYear(mJobDto.getYear());
				mJobs.add(mjob);
			}
		}
		return mJobs;
	}
}
