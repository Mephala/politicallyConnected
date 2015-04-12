package mainApp.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mainApp.manager.CloudToExcelManager;

public class PandMAnalyzerView extends JPanel {

	private static PandMAnalyzerView instance;
	private CloudToExcelManager c2eManager = CloudToExcelManager.getInstance();

	public static synchronized PandMAnalyzerView getInstance() {
		if (instance == null)
			instance = new PandMAnalyzerView();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private PandMAnalyzerView() {
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Merging Persons with political and management jobs");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(47, 13, 392, 16);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Please notice that newly added data may not be included in the list.");
		lblNewLabel_1.setBounds(12, 42, 457, 39);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Server merges data in every 30 mins.");
		lblNewLabel_2.setBounds(12, 94, 427, 16);
		add(lblNewLabel_2);

		JButton btnNewButton = new JButton("Merge data and create Excel.");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				c2eManager.writeExcelFileFromCloudForMergedData();
				JOptionPane.showMessageDialog(null, "Merged data is created as excel file succesfully.", "Success.", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(105, 443, 295, 25);
		add(btnNewButton);

	}
}
