import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

public class Ex2_1 {

    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
        int n = 10000;
        String[] files = createTextFiles(n, 7, 99999);
        System.out.println("Total " + getNumOfLines(files) + " lines");
        System.out.println("Total " + getNumOfLinesThreads(files)+ " lines");
        System.out.println("Total " + getNumOfLinesThreadPool(files)+ " lines");

        remove(n);
    }

public static void remove(int capacity)  {
    for (int i = 0; i < capacity; i++) {
        File file = new File("file_" + i + ".txt");
        file.delete();

    }
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
                System.err.println("error while crateing file: " + names[i]);
                e.printStackTrace();
            }

        }
        return names;
    }


    public static int getNumOfLines(String[] fileNames) throws FileNotFoundException {
        long startingTime = System.currentTimeMillis();
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
                ex.printStackTrace();
            }
        }

        long currentTime = System.currentTimeMillis();
        System.out.println("The time without using thread is: " + (currentTime - startingTime)/1000 + " Seconds");
        return count;
    }

    public static int getNumOfLinesThreads(String[] fileNames) {
        long startingTime = System.currentTimeMillis();
        int count = 0;
        countingLinesThread[] threads = new countingLinesThread[fileNames.length];


        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new countingLinesThread(fileNames[i]);
        }
        for (countingLinesThread countingLinesThread : threads) {
            countingLinesThread.start();
        }
        for (countingLinesThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("exception in 'join' ");
                e.printStackTrace();
            }
            count += thread.getSum();
        }
        long currentTime = System.currentTimeMillis();
        System.out.println("The time using Threads is: " + (currentTime - startingTime)/1000 + " Seconds");
        return count;

    }



    public static int getNumOfLinesThreadPool(String[] fileNames) throws ExecutionException, InterruptedException {
        long startingTime = System.currentTimeMillis();
        int numOfFiles = fileNames.length;
        int count = 0;
        myCallable[] threads = new myCallable[numOfFiles];
        for (int i = 0; i < numOfFiles; i++) {
            threads[i] = new myCallable(fileNames[i]);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(numOfFiles);
        for (int i = 0; i < threads.length; i++) {
            Future<Integer> result = executorService.submit(threads[i]);
            count += result.get();
        }
       executorService.shutdown();

            long currentTime = System.currentTimeMillis();
            System.out.println("The time using Threadpool is: " + (currentTime - startingTime)/1000 + " Seconds");
            return count;
    }
}
