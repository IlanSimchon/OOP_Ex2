import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class countingLinesThread extends Thread {
    private String fileName;
    private int sum;

    countingLinesThread(String fileName){
        this.fileName = fileName;
        this.sum = 0;
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

    public int getSum() {
        return sum;
    }
}
