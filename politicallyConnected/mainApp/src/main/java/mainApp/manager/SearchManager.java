package mainApp.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mainApp.ApplicationConstants;
import mainApp.model.ManagementJob;
import mainApp.model.Manager;
import mainApp.model.PoliticalJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gokhanozg.wordhasher.WordHasher;

public class SearchManager {
	private static SearchManager instance;
	private WordHasher managerNameHasher;
	private WordHasher jobNameHasher;
	private CloudManager cloudManager;
	private final Log logger = LogFactory.getLog(getClass());
	private final ExecutorService constructionExecution = Executors.newFixedThreadPool(1);

	private SearchManager() {
		constructionExecution.submit(new Runnable() {

			public void run() {
				try {
					long start = System.currentTimeMillis();
					logger.info("Initializing search manager...");
					cloudManager = CloudManager.getInstance();
					List<Manager> allManagers = cloudManager.getAllManagerDataFromCloud();
					logger.info("All data is fetched from cloud, generating hashers...");
					List<String> allNames = generateManagerNameList(allManagers);
					List<String> allJobs = generatePoliticalAndManagementJobNameList(allManagers);
					managerNameHasher = new WordHasher(allNames, 7);
					jobNameHasher = new WordHasher(allJobs, 7);
					logger.info("Search manager is initialized in " + (System.currentTimeMillis() - start) + " ms.");
				} finally {
					logger.info("Search Manager construction executor service is completed.");
					constructionExecution.shutdown(); // weird call.
				}
			}
		});

	}

	public boolean isConstructed() {
		return constructionExecution.isTerminated();
	}

	private List<String> generateManagerNameList(List<Manager> allManagers) {
		List<String> names = new ArrayList<String>();
		for (Manager manager : allManagers) {
			names.add(manager.getName());
		}
		return names;
	}

	private List<String> generatePoliticalAndManagementJobNameList(List<Manager> allManagers) {
		List<String> names = new ArrayList<String>();
		for (Manager manager : allManagers) {
			Set<ManagementJob> mJobs = manager.getJobs();
			Set<PoliticalJob> pJobs = manager.getpJobs();
			if (pJobs != null) {
				for (PoliticalJob politicalJob : pJobs) {
					names.add(politicalJob.getName());
				}
			}
			if (mJobs != null) {
				for (ManagementJob mJob : mJobs) {
					names.add(mJob.getName());
				}
			}
		}
		return names;
	}

	public static synchronized SearchManager getInstance() {
		if (instance == null)
			instance = new SearchManager();
		return instance;
	}

	public List<String> search(String personName, String jobName) {
		List<String> searchResults = new ArrayList<String>();
		searchResults.add("Sonuc 1");
		searchResults.add("Sonuc 2");
		searchResults.add("Sonuc 3");
		return searchResults;
	}

	public List<String> popupSearchForPerson(String writtenText) {
		return searchWithHasher(writtenText, managerNameHasher);
	}

	private List<String> searchWithHasher(String writtenText, WordHasher hasher) {
		List<String> searchResults = new ArrayList<String>();
		Set<String> result = hasher.search(writtenText);
		int maxSearchResult = 0;
		for (String r : result) {
			searchResults.add(r);
			maxSearchResult++;
			if (maxSearchResult == ApplicationConstants.MAXIMUM_AUTOCOMPLETE_RESULT)
				break;
		}
		return searchResults;
	}

	public List<String> popupSearchForJob(String writtenText) {
		return searchWithHasher(writtenText, jobNameHasher);
	}

}
