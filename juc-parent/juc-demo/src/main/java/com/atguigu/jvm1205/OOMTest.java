package com.atguigu.jvm1205;

import java.util.Random;

public class OOMTest {

    public static void main(String[] args) {
        //byte[] bytes = new byte[40 * 1024 * 1024];
        String str = "www.atguigu.com";

        while (true) {
            str += str + new Random().nextInt(88888888) + new Random().nextInt(999999999);
        }
    }
}
