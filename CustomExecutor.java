import java.util.HashMap;
import java.util.concurrent.*;

public class  CustomExecutor <T> extends ThreadPoolExecutor {

    private boolean isActive;

    private int[] count;

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue(1000,(t1 , t2 )->
                Integer.compare(((Task<T>)t1).getType().getPriorityValue(), ((Task<T>)t2).getType().getPriorityValue())));
        this.isActive = true;
    count = new int[4];
    }



//    public Future<T> submit(Task<T> task) {
//        if(isActive) {
//            count[task.getType().getPriorityValue()] ++;
//            Future<T> answer = super.submit(task);
//            return answer;
//        }
//        return null;
//    }
        public <T> Future<T> submit(Callable<T> callable){
        if(callable != null && isActive){
            if(callable instanceof Task<T>){
                Task<T> task = (Task<T>)callable;
                count[task.getType().getPriorityValue()] ++;
                return super.submit(task);
            }
            Task<T> task = (Task<T>) Task.createTask(callable);
            return submit(task);
        }
        return null;
    }

    public <T> Future<T> submit(Callable<T> callable , TaskType type){
        if(callable != null && isActive){
            Task<T> task = Task.createTask(callable , type);
            return (Future<T>)submit(task);
        }
        return null;
    }
//@Override
//public void run(ThreadPoolExecutor t){

//}
// to change!!
//
//    public <T> Future<T> submit(Callable<T> callable){
//        if(callable != null && isActive){
//            Task<T> task = (Task<T>) Task.createTask(callable);
//            return submit(task);
//        }
//        return null;
//    }
//
//    public <T> Future<T> submit(Callable<T> callable , TaskType type){
//        if(callable != null && isActive){
//            Task<T> task = Task.createTask(callable , type);
//            return (Future<T>)submit(task);
//        }
//        return null;
//    }



    public int getCurrentMax() {
        for (int i = 1; i < 4; i++) {
            if (count[i] != 0)
                return i;
        }
            return -1;
    }


//    @Override
//    public boolean remove(Runnable task){
//        for (int i = 1; i < count.length; i++) {
//            System.out.print(", "+ count[i]);
//            if (count[i] != 0) {
//                System.out.println(", "+ count[i]);
//                count[i]--;
//                return super.remove(task);
//            }
//        }
//        return false;
//    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        for (int i: count)
            System.out.print(i +"|");
        for (int i = 1; i < count.length; i++) {
            if (count[i] > 0) {
                count[i]--;
                System.out.println("");
                for (int j: count)
                    System.out.print(j +", ");
                System.out.println("\n*********************");
                //super.beforeExecute(t,r);
            }
        }
    }

    public void gracefullyTerminate() {
        isActive = false;
        super.shutdown();
    }
}


