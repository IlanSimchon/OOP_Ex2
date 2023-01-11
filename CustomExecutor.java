import java.util.concurrent.*;
/**

 CustomExecutor is a ThreadPoolExecutor with additional functionality to track the number of tasks submitted with a certain priority,

 and the ability to gracefully shut down the executor.
 */

public class CustomExecutor<T> extends ThreadPoolExecutor {

    private boolean isActive;
    /**

     count[i] tracks the number of tasks submitted with priority i
     */
    private int[] count;

    /**
     *
     * @return True if the threadPool is not Terminated
     */
    public boolean isActive() {
        return isActive;
    }


    /**
     Initializes a new CustomExecutor with the following parameters:
     Core pool size: half of the available processors
     Maximum pool size: the number of available processors minus 1
     Keep-alive time: 300 milliseconds
     The task queue is a PriorityBlockingQueue, which prioritizes tasks based on their priority
     */

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue(100, (mft1, mft2) ->
                        ((MyFutureTask) mft1).compareTo((MyFutureTask) mft2))
        );
        this.isActive = true;
        count = new int[11];
        for (int i = 0; i < 11; i++) {
            count[i] = 0;
        }
    }

    /**
     Submits a new task with a given priority
     @param callable the callable task to execute
     @param type the type of task, used to determine its priority
     @return a Future representing the task
     */
    public  Future<T> submit(Callable<T> callable, TaskType type) {
        if (callable != null && isActive) {
            Task<T> task = Task.createTask(callable, type);
            return (Future<T>) submit((Task<T>) task);
        }
        return null;
    }

    /**
     Submits a new task with the priority determined by its type
     @param task the task to execute
     @return a Future representing the task
     */
    public Future<T> submit(Task<T> task) {
        if (task != null && isActive) {
            count[task.getType().getPriorityValue()]++;
            MyFutureTask<T> ftask = new MyFutureTask(task);
            super.execute(ftask);
            return ftask;
        }
        return null;
    }

    /**
     Submits a new task with default priority
     @param callable the callable task to execute
     @return a Future representing the task
     */
    public Future<T> submit(Callable callable) {
        if (isActive) {
            Task task = Task.createTask(callable);
            return submit(task);
        }
        return null;
    }
    /**

     @return the highest priority, return -1 if the queue is empty
     */

    public int getCurrentMax() {
        for (int i = 1; i < 11; i++) {
            if (count[i] != 0)
                return i;
        }
        return -1;
    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        for (int i = 1; i < count.length; i++) {
            if (count[i] > 0) {
                count[i]--;
            }
        }
    }

    /**
     * Attempts to gracefully terminate an active thread pool.
     * Initiates an orderly shutdown of the thread pool, by preventing new tasks from being submitted
     * while allowing existing tasks to complete
     */
    public void gracefullyTerminate()  {
        if(isActive) {
            isActive = false;
            super.shutdown();
            try {
                if(!super.awaitTermination(100L *(getQueue().size()) , TimeUnit.MILLISECONDS)){
                    if(!super.awaitTermination(300L *(getQueue().size() ) , TimeUnit.MILLISECONDS)){
                        System.out.println("some tasks did not complete )=");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("exception while determination");
            }
        }
    }
}


