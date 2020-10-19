package com.atguigu.juc1205;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：现在两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1
 * 实现交替，来10轮，变量初始值为零
 * 当两个线程生产，两个消费时会有问题（虚假唤醒，将判断if换成while）
 * (一个线程生产，一个线程消费，等待不是加就是减；两个线程生产，两个线程消费，
 * 等待有加也有减)
 * 多线程编程(生产者与消费者:生产一个消费一个)：
 * 1、高聚低合前提下，线程操作资源类(实例变量+实例方法)
 * 2、判断\干活\通知
 * 3、防止多线程的虚假唤醒
 */
public class ProdConsumerDemo04 {

    /**
     * 空调类
     */
    static class OldAirCondition {

        private int number = 0;

        /**
         * synchronized 旧版本写法
         * 制热
         */
        synchronized void increment() throws InterruptedException {
            //1、判断（用while会将number拉回来重新判断）
            while (number != 0) {
                //等待
                this.wait();
            }

            //2、干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);

            //3、通知(唤醒)
            this.notifyAll();
        }

        /**
         * 制冷
         */
        synchronized void decrement() throws InterruptedException {
            //1、判断（多线程wait判断不应用if,应用while,防止虚假唤醒、while相当于循环加判断）
            while (number == 0) {
                //等待
                this.wait();
            }

            //2、干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);

            //3、通知(唤醒)
            this.notifyAll();
        }

    }

    /**
     * 空调类
     */
    static class NewAirCondition {

        private int number = 0;

        //可重入，默认非公平，递归锁
        private Lock lock = new ReentrantLock();

        private Condition condition = lock.newCondition();

        /**
         * synchronized 旧版本写法
         * synchronized 对应wait、notify、notifyAll
         * Lock 对应Condition的await、signal、signalAll
         * 制热
         */
        void increment() throws InterruptedException {
            //加锁
            lock.lock();

            try {
                //1、判断（用while会将number拉回来重新判断）
                while (number != 0) {
                    //等待
                    condition.await();
                }

                //2、干活
                number++;
                System.out.println(Thread.currentThread().getName() + "\t" + number);

                //3、通知(唤醒)
                condition.signalAll();
            }finally {
                //释放锁
                lock.unlock();
            }
        }

        /**
         * 制冷
         */
        void decrement() throws InterruptedException {
            //加锁
            lock.lock();

            try {
                //1、判断（多线程wait判断不应用if,应用while,防止虚假唤醒、while相当于循环加判断）
                while (number == 0) {
                    //等待
                    condition.await();
                }

                //2、干活
                number--;
                System.out.println(Thread.currentThread().getName() + "\t" + number);

                //3、通知(唤醒)
                condition.signalAll();
            }finally {
                //释放锁
                lock.unlock();
            }
        }

    }

    /**
     * 总结：多线程编程套路 + while + 现版本写法
     */
    public static void main(String[] args) {
        //旧版本同步方法
        oldAirCondition();

        //新版本同步方法
        newAirCondition();
    }

    private static void oldAirCondition() {
        //两个线程正确，四个线程错误(导致线程虚假唤醒，将if换成while)
        OldAirCondition oldAirCondition = new OldAirCondition();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    oldAirCondition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    oldAirCondition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    oldAirCondition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    oldAirCondition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();

    }

    private static void newAirCondition() {
        //两个线程正确，四个线程错误(导致线程虚假唤醒，将if换成while)
        NewAirCondition newAirCondition = new NewAirCondition();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    newAirCondition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    newAirCondition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    newAirCondition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    newAirCondition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();

    }

}
