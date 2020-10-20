package com.atguigu.juc1205;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("*****come in call method()");
        return 1024;
    }

}

/**
 * 获取返回值的线程
 */
public class CallableDemo {

    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());

        new Thread(futureTask, "A").start();

        try {
            Integer result = futureTask.get();

            System.out.println(result);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

}
