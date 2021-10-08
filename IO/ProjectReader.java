package IO;

import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProjectReader {

    public static int count;
    public static int classCount;
    public static LinkedHashMap<String, String> projectOne = new LinkedHashMap<>();
    public static LinkedHashMap<String, String> projectTwo = new LinkedHashMap<>();
    public static ArrayList<String> filename = new ArrayList<>();

    public static void fileRead(String fullPath, int i) throws IOException {
        Path folderToWalk = Paths.get(fullPath);
        //  ProjectPath = fullPath;
        Files.walkFileTree(folderToWalk, new SimpleFileVisitor<Path>() {
            // int count = 0;

            @Override
            public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {
                if (f.getFileName().toString().endsWith(".java")) {
                    //   count++;
                    byte[] p = Files.readAllBytes(f);
                    String s = new String(p, StandardCharsets.UTF_8).trim();
                    if (i == 0) {
                        classCount++;
                        String dir = f.getParent().toString().substring(f.getParent().toString().lastIndexOf(File.separator) + 1);

                        //  String fileNamewithPackage = f.getFileName().toString() + "$" + dir+".java";
                        filename.add(f.getParent().toString().trim() + "\\" + f.getFileName());
                        String fileNamewithPackage = dir + "$" + f.getFileName().toString(); //packagename$filename.java
                       //  System.out.println("name="+f.getFileName());
                        projectOne.put(fileNamewithPackage, s);

                    }
                    if (i == 1) {
                        //   project2.add(f.getFileName().toString());
                        String dir = f.getParent().toString().substring(f.getParent().toString().lastIndexOf(File.separator) + 1);
                        String fileNamewithPackage = dir + "$" + f.getFileName().toString();// packagename$filename.java
                        projectTwo.put(fileNamewithPackage, s);
                    }
                }
               
                return FileVisitResult.CONTINUE;
            }
 
        }
        );
       
    }

    public static void getFileList(String projectname, String pathname, ArrayList<String> projectFile) throws IOException {
      
        File folder = new File(pathname);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles.length == 0) {
            // System.out.println("\tThe prject doesnot have any java file");
            folder.delete();
            //   new Command().command();
        }
        count = 0;
        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].toString().endsWith(".txt")) {
                projectFile.add(listOfFiles[i].getName().replaceAll(".{3}$", "java"));
                count++;
            }

        }

    }
}
