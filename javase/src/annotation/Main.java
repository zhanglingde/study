package annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhangling 2021/7/1 11:45
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Class<?> userClass = User.class;
        Object user = userClass.newInstance();

        // 1. 获取最外层的类注解 TypeAnno
        System.out.println("=================类注解=====================");
        TypeAnno typeAnno = userClass.getAnnotation(TypeAnno.class);
        System.out.println("typeAnno.desc() = " + typeAnno.desc());

        // 2. 获取字段注解，首先通过反射获取类中的属性字段
        System.out.println("=================字段注解=====================");
        Field[] fields = userClass.getDeclaredFields();
        for (Field field : fields) {
            FieldAnno fieldAnno = field.getAnnotation(FieldAnno.class);
            System.out.println("fieldAnno.desc() = " + fieldAnno.desc());
        }

        // 3. 方法的注解
        System.out.println("=================方法注解=====================");
        Method work = userClass.getMethod("work", String.class, String.class);
        MethodAnno methodAnno = work.getAnnotation(MethodAnno.class);
        System.out.println("methodAnno.time() = " + methodAnno.time());
        System.out.println("methodAnno.sex() = " + methodAnno.sex());

        // 4. 参数的注解，使用动态代理方式实现参数
        System.out.println("=================参数注解=====================");
        UserImpl userImpl = getMethodParameter(UserImpl.class);
        userImpl.work("张三","法外狂徒");

        // 5. 局部变量注解
        System.out.println("=================局部变量注解=====================");

    }

    public static <T> T getMethodParameter(Class<T> target) {
        return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class<?>[]{target}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] annotations = parameterAnnotations[i];
                    ParamAnno paramAnno = (ParamAnno) annotations[0];
                    System.out.println("paramAnno.value() = " + paramAnno.value() + "==>" + args[i]);
                }
                return null;
            }
        });
    }
}
