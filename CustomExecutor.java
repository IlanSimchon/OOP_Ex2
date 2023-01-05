import java.util.concurrent.*;

public class  CustomExecutor <T> extends ThreadPoolExecutor {

    private final PriorityBlockingQueue<Task<T>> queue;
    private int maxPriority;
    private boolean isActive;

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue(11,(t1 , t2 )->
                Integer.compare(((Task<T>)t1).getType().getPriorityValue(), ((Task<T>)t2).getType().getPriorityValue())));
        queue = new PriorityBlockingQueue<Task<T>>();
        this.maxPriority = -1;
        this.isActive = true;
    }


    public void insertTask(Task<T> task){
        if (task != null && isActive) {
            queue.put(task);
            maxPriority = Math.max(maxPriority, task.getType().getPriorityValue());
        }
    }
    public void insertCallable(Callable<T> callable){
        if(callable != null && isActive){
            Task<T> task = Task.Factory(callable);
            insertTask(task);
        }
    }

    public void insertCallable(Callable<T> callable, TaskType type){
        if(callable != null && isActive){
            Task<T> task = Task.Factory(callable, type);
            insertTask(task);
        }
    }
    public int getMaxPriority() {
        return maxPriority;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        isActive = false;
    }

    public Future<T> submit() {
        Task<T> task = queue.remove();
        return (Future<T>) super.submit(task.getCallable());
    }

}


