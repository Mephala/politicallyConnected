package serviceprovider.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private final Log logger = LogFactory.getLog(getClass());
	private Map<String, PcaPerson> nameToPersonMap;
	private Map<String, PcaPoliticalJob> nameToPoliticalJobMap;
	private Map<String, PcaManagementJob> nameToManagementJobMap;
	private final ExecutorService cachedExecutor = Executors.newCachedThreadPool();

	private PcaManager() {
		dao = new PcaPersonDAO();
		nameToPersonMap = new HashMap<String, PcaPerson>();
		nameToPoliticalJobMap = new HashMap<>();
		nameToManagementJobMap = new HashMap<>();
		initializeDAO(dao);
	}

	public static synchronized PcaManager getInstance() {
		if (instance == null)
			instance = new PcaManager();
		return instance;
	}

	public void savePcaListAsync(final List<PcaPersonDto> personListToSave) {
		cachedExecutor.submit(new Runnable() {
			@Override
			public void run() {
				savePcaList(personListToSave);
			}
		});

	}

	public synchronized void savePcaList(List<PcaPersonDto> personListToSave) {
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
		List<PcaManagementJob> savedPersonManagementJobs = savedPerson.getManagementJobs();
		List<PcaPoliticalJob> politicalJobs = savedPerson.getPoliticalJobs();
		mergeManagementJobs(savedPersonManagementJobs, convertedPerson.getManagementJobs());
		mergePoliticalJobs(politicalJobs, convertedPerson.getPoliticalJobs());
	}

	private void mergePoliticalJobs(List<PcaPoliticalJob> politicalJobs, List<PcaPoliticalJob> politicalJobs2) {
		if (!CollectionUtils.isEmpty(politicalJobs2)) {
			for (PcaPoliticalJob pcaPoliticalJob : politicalJobs2) {
				if (!politicalJobs.contains(pcaPoliticalJob))
					politicalJobs.add(pcaPoliticalJob);
			}
		}
	}

	private void mergeManagementJobs(List<PcaManagementJob> savedPersonManagementJobs, List<PcaManagementJob> convertedPersonManagementJobs) {
		if (!CollectionUtils.isEmpty(convertedPersonManagementJobs)) {
			for (PcaManagementJob pcaManagementJob : convertedPersonManagementJobs) {
				if (!savedPersonManagementJobs.contains(pcaManagementJob))
					savedPersonManagementJobs.add(pcaManagementJob);
			}
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

	public List<PcaPerson> convertPcaPersonDtosToModelObjects(List<PcaPersonDto> pcaPersonDtos) {
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
		PcaPerson person = createOrReturnPerson(pDto.getName());
		person.setManagementJobs(convertManagementJobs(pDto.getManagementJobs()));
		person.setPoliticalJobs(convertPoliticalJobDtos(pDto.getPoliticalJobs()));
		return person;
	}

	private PcaPerson createOrReturnPerson(String name) {
		if (nameToPersonMap.containsKey(name))
			return nameToPersonMap.get(name);
		else {
			PcaPerson pcaPerson = new PcaPerson();
			pcaPerson.setName(name);
			nameToPersonMap.put(name, pcaPerson);
			return pcaPerson;
		}
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
		PcaPoliticalJob politicalJob = createOrGetPoliticalJob(politicalJobDto.getName());
		politicalJob.setYear(politicalJobDto.getYear());
		return politicalJob;
	}

	private PcaPoliticalJob createOrGetPoliticalJob(String name) {
		if (nameToPoliticalJobMap.containsKey(name))
			return nameToPoliticalJobMap.get(name);
		else {
			PcaPoliticalJob pJob = new PcaPoliticalJob();
			pJob.setName(name);
			nameToPoliticalJobMap.put(name, pJob);
			return pJob;
		}
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
		PcaManagementJob managementJob = createOrGetManagementJob(managementJobDto.getName());
		managementJob.setName(managementJobDto.getName());
		managementJob.setYear(managementJobDto.getYear());
		return managementJob;
	}

	private PcaManagementJob createOrGetManagementJob(String name) {
		if (nameToManagementJobMap.containsKey(name)) {
			return nameToManagementJobMap.get(name);
		} else {
			PcaManagementJob mJob = new PcaManagementJob();
			mJob.setName(name);
			nameToManagementJobMap.put(name, mJob);
			return mJob;
		}
	}

	public List<PcaPersonDto> covertDbDataToDto() {
		List<PcaPerson> pcaPersonList = getAllModelList();
		return convertPcaPersonListToDto(pcaPersonList);
	}

	private List<PcaPersonDto> convertPcaPersonListToDto(List<PcaPerson> pcaPersonList) {
		List<PcaPersonDto> convertedList = null;
		if (!CollectionUtils.isEmpty(pcaPersonList)) {
			convertedList = new ArrayList<>();
			for (PcaPerson pcaPerson : pcaPersonList) {
				convertedList.add(convertPcaPersonToDto(pcaPerson));
			}
		}
		return convertedList;
	}

	private PcaPersonDto convertPcaPersonToDto(PcaPerson pcaPerson) {
		PcaPersonDto convertedPersonDto = new PcaPersonDto();
		List<ManagementJobDto> managementJobDtoList = convertPcaManagementJobsToDto(pcaPerson.getManagementJobs());
		List<PoliticalJobDto> politicalJobDtoList = convertPoliticalJobsToDto(pcaPerson.getPoliticalJobs());
		convertedPersonDto.setManagementJobs(managementJobDtoList);
		convertedPersonDto.setPoliticalJobs(politicalJobDtoList);
		convertedPersonDto.setName(pcaPerson.getName());
		return convertedPersonDto;
	}

	private List<PoliticalJobDto> convertPoliticalJobsToDto(List<PcaPoliticalJob> politicalJobs) {
		List<PoliticalJobDto> politicalJobDtos = null;
		if (!CollectionUtils.isEmpty(politicalJobs)) {
			politicalJobDtos = new ArrayList<>();
			for (PcaPoliticalJob pJob : politicalJobs) {
				politicalJobDtos.add(convertPJobToDto(pJob));
			}
		}
		return politicalJobDtos;
	}

	private PoliticalJobDto convertPJobToDto(PcaPoliticalJob pJob) {
		PoliticalJobDto pJobDto = new PoliticalJobDto();
		pJobDto.setName(pJob.getName());
		pJobDto.setYear(pJob.getYear());
		return pJobDto;
	}

	private List<ManagementJobDto> convertPcaManagementJobsToDto(List<PcaManagementJob> managementJobs) {
		List<ManagementJobDto> managementJobDtos = null;
		if (!CollectionUtils.isEmpty(managementJobs)) {
			managementJobDtos = new ArrayList<>();
			for (PcaManagementJob mJob : managementJobs) {
				managementJobDtos.add(convertMJobToDto(mJob));
			}
		}
		return managementJobDtos;
	}

	private ManagementJobDto convertMJobToDto(PcaManagementJob mJob) {
		ManagementJobDto mJobDto = new ManagementJobDto();
		mJobDto.setName(mJob.getName());
		mJobDto.setYear(mJob.getYear());
		return mJobDto;
	}
}
