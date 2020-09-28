package com.atguigu.juc1205;

import java.util.*;

/**
 * 线程不安全
 * 1、故障现象（java.util.ConcurrentModificationException）
 * 2、导致原因 多线程争抢资源
 * 3、解决方法(1、Collections.synchronizedList(new ArrayList<>()));2、new Vector<>();)
 * 4、优化建议(同样错误不犯第2次) new CopyOnWriteArrayList();
 */
public class NotSafeDemo03 {
    /**
     * new ArrayList(),底层创建了一个初始容量为10的Object数组
     * @param args args
     */
    public static void main(String[] args) {
        //解决并发，使用CopyOnWriteArrayList;
        List<String> list = Collections.synchronizedList(new ArrayList<>());

        //list线程不安全的case
        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));

                System.out.println(list);
            }, Objects.toString(i)).start();
        }

        list.forEach(System.out::println);
    }

}
