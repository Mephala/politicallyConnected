package mainApp.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.ManagerComparator;
import mainApp.model.WordToExcelModel;
import mainApp.utils.ExcelWritingUtils;
import mainApp.utils.MainAppUtils;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class WordToExcellManager {

	private static WordToExcellManager instance;
	private final WordToExcelModel model;
	private Set<Manager> readWordData;
	private final Log logger = LogFactory.getLog(getClass());

	private final CloudManager cloudManager;

	private WordToExcellManager() {
		logger.info("WordToExcel manager is being initialized...");
		model = WordToExcelModel.getInstance();
		readWordData = new HashSet<Manager>();
		cloudManager = CloudManager.getInstance();
		logger.info("WordToExcel manager is initialized.");
	}

	public static synchronized WordToExcellManager getInstance() {
		if (instance == null)
			instance = new WordToExcellManager();
		return instance;
	}

	public void createExcel() {
		readFromWordDocumentsInSelectedFolder();
		if (MainAppUtils.isCollectionEmpty(readWordData)) {
			JOptionPane.showMessageDialog(null, "Code:0xA3 . Process edilen dökümanlardan hiç bir kişi - kurum bağlantısı bulunamadı! ---> gokhan.ozgozen@gmail.com", "Veri bulunamadi",
					JOptionPane.ERROR_MESSAGE);
		} else {
			logger.info("Read manager list is the following...");
			for (Manager manager : readWordData) {
				logger.info("Manager name:" + manager.getName());
			}
		}
		Thread saveToCloudThread = new Thread(new Runnable() {

			public void run() {
				cloudManager.saveToCloud(readWordData);

			}
		});
		saveToCloudThread.start();
		createExcelFiles();
		try {
			saveToCloudThread.join();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Thread fault! Code: 0xAA");
		}
		readWordData.clear();
	}

	private void createExcelFiles() {
		int threadCount = model.getThreadCount();
		final ExecutorService cachedThreads = Executors.newFixedThreadPool(threadCount);
		Future<File> companyFirstFuture = cachedThreads.submit(new Callable<File>() {
			public File call() throws Exception {
				return writeToExcelCompanyFirst();
			}
		});
		Future<File> personFirstFuture = cachedThreads.submit(new Callable<File>() {
			public File call() throws Exception {
				return writeToExcelPersonFirst();
			}
		});
		try {
			cachedThreads.shutdown();
			boolean gracefulTermination = cachedThreads.awaitTermination(30, TimeUnit.SECONDS);
			File companyFirst = companyFirstFuture.get();
			File personFirst = personFirstFuture.get();
			if (gracefulTermination) {
				boolean companyFirstSuccess = MainAppUtils.fileCreationSuccess(companyFirst);
				if (companyFirstSuccess)
					JOptionPane.showMessageDialog(null, "Company First excel dosyası başarı ile oluşturuldu. Dosya adı:" + companyFirst.getName(), "Excel Oluşturma başarılı",
							JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Code:0xA5. Company First excel dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
				boolean personFirstSuccess = MainAppUtils.fileCreationSuccess(personFirst);
				if (personFirstSuccess)
					JOptionPane.showMessageDialog(null, "Person First excel dosyası başarı ile oluşturuldu. Dosya adı:" + personFirst.getName(), "Excel Oluşturma başarılı",
							JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Code:0xA6. Person First excel dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
			} else {
				logger.error("Threads are not gracefully terminated. Debug is required.");
				JOptionPane.showMessageDialog(null, "Code:0xA7. Excel writing thread fault. Contact to Gökhan Özgözen, gokhan.ozgozen@gmail.com", "Hata", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Thread fault. Code:0xA7 Contact to Gökhan özgözen, gokhan.ozgozen@gmail.com. Error:" + e.getMessage(), "Big Fault", JOptionPane.ERROR);
		}
	}

	private void readFromWordDocumentsInSelectedFolder() {
		long start = System.currentTimeMillis();
		final JTextArea wordToExcelLogger = model.getLoggingArea();
		final JProgressBar wordToExcelProgress = model.getProgress();
		int threadCount = model.getThreadCount();
		final ExecutorService cachedThreads = Executors.newFixedThreadPool(threadCount);
		File file = model.getChosenFile();
		final String year = file.getName();
		File[] wordFiles = file.listFiles();
		logger.info(wordFiles.length + " files are going to be processed.");
		final int totalFiles = wordFiles.length;
		wordToExcelLogger.setText(totalFiles + " of files are going to be processed. \n");
		final AtomicInteger processCount = new AtomicInteger(1);
		for (final File wordFile : wordFiles) {
			cachedThreads.submit(new Runnable() {
				public void run() {
					readFileAsync(wordToExcelLogger, wordToExcelProgress, year, totalFiles, processCount, wordFile);
				}
			});
		}
		try {
			cachedThreads.shutdown();
			boolean gracefulTermination = cachedThreads.awaitTermination(120, TimeUnit.SECONDS);
			if (!gracefulTermination)
				JOptionPane.showMessageDialog(null, "Thread fault. Code:0xA1 Contact to Gökhan özgözen, gokhan.ozgozen@gmail.com.", "Big Fault", JOptionPane.ERROR);
			JOptionPane
					.showMessageDialog(null, totalFiles + " fıle(s) have been processed in " + (System.currentTimeMillis() - start) + " miliseconds.", "What now ?", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Thread fault. Code:0xA2 Contact to Gökhan özgözen, gokhan.ozgozen@gmail.com.", "Big Fault", JOptionPane.ERROR);
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
		if (file.getName().contains(".doc") || file.getName().contains(".DOC")) {
			WordExtractor extractor = null;
			try {
				if (file.getName().contains("BRMEN"))
					System.out.println("cebuddey");
				System.setProperty("file.encoding", "UTF-8");
				Field charset = Charset.class.getDeclaredField("defaultCharset");
				charset.setAccessible(true);
				charset.set(null, null);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				BufferedReader in = new BufferedReader(new InputStreamReader(fis, "UTF8"));
				ReaderInputStream ris = new ReaderInputStream(in, "UTF8");
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
				String companyName = file.getName().replace(".doc", "");
				companyName = companyName.replace(".DOC", "");
				addMembers(members, companyName, year);
				isSuccess = true;
				logger.info(file.getName() + " is processed and " + members.size() + " managers are associated with their companies.");
			} catch (Exception exep) {
				System.err.println(exep.getMessage());
				JOptionPane.showMessageDialog(null, file.getName() + " işlenirken hata oluştu! Hata :" + exep.getMessage(), "Processing Fault", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			logger.info("File is not processed because it is not a valid doc file. FileName:" + file.getName());
		}
		return isSuccess;
	}

	private synchronized void addMembers(List<String> memberName, String company, String year) {
		for (String mName : memberName) {
			Manager manager = new Manager();
			manager.setName(mName);
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
						member = MainAppUtils.normalizeMemberString(member);
						if (member.length() > 0) {
							logger.info("Adding member to members list for created word data. Member name:" + member);
							members.add(member);
						}
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

	private File writeToExcelPersonFirst() {
		logger.info("Logging person first company.");
		String sheetName = getSheetNameFromReadWord();
		String excelName = sheetName + "(ManagerFirst).xls";
		List<Manager> sortedReadWordData = MainAppUtils.convertSetToSortedList(readWordData, ManagerComparator.COMPARATOR);
		ExcelWritingUtils.createPersonFirstExcel(excelName, sheetName, sortedReadWordData);
		logger.info("Returning created person first excel file for further processing.");
		return new File(excelName);
	}

	private File writeToExcelCompanyFirst() {
		String sheetName = getSheetNameFromReadWord();
		String excelName = sheetName + "(CompanyFirst).xls";
		ExcelWritingUtils.createCompanyFirstExcel(excelName, sheetName, readWordData);
		return new File(excelName);
	}

	private String getSheetNameFromReadWord() {
		String sheetName = null;
		for (Manager manager : readWordData) {
			Set<ManagementJob> mjSet = manager.getJobs();
			boolean jobYearFound = false;
			for (ManagementJob managementJob : mjSet) {
				sheetName = managementJob.getYear();
				jobYearFound = true;
				break;
			}
			if (jobYearFound)
				break;
		}
		return sheetName;
	}
}
