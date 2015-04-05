package pca;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import mockit.integration.junit4.JMockit;

import org.apache.axis.utils.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.ManagementJobDto;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.dto.PoliticalJobDto;
import service.provider.common.exception.DatabaseCorruptedException;
import serviceprovider.manager.PcaManager;
import serviceprovider.model.PcaManagementJob;
import serviceprovider.model.PcaPerson;
import serviceprovider.model.PcaPoliticalJob;

@RunWith(JMockit.class)
public class PcaManagerTest {

	PcaManager pcaManager;

	@Before
	public void initializeTest() {
		pcaManager = PcaManager.getInstance();
	}

	@Test
	public void testSavingPcaListOnce() throws DatabaseCorruptedException {
		Random random = new Random();
		int totalPcaPersonToSave = (random.nextInt() % 100) + 100;
		List<PcaPersonDto> pcaListToSave = new ArrayList<>();
		for (int i = 0; i < totalPcaPersonToSave; i++) {
			addRandomPcaPersonToList(pcaListToSave);
		}
		pcaManager.savePcaList(pcaListToSave);
		List<PcaPerson> convertedPcaPerson = pcaManager.convertPcaPersonDtosToModelObjects(pcaListToSave);
		for (PcaPerson pcaPerson : convertedPcaPerson) {
			pcaManager.deleteModel(pcaPerson);
		}
		List<PcaPerson> allPersons = pcaManager.getAllModelList();
		assertTrue(CollectionUtils.isEmpty(allPersons));
	}

	@Test
	public void testModifyingPreviouslySavedPerson() throws DatabaseCorruptedException {
		Random r = new Random();
		int totalPcaPersonToSave = (r.nextInt() % 100) + 100;
		List<PcaPersonDto> pcaListToSave = new ArrayList<>();
		for (int i = 0; i < totalPcaPersonToSave; i++) {
			addRandomPcaPersonToList(pcaListToSave);
		}
		pcaManager.savePcaList(pcaListToSave);
		for (PcaPersonDto pcaPersonDto : pcaListToSave) {
			int moreRandomMJobs = r.nextInt() % 5;
			int moreRandomPJobs = r.nextInt() % 5;
			for (int i = 0; i < moreRandomMJobs; i++) {
				pcaPersonDto.getManagementJobs().add(createRandomManagementJobDto());
			}
			for (int i = 0; i < moreRandomPJobs; i++) {
				pcaPersonDto.getPoliticalJobs().add(createRandomPJobDto());
			}
		}
		pcaManager.savePcaList(pcaListToSave);
		List<PcaPerson> convertedPcaPerson = pcaManager.convertPcaPersonDtosToModelObjects(pcaListToSave);
		for (PcaPerson pcaPerson : convertedPcaPerson) {
			pcaManager.deleteModel(pcaPerson);
		}
		List<PcaPerson> allPersons = pcaManager.getAllModelList();
		assertTrue(CollectionUtils.isEmpty(allPersons));
	}

