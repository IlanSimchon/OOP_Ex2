import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * A class representing a task to be executed by a thread pool.
 * The task can be created with a specific type, that can be used to give priority to certain tasks over others.
 *
 * @param <T> the type of the result of the callable task.
 */
public class Task<T>   implements Comparable<Task<T>> , Callable<T> {
    private final Callable<T> callable;
    private final TaskType type;

    /**
     * Private constructor to constructs a new Task with the given callable and type.
     *
     * @param callable the callable task to be executed
     * @param type the type of the task, used to give priority to certain tasks over others.
     */
    private Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    /**
     * Create a new task using the private constructor of the given callable without specifying a type.
     *
     * @param callable the callable task to be executed
     * @param <V> the type of the result of the callable task.
     * @return new Task instance
     */
    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    /**
     * Create a new task using the private constructor of the given callable and with the given type.
     *
     * @param callable the callable task to be executed
     * @param type the type of the task, used to give priority to certain tasks over others.
     * @param <V> the type of the result of the callable task.
     * @return new Task instance
     */
    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    public T call() {

        try {
            return callable.call();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Get the type of this task
     *
     * @return the type of the task
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Get the callable of this task
     *
     * @return the callable of the task
     */
    public Callable<T> getCallable() {
        return callable;
    }

    /**
     * Compare the priority of this task with another task
     *
     * @param other the other task to compare with
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Task<T> other) {
        return (-1) * Integer.compare(this.type.getPriorityValue(), other.getType().getPriorityValue());
    }

}

