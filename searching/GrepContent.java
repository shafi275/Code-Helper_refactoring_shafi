
package searching;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
public class GrepContent {

    public String findBetweenBraces(int start, String fileContent) throws FileNotFoundException {
        int i = start;
        int openBraces = 0;
        int closedBraces = 0;
        String result;
        try{
        do {
            Character c = fileContent.charAt(i);
            if (c == '{') {
                openBraces++;

            } else if (c == '}') {
                closedBraces++;

            }
            ++i;
        } while (openBraces == 0 || openBraces != closedBraces);
        }catch(Exception e){
        //  System.out.println(e);
        }
        result = fileContent.substring(start, i);

        // System.out.println("" + result);
        return result;
    }

    /* public int grepLineNumber(String word, String file,int preLine) throws FileNotFoundException, IOException {
        String str;
        int linenumber = 0;
        BufferedReader buf = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
      
        String line;
        int LineNumber = 0;
        while ((line = buf.readLine()) != null) {
            LineNumber++;
            //     System.out.println("pop");
            if (line.contains(word)) {
                //System.out.println("po" + LineNumber);
                linenumber = LineNumber;
                break;
               // System.out.println("" + linenumber);
            }
        }
        //   System.out.println(""+LineNumber);
        return linenumber;
    }*/
    public int getLineNumber(String word, String file, int preLine) throws FileNotFoundException, IOException {
    //    System.out.println(word);
        int lineNumber = 0;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            
            for (String line; (line = reader.readLine()) != null;) {
                if (reader.getLineNumber() > preLine && line.contains(word)) {
                    // System.out.println(reader.getLineNumber() + ": " + line);
                    lineNumber = reader.getLineNumber();
                    break;

                }
            }
        }
        return lineNumber;
    }
}
