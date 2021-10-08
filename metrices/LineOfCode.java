package metrices;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LineOfCode {
    public static double totalLineOfProject;
public int countLines(String filename) throws IOException {
    InputStream is = new BufferedInputStream(new FileInputStream(filename));
    try {
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        boolean endsWithoutNewLine = false;
        while ((readChars = is.read(c)) != -1) {
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n')
                    ++count;
            }
            endsWithoutNewLine = (c[readChars - 1] != '\n');
        }
        if(endsWithoutNewLine) {
            ++count;
        }
        totalLineOfProject+=count;
        return count;
    } finally {
        is.close();
    }
}}
