package com.atguigu.juc1205;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData {

    //A:1  B:2  C:3 标志位
    private int number = 1;

    //一把锁配多把备用钥匙，精确定位
    private Lock lock = new ReentrantLock();

    private Condition condition1 = lock.newCondition();

    private Condition condition2 = lock.newCondition();

    private Condition condition3 = lock.newCondition();

    void print5() {
        lock.lock();

        try {
            //1、判断
            while (number != 1) {
                //wait
                condition1.await();
            }

            //2、干活
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }

            //3、通知(如何通知B)
            number = 2;

            //通知B
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void print10() {
        lock.lock();

        try {
            //1、判断
            while (number != 2) {
                //wait
                condition2.await();
            }

            //2、干活
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }

            //3、通知(如何通知C)
            number = 3;

            //通知C
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void print15() {
        lock.lock();

        try {
            //1、判断
            while (number != 3) {
                //wait
                condition3.await();
            }

            //2、干活
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }

            //3、通知(如何通知B)
            number = 1;

            //通知A
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 精准通知线程
 * 备注：多线程之间的顺序调用，实现A->B->C
 * 题目：三个线程启动，要求如下：
 * AA打印5次，BB打印10次，CC打印15次，接着：
 * AA打印5次，BB打印10次，CC打印15次
 * 来10轮
 */
public class ConditionDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareData.print5();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareData.print10();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                shareData.print15();
            }
        }, "C").start();
    }

}
