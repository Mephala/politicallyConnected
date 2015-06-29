package mainApp.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mainApp.manager.CloudManager;
import mainApp.manager.ReadFromExcel;
import mainApp.model.Manager;

public class ReadFromExcelPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ReadFromExcelPane instance;
	private final JFileChooser fileChooser;
	private final CloudManager cloudManager = CloudManager.getInstance();
	private AtomicBoolean showXLSNotification = new AtomicBoolean(false);

	public static synchronized ReadFromExcelPane getInstance() {
		if (instance == null)
			instance = new ReadFromExcelPane();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private ReadFromExcelPane() {
		fileChooser = new JFileChooser();
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Excelden okuma");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(118, 11, 226, 14);
		add(lblNewLabel);

		JButton btnNewButton = new JButton("Read Milletvekili!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readFromPoliticalExcel();
			}
		});
		btnNewButton.setBounds(139, 87, 181, 23);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Read Yönetim Kurulu");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean success = null;
				try {
					File chosenFile = getChosenFile();
					if (chosenFile != null) {
						String absolutePath = chosenFile.getAbsolutePath();
						String directory = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator) + 1);
						File directoryAsFile = new File(directory);
						if (directoryAsFile.exists() && directoryAsFile.isDirectory()) {
							String documentName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
							ReadFromExcel readFromExcel = new ReadFromExcel(directory);
							try {
								readFromExcel.readForCompany(documentName);
							} catch (Exception exc) {
								JOptionPane.showMessageDialog(null, "Excelden okuma hatasi. Detayli hata:" + exc.getMessage(), "Excelden Okunamadi", JOptionPane.ERROR_MESSAGE);
								success = false;
							}
							if (!Boolean.FALSE.equals(success)) {
								Set<Manager> readManagers = readFromExcel.getReadManagers(documentName);
								cloudManager.saveToCloud(readManagers);
								success = true;
							}
						}
					}
				} finally {
					if (Boolean.TRUE.equals(success))
						JOptionPane.showMessageDialog(null, "Okuma işlemi başarılı, okunan veriler server a gönderildi.", "YAPTIM!", JOptionPane.INFORMATION_MESSAGE);
					else if (success == null)
						JOptionPane.showMessageDialog(null, "Excelden okuma hatasi. Bilinmeyen, esrarengiz bir hata. Gokhan Ozgozen'e sorun, gokhan.ozgozen@gmail.com", "Excelden Okunamadi",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBounds(139, 188, 181, 23);
		add(btnNewButton_1);

	}

	private File getChosenFile() {
		if (!showXLSNotification.getAndSet(true))
			JOptionPane.showMessageDialog(instance, "Okumak için seçeceğiniz dosyaların XLS formatında olmasına dikkat ediniz, XLSX gibi saçma sapan, safsata formatlarla vakit kaybetmeyiniz.",
					"PEK ÖNEMLİ UYARI", JOptionPane.INFORMATION_MESSAGE);
		int read = fileChooser.showDialog(instance, "OkuVeServeraGönder");
		if (read == 0) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	private void readFromPoliticalExcel() {
		Boolean success = null;
		try {
			File chosenFile = getChosenFile();
			if (chosenFile != null) {
				String absolutePath = chosenFile.getAbsolutePath();
				String directory = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator) + 1);
				File directoryAsFile = new File(directory);
				if (directoryAsFile.exists() && directoryAsFile.isDirectory()) {
					String documentName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
					ReadFromExcel readFromExcel = new ReadFromExcel(directory);
					try {
						readFromExcel.readForPolitical(documentName);
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(null, "Excelden okuma hatasi. Detayli hata:" + exc.getMessage(), "Excelden Okunamadi", JOptionPane.ERROR_MESSAGE);
						success = false;
					}
					if (!Boolean.FALSE.equals(success)) {
						Set<Manager> readManagers = readFromExcel.getReadPoliticians(documentName);
						cloudManager.saveToCloud(readManagers);
						success = true;
					}
				}
			}
		} finally {
			if (Boolean.TRUE.equals(success))
				JOptionPane.showMessageDialog(null, "Okuma işlemi başarılı, okunan veriler server a gönderildi.", "YAPTIM!", JOptionPane.INFORMATION_MESSAGE);
			else if (success == null)
				JOptionPane.showMessageDialog(null, "Excelden okuma hatasi. Bilinmeyen, esrarengiz bir hata. Gokhan Ozgozen'e sorun, gokhan.ozgozen@gmail.com", "Excelden Okunamadi",
						JOptionPane.ERROR_MESSAGE);
		}
	}
}
