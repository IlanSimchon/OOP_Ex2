import java.util.concurrent.*;

public class  CustomExecutor <T> extends ThreadPoolExecutor {

    private boolean isActive;

    private int[] count;

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                 new PriorityBlockingQueue(1000,(mft1 , mft2 )->
                         ((MyFutureTask)mft1).compareTo((MyFutureTask) mft2) )
        );
        this.isActive = true;
    count = new int[4];
    }


    public Future<T> submit (Task<T> task)
    {
        if(task != null && isActive) {
            count[task.getType().getPriorityValue()]++;
            MyFutureTask myFutureTask = new MyFutureTask(task);
            MyFutureTask<T> ftask = new MyFutureTask(task);
            super.execute(ftask);
            return ftask;
        }
        return null;
    }

    public <T> Future<T> submit(Callable<T> callable , TaskType type){
        if(callable != null && isActive){
            Task<T> task = Task.createTask(callable , type);
            return (Future<T>)submit( task);
        }
        return null;
    }
    public  Future<T> submit(Callable callable){
        if(isActive) {
            Task task = Task.createTask(callable);
            return submit(task);
        }
        return null;
    }


    public int getCurrentMax() {
        for (int i = 1; i < 4; i++) {
            if (count[i] != 0)
                return i;
        }
            return -1;
    }



    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        for (int i = 1; i < count.length; i++) {
            if (count[i] > 0) {
                count[i]--;
            }
        }
    }

    public void gracefullyTerminate() {
        isActive = false;
        super.shutdown();
    }
}


