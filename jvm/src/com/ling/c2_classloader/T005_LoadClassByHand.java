package com.ling.c2_classloader;

/**
 * ClassLoader源码
 */
public class T005_LoadClassByHand {
    public static void main(String[] args) throws ClassNotFoundException {
        // 需要加载一个类，只需要调用ClassLoader的loadClass方法就可以
        // loadClass方法加载硬盘上的类到内存
        Class clazz = T005_LoadClassByHand.class.getClassLoader().loadClass("com.mashibing.jvm.c2_classloader.T002_ClassLoaderLevel");
        System.out.println(clazz.getName());

        //利用类加载器加载资源，参考坦克图片的加载
        //T005_LoadClassByHand.class.getClassLoader().getResourceAsStream("");
    }
}
