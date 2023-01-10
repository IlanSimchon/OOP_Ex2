import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * A class that implements the `Callable` interface and counts the number of lines in a given file.
 */
public class myCallable implements Callable {

    private String name;

    /**
     * Constructs a new myCallable with the given file name.
     *
     * @param name the name of the file to be read
     */
    public myCallable(String name) {
        this.name = name;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return the number of lines in the specified file
     * @throws Exception if an error occurs while reading the file
     */
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

