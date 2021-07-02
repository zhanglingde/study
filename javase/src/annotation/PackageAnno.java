package annotation;

import java.lang.annotation.*;

/**
 * 包注解
 */
@Documented
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PackageAnno {

    String desc() default "描述包的注解";
}
