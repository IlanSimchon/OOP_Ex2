import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class countingLinesThread extends Thread{
    private String fileName;
    private long sum;

    countingLinesThread(String fileName, int sum){
        this.fileName = fileName;
        this.sum = sum;
    }
    @Override
    public void run() {
        File file = new File(fileName);
        int counter = 0;
        try {
            Scanner sc =new Scanner(file);
            while (sc.hasNextLine()){
                sc.nextLine();
                counter++;
            }
            sc.close();
            sum += counter;
        } catch (FileNotFoundException e) {
            System.err.println("could not open file: " + fileName);
        }
    }
}
