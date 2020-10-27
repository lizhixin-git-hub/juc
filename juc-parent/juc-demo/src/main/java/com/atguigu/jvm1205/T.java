package com.atguigu.jvm1205;

public class T {

    public static void main(String[] args){
        //返回Java虚拟机试图使用的最大内存量
        long maxMemory = Runtime.getRuntime().maxMemory();
        //返回Java虚拟机中的内存总量
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("MAX_MEMORY=" + maxMemory + "字节、" +
                (maxMemory / (double)1024/1024) + "MB");
        System.out.println("TOTAL_MEMORY=" + totalMemory + "字节、" +
                (totalMemory / (double)1024/1024) + "MB");
    }

}
