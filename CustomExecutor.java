import jdk.jfr.Timestamp;

import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor {

    private final PriorityBlockingQueue<Runnable> queue;
    private int maxPriority;

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>());
        this.queue = (PriorityBlockingQueue<Runnable>) getQueue();
        this.maxPriority = Integer.MIN_VALUE;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof Task) {
            Task task = (Task) r;
            queue.put(task);
            maxPriority = Math.max(maxPriority, task.getType().getPriorityValue());
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) { // not sure about this submit. may be void
        Task<T> task = Task.of(callable);
        return (Future<T>) super.submit(task);
    }

    public <T> Future<T> submit(Callable<T> callable, TaskType type) {
        Task<T> task = Task.of(callable, type);
        return (Future<T>) super.submit(task);
    }

    public int getMaxPriority() {
        return maxPriority;
    }
}


