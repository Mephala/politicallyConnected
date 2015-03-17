package launcher.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class Launcher {

	public static void main(String[] args) throws IOException, InterruptedException {
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
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();

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
		Process proc = Runtime.getRuntime().exec("java -jar " + fileName);
		int retVal = proc.waitFor();
		System.out.println(retVal);

	}

}
