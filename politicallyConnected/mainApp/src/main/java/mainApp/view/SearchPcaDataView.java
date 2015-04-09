package mainApp.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mainApp.manager.SearchManager;

@SuppressWarnings("serial")
public class SearchPcaDataView extends JPanel {
	private static SearchPcaDataView instance;
	private final JTextField personNameField;
	private JTextField jobNameField;
	private SearchManager searchManager = SearchManager.getInstance();
	private final JTextArea searchResultArea;
	private ExecutorService searchViewExecutors = Executors.newFixedThreadPool(4);

	public static synchronized SearchPcaDataView getInstance() {
		if (instance == null)
			instance = new SearchPcaDataView();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private SearchPcaDataView() {
		setLayout(null);

		personNameField = new JTextField();
		personNameField.setBounds(237, 38, 233, 20);
		personNameField.setColumns(10);
		add(personNameField);
		JLabel lblVeritabanndaArama = new JLabel("Veritabanında Arama");
		lblVeritabanndaArama.setHorizontalAlignment(SwingConstants.CENTER);
		lblVeritabanndaArama.setBounds(120, 11, 214, 14);
		add(lblVeritabanndaArama);
		JLabel lblNewLabel = new JLabel("Kişi Adı");
		lblNewLabel.setBounds(10, 41, 139, 14);
		add(lblNewLabel);
		JLabel lblNewLabel_1 = new JLabel("Parti ya da Kurum Adı");
		lblNewLabel_1.setBounds(10, 93, 139, 14);
		add(lblNewLabel_1);
		jobNameField = new JTextField();
		jobNameField.setBounds(237, 90, 233, 20);
		add(jobNameField);
		jobNameField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Sonuçlar");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(171, 162, 109, 14);
		add(lblNewLabel_2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 204, 448, 265);
		add(scrollPane);

		searchResultArea = new JTextArea();
		Font font = new Font("Verdana", Font.BOLD, 12);
		searchResultArea.setFont(font);
		searchResultArea.setForeground(Color.BLUE);
		scrollPane.setViewportView(searchResultArea);
		searchViewExecutors.submit(new Runnable() {
			public void run() {
				jobNameField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						printResults();
					}
				});
			}
		});
		searchViewExecutors.submit(new Runnable() {
			public void run() {
				personNameField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						printResults();
					}
				});
			}
		});
		searchViewExecutors.submit(new Runnable() {
			public void run() {
				personNameField.getDocument().addDocumentListener(new DocumentListener() {

					public void removeUpdate(DocumentEvent e) {
						showPopUpForPersonName();

					}

					public void insertUpdate(DocumentEvent e) {
						showPopUpForPersonName();
					}

					public void changedUpdate(DocumentEvent e) {
						showPopUpForPersonName();
					}
				});
			}
		});
		searchViewExecutors.submit(new Runnable() {
			public void run() {
				jobNameField.getDocument().addDocumentListener(new DocumentListener() {

					public void removeUpdate(DocumentEvent e) {
						showPopUpForJobName();
					}

					public void insertUpdate(DocumentEvent e) {
						showPopUpForJobName();
					}

					public void changedUpdate(DocumentEvent e) {
						showPopUpForJobName();
					}
				});
			}
		});
	}

	private void showPopUpForPersonName() {

		String writtenText = personNameField.getText();
		if (writtenText != null && writtenText.length() > 0) {
			final JPopupMenu popMenu = new JPopupMenu();
			List<String> searchResults = searchManager.popupSearchForPerson(writtenText);
			for (final String sResult : searchResults) {
				JMenuItem jmenuItem = new JMenuItem(sResult);
				jmenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						personNameField.setText(sResult);
					}
				});
				popMenu.add(jmenuItem);
			}
			popMenu.show(personNameField, 0, 25);
		}
	}

	private void showPopUpForJobName() {

		String writtenText = jobNameField.getText();
		if (writtenText != null && writtenText.length() > 0) {
			final JPopupMenu popMenu = new JPopupMenu();
			List<String> searchResults = searchManager.popupSearchForJob(writtenText);
			for (final String sResult : searchResults) {
				JMenuItem jmenuItem = new JMenuItem(sResult);
				jmenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jobNameField.setText(sResult);
					}
				});
				popMenu.add(jmenuItem);
			}
			popMenu.show(jobNameField, 0, 25);
		}
	}

	private void printResults() {
		String personName = personNameField.getText();
		String jobName = jobNameField.getText();
		List<String> searchResults = searchManager.search(personName, jobName);
		StringBuilder sb = new StringBuilder();
		for (String sResult : searchResults) {
			sb.append(sResult + " \n");
		}
		searchResultArea.setText(sb.toString());
	}
}
