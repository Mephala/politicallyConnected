package fileSharer;

public class FileChunk {
	private int[] fileData;

	public FileChunk() {
		fileData = new int[ApplicationConstants.TOTAL_INTEGER_PER_CHUNK];
	}

	public void write(int loc, int data) {
		fileData[loc] = data;
	}

	public int[] getNormalizedData() {
		int notNullCount = 0;
		for (int i = 0; i < fileData.length; i++) {
			if (fileData[i] != 0)
				notNullCount++;
			else {
				boolean hasNonNull = false;
				for (int j = i; j < fileData.length && !hasNonNull; j++) {
					if (fileData[j] != 0)
						hasNonNull = true;
				}
				if (!hasNonNull)
					break;
				else
					notNullCount++;
			}
		}
		int[] retVal = new int[notNullCount];
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = fileData[i];
		}
		return retVal;
	}
}
