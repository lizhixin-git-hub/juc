package com.atguigu.juc1205;

import java.util.concurrent.TimeUnit;

/**
 * 8种锁机制
 * 1、一个对象里面如果有多个synchronized方法，某个时刻内，只要一个线程去调用其中的一个
 *  synchronized方法了，其他的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程
 *  去访问这些synchronized方法，锁的是当前对象this（对象锁），被锁定后，其他的线程都不能进入到当前
 *  对象的其他的synchronized方法。
 * 2、加个普通方法后发现与同步锁无关
 * 3、换成两个对象后，不是同一把锁，情况立刻变化。
 * 4、都换成静态同步方法后，情况又变化。
 * 5、所有的非静态同步方法用的都是同一把锁--实例对象本身。
 * 6、synchronized实现同步锁的基础：Java中的每一个对象都可以作为锁。具体表现为3中形式：
 *   1）对于普通同步方法，锁是当前实例对象。
 *   2）对于同步方法块，锁是synchronized括号里配置的对象。synchronized(this){}
 *   3）对于静态同步方法，锁是当前类的Class对象
 * 7、当一个线程试图访问同步代码块时，它首先必须得到锁，退出或抛出异常时必须释放锁，
 * 也就是说如果一个实例对象的非静态同步方法获取锁后，该实例对象的其他非静态同步方法必须
 * 等待获取锁的方法释放锁后才能获取锁。
 * 可是别的实例对象的非静态同步方法因为跟该实例对象的非静态同步方法用的是不同的锁，所以毋须等待
 * 该实例对象已获取锁的非静态同步方法释放锁就可以获取他们自己的锁。
 * 8、所有的静态同步方法用的也是同一把锁--类对象本身。
 * 这两把锁是两个不同的对象，所以静态同步方法与非静态同步方法之间是不会有竞态条件的。
 * 但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获得锁。
 * 而不管是同一个实例对象的静态同步方法之间，
 * 还是不同的实例对象的静态同步方法之间，只要它们同一个类的实例对象！
 */
class Phone {

    //5、6、7、8 static（全局锁）
    synchronized void sendEmail() throws Exception {
        //1、TimeUnit.SECONDS.sleep(4);
        TimeUnit.SECONDS.sleep(4);
        System.out.println("*****sendEmail");
    }

    //5、6、7、8 static（全局锁）
    synchronized void sendSMS() {
        System.out.println("*****sendSMS");
    }

    void sayHello() {
        System.out.println("*****sayHello");
    }

}

/**
 * Phone.java--编译-->Phone.class（二进制字节码文件，装载成一个类） 反射（类）Class.forName();
 * 总结：
 * 1、一个对象里面如果有多个synchronized方法，某个时刻内，只要一个线程去调用其中的一个
 * synchronized方法了，其他的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程
 * 去访问这些synchronized方法，锁的是当前对象this（对象锁），被锁定后，其他的线程都不能进入到当前
 * 对象的其他的synchronized方法。
 * 2、加个普通方法后发现和同步锁无关。
 * 3、换成两个对象后，不是同一把锁了，情况立刻变化。
 * 4、synchronized实现同步锁的基础：Java中的每一个对象都可以作为锁。
 * 5、具体表现为3中形式：
 *   1）对于普通同步方法，锁是当前实例对象。
 *   2）对于同步方法块，锁是synchronized括号里配置的对象。synchronized(this){}
 *   3）对于静态同步方法，锁是当前类的Class对象
 */
public class Lock8Demo05 {

    /**
     * 1、标准访问是先打印邮件还是短信
     * 2、暂停4秒钟在邮件方法，是先打印邮件还是短信
     * 3、新增普通的sayHello方法，是先打印邮件还是sayHello
     * 4、两部手机，先打印邮件还是短信
     * 5、两个静态同步方法，同一部手机，是先打印邮件还是短信
     * 6、两个静态同步方法，两一部手机，是先打印邮件还是短信
     * 7、一个静态同步方法，一个普通同步方法，同一部手机，是先打印邮件还是短信
     * 8、一个静态同步方法，一个普通同步方法，两一部手机，是先打印邮件还是短信
     */
    public static void main(String[] args) throws InterruptedException {
        //对象锁
        Phone phone = new Phone();

        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        Thread.sleep(100);

        //1、phone::sendSMS
        //2、phone::sendSMS
        //3、phone::sayHello
        //4、phone2::sendSMS
        //5、phone.sendSMS()
        //6、phone2.sendSMS()
        //7、phone.sendSMS()
        //8、phone2.sendSMS()
        new Thread(phone2::sayHello, "B").start();

        //不用的方法，只是使用sendSMS方法
        new Thread(phone2::sendSMS, "B").start();
    }

}
