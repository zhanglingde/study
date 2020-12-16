package com.ling.c2_classloader;

/**
 * 自定义类加载器指定父加载器：默认是AppClassLoader
 */
public class T010_Parent {
    private static T006_MyClassLoader parent = new T006_MyClassLoader();

    private static class MyLoader extends ClassLoader {
        // 自定义类加载器MyLoader可以通过父类ClassLoader的构造方法设置父加载器
        public MyLoader() {
            super(parent);
            // 自定义类加载器如果不设置父加载器，默认使用getSystemClassLoader()，AppClassLoader作为父加载器
            System.out.println("默认设置的父加载器："+ClassLoader.getSystemClassLoader());
        }
    }

    public static void main(String[] args) {
        System.out.println("MyLoader的父加载器："+new MyLoader().getParent());

    }
}
