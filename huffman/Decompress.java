package huffman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Decompress {
	private String sourceFilePath;
	private String targetFilePath;

	public Decompress(String sourceFilePath, String targetFilePath) {
		this.sourceFilePath = sourceFilePath;
		this.targetFilePath = targetFilePath;
	}

	
	public void decompressFile() {
	         
		Huffman.writeDecodedFile(this.sourceFilePath, this.targetFilePath);
		System.out.println("==== Write Decompressed File to " + this.targetFilePath + " ====");
		
	}

	
	protected static Map<String, Character> convertTuplesToLookupMap(ArrayList<HuffmanTuple> tuples) {
		Map<String, Character> map = new HashMap<>();
		for (HuffmanTuple t : tuples) {
			map.put(t.representation, t.letter);
		}
		return map;
	}

	
	protected static ArrayList<HuffmanTuple> convertMapToTuples(Map<Character, Integer> map) {
		ArrayList<HuffmanTuple> list = new ArrayList<>();
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < entry.getValue(); i++) {
				sb.append("1");
			}
			list.add(new HuffmanTuple(entry.getKey(), sb.toString()));
		}
		return list;
	}

	
}
