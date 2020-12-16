package com.ling.c2_classloader;

public class T011_ClassReloading1 {
    public static void main(String[] args) throws Exception {
        T006_MyClassLoader msbClassLoader = new T006_MyClassLoader();
        Class clazz = msbClassLoader.loadClass("com.ling.Hello");

        msbClassLoader = null;
        System.out.println(clazz.hashCode());

        msbClassLoader = null;

        msbClassLoader = new T006_MyClassLoader();
        // clazz 等于clazz1，双亲委派机制，一个类加载了一次就不再加载了
        Class clazz1 = msbClassLoader.loadClass("com.ling.Hello");
        System.out.println(clazz1.hashCode());

        System.out.println(clazz == clazz1);
    }
}
