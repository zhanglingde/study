package annotation;

import java.lang.annotation.*;

/**
 * 方法注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAnno {

    /**
     * 默认提交时间
     */
    String time() default "2021-07-01";

    /**
     * 默认：男
     */
    boolean sex() default true;
}
