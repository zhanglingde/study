package com.ling.c2_classloader;

/**
 * JIT 即时编译器
 */
public class T009_WayToRun {
    public static void main(String[] args) {
        // 方法被执行很多次，会被JIT编译成本地代码
        for(int i=0; i<10_0000; i++)
            m();

        long start = System.currentTimeMillis();
        for(int i=0; i<10_0000; i++) {
            m();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void m() {
        for(long i=0; i<10_0000L; i++) {
            long j = i%3;
        }
    }
}
