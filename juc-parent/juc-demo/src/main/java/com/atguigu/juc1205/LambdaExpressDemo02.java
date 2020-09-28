package com.atguigu.juc1205;

/**
 * java是面向对象编程、面向接口编程
 */
interface Foo {

    void sayHello();

}

/**
 * 函数式接口，有且只有一个方法
 */
@FunctionalInterface//显示函数式接口定义
interface Foo1 {

    int add(int x, int y);

    /**
     * 接口可以有默认实现,函数式接口可以定义多个默认方法
     *
     * @param x x
     * @param y y
     * @return x + y
     */
    default int mul(int x, int y) {
        return x + y;
    }

    /**
     * 接口可以有静态方法实现,函数式接口可以定义多个静态方法
     *
     * @param x x
     * @param y y
     * @return x / y
     */
    static int div(int x, int y){
        return x / y;
    }

}

/**
 * 只有函数式接口才可以使用Lambda表达式
 * 函数式编程
 * 1、(拷贝小括号、写死右箭头、落地大括号)
 * 2、@FunctionalInterface
 * 3、default
 * 4、static
 */
public class LambdaExpressDemo02 {

    public static void main(String[] args) {
        //接口可以new，接口中只有一个方法，可以省略方法名
        Foo foo = () -> System.out.println("****hello 1205");

        foo.sayHello();

        //有参，有返回值
        Foo1 foo1 = (x, y) -> {
            System.out.println("come in add method");

            return x + y;
        };

        System.out.println(foo1.add(3, 5));

        System.out.println(foo1.mul(3, 5));

        System.out.println(Foo1.div(10, 2));
    }

}
