package main.java.javaguide;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {
    //    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {
//        /**
//         * 获取TargetObject类的Class对象并且创建TargetObject类实例
//         */
//        Class<?> targetClass = Class.forName("main.java.javaguide.TargetObject");
//        TargetObject targetObject = (TargetObject) targetClass.newInstance();
//        /**
//         * 获取所有类中所有定义的方法
//         */
//        Method[] methods = targetClass.getDeclaredMethods();
//        for(Method method: methods) {
//            System.out.println(method.getName());
//        }
//        /**
//         * 获取指定方法并调用
//         */
//        Method publicMethod = targetClass.getDeclaredMethod("publicMethod", String.class);
//        publicMethod.invoke(targetObject, "JavaGuide");
//        /**
//         * 获取指定参数并对参数进行修改
//         */
//        Field field = targetClass.getDeclaredField("value");
//        field.setAccessible(true);
//        field.set(targetObject, "JavaGuide");
//        /**
//         * 调用 private 方法
//         */
//        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
//        privateMethod.setAccessible(true);
//        privateMethod.invoke(targetObject);
//    }
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(-1);
        arrayList.add(3);
        arrayList.add(5);
        arrayList.add(-5);
        arrayList.add(7);
        arrayList.add(4);
        arrayList.add(-9);
        arrayList.add(-7);
        System.out.println("原始数组");
        System.out.println(arrayList);
        // 反转
        Collections.reverse(arrayList);
        System.out.println("Collections.reverse(arrayList):");
        System.out.println(arrayList);

        Collections.sort(arrayList);
        System.out.println("Collections.sort(arrayList):");
        System.out.println(arrayList);

        //定制排序的用法
        Collections.sort(arrayList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("定制排序后：");
        System.out.println(arrayList);
    }

}



