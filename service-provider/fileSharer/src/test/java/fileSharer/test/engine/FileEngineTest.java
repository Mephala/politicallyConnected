package fileSharer.test.engine;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import fileSharer.ApplicationConstants;
import fileSharer.DynamicTransferFile;
import fileSharer.engine.FileEngine;

@RunWith(JMockit.class)
public class FileEngineTest {
	private static final String folderNameToCompress = "src";

	@Test
	public void testZippingFile() throws IOException {
		File zipFile = createZipFileFromFileName(folderNameToCompress);
		assertTrue(zipFile.length() > 0);
		assertTrue(zipFile.delete());
	}

	private File createZipFileFromFileName(String folderNameToCompress) {
		File folderToZip = new File(folderNameToCompress);
		assertTrue(folderToZip.isDirectory());
		FileEngine fileZipperEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(folderNameToCompress, zippedFileName);
		fileZipperEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		return zipFile;
	}

	@Test
	public void testCreatingPiecesOfZippedFile() {
		File folderToZip = new File(folderNameToCompress);
		FileEngine fileZipperEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(folderNameToCompress, zippedFileName);
		fileZipperEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileZipperEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile != null);
		assertTrue(zipFile.delete());
	}

	@Test
	public void testCreatingDynamicTransferFile() throws IOException {
		String randomFolderName = UUID.randomUUID().toString();
		File file = new File(randomFolderName);
		assertTrue(file.mkdir());
		String createdFolderAbsolutePath = file.getAbsolutePath();
		File textFile = new File(createdFolderAbsolutePath + "\\test.txt");
		assertTrue(textFile.createNewFile());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append(UUID.randomUUID().toString());
		}
		FileUtils.writeStringToFile(textFile, sb.toString());
		File folderToZip = new File(createdFolderAbsolutePath);
		FileEngine fileEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(randomFolderName, zippedFileName);
		fileEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile.getTotalChunkValue() > 0);
	}

	@Test
	public void testPreparingCurrentChunk() throws IOException {
		File folderToZip = new File(folderNameToCompress);
		FileEngine fileEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(folderNameToCompress, zippedFileName);
		fileEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile != null);
		fileEngine.prepareDynamicTransferFileToTransfer(dynamicTransferFile);
		assertTrue(dynamicTransferFile.getCurrentChunk() != null);
		int[] normalizedChunks = dynamicTransferFile.getCurrentChunk().getNormalizedData();
		assertTrue(normalizedChunks.length > 0);
		assertTrue(normalizedChunks.length <= ApplicationConstants.TOTAL_INTEGER_PER_CHUNK);
		assertTrue(zipFile.delete());
	}

	@Test
	public void testNormalizingDataEquivalance() throws IOException {
		File folderToZip = new File(folderNameToCompress);
		FileEngine fileEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(folderNameToCompress, zippedFileName);
		fileEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile != null);
		fileEngine.prepareDynamicTransferFileToTransfer(dynamicTransferFile);
		assertTrue(dynamicTransferFile.getCurrentChunk() != null);
		int[] normalizedChunks = dynamicTransferFile.getCurrentChunk().getNormalizedData();
		int[] originalDataChunk = Deencapsulation.getField(dynamicTransferFile.getCurrentChunk(), "fileData");
		for (int i = 0; i < originalDataChunk.length; i++) {
			if (originalDataChunk[i] != 0)
				assertTrue(normalizedChunks[i] == originalDataChunk[i]);
		}
		assertTrue(normalizedChunks.length > 0);
		assertTrue(normalizedChunks.length <= ApplicationConstants.TOTAL_INTEGER_PER_CHUNK);
		assertTrue(zipFile.delete());
	}

	@Test
	public void testNormalizingFileZip() throws IOException {
		String randomFolderName = UUID.randomUUID().toString();
		File file = new File(randomFolderName);
		assertTrue(file.mkdir());
		String createdFolderAbsolutePath = file.getAbsolutePath();
		File textFile = new File(createdFolderAbsolutePath + "\\test.txt");
		assertTrue(textFile.createNewFile());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append(UUID.randomUUID().toString());
		}
		FileUtils.writeStringToFile(textFile, sb.toString());
		File folderToZip = new File(createdFolderAbsolutePath);
		FileEngine fileEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(randomFolderName, zippedFileName);
		fileEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile != null);
		fileEngine.prepareDynamicTransferFileToTransfer(dynamicTransferFile);
		assertTrue(dynamicTransferFile.getCurrentChunk() != null);
		int[] normalizedChunks = dynamicTransferFile.getCurrentChunk().getNormalizedData();
		assertTrue(normalizedChunks.length > 0);
		assertTrue(normalizedChunks.length <= ApplicationConstants.TOTAL_INTEGER_PER_CHUNK);
		File reCreatedFile = fileEngine.createFileFromDynamicTransferFile(dynamicTransferFile);
		assertTrue(reCreatedFile != null);
		assertTrue(reCreatedFile.length() > 0);
		assertTrue(zipFile.delete());
		assertTrue(reCreatedFile.delete());
	}

	@Test
	public void recreateFileFromStringDataTest() throws IOException {
		String randomFolderName = UUID.randomUUID().toString();
		File file = new File(randomFolderName);
		assertTrue(file.mkdir());
		String createdFolderAbsolutePath = file.getAbsolutePath();
		File textFile = new File(createdFolderAbsolutePath + "\\test.txt");
		assertTrue(textFile.createNewFile());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append(UUID.randomUUID().toString());
		}
		FileUtils.writeStringToFile(textFile, sb.toString());
		File folderToZip = new File(createdFolderAbsolutePath);
		FileEngine fileEngine = new FileEngine(folderToZip.getAbsolutePath());
		String zippedFileName = UUID.randomUUID().toString() + ".zip";
		String absoluteFolderNameToCompress = folderToZip.getAbsolutePath();
		zippedFileName = absoluteFolderNameToCompress.replaceAll(randomFolderName, zippedFileName);
		fileEngine.zipIt(zippedFileName);
		File zipFile = new File(zippedFileName);
		DynamicTransferFile dynamicTransferFile = fileEngine.createDynamicTransferFile(zipFile);
		assertTrue(dynamicTransferFile != null);
		fileEngine.prepareDynamicTransferFileToTransfer(dynamicTransferFile);
		assertTrue(dynamicTransferFile.getCurrentChunk() != null);
		File reCreatedFile = fileEngine.createFileFromDynamicTransferFile(dynamicTransferFile);
		assertTrue(reCreatedFile != null);
		assertTrue(reCreatedFile.length() > 0);
		assertTrue(zipFile.delete());
		assertTrue(reCreatedFile.delete());
	}

	@Test
	public void testEncodeDecodeDivisibility() throws IOException {

		File largeFile = new File("D:\\Blizzard\\WoW\\Heroes of the Storm.zip"); // beware
		// ,
		// large
		// file
		assertTrue(largeFile.isFile());
		// FileUtils.readFileToByteArray(largeFile);
		File recreationOfHeroesOfTheStorm = new File("heroesOfTheStorm.zip");
		assertTrue(recreationOfHeroesOfTheStorm.createNewFile());
		FileOutputStream out = new FileOutputStream(recreationOfHeroesOfTheStorm);
		RandomAccessFile aFile = new RandomAccessFile(largeFile, "r");
		FileChannel inChannel = aFile.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		while (inChannel.read(buffer) > 0) {
			buffer.flip();
			byte[] initialReadBytes = buffer.array();
			byte[] encoded = Base64.encodeBase64(initialReadBytes);
			String encodedString = new String(encoded, "UTF-8");
			byte[] transferredStringBytes = encodedString.getBytes();
			byte[] decodedStringBytes = Base64.decodeBase64(transferredStringBytes);
			out.write(decodedStringBytes);
			buffer.clear(); // do something with the data and clear/compact it.
		}
		inChannel.close();
		aFile.close();
		out.close();

	}

	@Test
	public void testUnzipingLargeFile() throws IOException {
		// byte[] buffer = new byte[1024];
		//
		// try {
		//
		// // create output directory is not exists
		// File folder = new File("mahi");
		// if (!folder.exists()) {
		// folder.mkdir();
		// }
		//
		// // get the zip file content
		// ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		// // get the zipped file list entry
		// ZipEntry ze = zis.getNextEntry();
		//
		// while (ze != null) {
		//
		// String fileName = ze.getName();
		// File newFile = new File(outputFolder + File.separator + fileName);
		//
		// System.out.println("file unzip : " + newFile.getAbsoluteFile());
		//
		// // create all non exists folders
		// // else you will hit FileNotFoundException for compressed folder
		// new File(newFile.getParent()).mkdirs();
		//
		// FileOutputStream fos = new FileOutputStream(newFile);
		//
		// int len;
		// while ((len = zis.read(buffer)) > 0) {
		// fos.write(buffer, 0, len);
		// }
		//
		// fos.close();
		// ze = zis.getNextEntry();
		// }
		//
		// zis.closeEntry();
		// zis.close();
		//
		// System.out.println("Done");
		//
		// } catch (IOException ex) {
		// ex.printStackTrace();
		// }
	}

}
