package com.ling.c2_classloader;

/**
 * 父加载器的概念：父加载器是指加载器中有一个变量Parent；Parent指向谁谁就是父加载器
 */
public class T004_ParentAndChild {
    public static void main(String[] args) {
        System.out.println(T004_ParentAndChild.class.getClassLoader());     // App
        System.out.println(T004_ParentAndChild.class.getClassLoader().getClass().getClassLoader()); // Bootstrap 返回null
        System.out.println(T004_ParentAndChild.class.getClassLoader().getParent());
        System.out.println(T004_ParentAndChild.class.getClassLoader().getParent().getParent());
        //System.out.println(T004_ParentAndChild.class.getClassLoader().getParent().getParent().getParent());

    }
}
