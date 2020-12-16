package com.ling.c2_classloader;

/**
 * count = 2在前面：准备阶段赋默认值，count=0,t=null;初始化阶段赋初始值，count=2,t=new T()后count=3
 * new T()在前面：准备阶段赋默认值，t=null,count=0;初始化阶段赋初始值，t = new T(),count++=0,然后count=2
 */
class T {
    // count=2;和t=new T()换下位置
    public static int count = 2; //0
    public static T t = new T(); // null

    //private int m = 8;

    private T() {
        count ++;
        //System.out.println("--" + count);
    }
}

public class T001_ClassLoadingProcedure {
    public static void main(String[] args) {
        System.out.println(T.count);
    }
}
