package fileSharer.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import fileSharer.ApplicationConstants;
import fileSharer.DynamicTransferFile;
import fileSharer.FileChunk;

public class FileEngine {

	private List<String> fileList;
	private final String SOURCE_FOLDER;

	public FileEngine(String sourceFolder) {
		fileList = new ArrayList<String>();
		this.SOURCE_FOLDER = sourceFolder;
		generateFileList(new File(SOURCE_FOLDER));
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 */
	public void zipIt(String zipFile) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);

			for (String file : this.fileList) {

				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			// remember close it
			zos.close();

			System.out.println("Done");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	public DynamicTransferFile createDynamicTransferFile(File zipFile) {
		BigDecimal fileSize = new BigDecimal(zipFile.length());
		BigDecimal chunkSize = new BigDecimal((4 * ApplicationConstants.TOTAL_INTEGER_PER_CHUNK));
		BigDecimal chunkCount = fileSize.divide(chunkSize, 10, BigDecimal.ROUND_HALF_UP);
		int integerChunkCount = chunkCount.intValue();
		BigDecimal integerEquivalanceBigDecimal = new BigDecimal(integerChunkCount);
		integerEquivalanceBigDecimal = integerEquivalanceBigDecimal.setScale(10);
		int totalCount = integerEquivalanceBigDecimal.intValue();
		if (!integerEquivalanceBigDecimal.equals(chunkCount))
			totalCount++;
		DynamicTransferFile dtf = new DynamicTransferFile(totalCount, zipFile);
		return dtf;
	}

	public void prepareDynamicTransferFileToTransfer(DynamicTransferFile dynamicTransferFile) throws IOException {
		FileInputStream in = null;
		try {
			File zipFile = dynamicTransferFile.getReferringZipFile();
			in = new FileInputStream(zipFile);
			int c;
			int integerSizeLimit = ApplicationConstants.TOTAL_INTEGER_PER_CHUNK;
			int currentDataAsInteger = 0;
			FileChunk fc = dynamicTransferFile.getCurrentChunk();
			if (fc == null) {
				fc = new FileChunk();
				dynamicTransferFile.setCurrentChunk(fc);
			}
			while ((c = in.read()) != -1) {
				if (currentDataAsInteger >= integerSizeLimit)
					break;
				fc.write(currentDataAsInteger, c);
				currentDataAsInteger++;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public File createFileFromDynamicTransferFile(DynamicTransferFile dynamicTransferFile) throws IOException {
		// FileOutputStream out = null;
		// File createdFile = null;
		// try {
		// createdFile = new File(UUID.randomUUID().toString() + ".zip");
		// createdFile.createNewFile();
		// out = new FileOutputStream(createdFile);
		// int totalChunk = dynamicTransferFile.getTotalChunkValue();
		// int currentChunk = dynamicTransferFile.getCurrentChunkValue();
		// while (currentChunk <= totalChunk) {
		// transferCurrentChunk(dynamicTransferFile);
		// dynamicTransferFile.setCurrentChunkValue(currentChunk + 1);
		// currentChunk++;
		// FileChunk fc = dynamicTransferFile.getCurrentChunk();
		// int[] normalizedArray = fc.getNormalizedData();
		// for (int i = 0; i < normalizedArray.length; i++) {
		// out.write(normalizedArray[i]);
		// }
		// }
		// } finally {
		//
		// if (out != null) {
		// out.close();
		// }
		// }
		// return createdFile;
		FileOutputStream out = null;
		File createdFile = null;
		try {
			createdFile = new File(UUID.randomUUID().toString() + ".zip");
			createdFile.createNewFile();
			out = new FileOutputStream(createdFile);
			byte[] readBytez = FileUtils.readFileToByteArray(dynamicTransferFile.getReferringZipFile());
			byte[] encodedBytes = Base64.encodeBase64(readBytez);
			String readData = new String(encodedBytes, "UTF-8");
			byte[] readDataBytes = readData.getBytes();
			byte[] decodedReadBytes = Base64.decodeBase64(readDataBytes);
			FileUtils.writeByteArrayToFile(createdFile, decodedReadBytes);
			// FileUtils.writeStringToFile(createdFile, readData, "UTF-8");
		} catch (Exception e) {
			System.err.println("Error during file recreation. Error:" + e.getMessage());
			e.printStackTrace();
		} finally {

			if (out != null) {
				out.close();
			}
		}
		return createdFile;

	}

	private void transferCurrentChunk(DynamicTransferFile dynamicTransferFile) {
		// FIXME implement later on.

	}
}
