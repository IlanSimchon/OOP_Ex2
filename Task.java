import java.util.concurrent.Callable;

public class Task<T> implements Comparable < Task<T> > , Runnable {
    private final Callable<T> callable;
    private final TaskType type;
    private T result;
    private Exception exception;

    public Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    public static <V> Task<V> of(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    public static <V> Task<V> of(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public T getResult() throws Exception {
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    public TaskType getType() {
        return type;
    }

    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            exception = e;
        }
    }

    @Override
    public int compareTo(Task<T> other) {
        return Integer.compare(type.getPriorityValue(), other.getType().getPriorityValue());
    }

}

