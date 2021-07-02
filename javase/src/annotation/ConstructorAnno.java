package annotation;

import java.lang.annotation.*;

/**
 * 构造器注解
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstructorAnno {

    String desc() default "描述构造函数的注解";
}
