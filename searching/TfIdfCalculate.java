package searching;

import code_clone.getTfIdf;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class TfIdfCalculate {

    ArrayList<String> allterms = new ArrayList<>();
    ArrayList<String> FileWord = new ArrayList<>(); //store all files one by one
    ArrayList<String[]> FilesWords = new ArrayList<>(); //store all files one by one as a String array
    HashMap<String, Double> idfmap = new HashMap<>();
    public static ArrayList<String> queryTerms = new ArrayList<>();
    public static ArrayList<double[]> queryTfIdfVector = new ArrayList<>();
    public static ArrayList<double[]> tfidfvectorProject = new ArrayList<>();

    public void fileRead(String path) throws IOException {

        File directoryPath = new File(path);
        File fileList[] = directoryPath.listFiles();
        for (File file : fileList) {

            if (file.getName().endsWith(".txt")) {
              //  System.out.println("" + file.getName());
                String filePath = path + "\\" + file.getName();
                Path p = Paths.get(filePath);
                byte[] filecontent = Files.readAllBytes(p);
                String fileContent = new String(filecontent, StandardCharsets.UTF_8).trim();
                //  System.out.println(""+fileContent);
                String[] allterm = fileContent.split(" ");
                //  for(int i=0;i<allterm.length;i++){
                //     System.out.println(""+allterm[i]);
                //    }
                for (String terms : allterm) {
                    // System.out.println(""+terms);
                    if (!allterms.contains(terms)) {
                        allterms.add(terms);
                    }
                }
                FileWord.add(fileContent.trim());
                FilesWords.add(allterm);
            }

        }

    }

    public void Idfcal() {
        double idf;
        for (String term : allterms) {
            idf = new getTfIdf().getIdf(FileWord, term);
            idfmap.put(term, idf);

        }
//for(HashMap.Entry<String,Double> pair:idfmap.entrySet()){
        //   System.out.println(pair.getKey()+"="+pair.getValue());
//}
    }

    public void UniqueQueryTerms(String processQuery) {
        String[] queryTerm = processQuery.trim().split(" ");
        for (String term : queryTerm) {
            //     System.out.println("term=" + term);
            if (!queryTerms.contains(term)) {
                // System.out.println("p=="+term);
                queryTerms.add(term);
            }

        }

    }

    public void ProjectTfIdfCal() {

        double tf;
        double idf;
        double tfidf;

        for (String[] fileword : FilesWords) {
            int count = 0;

            double[] tfidfvector = new double[queryTerms.size()];
            for (String term : queryTerms) {
                tf = new getTfIdf().getTf(fileword, term);
                if (idfmap.containsKey(term)) {
                    idf = idfmap.get(term);

                } else {
                    idf = 0;
                }
                tfidf = tf * idf;
                //  System.out.println("pro="+tfidf);
                tfidfvector[count] = tfidf;
                count++;

            }
            tfidfvectorProject.add(tfidfvector);
        }

    }

    public void queryTfIdfCal(String processQuery) {
        String[] queryTerm = processQuery.trim().split(" ");
        double Tf;
        double Idf = 0;
        double queryTfIdf = 0;
        int count = 0;
        double[] queryvector = new double[queryTerms.size()];
        for (String q : queryTerms) {
            //  System.out.println("q=" + q);
            Tf = new getTfIdf().getTf(queryTerm, q);
            //  System.out.println("tf="+Tf);
            if (idfmap.containsKey(q)) {
                Idf = idfmap.get(q);
                //     System.out.println("idf="+Idf);
            } else {
                Idf = 0;

            }
            queryTfIdf = Tf * Idf;
            //   System.out.println("tfidf=" + queryTfIdf);
            queryvector[count] = queryTfIdf;
            count++;
        }
        // System.out.println("p");
        queryTfIdfVector.add(queryvector);

    }

}
