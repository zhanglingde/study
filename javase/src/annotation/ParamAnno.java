package annotation;

import java.lang.annotation.*;

/**
 * 参数注解
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamAnno {

    String value() default "";
}
