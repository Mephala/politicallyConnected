package mainApp.model;

import java.io.File;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class WordToExcelModel {
	private static WordToExcelModel instance;
	private File chosenFile;
	private JProgressBar progress;
	private JTextArea loggingArea;
	private Integer threadCount;

	private WordToExcelModel() {

	}

	public static synchronized WordToExcelModel getInstance() {
		if (instance == null)
			instance = new WordToExcelModel();
		return instance;
	}

	public synchronized File getChosenFile() {
		return chosenFile;
	}

	public synchronized void setChosenFile(File chosenFile) {
		this.chosenFile = chosenFile;
	}

	public synchronized JProgressBar getProgress() {
		return progress;
	}

	public synchronized void setProgress(JProgressBar progress) {
		this.progress = progress;
	}

	public synchronized JTextArea getLoggingArea() {
		return loggingArea;
	}

	public synchronized void setLoggingArea(JTextArea loggingArea) {
		this.loggingArea = loggingArea;
	}

	public synchronized Integer getThreadCount() {
		return threadCount;
	}

	public synchronized void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}
}
