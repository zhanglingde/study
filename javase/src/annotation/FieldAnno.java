package annotation;

import java.lang.annotation.*;

/**
 * 字段注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAnno {

    String desc() default "描述字段的注解";
}
