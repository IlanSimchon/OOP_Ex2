import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Ex2_1 {

    public static void main(String[] args) {
        createTextFiles(5, 12, 34);
    }


    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] names = new String[n];
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            int num_of_lines = random.nextInt(bound); // how much lines to write
            try {
                FileWriter writeFile = new FileWriter("file_" + i + ".txt");
                PrintWriter outs = new PrintWriter(writeFile);
                for (int j = 0; j < num_of_lines; j++) {
                    outs.println("hello world"); // writing seed times
                }
                outs.close(); // close the files
                writeFile.close();

                names[i] = ("file_" + i + ".txt"); // send the name to array
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return names;
    }


    public static int getNumOfLines(String[] fileNames) throws FileNotFoundException {
        int count = 0;
        for (int i = 0; i < fileNames.length; i++) {
            try {
                File file = new File(fileNames[i]);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    sc.nextLine();
                    count++;

                }
                sc.close();
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return count;
    }


    public int getNumOfLinesThreads(String[] fileNames){
        countingLinesThread [] threads = new countingLinesThread[fileNames.length];
        int [] answers = new int [fileNames.length];

        for (int i = 0; i < fileNames.length; i++){
            threads[i] = new countingLinesThread(fileNames[i],answers , i);
        }
        for (countingLinesThread countingLinesThread : threads) {
            countingLinesThread.start();
        }
        for (countingLinesThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("exception in 'join' ");
            }
        }
        int sum  = 0;
        for (int answer : answers) {
            sum += answer;
        }
        return sum;
    }
//
//    public int getNumOfLinesThreadPool(String[] fileNames){
//
//    }
//}





}