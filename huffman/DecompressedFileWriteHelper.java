package huffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DecompressedFileWriteHelper extends FileWriteHelper {

	private Map<String, Character> map;
	private Map<Character, Integer> frequencyMap;
	private char charToWrite;
	private int numberOfChars;
	private int currentCodeIndex;
	private boolean readChar;
	private char inputChar;
	private enum State {
		FIRST_BYTE, DICTIONARY, ENCODED_FILE, FINISHED
	};
	private State currentState;

	
	public DecompressedFileWriteHelper(String path) {
		super(path);
		this.charToWrite = (char) 0x00;
		this.numberOfChars = 0;
		this.currentCodeIndex = 0;
		this.readChar = false;
		this.frequencyMap = new HashMap<>();
		this.currentState = State.FIRST_BYTE;
	}

	
	public void writeToFile() {
		try {
			fileOutputStream.write(this.charToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void handleReadDictionary(int currentByte) {
		char currentChar = (char) currentByte;
		if (readChar) {
			frequencyMap.put(currentChar, 0);
			this.inputChar = currentChar;
			this.readChar = false;
		} else {
			frequencyMap.put(this.inputChar, currentByte);
			this.readChar = true;
			currentCodeIndex++;
		}

		if (this.currentCodeIndex >= this.numberOfChars) {
			ArrayList<HuffmanTuple> tuples = Decompress.convertMapToTuples(frequencyMap);
			Huffman.sortHuffmanTuples(tuples);
			Huffman.canonizeEncodings(tuples);
			this.map = Decompress.convertTuplesToLookupMap(tuples);
			
			this.currentState = State.ENCODED_FILE;
		}
	}

	
	private void handleReadFirstByte(int currentByte) {
		
		this.numberOfChars = currentByte;
		this.readChar = true;
		this.currentState = State.DICTIONARY;
	}

	
	private void handleDecodeByByte(int currentByte) {
		this.byteBuffer += Huffman.rightPadString(Integer.toBinaryString(currentByte), NUM_OF_BITS_TO_WRITE);
		int currentLength = 1;

		while (true) {
			try {
				String current = this.byteBuffer.substring(0, currentLength);
				Character possibility = this.map.get(current);
				if (possibility != null) {
					if (possibility == '\u0000') {
						this.currentState = State.FINISHED;
						break;
					}
					this.byteBuffer = this.byteBuffer.substring(currentLength);
					this.charToWrite = possibility;
					this.writeToFile();
					currentLength = 1;
				} else {
					currentLength++;
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
	}

	
	@Override
	public void doWork(int currentByte) {
		if (this.currentState == State.FINISHED) {
			return;
		} else if (this.currentState == State.FIRST_BYTE) {
			this.handleReadFirstByte(currentByte);
		} else if (this.currentState == State.DICTIONARY) {
			this.handleReadDictionary(currentByte);
		} else if (this.currentState == State.ENCODED_FILE) {
			this.handleDecodeByByte(currentByte);
		}
	}
}