	@Test
	public synchronized void testModifyingPcaPersonManagementJobsValidity() throws DatabaseCorruptedException {
		Map<PcaPersonDto, List<ManagementJobDto>> personToAddedManagementJobs = new HashMap<>();
		Map<PcaPersonDto, List<PoliticalJobDto>> personToAddedPoliticalJobs = new HashMap<>();
		Random r = new Random();
		int totalPcaPersonToSave = (r.nextInt() % 100) + 100;
		List<PcaPersonDto> pcaListToSave = new ArrayList<>();
		for (int i = 0; i < totalPcaPersonToSave; i++) {
			addRandomPcaPersonToList(pcaListToSave);
		}
		pcaManager.savePcaList(pcaListToSave);
		for (PcaPersonDto pcaPersonDto : pcaListToSave) {
			int moreRandomMJobs = r.nextInt() % 5;
			int moreRandomPJobs = r.nextInt() % 5;
			List<ManagementJobDto> pcaMJobs = new ArrayList<>();
			for (int i = 0; i < moreRandomMJobs; i++) {
				ManagementJobDto mJobDto = createRandomManagementJobDto();
				pcaMJobs.add(mJobDto);
				pcaPersonDto.getManagementJobs().add(mJobDto);
			}
			personToAddedManagementJobs.put(pcaPersonDto, pcaMJobs);
			List<PoliticalJobDto> politicalJobs = new ArrayList<>();
			for (int i = 0; i < moreRandomPJobs; i++) {
				PoliticalJobDto pJob = createRandomPJobDto();
				politicalJobs.add(pJob);
				pcaPersonDto.getPoliticalJobs().add(pJob);
			}
			personToAddedPoliticalJobs.put(pcaPersonDto, politicalJobs);
		}
		List<PcaPerson> convertedRandomList = pcaManager.convertPcaPersonDtosToModelObjects(pcaListToSave);
		pcaManager.savePcaList(pcaListToSave);
		List<PcaPerson> persistedPcaPersonList = pcaManager.getAllModelList();
		for (PcaPerson persistedPerson : persistedPcaPersonList) {
			for (PcaPerson convertedPerson : convertedRandomList) {
				if (convertedPerson.equals(persistedPerson)) {
					Set<PcaManagementJob> persistedManagemetnJobSet = new HashSet<>();
					List<PcaManagementJob> persistedManagementJobs = persistedPerson.getManagementJobs();
					if (!CollectionUtils.isEmpty(persistedManagementJobs)) {
						for (PcaManagementJob pcaManagementJob : persistedManagementJobs) {
							persistedManagemetnJobSet.add(pcaManagementJob);
						}
					}
					Set<PcaPoliticalJob> persistedPoliticalJobSet = new HashSet<>();
					List<PcaPoliticalJob> persistedPoliticalJobs = persistedPerson.getPoliticalJobs();
					if (!CollectionUtils.isEmpty(persistedPoliticalJobs)) {
						for (PcaPoliticalJob pcaPoliticalJob : persistedPoliticalJobs) {
							persistedPoliticalJobSet.add(pcaPoliticalJob);
						}
					}
					List<PcaPoliticalJob> convertedPoliticalJobs = convertedPerson.getPoliticalJobs();
					if (!CollectionUtils.isEmpty(convertedPoliticalJobs)) {
						for (PcaPoliticalJob pcaPoliticalJob : convertedPoliticalJobs) {
							assertTrue(persistedPoliticalJobSet.contains(pcaPoliticalJob));
						}
					}
					List<PcaManagementJob> convertedManagementJobs = convertedPerson.getManagementJobs();
					if (!CollectionUtils.isEmpty(convertedManagementJobs)) {
						for (PcaManagementJob pcaManagementJob : convertedManagementJobs) {
							assertTrue(persistedManagemetnJobSet.contains(pcaManagementJob));
						}
					}
				}
			}
		}
		persistedPcaPersonList = pcaManager.getAllModelList();
		for (PcaPerson pcaPerson : persistedPcaPersonList) {
			pcaManager.deleteModel(pcaPerson);
		}
		persistedPcaPersonList = pcaManager.getAllModelList();
		assertTrue(CollectionUtils.isEmpty(persistedPcaPersonList));
	}

	@Test
	public void testUpdatingPcaPersonWithManagementJobLikeRealCase() throws DatabaseCorruptedException {
		List<ManagementJobDto> mList = new ArrayList<>();
		ManagementJobDto mJob = new ManagementJobDto();
		mJob.setName("PPPPP");
		mJob.setYear("1453");
		PcaPersonDto pDto = new PcaPersonDto();
		mList.add(mJob);
		pDto.setManagementJobs(mList);
		String interestingName = "Mükerrem Şiğürtlüçöşiğ";
		pDto.setName(interestingName);
		List<PcaPersonDto> pList = new ArrayList<>();
		pList.add(pDto);
		pcaManager.savePcaList(pList);
		List<PcaPerson> pcaPersonList = pcaManager.getAllModelList();
		PcaPerson containingPerson = null;
		boolean containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(containsInterestingName);
		assertTrue(containingPerson != null);
		List<PcaManagementJob> managementJobs = containingPerson.getManagementJobs();
		PcaManagementJob pmjob = managementJobs.get(0);
		assertTrue(pmjob.getName().equals("PPPPP"));
		assertTrue(pmjob.getYear().equals("1453"));
		mJob = new ManagementJobDto();
		mJob.setName("ŞŞŞŞŞ");
		mJob.setYear("1071");
		mList.add(mJob);
		pcaManager.savePcaList(pList);
		pcaPersonList = pcaManager.getAllModelList();
		containingPerson = null;
		containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(containsInterestingName);
		assertTrue(containingPerson != null);
		managementJobs = containingPerson.getManagementJobs();
		assertTrue(managementJobs.size() == 2);
		for (PcaManagementJob pcaManagementJob : managementJobs) {
			boolean turkishShh = pcaManagementJob.getName().equals("ŞŞŞŞŞ") && pcaManagementJob.getYear().equals("1071");
			boolean pppppppp = pcaManagementJob.getName().equals("PPPPP") && pcaManagementJob.getYear().equals("1453");
			assertTrue(pppppppp || turkishShh);
		}
		pcaManager.deleteModel(containingPerson);
		pcaPersonList = pcaManager.getAllModelList();
		containingPerson = null;
		containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(!containsInterestingName);
		assertTrue(containingPerson == null);
	}

	private void addRandomPcaPersonToList(List<PcaPersonDto> pcaListToSave) {
		Random r = new Random();
		PcaPersonDto pcaPersonDto = new PcaPersonDto();
		List<ManagementJobDto> mJobDtoList = new ArrayList<ManagementJobDto>();
		List<PoliticalJobDto> pJobDtoList = new ArrayList<>();
		pcaPersonDto.setName(UUID.randomUUID().toString());
		pcaPersonDto.setManagementJobs(mJobDtoList);
		pcaPersonDto.setPoliticalJobs(pJobDtoList);
		int mJobPerPersonCount = r.nextInt() % 10;
		int pJobPerPersonCount = r.nextInt() % 10;
		addNumberOfRandomMJobsToList(mJobDtoList, mJobPerPersonCount);
		addNumberOfRandomPJobsToList(pJobDtoList, pJobPerPersonCount);
		pcaListToSave.add(pcaPersonDto);
	}

