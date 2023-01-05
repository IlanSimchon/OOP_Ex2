import java.util.concurrent.Callable;

public class Task<T> implements Comparable<Task<T>>, Runnable {
    private final Callable<T> callable;
    private final TaskType type;

    public Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    public static <V> Task<V> Factory(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    public static <V> Task<V> Factory(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public T call() throws Exception {
        try {
            return callable.call();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
        return null;
    }
    public void run(){
        try {
            this.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public TaskType getType() {
        return type;
    }

    public Callable<T> getCallable() {
        return callable;
    }

    @Override
    public int compareTo(Task<T> other) {
        return Integer.compare(type.getPriorityValue(), other.getType().getPriorityValue());
    }

}

