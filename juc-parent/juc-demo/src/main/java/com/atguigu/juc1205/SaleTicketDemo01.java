package com.atguigu.juc1205;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 资源类 = 实例变量 + 实例方法
 */
class Ticket {

    //票数
    private int number = 30;

    //重入锁
    private Lock lock = new ReentrantLock();

    //操作
    void sale() {
        //加锁
        lock.lock();

        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t卖出第：" + (number--) + "\t还剩下：" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            lock.unlock();
        }
    }

}

/**
 * 题目：三个售票员   卖出    30张票
 * 在高内聚低耦合的前提下：线程    操作   资源类
 */
public class SaleTicketDemo01 {

    //线程
    public static void main(String[] args) {
        //资源类
        Ticket ticket = new Ticket();

        new Thread(() -> { for (int i = 0; i < 40; i++) ticket.sale();}, "A").start();

        new Thread(() -> { for (int i = 0; i < 40; i++) ticket.sale();}, "B").start();

        new Thread(() -> { for (int i = 0; i < 40; i++) ticket.sale();}, "C").start();
    }

}
