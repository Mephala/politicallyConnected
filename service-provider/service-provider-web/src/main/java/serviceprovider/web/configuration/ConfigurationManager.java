package serviceprovider.web.configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.client.executor.ServiceClient;
import service.provider.common.core.RequestApplication;
import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.ConfigurationDto;
import service.provider.common.request.GetAllConfigurationRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SaveConfigurationRequestDto;
import service.provider.common.response.GetAllConfigurationResponseDto;
import service.provider.common.response.SaveConfigurationResponseDto;
import serviceprovider.web.authentication.AuthenticationLevel;
import serviceprovider.web.controller.Model;

public class ConfigurationManager {
	private static ConfigurationManager instance;
	private final Log logger = LogFactory.getLog(getClass());
	private boolean isDevMode = false;
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private ExecutorService asyncExecutors = Executors.newCachedThreadPool();
	private final Map<String, String> cachedRemoteConfigurations;
	private static final long INITIAL_DELAY_TO_FETCH_CONFIGURATIONS = 10; // Seconds
	private static final long REMOTE_CONFIGURATION_REFRESH_RATE_IN_SECONDS = 120;
	private final ReadWriteLock rwLock;
	private final Lock readLock;
	private final Lock writeLock;
	private final RequestApplication requestApp;
	private final String DEV_MODE_SERVICE_CLIENT_URL = "http://localhost:8081/";

	private ConfigurationManager() {
		determineEnvironment();
		initializeServiceClient();
		this.requestApp = RequestApplication.WEB;
		this.rwLock = new ReentrantReadWriteLock();
		this.readLock = rwLock.readLock();
		this.writeLock = rwLock.writeLock();
		this.cachedRemoteConfigurations = new HashMap<>();
		initializeCachedConfigurationsToDefault();
		createNonCreatedConfigurationAtServiceBackEnd();
		startConfigurationRefreshingJob();
	}

