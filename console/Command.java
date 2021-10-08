package console;

import code_clone.CloneCheck;
import huffman.mainDecode;
import huffman.mainEncode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import metrices.Average_LOC;
import metrices.FileCount;
import metrices.LineOfCode;
import metrices.MethodCount;
import searching.Search;

public class Command {

    public static String forwardDir;
    String directoryName = null;
    public static String currentPath = null;
    //  boolean pexist = false;
    //String project;

    Scanner scan = new Scanner(System.in);

    public void command() throws IOException {

        String projectSelect = null;
        while (true) {
            if (currentPath != null) {
                System.out.print("" + currentPath + ">");
            }
            if (currentPath == null) {
                currentPath = getcurrentPath();
                System.out.print("" + currentPath + ">");
            }

            String choice = scan.nextLine().trim();
            Pattern forward = Pattern.compile("(?i)\\b(cd)\\b\\s+(.+)");

            Pattern specialChar = Pattern.compile("[\"*<>\\/://?\\|\\.]+");
            Matcher m = forward.matcher(choice);

            Matcher special = specialChar.matcher(choice);
            boolean specialvalue = special.find();

            boolean wardval = m.find();

            if (choice.equalsIgnoreCase("help")) {
                System.out.println("\t1.clone");
                System.out.println("\t2.File_Compress & File_Decompress");
                System.out.println("\t3.Search");
                System.out.println("\t4.Matrices\n\t\tFile Count-->fc\n\t\tMethod  Count-->mc\n\t\tLine of Code-->loc\n\tAverage line of Code-->a_loc");
                System.out.println("\t5.exit");

            } else if (choice.equalsIgnoreCase("clone") | choice.equalsIgnoreCase("1")) {

                System.out.println("\tSelect two project:");
                projectPath();

            } else if (choice.equalsIgnoreCase("File_Compress & File_Decompress") | choice.equalsIgnoreCase("2")) {
                System.out.println("\t\tFor Compress-->fcom");
                System.out.println("\t\tFor Decompress-->dcom");
            } else if (choice.equalsIgnoreCase("fcom")) {
                new mainEncode().Compress(currentPath);

            } else if (choice.equalsIgnoreCase("dcom")) {
                new mainDecode().Decompress(currentPath);

            } else if (choice.equalsIgnoreCase("search") | choice.equalsIgnoreCase("3")) {
                System.out.print("\tWrite \"query\" and projectname:");
                //   System.out.println("x=" + currentPath);
                Search(currentPath);
                //  projectPath();
            } else if (choice.equalsIgnoreCase("4")| choice.equalsIgnoreCase("Matrices")) {
                System.out.println("\t4.Matrics\n\t\tJava File Count-->fc\n\t\tMethod  Count-->mc\n\t\tLine of Code-->loc\n\t\tAverage LOC of a class");

            } else if (choice.equalsIgnoreCase("mc") | choice.equalsIgnoreCase("method_count")) {
                getMethod(currentPath);

            } else if (choice.equalsIgnoreCase("Line_Of_Code") | choice.equalsIgnoreCase("LOC")) {
                LineCode(currentPath);

            } else if (choice.equalsIgnoreCase("a_loc")|choice.equalsIgnoreCase("average LOc of a Class")) {
                average_Line_of_Project(currentPath);

            } else if (choice.equalsIgnoreCase("fc")| choice.equalsIgnoreCase("File_Cunt")) {
                getTotalClass(currentPath);

            } else if (choice.equalsIgnoreCase("cd")) {
                currentPath = getcurrentPath();

            } else if (Pattern.matches("(?i)\\bcd\\b\\s*\\.\\.", choice)) {
                String result;

                String newPath = currentPath;
                // System.out.println("newpath=" + newPath);
                result = backDirectory(newPath);
                //   }
            } else if (Pattern.matches("(?i)\\bcd\\b\\s*\\\\", choice)) {
                currentPath = currentPath.substring(0, 3);

            } else if (Pattern.matches("((?i)\\bcdrive\\b\\s+(\\w+[:]))", choice)) {

                Matcher drv = Pattern.compile("(?i)\\bcdrive\\b\\s*(\\w+[:])").matcher(choice);
                if (drv.find()) {
                    checkFileExist(drv.group(1).toUpperCase());
                }
            } else if (choice.equalsIgnoreCase("dir")) {
                //  String path;             
                String path;
                if (currentPath == null) {
                    path = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
                } else {

                    path = currentPath;
                    //    System.out.println(currentPath);
                }
                listDirectory(path + "\\");
            } else if (specialvalue == false && wardval == true) {
                forwardDirectory(m.group(2));

            } else if (choice.equalsIgnoreCase("exit") | choice.equals("5")) {
                System.exit(0);
            } else {
                System.out.println("'" + choice + "' is not recognized as a command");
            }
        }
    }

