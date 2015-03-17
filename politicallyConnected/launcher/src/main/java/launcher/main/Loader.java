package launcher.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import launcher.view.LoadingData;

public class Loader {

	private LoadingData loadingData;
	private AtomicBoolean done = new AtomicBoolean(false);

	public Loader() {
		this.loadingData = LoadingData.constructLoadingData(1000l);// TODO
		// fileSize
		// alinmali!!!
	}

	public void load() {
		try {

			String fileName = UUID.randomUUID().toString() + ".jar";
			// computer
			URL link = new URL("http://isteralistersat.net/cpuStress.jar/downloadFile.do"); // The
																							// file
																							// that
																							// you
																							// want
			// to download

			// Code to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			long processData = 100l;
			while (-1 != (n = in.read(buf))) {
				System.out.println("Buffering...");
				loadingData.setInfo("Bağlantı kuruldu, güncel uygulama verileri çekiliyor.... \n");
				loadingData.setProcessedData(processData);
				Thread.sleep(2000);
				out.write(buf, 0, n);
				processData += 50;
			}
			loadingData.setInfo("Uygulama verilerinin tamamı çekildi, uygulama hazırlanıyor...");
			loadingData.setProcessedData(800l);
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			Thread.sleep(2000);
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			// End download code

			System.out.println("Finished");
			// String fileName = "mainApp.jar";
			// InputStream is =
			// Launcher.class.getClassLoader().getResourceAsStream(fileName);
			// // write the inputStream to a FileOutputStream
			// FileOutputStream outputStream = new FileOutputStream(new
			// File(fileName));
			//
			// int read = 0;
			// byte[] bytes = new byte[1024];
			//
			// while ((read = is.read(bytes)) != -1) {
			// outputStream.write(bytes, 0, read);
			// }
			//
			// System.out.println("Done!");
			loadingData.setInfo("Uygulama hazırlandı. Sisteme uygulama açılış komutu veriliyor....Bizi tercih ettiğiniz için teşekkür ederiz....");
			loadingData.setProcessedData(900l);
			Thread.sleep(4000);
			Process proc = Runtime.getRuntime().exec("java -jar " + fileName);
			loadingData.setProcessedData(1000l);
			int retVal = proc.waitFor();
			if (retVal != 0) {
				JOptionPane.showMessageDialog(null, "Meydana gelen bir hatadan dolayı app kapandı. Gökhan Özgözen ile irtibata geçiniz!", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
			} else {
				File file = new File(fileName);
				boolean fileDeleteSuccess = file.delete();
				if (!fileDeleteSuccess)
					JOptionPane.showMessageDialog(null, fileName + " isimli uygulama silinmelidir, otomatik olarak silinirken bir hata olustu, lutfen siz siliniz....", "HATA!!!!",
							JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "HATA!!!!", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} finally {
			done.set(true);
		}
	}

	public synchronized LoadingData getLoadingData() {
		return loadingData;
	}

	public synchronized AtomicBoolean getDone() {
		return done;
	}

}
