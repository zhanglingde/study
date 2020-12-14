package com.ling.c2_classloader;

/**
 * 类加载器范围
 */
public class T003_ClassLoaderScope {
    public static void main(String[] args) {
        // BootStrap类加载器加载的jar的路径
        String pathBoot = System.getProperty("sun.boot.class.path");
        System.out.println(pathBoot.replaceAll(";", System.lineSeparator()));

        System.out.println("--------------------");
        // Extension类加载器加载的jar的路径
        String pathExt = System.getProperty("java.ext.dirs");
        System.out.println(pathExt.replaceAll(";", System.lineSeparator()));

        System.out.println("--------------------");
        // App类加载器加载的jar的路径
        String pathApp = System.getProperty("java.class.path");
        System.out.println(pathApp.replaceAll(";", System.lineSeparator()));
    }
}
