import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyFutureTask<T> extends FutureTask implements Runnable ,Comparable<MyFutureTask<T>>{
    private Task<T> task;

    public MyFutureTask(Callable callable) {
        super(callable);
        task = null;
    }
    public MyFutureTask(Task<T> t){
        super(t.getCallable());
        task = t;
    }
    public Task<T> getTask(){
        return task;
    }
    public int compareTo(MyFutureTask<T> m){
        return task.compareTo(m.getTask());
    }

}
