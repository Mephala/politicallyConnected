package mainApp.manager;

import java.util.UUID;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import mainApp.model.WordToExcelModel;

public class WordToExcellManager {

	private static WordToExcellManager instance;
	private final WordToExcelModel model;

	private WordToExcellManager() {
		model = WordToExcelModel.getInstance();
	}

	public static synchronized WordToExcellManager getInstance() {
		if (instance == null)
			instance = new WordToExcellManager();
		return instance;
	}

	public void createExcel() {
		JTextArea wordToExcelLogger = model.getLoggingArea();
		JProgressBar wordToExcelProgress = model.getProgress();
		for (int i = 1; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String currentText = wordToExcelLogger.getText();
			String nextLog = currentText + " \n" + UUID.randomUUID().toString();
			wordToExcelLogger.setText(nextLog);
			wordToExcelProgress.setValue(i);
		}

	}

}
