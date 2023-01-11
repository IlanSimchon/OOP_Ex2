import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;


public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    void TaskType() {
        TaskType tt1 = TaskType.IO;
        assertEquals(2, tt1.getPriorityValue());
        tt1.setPriority(5);
        assertEquals(5, tt1.getPriorityValue());
    }
    @Test
    void Task_constructors(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "| abcd |";
            }
        };
        Task<String> t1 = Task.createTask(callable);
        Task<String> t2 = Task.createTask(callable,TaskType.IO);
        assertEquals(3,t1.getType().getPriorityValue());
        assertEquals(2,t2.getType().getPriorityValue());
        assertEquals("| abcd |",t1.call());
    }
    @Test
    void Task_call(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "| abcd |";
            }
        };
        Task<String> t1 = Task.createTask(callable);
        assertEquals("| abcd |", t1.call());
    }
    @Test
    void Task_get_type(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "| abcd |";
            }
        };
        Task<String> t1 = Task.createTask(callable,TaskType.IO);
        assertEquals(TaskType.IO,t1.getType());
    }
    @Test
    void Task_compare(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "| abcd |";
            }
        };
        Task<String> t1 = Task.createTask(callable,TaskType.IO);
        Callable<String> callable2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "|";
            }
        };
        Task<String> t6 = Task.createTask(callable2,TaskType.IO);
        Task<String> t7 = Task.createTask(callable2,TaskType.COMPUTATIONAL);
        Task<String> t8 = Task.createTask(callable2,TaskType.OTHER);
        assertEquals(0,t1.compareTo(t6));
        assertEquals(1,t7.compareTo(t8));
        assertEquals(-1,t6.compareTo(t7));

    }
    @Test
    void customExecutor_submit(){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                return "| abcd |";
            }
        };

        Task<String> t1 = Task.createTask(callable,TaskType.IO);
        CustomExecutor<String> customExecutor = new CustomExecutor<>();
        assertNotNull(customExecutor.submit(t1));
        assertNotNull(customExecutor.submit(callable));
        assertNotNull(customExecutor.submit(callable,TaskType.COMPUTATIONAL));
        assertEquals(3,customExecutor.getActiveCount());
        assertNull(customExecutor.submit((Task<String>) null));
        assertNull(customExecutor.submit((Callable<String>) null,TaskType.IO));

    }

    @Test
    void getMax() {
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        Task<Integer> task2 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.IO);
        Task<Integer> task3 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.OTHER);
        CustomExecutor<Integer> customExecutor = new CustomExecutor<>();
        for(int i = 0; i< 18;i++){
            customExecutor.submit(task3);
        }
        assertEquals(3, customExecutor.getCurrentMax());
        customExecutor.submit(task3);
        customExecutor.submit(task2);
        assertEquals(2, customExecutor.getCurrentMax());
        customExecutor.submit(task3);
        customExecutor.submit(task2);
        customExecutor.submit(task1);
        assertEquals(1, customExecutor.getCurrentMax());
    }

    @Test
    void answer_of_tasks(){
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        Task<Integer> task2 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.IO);
        Task<Integer> task3 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.OTHER);
        CustomExecutor<Integer> customExecutor = new CustomExecutor<>();
        Future<Integer> f3 = customExecutor.submit(task3);
        Future<Integer> f2 = customExecutor.submit(task2);
        Future<Integer> f1 = customExecutor.submit(task1);
        int answer = 0;
        try {
            answer = f1.get() + f2.get() + f3.get();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, answer);
    }

    @Test
    void gracefullyTerminate(){
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        CustomExecutor<Integer> customExecutor = new CustomExecutor<>();
        for(int i = 0; i < 50; i++){
            customExecutor.submit(task1);
        }
        customExecutor.gracefullyTerminate();
        assertDoesNotThrow( ()-> customExecutor.submit(task1));
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(50,customExecutor.getCompletedTaskCount());
        assertEquals(true,customExecutor.isTerminated());
    }

}

