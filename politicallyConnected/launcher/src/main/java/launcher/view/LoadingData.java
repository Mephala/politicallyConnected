package launcher.view;

public class LoadingData {
	private static LoadingData instance;
	private final long totalData;
	private long processedData;
	private String info;

	private LoadingData(final long totalData) {
		this.totalData = totalData;
		this.info = "Launcher app başlatılıyor...";
	}

	public synchronized void setProcessedData(long processedData) {
		this.processedData = processedData;
	}

	public synchronized Integer getLoadPercentage() {
		return (int) (processedData * 100 / totalData);
	}

	public static synchronized LoadingData constructLoadingData(final long totalDataToProcess) {
		if (instance == null)
			instance = new LoadingData(totalDataToProcess);
		return instance;
	}

	public synchronized String getInfo() {
		return info;
	}

	public synchronized void setInfo(String info) {
		this.info = info;
	}
}
