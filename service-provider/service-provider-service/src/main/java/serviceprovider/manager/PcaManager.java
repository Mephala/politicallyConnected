package serviceprovider.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.ManagementJobDto;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.dto.PoliticalJobDto;
import serviceprovider.dao.PcaPersonDAO;
import serviceprovider.model.PcaManagementJob;
import serviceprovider.model.PcaPerson;
import serviceprovider.model.PcaPoliticalJob;

public class PcaManager extends AbstractServiceManager<PcaPerson> {

	private static PcaManager instance;
	private final PcaPersonDAO dao;
	private Log logger = LogFactory.getLog(getClass());

	private PcaManager() {
		dao = new PcaPersonDAO();
		initializeDAO(dao);
	}

	public static synchronized PcaManager getInstance() {
		if (instance == null)
			instance = new PcaManager();
		return instance;
	}

	public void savePcaList(List<PcaPersonDto> personListToSave) {
		logger.info("Converting requested person list to model objects...");
		List<PcaPerson> convertedPcaPersons = convertPcaPersonDtosToModelObjects(personListToSave);
		logger.info("Fetching persisted person data from database...");
		Set<PcaPerson> savedPcaPersonSet = getSavedPersonsAsSet();
		logger.info("Merging requested save data with previously persisted data...");
		mergeSaveRequestWithDB(savedPcaPersonSet, convertedPcaPersons);
		logger.info("Saving merged data to database, updating necessary fields or creating new fields...");
		persistMergedData(savedPcaPersonSet);
		logger.info("Saving pca person list is completed successfully.");
	}

	private void persistMergedData(Set<PcaPerson> savedPcaPersonSet) {
		for (PcaPerson pcaPerson : savedPcaPersonSet) {
			saveModel(pcaPerson);
		}
	}

	private void mergeSaveRequestWithDB(Set<PcaPerson> savedPcaPersonSet, List<PcaPerson> convertedPcaPersons) {
		for (PcaPerson convertedPerson : convertedPcaPersons) {
			if (savedPcaPersonSet.contains(convertedPerson)) {
				mergePerson(savedPcaPersonSet, convertedPerson);
			} else {
				savedPcaPersonSet.add(convertedPerson);
			}
		}
	}

	private void mergePerson(Set<PcaPerson> savedPcaPersonSet, PcaPerson convertedPerson) {
		PcaPerson savedPerson = getSavedPersonFromSet(savedPcaPersonSet, convertedPerson);
		if (savedPerson != null) {
			mergePersonDetails(savedPerson, convertedPerson);
		}
	}

	private void mergePersonDetails(PcaPerson savedPerson, PcaPerson convertedPerson) {
		List<PcaManagementJob> managementJobs = savedPerson.getManagementJobs();
		List<PcaPoliticalJob> politicalJobs = savedPerson.getPoliticalJobs();
		mergeManagementJobs(managementJobs, convertedPerson.getManagementJobs());
		mergePoliticalJobs(politicalJobs, convertedPerson.getPoliticalJobs());
	}

	private void mergePoliticalJobs(List<PcaPoliticalJob> politicalJobs, List<PcaPoliticalJob> politicalJobs2) {
		for (PcaPoliticalJob pcaPoliticalJob : politicalJobs2) {
			if (!politicalJobs.contains(pcaPoliticalJob))
				politicalJobs.add(pcaPoliticalJob);
		}

	}

	private void mergeManagementJobs(List<PcaManagementJob> managementJobs, List<PcaManagementJob> managementJobs2) {
		for (PcaManagementJob pcaManagementJob : managementJobs2) {
			if (!managementJobs.contains(pcaManagementJob))
				managementJobs.add(pcaManagementJob);
		}
	}

	private PcaPerson getSavedPersonFromSet(Set<PcaPerson> savedPcaPersonSet, PcaPerson convertedPerson) {
		for (PcaPerson pcaPerson : savedPcaPersonSet) {
			if (pcaPerson.equals(convertedPerson))
				return pcaPerson;
		}
		return null; // should never happen
	}

	private Set<PcaPerson> getSavedPersonsAsSet() {
		Set<PcaPerson> savedPersonsSet = new HashSet<PcaPerson>();
		List<PcaPerson> allPersonList = getAllModelList();
		for (PcaPerson pcaPerson : allPersonList) {
			savedPersonsSet.add(pcaPerson);
		}
		return savedPersonsSet;
	}

	private List<PcaPerson> convertPcaPersonDtosToModelObjects(List<PcaPersonDto> pcaPersonDtos) {
		List<PcaPerson> pcaPersonList = null;
		if (!CollectionUtils.isEmpty(pcaPersonDtos)) {
			pcaPersonList = new ArrayList<PcaPerson>();
			for (PcaPersonDto pDto : pcaPersonDtos) {
				pcaPersonList.add(converDtoToPerson(pDto));
			}
		}
		return pcaPersonList;
	}

	private PcaPerson converDtoToPerson(PcaPersonDto pDto) {
		PcaPerson person = new PcaPerson();
		person.setName(pDto.getName());
		person.setManagementJobs(convertManagementJobs(pDto.getManagementJobs()));
		person.setPoliticalJobs(convertPoliticalJobDtos(pDto.getPoliticalJobs()));
		return person;
	}

	private List<PcaPoliticalJob> convertPoliticalJobDtos(List<PoliticalJobDto> politicalJobDtos) {
		List<PcaPoliticalJob> politicalJobs = null;
		if (!CollectionUtils.isEmpty(politicalJobDtos)) {
			politicalJobs = new ArrayList<PcaPoliticalJob>();
			for (PoliticalJobDto politicalJobDto : politicalJobDtos) {
				politicalJobs.add(convertPoliticalJob(politicalJobDto));
			}
		}
		return politicalJobs;
	}

	private PcaPoliticalJob convertPoliticalJob(PoliticalJobDto politicalJobDto) {
		PcaPoliticalJob politicalJob = new PcaPoliticalJob();
		politicalJob.setName(politicalJobDto.getName());
		politicalJob.setYear(politicalJobDto.getYear());
		return politicalJob;
	}

	private List<PcaManagementJob> convertManagementJobs(List<ManagementJobDto> managementJobDtos) {
		List<PcaManagementJob> managementJobs = null;
		if (!CollectionUtils.isEmpty(managementJobDtos)) {
			managementJobs = new ArrayList<PcaManagementJob>();
			for (ManagementJobDto managementJobDto : managementJobDtos) {
				managementJobs.add(convertManagementJob(managementJobDto));
			}
		}
		return managementJobs;
	}

	private PcaManagementJob convertManagementJob(ManagementJobDto managementJobDto) {
		PcaManagementJob managementJob = new PcaManagementJob();
		managementJob.setName(managementJobDto.getName());
		managementJob.setYear(managementJobDto.getYear());
		return managementJob;
	}
}
