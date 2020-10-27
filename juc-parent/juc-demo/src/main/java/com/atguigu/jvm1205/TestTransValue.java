package com.atguigu.jvm1205;

class Person {

    private String personName;

    Person(String personName) {
        this.personName = personName;
    }

    String getPersonName() {
        return personName;
    }

    void setPersonName(String personName) {
        this.personName = personName;
    }
}

public class TestTransValue {

    private void changeValue1(int age) {
        age = 30;
    }

    private void changeValue2(Person person) {
        person.setPersonName("XXX");
    }

    private void changeValue3(String str) {
        str = "XXX";
    }

    public static void main(String[] args) {
        //基本类型传递是副本，原数据不动
        TestTransValue testTransValue = new TestTransValue();
        int age = 20;
        testTransValue.changeValue1(age);
        System.out.println("age-------" + age);

        //引用类型传递内存地址
        Person person = new Person("abc");
        testTransValue.changeValue2(person);
        System.out.println("personName-------" + person.getPersonName());

        //String常量池，java8放在了元空间，在常量池中存在，直接复用，不存在，重新创建
        String str = "abc";
        testTransValue.changeValue3(str);
        System.out.println("String------" + str);
    }

}
