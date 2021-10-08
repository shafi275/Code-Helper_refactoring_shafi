package huffman;

import java.util.HashMap;
import java.util.Map;


public class FrequencyMapCreate implements FileContentReader {

	Map<Character, Integer> map; 

	
	public FrequencyMapCreate() {
		this.map = new HashMap<>();
	}

	
	@Override
	public void doWork(int currentByte) {
		char currentChar = (char) currentByte;

		if (map.get(currentChar) == null) {
			map.put(currentChar, 1);
		} else {
			map.put(currentChar, map.get(currentChar) + 1);
		}
	}
}
