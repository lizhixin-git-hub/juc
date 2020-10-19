package com.atguigu.juc1205;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 线程不安全
 * 1、故障现象（java.util.ConcurrentModificationException）
 * 2、导致原因 多线程争抢资源、未加锁
 * 3、解决方法(1、Collections.synchronizedList(new ArrayList<>()));2、new Vector<>();3、new CopyOnWriteArrayList())
 * 4、优化建议(同样错误不犯第2次) new CopyOnWriteArrayList();
 */
public class NotSafeDemo03 {

    /**
     * new ArrayList(),底层创建了一个初始容量为10的Object数组，会扩容，通过
     * Arrays.copyOf扩到原值的一半（第一次扩容为15，第二次扩容为22...）
     * new HashMap();扩容为原值的一倍，2的n次方
     * @param args args
     */
    public static void main(String[] args) {
        listNotSafe();

        setNotSafe();

        mapNotSafe();
    }

    private static void listNotSafe() {
        //解决并发，使用CopyOnWriteArrayList读写复制;
        //CopyOnWrite容器即写时复制的容器，往一个容器中添加元素的时候，不直接往当前容器的Object[]
        //添加，而是先将当前容器Object[]进行Copy,复制出一个新的容器Object[] newElements,
        //然后新的容器Object[] newElements里添加元素，添加完元素之后，再将原容器的引用指向新的容器
        //setArray(newElements);这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，
        //因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
        List<String> list = new CopyOnWriteArrayList<>();

        //list线程不安全的case
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));

                System.out.println(list);
            }, Objects.toString(i)).start();
        }

        list.forEach(System.out::println);
    }

    private static void setNotSafe() {
        //HashSet的底层是HashMap
        Set<String> set = new CopyOnWriteArraySet<>();

        //set线程不安全的case
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));

                System.out.println(set);
            }, Objects.toString(i)).start();
        }
    }

    private static void mapNotSafe() {
        Map<String, String> map = new ConcurrentHashMap<>();

        //set线程不安全的case
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));

                System.out.println(map);
            }, Objects.toString(i)).start();
        }
    }

}
