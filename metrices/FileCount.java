package metrices;

import IO.ProjectReader;

public class FileCount {

    public int classCount(String path) {
        int totalClass=0;

        try {
            ProjectReader.fileRead(path, 0);
            totalClass = ProjectReader.classCount;
            System.out.println("\tTotal number of file:" + totalClass);
            ProjectReader.classCount = 0;
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }
        return totalClass;
    }
}
