package annotation;

import java.lang.annotation.*;

/**
 * 类型注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeAnno {

    String desc() default "描述类型的注解";
}
