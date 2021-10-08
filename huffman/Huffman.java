package huffman;

import java.io.*;
import java.util.*;


public class Huffman {

	
	public static Node huffman(Map<Character, Integer> map) {
		PriorityQueue<Node> q = new PriorityQueue<>(convertMapToList(map));

		for (int i = 1; i < map.size(); i++) {
			Node z = new Node();
			Node x = q.poll();
			z.left = x;
			Node y = q.poll();
			z.right = y;
			z.freq = x.freq + y.freq;
			z.letter = z.INTERIOR_NODE_CHAR;
			q.add(z);
		}
		return q.poll();
	}

	
	private static void performInorderTraversal(Node current, String representation, ArrayList<HuffmanTuple> list) {
		if (current == null) {
			return;
		}

		performInorderTraversal(current.left, representation + "0", list);
		if (current.letter != (char) 0x01) {
			list.add(new HuffmanTuple(current.letter, representation));
		}
		performInorderTraversal(current.right, representation + "1", list);
	}

	
	private static ArrayList<Node> convertMapToList(Map<Character, Integer> map) {
		ArrayList<Node> list = new ArrayList<>();
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			list.add(new Node(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	
	public static Map<Character, Integer> createMapFromFile(String filePath) {
		FrequencyMapCreate createFrequencyMapWorker = new FrequencyMapCreate();
		
		createFrequencyMapWorker.map.put((char) 0x00, 1);
		Huffman.readFromFileAndDoWork(filePath, createFrequencyMapWorker);
		return createFrequencyMapWorker.map;
	}

	protected static ArrayList<HuffmanTuple> canonizeHuffmanTree(Node root) {
		
		ArrayList<HuffmanTuple> encodings = Huffman.extractEncodings(root);

		
		Huffman.sortHuffmanTuples(encodings);

		
		Huffman.canonizeEncodings(encodings);
		return encodings;
	}

	
	protected static void canonizeEncodings(ArrayList<HuffmanTuple> encodings) {
		int currentNum = 0;
		for (int i = encodings.size() - 1; i >= 0; i--) {
			HuffmanTuple currentTuple = encodings.get(i);
			currentTuple.representation = Huffman.rightPadString(Integer.toBinaryString(currentNum), currentTuple.representation.length());
			if (i > 0) {
				
				currentNum = (currentNum + 1) >> (currentTuple.representation.length() - encodings.get(i - 1).representation.length());
			}
		}
	}

	protected static void sortHuffmanTuples(ArrayList<HuffmanTuple> list) {
		Collections.sort(list, new Comparator<HuffmanTuple>() {
			@Override
			public int compare(HuffmanTuple o1, HuffmanTuple o2) {
				if (o1.representation.length() > o2.representation.length()) {
					return 1;
				} else if (o1.representation.length() < o2.representation.length()) {
					return -1;
				} else {
					
					if (o1.letter < o2.letter) {
						return 1;
					} else if (o1.letter > o2.letter) {
						return -1;
					}
				}
				return 0;
			}
		});
	}

	
	public static String generateLookupCode(ArrayList<HuffmanTuple> encodings) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(Huffman.rightPadString(Integer.toHexString(encodings.size()), 2));
		Collections.sort(encodings, new Comparator<HuffmanTuple>() {
			@Override
			public int compare(HuffmanTuple o1, HuffmanTuple o2) {
				if (o1.letter > o2.letter) {
					return 1;
				} else if (o1.letter < o2.letter) {
					return -1;
				}
				return 0;
			}
		});
		
		for (HuffmanTuple tuple : encodings) {
			builder.append(Huffman.rightPadString(Integer.toHexString((int) tuple.letter), 2));
			builder.append(Huffman.rightPadString(Integer.toHexString(tuple.representation.length()), 2));
		}
		return builder.toString();
	}

	
	protected static void writeEncodedFile(String inputPath, String outputPath, ArrayList<HuffmanTuple> encodings) {
		CompressedFileWriteHelper writeEncodedFile = new CompressedFileWriteHelper(outputPath, encodings);
		writeEncodedFile.writeBeginningOfFile(Huffman.generateLookupCode(encodings));
		Huffman.readFromFileAndDoWork(inputPath, writeEncodedFile);
		writeEncodedFile.writeEndOfFile();
	}

	
	protected static void writeDecodedFile(String inputPath, String outputPath) {
		DecompressedFileWriteHelper writeDecodedFileWorker = new DecompressedFileWriteHelper(outputPath);
		Huffman.readFromBinaryFileAndDoWork(inputPath, writeDecodedFileWorker);
	}

	
	public static String rightPadString(String input, int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		try {
			return sb.toString().substring(input.length()) + input;
		} catch (StringIndexOutOfBoundsException e) {
			return input;
		}
	}

	public static String leftPadString(String input, int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		try {
			return input + sb.toString().substring(input.length());
		} catch (StringIndexOutOfBoundsException e) {
			return input;
		}
	}

	
	private static ArrayList<HuffmanTuple> extractEncodings(Node root) {
		ArrayList<HuffmanTuple> list = new ArrayList<>();
		Huffman.performInorderTraversal(root, "", list);
		return list;
	}

	
	private static void readFromFileAndDoWork(String filePath, FileContentReader handler) {
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
			int currentByte;
			while ((currentByte = br.read()) != -1) {
				handler.doWork(currentByte);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file with the given path of: " + filePath);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readFromBinaryFileAndDoWork(String filePath, FileContentReader handler) {
		try (DataInputStream ds = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)))) {
			int currentByte;
			while((currentByte = ds.read()) != -1) {
				handler.doWork(currentByte);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file with the given path of: " + filePath);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
