package fileSharer;

import java.io.File;

public class DynamicTransferFile {
	private FileChunk currentChunk;
	private int currentChunkValue;
	private final int totalChunkValue;
	private final File referringZipFile;

	public DynamicTransferFile(int totalChunkValue, File referringFile) {
		this.totalChunkValue = totalChunkValue;
		this.referringZipFile = referringFile;
	}

	public FileChunk getCurrentChunk() {
		return currentChunk;
	}

	public void setCurrentChunk(FileChunk currentChunk) {
		this.currentChunk = currentChunk;
	}

	public int getCurrentChunkValue() {
		return currentChunkValue;
	}

	public void setCurrentChunkValue(int currentChunkValue) {
		this.currentChunkValue = currentChunkValue;
	}

	public int getTotalChunkValue() {
		return totalChunkValue;
	}

	public File getReferringZipFile() {
		return referringZipFile;
	}

}
