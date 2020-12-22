package com.ling.c2_classloader;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 热部署就是使用一个新的ClassLoader,重新加载改变了的class
 */
public class T012_ClassReloading2 {
    private static class MyLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            File f = new File("D:\\AAAShuju\\IntelliJIDEA\\study\\out\\production\\jvm\\com\\ling" + name.replace(".", "/").concat(".class"));

            if(!f.exists()) return super.loadClass(name);

            try {
                // 在硬盘路径中class就直接找到了直接加载
                InputStream is = new FileInputStream(f);

                byte[] b = new byte[is.available()];
                is.read(b);
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return super.loadClass(name);
        }
    }

    public static void main(String[] args) throws Exception {
        MyLoader m = new MyLoader();
        Class clazz = m.loadClass("com.ling.Hello");

        // 两次不是同一个ClassLoader，两次都加载了class对象，所以不是一个对象
        m = new MyLoader();
        Class clazzNew = m.loadClass("com.ling.Hello");

        System.out.println(clazz == clazzNew);
    }
}