	private void addNumberOfRandomPJobsToList(List<PoliticalJobDto> pJobDtoList, int pJobPerPersonCount) {
		for (int i = 0; i < pJobPerPersonCount; i++) {
			pJobDtoList.add(createRandomPJobDto());
		}
	}

	private PoliticalJobDto createRandomPJobDto() {
		PoliticalJobDto pJob = new PoliticalJobDto();
		pJob.setName(UUID.randomUUID().toString().substring(0, 5));
		pJob.setYear(UUID.randomUUID().toString().substring(0, 5));
		return pJob;
	}

	private void addNumberOfRandomMJobsToList(List<ManagementJobDto> mJobDtoList, int mJobPerPersonCount) {
		for (int i = 0; i < mJobPerPersonCount; i++) {
			mJobDtoList.add(createRandomManagementJobDto());
		}
	}

	private ManagementJobDto createRandomManagementJobDto() {
		ManagementJobDto mJob = new ManagementJobDto();
		mJob.setName(UUID.randomUUID().toString().substring(0, 5));
		mJob.setYear(UUID.randomUUID().toString().substring(0, 5));
		return mJob;
	}

	@Test
	public void createAllPcaDtosFromDB() throws DatabaseCorruptedException {
		Random random = new Random();
		int totalPcaPersonToSave = (random.nextInt() % 100) + 100;
		List<PcaPersonDto> pcaListToSave = new ArrayList<>();
		for (int i = 0; i < totalPcaPersonToSave; i++) {
			addRandomPcaPersonToList(pcaListToSave);
		}
		pcaManager.savePcaList(pcaListToSave);
		List<PcaPersonDto> allPersonDto = pcaManager.covertDbDataToDto();
		assertTrue(!CollectionUtils.isEmpty(allPersonDto));
		for (PcaPersonDto pcaPersonDto : allPersonDto) {
			assertTrue(!StringUtils.isEmpty(pcaPersonDto.getName()));
		}
		List<PcaPerson> convertedPcaPerson = pcaManager.convertPcaPersonDtosToModelObjects(pcaListToSave);
		for (PcaPerson pcaPerson : convertedPcaPerson) {
			pcaManager.deleteModel(pcaPerson);
		}
		List<PcaPerson> allPersons = pcaManager.getAllModelList();
		assertTrue(CollectionUtils.isEmpty(allPersons));
	}

	@Test
	public void testHavingSameManagementJobDifferentYear() throws DatabaseCorruptedException {
		List<ManagementJobDto> mList = new ArrayList<>();
		ManagementJobDto mJob = new ManagementJobDto();
		mJob.setName("PPPPP");
		mJob.setYear("1453");
		PcaPersonDto pDto = new PcaPersonDto();
		mList.add(mJob);
		pDto.setManagementJobs(mList);
		String interestingName = "Mükerrem Şiğürtlüçöşiğ";
		pDto.setName(interestingName);
		List<PcaPersonDto> pList = new ArrayList<>();
		pList.add(pDto);
		pcaManager.savePcaList(pList);
		List<PcaPerson> pcaPersonList = pcaManager.getAllModelList();
		PcaPerson containingPerson = null;
		boolean containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(containsInterestingName);
		assertTrue(containingPerson != null);
		List<PcaManagementJob> managementJobs = containingPerson.getManagementJobs();
		PcaManagementJob pmjob = managementJobs.get(0);
		assertTrue(pmjob.getName().equals("PPPPP"));
		assertTrue(pmjob.getYear().equals("1453"));
		mJob = new ManagementJobDto();
		mJob.setName("PPPPP");
		mJob.setYear("1454");
		mList.add(mJob);
		pcaManager.savePcaList(pList);
		pcaPersonList = pcaManager.getAllModelList();
		containingPerson = null;
		containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(containsInterestingName);
		assertTrue(containingPerson != null);
		managementJobs = containingPerson.getManagementJobs();
		assertTrue(managementJobs.size() == 2);
		for (PcaManagementJob pcaManagementJob : managementJobs) {
			boolean turkishShh = pcaManagementJob.getName().equals("PPPPP") && pcaManagementJob.getYear().equals("1454");
			boolean pppppppp = pcaManagementJob.getName().equals("PPPPP") && pcaManagementJob.getYear().equals("1453");
			assertTrue(pppppppp || turkishShh);
		}
		pcaManager.deleteModel(containingPerson);
		pcaPersonList = pcaManager.getAllModelList();
		containingPerson = null;
		containsInterestingName = false;
		for (PcaPerson pcaPerson : pcaPersonList) {
			if (interestingName.equals(pcaPerson.getName())) {
				containsInterestingName = true;
				containingPerson = pcaPerson;
				break;
			}
		}
		assertTrue(!containsInterestingName);
		assertTrue(containingPerson == null);
	}
}
