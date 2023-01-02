import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class countingLinesThread extends Thread{
    private String fileName;
    private int[] ans;
    private int index;
    countingLinesThread(String fileName, int [] ans , int index){
        this.fileName = fileName;
        this.ans = ans;
        this.index = index;

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
            ans[index] = counter;
        } catch (FileNotFoundException e) {
            System.err.println("could not open file: " + fileName);
        }
    }
}
