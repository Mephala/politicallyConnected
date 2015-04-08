package mainApp.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mainApp.manager.CloudToExcelManager;

@SuppressWarnings("serial")
public class CreateFromWebPanel extends JPanel {

	private static CreateFromWebPanel instance;
	private final CloudToExcelManager c2eManager;

	public static synchronized CreateFromWebPanel getInstance() {
		if (instance == null)
			instance = new CreateFromWebPanel();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private CreateFromWebPanel() {
		setLayout(null);
		c2eManager = CloudToExcelManager.getInstance();
		JLabel lblDatabasedenExcelOlutur = new JLabel("Database'den Excel Oluştur");
		lblDatabasedenExcelOlutur.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatabasedenExcelOlutur.setBounds(173, 11, 149, 31);
		add(lblDatabasedenExcelOlutur);

		JLabel lblNewLabel = new JLabel("Person-First");
		lblNewLabel.setBounds(24, 48, 155, 23);
		add(lblNewLabel);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Var");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(207, 49, 97, 23);
		add(chckbxNewCheckBox);

		JLabel lblNewLabel_1 = new JLabel("Company-First");
		lblNewLabel_1.setBounds(24, 82, 155, 23);
		add(lblNewLabel_1);

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Var");
		chckbxNewCheckBox_1.setSelected(true);
		chckbxNewCheckBox_1.setBounds(207, 82, 97, 23);
		add(chckbxNewCheckBox_1);

		final JButton btnNewButton = new JButton("Oluştur");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c2eManager.writeExcelFileFromCloud(null);
			}
		});
		btnNewButton.setBounds(199, 448, 91, 23);
		add(btnNewButton);

	}
}
