package mainApp.manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.WordToExcelModel;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class WordToExcellManager {

	private static WordToExcellManager instance;
	private final WordToExcelModel model;
	private Set<Manager> readWordData;

	private WordToExcellManager() {
		model = WordToExcelModel.getInstance();
		readWordData = new HashSet<Manager>();
	}

	public static synchronized WordToExcellManager getInstance() {
		if (instance == null)
			instance = new WordToExcellManager();
		return instance;
	}

	public void createExcel() {
		JTextArea wordToExcelLogger = model.getLoggingArea();
		JProgressBar wordToExcelProgress = model.getProgress();
		File file = model.getChosenFile();
		String year = file.getName();
		File[] wordFiles = file.listFiles();
		final int totalFiles = wordFiles.length;
		wordToExcelLogger.setText(totalFiles + " of files are going to be processed. \n");
		AtomicInteger processCount = new AtomicInteger(1);
		for (File wordFile : wordFiles) {
			readFileAsync(wordToExcelLogger, wordToExcelProgress, year, totalFiles, processCount, wordFile);
		}

	}

	private void readFileAsync(JTextArea wordToExcelLogger, JProgressBar wordToExcelProgress, String year, final int totalFiles, AtomicInteger processCount, File wordFile) {
		boolean success = readFromDocument(wordFile, year);
		logProcess(wordToExcelLogger, wordFile, success);
		incrementPorcessPercentage(totalFiles, processCount, wordToExcelProgress);
	}

	private synchronized void incrementPorcessPercentage(int totalFiles, AtomicInteger processCount, JProgressBar wordToExcelProgress) {
		int recentProcessCount = processCount.incrementAndGet();
		int recentPercentage = recentProcessCount * 100 / totalFiles;
		wordToExcelProgress.setValue(recentPercentage);
	}

	private synchronized void logProcess(JTextArea wordToExcelLogger, File wordFile, boolean success) {
		String currentText = wordToExcelLogger.getText();
		String nextLog = null;
		if (success)
			nextLog = currentText + wordFile.getName() + "-> OK.\n";
		else
			nextLog = currentText + wordFile.getName() + "-> FAILED!!.\n";
		wordToExcelLogger.setText(nextLog);
	}

	private boolean readFromDocument(File file, String year) {
		boolean isSuccess = false;
		WordExtractor extractor = null;
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			POIFSFileSystem pfs = new POIFSFileSystem(fis);
			HWPFDocument document = new HWPFDocument(pfs);
			extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < fileData.length; i++) {
				if (fileData[i] != null)
					sb.append(fileData[i]);
			}
			String documentAsString = sb.toString();
			documentAsString = new String(documentAsString.getBytes(), "UTF-8");
			int gmIndex = documentAsString.indexOf("GENEL MÜDÜR");
			int phoneIndex = documentAsString.indexOf("TELEFON NO");
			String actionArea = documentAsString.substring(gmIndex + 11, phoneIndex);
			List<String> members = new ArrayList<String>();
			getMembers(actionArea, members);
			isSuccess = true;
		} catch (Exception exep) {
			System.err.println(exep.getMessage());
			JOptionPane.showMessageDialog(null, file.getName() + " işlenirken hata oluştu! Hata :" + exep.getMessage(), "Processing Fault", JOptionPane.ERROR_MESSAGE);
		}
		return isSuccess;
	}

	private synchronized void addMembers(List<String> memberName, String company, String year) {
		Manager manager = new Manager();
		Set<ManagementJob> jobs = new HashSet<ManagementJob>();
		ManagementJob mj = new ManagementJob();
		mj.setName(company);
		mj.setYear(year);
		jobs.add(mj);
		manager.setJobs(jobs);
		if (readWordData.contains(manager)) {
			Iterator<Manager> managerIterator = readWordData.iterator();
			while (managerIterator.hasNext()) {
				Manager m = managerIterator.next();
				if (m.equals(manager)) {
					Set<ManagementJob> savedJobs = m.getJobs();
					if (!savedJobs.contains(mj)) {
						savedJobs.add(mj);
					}
					break;
				}
			}
		} else {
			readWordData.add(manager);
		}
	}

	private void getMembers(String actionArea, List<String> members) {
		int alphaIndex = actionArea.indexOf(7);
		if (alphaIndex == -1)
			return;
		else {
			if (alphaIndex < actionArea.length() - 1) {
				String tmp = actionArea.substring(alphaIndex + 1);
				int betaIndex = tmp.indexOf(7);
				if (betaIndex == -1)
					return;
				else {
					if (alphaIndex < betaIndex) {
						String member = actionArea.substring(alphaIndex + 1, betaIndex + 1);
						member = member.trim();
						if (member.length() > 0 && !member.contains("YÖNETİM") && !member.contains("(") && !member.contains("Board") && !member.contains(":")
								&& !member.contains("-"))
							members.add(member);
						if (betaIndex < actionArea.length() - 1)
							getMembers(actionArea.substring(betaIndex + 1), members);
						else
							return;
					} else
						getMembers(actionArea.substring(betaIndex + 1), members);
				}
			} else {
				return;
			}
		}
	}
}
