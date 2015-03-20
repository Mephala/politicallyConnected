package mainApp.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import mainApp.manager.WordToExcellManager;
import mainApp.model.WordToExcelModel;

public class WordToExcelPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 478928832195521273L;
	private final WordToExcellManager manager;
	private final WordToExcelModel model;
	private ExecutorService panelExecutors = Executors.newCachedThreadPool();
	private final JFileChooser fileChooser;
	private static WordToExcelPanel instance;
	private final JButton chooseFileButton;
	private final JButton createExcelButton;
	private final JTextArea wordToExcelLogger;
	private final JProgressBar wordToExcelProgress;
	private final JScrollPane loggingScrollPane;
	private final JSlider slider;
	private final JLabel fileChooserLabel;
	private final JLabel progressBarLabel;

	public static synchronized WordToExcelPanel getInstance() {
		if (instance == null)
			instance = new WordToExcelPanel();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private WordToExcelPanel() {
		this.model = WordToExcelModel.getInstance();
		this.manager = WordToExcellManager.getInstance();
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		setLayout(null);
		JLabel lblNewLabel = new JLabel("Word To Excel");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(166, 11, 178, 14);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Word dosyalarını içeren klasörü seçin");
		lblNewLabel_1.setBounds(10, 61, 228, 14);
		add(lblNewLabel_1);

		chooseFileButton = new JButton("Klasör Seç");
		chooseFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelExecutors.submit(new Runnable() {
					public void run() {
						chooseFile();
					}
				});
			}
		});
		chooseFileButton.setBounds(253, 57, 128, 23);
		add(chooseFileButton);

		createExcelButton = new JButton("Oluştur");
		createExcelButton.setEnabled(false);
		createExcelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelExecutors.submit(new Runnable() {
					public void run() {
						createExcelFile();
					}
				});
			}
		});
		createExcelButton.setBounds(209, 433, 91, 23);
		add(createExcelButton);

		loggingScrollPane = new JScrollPane();
		loggingScrollPane.setVisible(false);
		loggingScrollPane.setBounds(10, 134, 272, 192);
		add(loggingScrollPane);

		wordToExcelLogger = new JTextArea();
		Font font = new Font("Verdana", Font.BOLD, 12);
		wordToExcelLogger.setFont(font);
		wordToExcelLogger.setForeground(Color.BLUE);
		model.setLoggingArea(wordToExcelLogger);
		loggingScrollPane.setViewportView(wordToExcelLogger);

		wordToExcelProgress = new JProgressBar(0, 100);
		model.setProgress(wordToExcelProgress);
		wordToExcelProgress.setBounds(292, 204, 178, 16);
		wordToExcelProgress.setStringPainted(true);
		wordToExcelProgress.setVisible(false);
		add(wordToExcelProgress);

		fileChooserLabel = new JLabel("Lütfen bir dosya seçiniz...");
		fileChooserLabel.setBounds(10, 103, 371, 14);
		fileChooserLabel.setVisible(false);
		add(fileChooserLabel);

		progressBarLabel = new JLabel("New labela");
		progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressBarLabel.setBounds(292, 165, 178, 14);
		progressBarLabel.setVisible(false);
		add(progressBarLabel);

		slider = new JSlider(JSlider.HORIZONTAL);
		slider.setValue(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(1);
		slider.setMaximum(8);
		slider.setMinimum(1);
		slider.setBounds(198, 356, 272, 24);
		add(slider);

		JLabel lblNewLabel_4 = new JLabel("İşlemci Kullanımı");
		lblNewLabel_4.setBounds(10, 366, 178, 14);
		add(lblNewLabel_4);

	}

	private void chooseFile() {
		int fileChooseStatus = fileChooser.showOpenDialog(instance);
		if (fileChooseStatus == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			model.setChosenFile(chosenFile);
			fileChooserLabel.setText(chosenFile.getAbsolutePath() + " selected.");
			fileChooserLabel.setVisible(true);
			createExcelButton.setEnabled(true);
		}
	}

	private void createExcelFile() {
		lockViewForProcessing();

		manager.createExcel();

		openViewForProcessing();
	}

	private void openViewForProcessing() {
		chooseFileButton.setEnabled(true);
		createExcelButton.setEnabled(true);
		fileChooserLabel.setVisible(false);
		loggingScrollPane.setVisible(false);
		wordToExcelProgress.setVisible(false);
	}

	private void lockViewForProcessing() {
		wordToExcelProgress.setValue(0);
		wordToExcelLogger.setText("");
		chooseFileButton.setEnabled(false);
		createExcelButton.setEnabled(false);
		model.setThreadCount(slider.getValue());
		if (!loggingScrollPane.isVisible())
			loggingScrollPane.setVisible(true);
		if (!wordToExcelProgress.isVisible())
			wordToExcelProgress.setVisible(true);
	}
}
