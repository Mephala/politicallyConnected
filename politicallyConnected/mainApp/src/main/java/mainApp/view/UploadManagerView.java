package mainApp.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import mainApp.manager.CloudManager;
import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.PoliticalJob;

@SuppressWarnings("serial")
public class UploadManagerView extends JPanel {

	private static UploadManagerView instance;
	private JTextField managerName;
	private JTextField managementJobName;
	private JTextField managementJobYear;
	private JTextField politicalJobName;
	private JTextField politicalJobYear;
	private CloudManager cloudManager = CloudManager.getInstance();

	public static synchronized UploadManagerView getInstance() {
		if (instance == null)
			instance = new UploadManagerView();
		return instance;
	}

	/**
	 * Create the panel.
	 */
	private UploadManagerView() {
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Manual Upload");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(148, 11, 155, 30);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Full Name");
		lblNewLabel_1.setBounds(10, 65, 124, 14);
		add(lblNewLabel_1);

		managerName = new JTextField();
		managerName.setBounds(247, 62, 210, 20);
		add(managerName);
		managerName.setColumns(10);

		JLabel lblManagementJobCode = new JLabel("Management Job Code (ie: AKBNK)");
		lblManagementJobCode.setBounds(10, 115, 197, 20);
		add(lblManagementJobCode);

		managementJobName = new JTextField();
		managementJobName.setBounds(247, 115, 210, 20);
		add(managementJobName);
		managementJobName.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Management Job Year");
		lblNewLabel_2.setBounds(10, 176, 197, 14);
		add(lblNewLabel_2);

		managementJobYear = new JTextField();
		managementJobYear.setBounds(247, 173, 210, 20);
		add(managementJobYear);
		managementJobYear.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Political Job Code (ie:CHP, MHP..) ");
		lblNewLabel_3.setBounds(10, 242, 197, 14);
		add(lblNewLabel_3);

		politicalJobName = new JTextField();
		politicalJobName.setBounds(247, 239, 210, 20);
		add(politicalJobName);
		politicalJobName.setColumns(10);

		politicalJobYear = new JTextField();
		politicalJobYear.setBounds(247, 317, 210, 20);
		add(politicalJobYear);
		politicalJobYear.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Political Job Year");
		lblNewLabel_4.setBounds(10, 320, 197, 14);
		add(lblNewLabel_4);

		JButton btnNewButton = new JButton("Reset");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetTextFields();
			}
		});
		btnNewButton.setBounds(97, 446, 91, 23);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Upload To DB");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Set<Manager> managerSet = generateManagerSetFromFields();
				cloudManager.saveToCloud(managerSet);
				instance.setCursor(Cursor.getDefaultCursor());
				resetTextFields();
			}

		});
		btnNewButton_1.setBounds(276, 446, 124, 23);
		add(btnNewButton_1);

	}

	private Set<Manager> generateManagerSetFromFields() {
		Set<ManagementJob> mJobs = new HashSet<ManagementJob>();
		Set<PoliticalJob> pJobs = new HashSet<PoliticalJob>();
		ManagementJob mJob = new ManagementJob();
		mJob.setName(managementJobName.getText());
		mJob.setYear(managementJobYear.getText());
		mJobs.add(mJob);
		PoliticalJob pJob = new PoliticalJob(politicalJobName.getText(), politicalJobYear.getText());
		pJobs.add(pJob);
		Manager manager = new Manager();
		manager.setName(managerName.getText());
		manager.setJobs(mJobs);
		manager.setpJobs(pJobs);
		Set<Manager> managerSet = new HashSet<Manager>();
		managerSet.add(manager);
		return managerSet;
	}

	private void resetTextFields() {
		managementJobName.setText("");
		managementJobYear.setText("");
		politicalJobName.setText("");
		managerName.setText("");
		politicalJobYear.setText("");
	}
}
