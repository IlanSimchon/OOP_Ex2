import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Adapter class representing a FutureTask with the ability to store a reference to a Task,
 * as well as implementing the comparable interface to allow for sorting based on task's priority.
 * the class used to adapt between the Task (witch us Callable) to the priority Queue of
 * ThreadPoolExecutor witch is Runnable Queue.
 *
 * @param <T> the type of the result of the callable task.
 */
public class MyFutureTask<T> extends FutureTask implements Runnable ,Comparable<MyFutureTask<T>>{
    private Task<T> task;

    /**
     * Constructs a new MyFutureTask with the given Callable.
     *
     * @param callable the callable task to be executed
     */
    public MyFutureTask(Callable callable) {
        super(callable);
        task = null;
    }

    /**
     * Constructs a new MyFutureTask with the given task.
     *
     * @param t the task containing callable task to be executed and priority information
     */
    public MyFutureTask(Task<T> t){
        super(t.getCallable());
        task = t;
    }

    /**
     * Get the task of this future task
     *
     * @return the task
     */
    public Task<T> getTask(){
        return task;
    }

    /**
     * Compare the priority of this task with another future task's task
     *
     * @param m the other future task to compare with
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    public int compareTo(MyFutureTask<T> m){
        return task.compareTo(m.getTask());
    }

}
