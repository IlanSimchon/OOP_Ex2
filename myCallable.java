import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.*;

public class myCallable implements Callable {

    private String name;
    public myCallable(String name) {
        this.name = name;
    }

    @Override
    public Object call() throws Exception {
        File file = new File(name);
        int counter = 0;
        try {
            Scanner sc =new Scanner(file);
            while (sc.hasNextLine()){
                sc.nextLine();
                counter++;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("could not open file: " + name);
        }
        return counter;
    }
}

