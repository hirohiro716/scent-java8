package test;

import java.util.concurrent.Callable;

import com.hirohiro716.thread.Task;

@SuppressWarnings("all")
public class TestTaskHelper {

    public static void main(String[] args) throws Exception {

        Task<String> task = new Task<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "aho";
            }
        });

        task.start();
        Thread.sleep(3000);

        System.out.println(task.getResult());

    }

}
