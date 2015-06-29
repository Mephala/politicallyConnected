//package mainApp.manager;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import mainApp.model.Manager;
//import mainApp.utils.MainAppUtils;
//import mockit.integration.junit4.JMockit;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(JMockit.class)
//public class ReadFromExcelTest {
//
//	@Test
//	public void testCreatingReadFromExcel() {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		assertTrue(readFromExcel != null);
//	}
//
//	@Test(expected = Exception.class)
//	public void testReadingFromNonExistentExcelFile() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "19ssas98.xls";
//		readFromExcel.readForCompany(documentName);
//	}
//
//	@Test(expected = Exception.class)
//	public void testReadingFromNullDocumentName() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		readFromExcel.readForCompany(null);
//	}
//
//	@Test
//	public void testReadingExcelFile() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "1998.xls";
//		readFromExcel.readForCompany(documentName);
//		Map<String, List<Manager>> personFirstReadMap = readFromExcel.getPersonFirstMap();
//		assertTrue(personFirstReadMap != null);
//		assertTrue(personFirstReadMap.get("1998.xls") != null);
//	}
//
//	@Test
//	public void testConstructingManagersWithMultipleFirms() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "1998.xls";
//		readFromExcel.readForCompany(documentName);
//		Map<String, List<Manager>> personFirstReadMap = readFromExcel.getPersonFirstMap();
//		assertTrue(personFirstReadMap != null);
//		assertTrue(personFirstReadMap.get("1998.xls") != null);
//		List<Manager> managers = personFirstReadMap.get("1998.xls");
//		boolean moreThanOneJobsFound = false;
//		for (Manager manager : managers) {
//			if (manager.getJobs().size() > 1)
//				moreThanOneJobsFound = true;
//		}
//		assertTrue(moreThanOneJobsFound);
//	}
//
//	@Test
//	public void testSkippingEmptyCells() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "1998.xls";
//		readFromExcel.readForCompany(documentName);
//		Map<String, List<Manager>> personFirstReadMap = readFromExcel.getPersonFirstMap();
//		assertTrue(personFirstReadMap != null);
//		assertTrue(personFirstReadMap.get("1998.xls") != null);
//		List<Manager> managers = personFirstReadMap.get("1998.xls");
//		boolean emptyNameFound = false;
//		for (Manager manager : managers) {
//			if (manager.getName() == null || manager.getName().length() == 0)
//				emptyNameFound = true;
//		}
//		assertTrue(emptyNameFound == false);
//	}
//
//	@Test
//	public void testGettingReadManagerSet() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "1998.xls";
//		readFromExcel.readForCompany(documentName);
//		Set<Manager> readManagers = readFromExcel.getReadManagers("1998.xls");
//		assertTrue(readManagers != null || readManagers.size() > 0);
//	}
//
//	@Test
//	public void readPoliticalExcel() throws Exception {
//		String userHomePath = System.getProperty("user.home");
//		ReadFromExcel readFromExcel = new ReadFromExcel(userHomePath);
//		String documentName = "21.xls";
//		readFromExcel.readForPolitical(documentName);
//		Set<Manager> readManagers = readFromExcel.getReadPoliticians(documentName);
//		assertTrue(readManagers != null || readManagers.size() > 0);
//		List<Manager> convertedReadManagers = MainAppUtils.convertSetToList(readManagers);
//		assertTrue(convertedReadManagers != null);
//	}
//
// }
