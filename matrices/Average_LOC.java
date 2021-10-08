
package metrices;

import IO.ProjectReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Average_LOC {
    public void totalClass(String path){
    double average;
        try{
    ProjectReader.fileRead(path, 0);
     int totalClass=ProjectReader.classCount;
     for(int i=0;i<ProjectReader.filename.size();i++){      
         new LineOfCode().countLines(ProjectReader.filename.get(i));
     
     
     }
     
   average=(float) ((LineOfCode.totalLineOfProject)/(ProjectReader.filename.size()));
    BigDecimal bd = new BigDecimal(average).setScale(2, RoundingMode.HALF_UP);
    double val2 = bd.doubleValue();
            System.out.println("\tAverage LOC in a class:"+val2);
            ProjectReader.classCount=0;
            ProjectReader.filename.clear();
            LineOfCode.totalLineOfProject=0; 
    }catch(Exception e){
       e.printStackTrace();
        
        
    }}
}
