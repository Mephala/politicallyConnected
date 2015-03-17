package launcher.view;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingData {
	private static LoadingData instance;
	private long totalData;
	private long processedData;
	private String info;
	private AtomicBoolean downloadStarted = new AtomicBoolean(false);

	private LoadingData() {
		this.info = "Launcher app başlatılıyor...";
	}

	public synchronized void setProcessedData(long processedData) {
		this.processedData = processedData;
	}

	public synchronized Integer getLoadPercentage() {
		return (int) (processedData * 100 / totalData);
	}

	public static synchronized LoadingData constructLoadingData() {
		if (instance == null)
			instance = new LoadingData();
		return instance;
	}

	public synchronized String getInfo() {
		return info;
	}

	public synchronized void setInfo(String info) {
		this.info = info;
	}

	public synchronized AtomicBoolean getDownloadStarted() {
		return downloadStarted;
	}

	public synchronized void setTotalData(long totalData) {
		this.totalData = totalData;
	}
}
