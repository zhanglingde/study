package com.ling.c2_classloader;

/**
 * 懒加载，在使用的使用进行加载，但初始化
 */
public class T008_LazyLoading { //严格讲应该叫lazy initialzing，因为java虚拟机规范并没有严格规定什么时候必须loading,但严格规定了什么时候initialzing
    public static void main(String[] args) throws Exception {
        //P p;                              // 类P不会被加载
        //X x = new X();                    // 会被加载
        //System.out.println(P.i);          // 类P不会被加载
        //System.out.println(P.j);          // 类P会被加载
        Class.forName("com.mashibing.jvm.c2_classloader.T008_LazyLoading$P"); // 类P会被加载

    }

    public static class P {
        final static int i = 8;
        static int j = 9;
        static {
            // 打印了P，说明类被初始化了，一定被加载了
            System.out.println("P");
        }
    }

    public static class X extends P {
        static {
            System.out.println("X");
        }
    }
}
