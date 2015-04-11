package mainApp.test.manager;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.JOptionPane;

import mainApp.manager.WordToExcellManager;
import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class WordToExcelManagerTestCase {

	@Ignore
	public void createExcelFromWordDataPersonFirst() {
		WordToExcellManager wManager = WordToExcellManager.getInstance();
		Manager manager = new Manager();
		manager.setName("Muhittin DevPenis");
		ManagementJob mj = new ManagementJob();
		mj.setName("OKALIPTUS");
		mj.setYear("1998");
		Set<ManagementJob> mjSet = new HashSet<ManagementJob>();
		mjSet.add(mj);
		manager.setJobs(mjSet);
		Set<Manager> readWordData = new HashSet<Manager>();
		readWordData.add(manager);
		Deencapsulation.setField(wManager, readWordData);
		File createdExcelFile = Deencapsulation.invoke(wManager, "writeToExcelPersonFirst");
		int success = JOptionPane.showConfirmDialog(null, "Manager first excel olustu mu ?", "Doğrulama", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		assertTrue(success == 0);
		boolean deleteFile = createdExcelFile.delete();
		assertTrue(deleteFile);
	}

	@Ignore
	public void testCreatingJobToManagerMap() {
		WordToExcellManager wManager = WordToExcellManager.getInstance();
		Manager manager = createManagerWithName("Muhittin Damarli");
		Manager m2 = createManagerWithName("Tunç Demirsiken");
		Set<Manager> readWordData = new HashSet<Manager>();
		readWordData.add(manager);
		readWordData.add(m2);
		Deencapsulation.setField(wManager, readWordData);
		Map<ManagementJob, Set<Manager>> jobToManagerSetMap = Deencapsulation.invoke(wManager, "createManagementJobToManagerMap");
		Set<ManagementJob> keySet = jobToManagerSetMap.keySet();
		assertTrue(keySet.size() == 3);
		for (ManagementJob managementJob : keySet) {
			Set<Manager> managersForJob = jobToManagerSetMap.get(managementJob);
			assertTrue(managersForJob.size() == 2);
		}
	}

	private Manager createManagerWithName(String mName) {
		Manager manager = new Manager();
		manager.setName(mName);
		Set<ManagementJob> mjSet = new HashSet<ManagementJob>();
		ManagementJob mj = new ManagementJob();
		mj.setName("OKALIPTUS");
		mj.setYear("1998");
		mjSet.add(mj);
		mj = new ManagementJob();
		mj.setName("Mokaliptus");
		mj.setYear("1998");
		mjSet.add(mj);
		mj = new ManagementJob();
		mj.setName("Cokaliptus");
		mj.setYear("1998");
		mjSet.add(mj);
		manager.setJobs(mjSet);
		return manager;
	}

	@Ignore
	public void testCreatingCompanyFirstExcelData() {
		WordToExcellManager wManager = WordToExcellManager.getInstance();
		Manager manager = createManagerWithName("Muhittin Damarli");
		Manager m2 = createManagerWithName("Tunç Demirsiken");
		Set<Manager> readWordData = new HashSet<Manager>();
		readWordData.add(manager);
		readWordData.add(m2);
		Deencapsulation.setField(wManager, readWordData);
		File createdExcelFile = Deencapsulation.invoke(wManager, "writeToExcelCompanyFirst");
		assertTrue(createdExcelFile.length() > 0);
		int success = JOptionPane.showConfirmDialog(null, "Company first excel olustu mu ?", "Doğrulama", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		assertTrue(success == 0);
		boolean deleteFile = createdExcelFile.delete();
		assertTrue(deleteFile);
	}

	@Test(timeout = 3000)
	public void speedTest() {
		Locale l = new Locale("TR", "tr");
		int limit = 1000000;
		for (int i = 0; i < limit; i++) {
			String s1 = UUID.randomUUID().toString();
			String s2 = UUID.randomUUID().toString();
			s1 = s1.toLowerCase(l);
			s2 = s2.toLowerCase(l);
			s1.contains(s2);
		}
	}

	@Test(timeout = 3000)
	public void speedTest2() {
		Locale l = new Locale("TR", "tr");
		int limit = 1000000;
		for (int i = 0; i < limit; i++) {
			String s1 = UUID.randomUUID().toString();
			String s2 = UUID.randomUUID().toString();
			s1.contains(s2);
		}
	}

}
