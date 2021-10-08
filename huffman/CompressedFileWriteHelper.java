package huffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CompressedFileWriteHelper extends FileWriteHelper {

	public Map<Character, String> map;

	
	public CompressedFileWriteHelper(String path, ArrayList<HuffmanTuple> encodings) {
		super(path);
		this.map = new HashMap<>();
		this.initMap(encodings);
	}


	
	private void initMap(ArrayList<HuffmanTuple> encodings) {
		for (HuffmanTuple tuple : encodings) {
			map.put(tuple.letter, tuple.representation);
		}
	}

	
	protected void writeBeginningOfFile(String headerString) {
		for (int i = 0; i < headerString.length(); i += 2) {
			int intRep = Integer.parseInt(headerString.substring(i, i + 2), 16);
			String bin = Integer.toBinaryString(intRep);
			String paddedBin = Huffman.rightPadString(bin, NUM_OF_BITS_TO_WRITE);
			byteBuffer += paddedBin;
			this.writeToFile();
		}
	}

	protected void writeEndOfFile() {
		byteBuffer += map.get((char) 0x00);
		if (byteBuffer.length() != 8) {
			int howManyBytes = byteBuffer.length() / 8;
			byteBuffer = Huffman.leftPadString(byteBuffer, NUM_OF_BITS_TO_WRITE * (howManyBytes + 1));
		}
		this.writeToFile();
	}

	public void writeToFile() {
		try {
			while (byteBuffer.length() >= NUM_OF_BITS_TO_WRITE) {
				int i = Integer.parseInt(byteBuffer.substring(0, NUM_OF_BITS_TO_WRITE), 2);
				fileOutputStream.write(i);
				byteBuffer = byteBuffer.substring(NUM_OF_BITS_TO_WRITE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void doWork(int currentByte) {
		byteBuffer += map.get((char) currentByte);
		this.writeToFile();
	}
}
