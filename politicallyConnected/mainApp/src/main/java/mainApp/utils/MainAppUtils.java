package mainApp.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.PoliticalJob;
import service.provider.common.dto.ManagementJobDto;
import service.provider.common.dto.PcaPersonDto;
import service.provider.common.dto.PoliticalJobDto;

public class MainAppUtils {

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path, String description) {
		if (path != null) {
			java.net.URL imgURL = MainAppUtils.class.getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL, description);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
		return null;
	}

	public static boolean isCollectionEmpty(Collection collection) {
		boolean isEmpty = true;
		if (collection != null) {
			Iterator iterator = collection.iterator();
			if (iterator.hasNext())
				isEmpty = false;
		}
		return isEmpty;
	}

	public static boolean fileCreationSuccess(File file) {
		boolean isSuccess = false;
		if (file != null)
			isSuccess = file.length() > 0;
		return isSuccess;
	}

	public static <E> List<E> convertSetToSortedList(Set<E> unsortedSet, Comparator<E> eComparator) {
		List<E> sortedList = convertSetToList(unsortedSet);
		Collections.sort(sortedList, eComparator);
		return sortedList;
	}

	public static String removeExtraSpaceBetweenNames(String stringWithPotentialExtraNames) {
		stringWithPotentialExtraNames = stringWithPotentialExtraNames.trim();
		if (stringWithPotentialExtraNames.length() > 1) {
			StringBuilder sb = new StringBuilder();
			final char space = ' ';
			char previousChar = stringWithPotentialExtraNames.charAt(0);
			for (int i = 1; i < stringWithPotentialExtraNames.length(); i++) {
				char currentChar = stringWithPotentialExtraNames.charAt(i);
				if (previousChar == space && currentChar == space) {
					previousChar = currentChar;
				} else {
					sb.append(previousChar);
					previousChar = currentChar;
				}
			}
			sb.append(previousChar);
			return sb.toString();
		} else {
			return stringWithPotentialExtraNames;
		}
	}

	public static String normalizeMemberString(String member) {
		if (member.equals("."))
			member = "";
		member = member.trim();
		for (String unwantedWord : Constants.nonManagerStrings) {
			member = member.replaceAll(unwantedWord, "");
		}
		member = removeParanthesis(member);
		member = removeExtraSpaceBetweenNames(member);
		member = member.trim();
		return member;
	}

	public static String removeParanthesis(String member) {
		int openPIndex = member.indexOf('(');
		if (openPIndex != -1) {
			int closePIndex = member.indexOf(')');
			if (closePIndex != -1) {
				String unwantedWord = member.substring(openPIndex, closePIndex + 1);
				member = member.replaceAll(unwantedWord, "");
				member = member.replaceAll("\\(", "");
				member = member.replaceAll("\\)", "");
			}
		}
		return member;
	}

	public static Set<Manager> createManagerList(List<PcaPersonDto> pcaPersonDtoList) {
		Set<Manager> managerList = new HashSet<Manager>();
		if (pcaPersonDtoList != null && !pcaPersonDtoList.isEmpty()) {
			for (PcaPersonDto pcaPersonDto : pcaPersonDtoList) {
				managerList.add(convertPcaPersonDtoToManager(pcaPersonDto));
			}
		}
		return managerList;
	}

	private static Manager convertPcaPersonDtoToManager(PcaPersonDto pcaPersonDto) {
		Manager manager = null;
		if (pcaPersonDto != null) {
			manager = new Manager();
			manager.setName(pcaPersonDto.getName());
			manager.setJobs(createManagementJobSet(pcaPersonDto.getManagementJobs()));
			manager.setpJobs(createPoliticalJobSet(pcaPersonDto.getPoliticalJobs()));
		}
		return manager;
	}

	private static Set<PoliticalJob> createPoliticalJobSet(List<PoliticalJobDto> politicalJobs) {
		Set<PoliticalJob> politicalJobSet = null;
		if (politicalJobs != null && !politicalJobs.isEmpty()) {
			politicalJobSet = new HashSet<PoliticalJob>();
			for (PoliticalJobDto pJobDto : politicalJobs) {
				politicalJobSet.add(convertPoliticalJobDtoToPoliticalJob(pJobDto));
			}
		}
		return politicalJobSet;
	}

	private static PoliticalJob convertPoliticalJobDtoToPoliticalJob(PoliticalJobDto pJobDto) {
		if (pJobDto == null)
			return null;
		return new PoliticalJob(pJobDto.getName(), pJobDto.getYear());
	}

	private static Set<ManagementJob> createManagementJobSet(List<ManagementJobDto> managementJobDtos) {
		Set<ManagementJob> managementJobSet = null;
		if (managementJobDtos != null && !managementJobDtos.isEmpty()) {
			managementJobSet = new HashSet<ManagementJob>();
			for (ManagementJobDto mJobDto : managementJobDtos) {
				managementJobSet.add(convertManagementJobDtoToManagementJob(mJobDto));
			}
		}
		return managementJobSet;
	}

	private static ManagementJob convertManagementJobDtoToManagementJob(ManagementJobDto mJobDto) {
		ManagementJob mJob = null;
		if (mJobDto != null) {
			mJob = new ManagementJob();
			mJob.setName(mJobDto.getName());
			mJob.setYear(mJobDto.getYear());
		}
		return mJob;
	}

	public static <E> List<E> convertSetToList(Set<E> set) {
		List<E> list = new ArrayList<E>();
		for (E e : set) {
			list.add(e);
		}
		return list;
	}
}
