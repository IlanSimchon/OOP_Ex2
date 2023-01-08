import java.util.concurrent.Callable;

public class Task<T> implements Comparable<Task<T>> , Callable<T>{
    private final Callable<T> callable;
    private final TaskType type;
    //private T answer;

    private Task(Callable<T> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
        //answer = null;
    }

    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public T call() {
        try {
            return callable.call();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
        return null;
    }

//    public void run(){
//        try {
//            answer = this.call();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


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

//    public T getAnswer() {
//        return answer;
//
//    }
}