    public String getcurrentPath() {
        String path;

        if (currentPath == null) {
            path = FileSystems.getDefault().getPath("").toAbsolutePath().toString();

        } else {
            path = currentPath;
        }

        return path;

    }

    public void getMethod(String path) throws IOException {      //Count total number of methods of a project/java file
        
        String newpath = pathGenerate(path);
        try {
            System.out.print("\tProject\\File Name:");
            String name = Input();
            String projectPath = newpath + "\\" + name;
            Path filepath = Paths.get(projectPath);
            if (Files.exists(filepath) && Files.isDirectory(filepath) && !name.isEmpty()) {
              //  System.out.println("project");
             new MethodCount().getTotalMethods(projectPath, name);
            } else if (Files.exists(filepath) && !Files.isDirectory(filepath) && !name.isEmpty()) {
                String file = filepath.toString();
                new MethodCount().getTotalMethods(projectPath, name);

            } else {

                System.out.println("The program cannot find '" + name + "'");
            }
        } catch (Exception e) {
            System.out.println("Invalid input");

        }

    }

    public void LineCode(String Currentpath) throws IOException {
        String newpath = pathGenerate(Currentpath);    //count total number of line of a java file
        System.out.print("\tWrite the file name:");
       String fileName=Input();
        String p = newpath + "\\" + fileName.trim();
        try {
            Path path = Paths.get(p);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                int totalLine = new LineOfCode().countLines(path.toString());
               System.out.println("\tLine of " + fileName + " is " + totalLine);
            } else {
                System.out.println("The program cannot find '" + fileName + "'");
            }
        } catch (Exception e) {
            System.out.println("Invalid filename");
        }

    }
  

    public String Input() {
        Scanner scan = new Scanner(System.in);
        String projectName = scan.nextLine().trim();
        return projectName;

    }

    public void getTotalClass(String currenctpath) {    //Count total number of class of a projct
        String newpath = pathGenerate(currenctpath);
        System.out.print("\tWrite Project name:");
        String projectName = Input();
        String path = newpath + "\\" + projectName;
        try {
            Path p = Paths.get(path);
            if (Files.exists(p) && Files.isDirectory(p) && !projectName.isEmpty()) {
             new FileCount().classCount(path);

            } else if (Files.exists(p) && !Files.isDirectory(p)) {
                System.out.println("Invalid project name");
            } else {
                System.out.println("The program cannot find '" + projectName + "'");
            }
        } catch (Exception e) {
            System.out.println("Invalid project name");
        }

    }

    public void average_Line_of_Project(String currentpath) { //average line of code of a class
        String newpath = pathGenerate(currentpath);        
        System.out.print("\tWrite the project name:");
        String projectName = Input();
        String path = newpath + "\\" + projectName;

        try {
            Path p = Paths.get(path);        
            if (Files.exists(p) && Files.isDirectory(p) && !projectName.isEmpty()) {
                new Average_LOC().totalClass(path);

            } else if (Files.exists(p) && !Files.isDirectory(p)) {
                System.out.println("Invalid project name");
            } else {
                System.out.println("The program cannot find '" + projectName + "'");
            }
        } catch (Exception e) {
            System.out.println("Invalid projectname");
        }

    }

    public void Search(String path) {
        String newpath = pathGenerate(path);
        String queryWithFile = scan.nextLine().trim();
        try {
            String query = queryWithFile.substring(queryWithFile.indexOf("\"") + 1, queryWithFile.lastIndexOf("\"")).trim();
            String projectName = queryWithFile.substring(queryWithFile.lastIndexOf("\"") + 1).trim();
            String p = newpath + "\\" + projectName;
            // System.out.println(p);
            Path filepath = Paths.get(p);
            if ((projectName.isEmpty() && !query.isEmpty()) || (query.isEmpty() && !projectName.isEmpty())) {
                System.out.println("Wrong Command");
            } else if (query.isEmpty() && projectName.isEmpty()) {
                System.out.println("Wrong Command");
            } 
            else{
              if(Files.exists(filepath)){
                 SearchResult(newpath, projectName, query);
              
              }else {
                    System.out.println("The program cannot find '" + projectName + "'");
                }
            
            }
        /*    else {
                if (Files.exists(filepath) && Files.isDirectory(filepath)) {
                    SearchResult(newpath, projectName, query);
                } else if (Files.exists(filepath) && !Files.isDirectory(filepath)) {
                    SearchResult(newpath, projectName, query);
                } 
                else {
                    System.out.println("The program cannot find '" + projectName + "'");
                }
            }*/

        } catch (Exception e) {
            
          System.out.println("Wrong Command");
        }//catch (Exception e) {
        //   e.printStackTrace();
        // }

    }

    public void SearchResult(String path, String projectName, String query) throws IOException {
        String projectPath = path + "\\" + projectName;
        new Search().processProject(projectPath, projectName);
        new Search().SearchingResult(query, projectPath);  //calculate similarity

    }

    public String pathGenerate(String path) {
        // System.out.println("p="+path);          //remove the last backslash from a path 
        String newPath = path;
        if (path.endsWith("\\")) {
            newPath = path.substring(0, path.length() - 1);
            //  System.out.println("new=" + newPath);
        }
        return newPath;

    }

    public void projectPath() throws IOException {         
        ArrayList<String> projectList = new ArrayList<>(2);
        System.out.print("\t\tFirst Project:");
       try{
        String Firstproject =Input();
            //  String FirstprojectPath = currentPath + "\\" + Firstproject;
          //   File project1=new File(FirstprojectPath);
        if (Firstproject.length() == 0 | Firstproject.contains(".")) {
            System.out.println("\tInvalid project name");
            command();
        }
   
        projectExist(Firstproject);
        System.out.print("\t\tSecond Project:");

        String SecondProject = Input();
      //   String SecondprojectPath = currentPath + "\\" + SecondProject;
            // File project2=new File(SecondprojectPath);
        if (SecondProject.length() == 0 | SecondProject.contains(".")) {
            System.out.println("\tInvalid project name");
            command();
        }
           
             
        projectExist(SecondProject);
       
        
        if (projectExist(Firstproject) && projectExist(SecondProject) && (!(Firstproject.isEmpty() | SecondProject.isEmpty()))) {

            projectList.add(Firstproject);
            projectList.add(SecondProject);
            CloneCheck ob1 = new CloneCheck();

            ob1.Code_clone(Firstproject, SecondProject);
            projectList.clear();
        }  else {

            System.out.println("Wrong command");
        }}catch(Exception e){
        
        }

    }

    public boolean projectExist(String projectName) throws IOException {
        boolean exist = false;
        String projectPath = currentPath + "\\" + projectName;
   
        
        try {
            Path path = Paths.get(projectPath);
          
            if (Files.exists(path)) {
               exist = true;
            }      
            else {
                System.out.println("The program cannot find project '" + projectName + "'");
                //   exist=false;
                command();

            }
        } catch (Exception e) {
            System.out.println("Invalid Input");
            command();
        }
        return exist;
    }

    public String backDirectory(String newpath) {
        String result = null;
        if (newpath.length() > 3) {    //show stringIndexoutofboundException
            result = newpath.substring(0, newpath.lastIndexOf("\\") + 1);
            if (result.endsWith("\\") && result.length() > 3) {
                result = result.substring(0, result.length() - 1);
            }
            currentPath = result;
            forwardDir = result;
        } else {
            currentPath = newpath;
            forwardDir = currentPath;

        }
        return currentPath;
    }

    public void forwardDirectory(String dirName) {
        String forwardPath = null;

        String result = null;
        if (currentPath == null) {
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            forwardPath = path.toString().concat("\\" + dirName);
            checkFileExist(forwardPath);

        } else {
            forwardPath = currentPath + "\\" + dirName;

            checkFileExist(forwardPath);

        }

    }

    public void checkFileExist(String path) {
        Path p1 = Paths.get(path);
        try {
            if (Files.exists(p1)) {
                forwardDir = p1.toString();
                currentPath = p1.toString();
                currentPath = setDirectory(forwardDir).toString();
                if (currentPath.length() < 3) {
                    currentPath = setDirectory(forwardDir).toString() + "\\";
                } else {
                    currentPath = setDirectory(forwardDir).toString();
                }
                // System.out.println(currentPath);
                forwardDir = currentPath;

            } else {
                System.out.println("The program cannot find the path specified.");

            }
        } catch (Exception e) {
            System.out.println("Invalid path");
        }

    }

    public File setDirectory(String s) {
        File file = new File("");
        System.setProperty("user.dir", "\\" + s);
        // System.out.println("" + file.getAbsolutePath());

        return file.getAbsoluteFile();
    }

    public void listDirectory(String path) throws IOException {
        File f = new File(path);
        Path file = Paths.get(path);
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        int dircount = 0;
        int filecount = 0;
        File[] flist = f.listFiles();
        if (flist.length != 0) {
            for (File filee : flist) {
                if (filee.isDirectory()) {
                    System.out.println(attr.creationTime() + "\t <DIR>\t\t" + filee.getName());
                    dircount++;
                } else {
                    System.out.println(attr.creationTime() + "\t \t\t" + filee.getName());
                    filecount++;
                }
            }
            System.out.println("\t" + dircount + " Dir(s)");
            System.out.println("\t" + filecount + " File(s)");
        } else {
            System.out.print("Empty directory");
        }
    }

    public static void main(String[] args) throws IOException {

        Command ob = new Command();
        ob.command();
    }
}
