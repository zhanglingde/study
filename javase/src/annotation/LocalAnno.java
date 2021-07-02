package annotation;

import java.lang.annotation.*;

/**
 * 局部变量注解
 */
@Documented
@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalAnno {

    String desc() default "描述局部变量的注解";
}
