package IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Filewriter {
  
    public String createProcessFile(String filename, String fileContent,String path) throws IOException {
      Path p=Paths.get(path);    
        String newFilename = filename.replaceAll(".{5}$", ".txt");
        Path newpath = Paths.get(path);
        BufferedWriter br = new BufferedWriter(new FileWriter(path + "//" + newFilename));
        br.write(fileContent);
        br.close();
        return path;
    }

}
