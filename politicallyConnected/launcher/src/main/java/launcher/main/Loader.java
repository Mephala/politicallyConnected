package launcher.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

import launcher.view.LauncherView;
import launcher.view.LoadingData;
import service.provider.client.executor.ServiceClient;

public class Loader {
	private static final String MAIN_APP_NAME_TO_DOWNLOAD = "mainApp.jar";

	public static void main(String[] args) throws InterruptedException {
		final LauncherView launcherView;
		try {
			ExecutorService executor = Executors.newCachedThreadPool();
			final LoadingData loadingData = LoadingData.constructLoadingData();
			loadingData.setInfo("Checking internet connection ...");
			Future<LaunchViewStartupResult> launchResultFuture = executor.submit(new Callable<LaunchViewStartupResult>() {
				public LaunchViewStartupResult call() throws Exception {
					LauncherViewLoader viewLoader = new LauncherViewLoader();
					return viewLoader.launchView(loadingData);
				}
			});
			Long totalData = ServiceClient.getFileSizeForDownload(MAIN_APP_NAME_TO_DOWNLOAD);
			boolean connectionSuccess = !totalData.equals(Long.valueOf(-1));
			if (connectionSuccess) {
				while (!launchResultFuture.isDone())
					;
				LaunchViewStartupResult startupResult = launchResultFuture.get();
				if (startupResult.isSuccess) {
					launcherView = startupResult.lw;
					loadingData.setTotalData(totalData);
					loadingData.getDownloadStarted().set(true);
					loadingData.setInfo("Connected. Downloading updates...");
					String fileName = UUID.randomUUID().toString() + ".jar";
					URL link = new URL("http://kelepirpc.com/serviceProvider/" + MAIN_APP_NAME_TO_DOWNLOAD + "/downloadFile.do");
					InputStream in = new BufferedInputStream(link.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int n = 0;
					long processData = 0l;
					while (-1 != (n = in.read(buf))) {
						loadingData.setInfo("Downloading app data...");
						loadingData.setProcessedData(processData);
						out.write(buf, 0, n);
						processData += 1024;
					}
					loadingData.setInfo("App is downloaded successfully.");
					loadingData.setProcessedData(totalData);
					out.close();
					in.close();
					byte[] response = out.toByteArray();
					FileOutputStream fos = new FileOutputStream(fileName);
					fos.write(response);
					fos.close();
					Process proc = Runtime.getRuntime().exec("java -jar " + fileName);
					launcherView.setVisible(false);
					int retVal = proc.waitFor();
					if (retVal != 0)
						JOptionPane.showMessageDialog(null, "Meydana gelen bir hatadan dolayı app kapandı. Gökhan Özgözen ile irtibata geçiniz!", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
					File file = new File(fileName);
					boolean fileDeleteSuccess = file.delete();
					if (!fileDeleteSuccess)
						JOptionPane.showMessageDialog(null, fileName + " isimli uygulama silinmelidir, otomatik olarak silinirken bir hata olustu, lutfen siz siliniz....", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Error occured during loader window startup. Error message:" + startupResult.error + ". Contact to Gökhan Özgözen: gokhan.ozgozen@gmail.com", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Connection failure. Either you are offline or the servers are down. Contact to Gökhan Özgözen: gokhan.ozgozen@gmail.com", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "HATA!!!!", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} finally {
			System.exit(0);
		}
	}
}