	private void startConfigurationRefreshingJob() {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("Refreshing configurations and sycning with service back-end.");
					GetAllConfigurationResponseDto getAllConfigurationResponse = getAllConfigurations();
					List<ConfigurationDto> freshConfigurations = getAllConfigurationResponse.getAllConfigurations();
					logger.info("Locking configurations due to update.");
					writeLock.lock();
					for (ConfigurationDto configurationDto : freshConfigurations) {
						String key = configurationDto.getKey();
						String value = configurationDto.getValue();
						if (cachedRemoteConfigurations.get(key) != null) {
							cachedRemoteConfigurations.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.error("Error occured during configuration refresh job. Releasing lock.", e);
				} finally {
					writeLock.unlock();
					logger.info("Cached configurations are updated successfully.");
				}
			}
		}, INITIAL_DELAY_TO_FETCH_CONFIGURATIONS, REMOTE_CONFIGURATION_REFRESH_RATE_IN_SECONDS, TimeUnit.SECONDS);
	}

	private void createNonCreatedConfigurationAtServiceBackEnd() {
		asyncExecutors.submit(new Runnable() {
			@Override
			public void run() {
				determineAndHandleConfigurationDifferences();
			}
		});
	}

	private void saveConfigurationWithKey(String webSideDefinedConfigurationKey) {
		SaveConfigurationRequestDto saveConfigRequest = RequestDtoFactory.createSaveConfigurationRequestDto(requestApp);
		ConfigurationDto savedConfigurationDto = new ConfigurationDto();
		savedConfigurationDto.setKey(webSideDefinedConfigurationKey);
		savedConfigurationDto.setValue(AppConf.defaultConfigurationsMap.get(webSideDefinedConfigurationKey));
		saveConfigRequest.setConfigurationDto(savedConfigurationDto);
		SaveConfigurationResponseDto saveConfResponse = ServiceClient.saveConfiguration(saveConfigRequest);
		if (ResponseStatus.OK.equals(saveConfResponse.getResponseStatus()))
			logger.info("Configuration with key:" + webSideDefinedConfigurationKey + " has been saved on the service back-end succesfully.");
		else
			logger.info("Configuration with key:" + webSideDefinedConfigurationKey + "  could not be saved on the service back-end. Reason: " + saveConfResponse);
	}

	private void initializeServiceClient() {
		if (isDevMode) {
			logger.info("ServiceClient is initialized for DEV environment. Target URL:" + DEV_MODE_SERVICE_CLIENT_URL);
			ServiceClient.initialize(DEV_MODE_SERVICE_CLIENT_URL);
		} else {
			logger.info("PROD mode is detected for configuration manager. Carrying on...");
		}
	}

	private void initializeCachedConfigurationsToDefault() {
		try {
			logger.info("Initializing web default configurations to cache.");
			writeLock.lock();
			Set<String> configKeySet = AppConf.defaultConfigurationsMap.keySet();
			for (String defKey : configKeySet) {
				cachedRemoteConfigurations.put(defKey, AppConf.defaultConfigurationsMap.get(defKey));
			}
		} catch (Exception e) {
			logger.info("Error occured during first cached configuration initialization.");
		} finally {
			writeLock.unlock();
		}
	}

	private void determineEnvironment() {
		try {
			logger.info("Connecting to amazon servers to receive external ip.");
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine(); // you get the IP as a String
			logger.info("External ip is calculated as:" + ip + " during configurationManager initialization.");
			isDevMode = ip.equals("213.14.153.57");
		} catch (Exception e) {
			logger.info("Error occured during ip fetching. Prod mode is assumed.");
		}
	}

	public static synchronized ConfigurationManager getInstance() {
		if (instance == null)
			instance = new ConfigurationManager();
		return instance;
	}

	private String readConfiguration(String key) {
		String value = null;
		try {
			readLock.lock();
			value = cachedRemoteConfigurations.get(key);
		} catch (Exception e) {
			logger.fatal("Read error !!!", e);
		} finally {
			readLock.unlock();
		}
		return value;
	}

	public int getMaxAuthenticationPasswordKey() {
		return Integer.parseInt(readConfiguration(AppConf.MAX_AUTHENTICATION_PASSWORD_KEY));
	}

	public Set<String> getRestrictedUris(AuthenticationLevel authLevel) {
		String uriList = null;
		switch (authLevel) {
		case REGISTERED:
			uriList = readConfiguration(AppConf.REGISTERED_RESTRICTED_URI_LIST);
			break;
		case ADMIN:
			uriList = readConfiguration(AppConf.ADMIN_RESTRICTED_URI_LIST);
			break;
		case COMMON:
			break;
		}
		if (uriList == null)
			return null;
		StringTokenizer st = new StringTokenizer(uriList, ",");
		Set<String> restrictedUris = new HashSet<>();
		while (st.hasMoreTokens())
			restrictedUris.add(st.nextToken());
		return restrictedUris;
	}

	public String getAuthenticationPageUri() {
		return Model.MAIN_AUTHENTICATION_PAGE.getUri();
	}

	public boolean isDevelopmentEnvironment() {
		return isDevMode;
	}

	private void determineAndHandleConfigurationDifferences() {
		try {
			logger.info("Creating default Service Provider Web registeries in the remote service if they do not exist.");
			GetAllConfigurationResponseDto getAllConfigurationResponse = getAllConfigurations();
			Set<String> savedConfigurationsAtServiceSide = new HashSet<>();
			List<ConfigurationDto> configurations = getAllConfigurationResponse.getAllConfigurations();
			for (ConfigurationDto configurationDto : configurations) {
				savedConfigurationsAtServiceSide.add(configurationDto.getKey());
			}
			Set<String> definedConfigurationsAtWebSide = AppConf.defaultConfigurationsMap.keySet();
			for (String webSideDefinedConfigurationKey : definedConfigurationsAtWebSide) {
				if (!savedConfigurationsAtServiceSide.contains(webSideDefinedConfigurationKey)) {
					logger.info("Missing configuration entry is found at service side. Configuration key:" + webSideDefinedConfigurationKey);
					try {
						logger.info("Attempting to save configuration at service side.");
						saveConfigurationWithKey(webSideDefinedConfigurationKey);
					} catch (Exception e) {
						logger.info("Exception occured during missing configuration save. Carrying on usual tasks.");
					}
				}
			}
		} catch (Exception e) {
			logger.info("Failed to map configuration differences between web default and service back-end. Carrying on initialization...");
		}
	}

	private GetAllConfigurationResponseDto getAllConfigurations() {
		GetAllConfigurationRequestDto getAllConfigurationsRequest = RequestDtoFactory.createGetAllConfigurationRequest(requestApp);
		GetAllConfigurationResponseDto getAllConfigurationResponse = ServiceClient.getAllConfigurations(getAllConfigurationsRequest);
		return getAllConfigurationResponse;
	}
}
