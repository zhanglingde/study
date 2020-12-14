package com.ling.c2_classloader;


import com.ling.Hello;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 自定义类加载器：
 * 把硬盘上的内容，读取到内存中（以二进制形式存在），然后通过defineClass把二进制转换成class对象
 *
 */
public class T006_MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 将类的全类名替换成在硬盘上的路径名
        File f = new File("d:/test/", name.replace(".", "/").concat(".class"));
        try {
            // 文件转换成输入流
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            // 将文件流转换成二进制字节数组，（从文件流中读出来，写入二进制数组里）
            while ((b=fis.read()) !=0) {
                baos.write(b);
            }
            // 转换成二进制字节数组
            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();//可以写的更加严谨

            // 把二进制字节数组转换成class对象
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name); //throws ClassNotFoundException
    }

    public static void main(String[] args) throws Exception {
        ClassLoader l = new T006_MyClassLoader();
        Class clazz = l.loadClass("com.ling.Hello");
        Class clazz1 = l.loadClass("com.ling.Hello");


        System.out.println(clazz == clazz1);

        Hello h = (Hello)clazz.newInstance();
        h.m();

        System.out.println(l.getClass().getClassLoader());
        System.out.println(l.getParent());

        System.out.println(getSystemClassLoader());
    }
}
