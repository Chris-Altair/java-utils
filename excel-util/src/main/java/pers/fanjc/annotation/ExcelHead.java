package pers.fanjc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//应用于属性
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHead {
    String value() default "";//表头名

    String dict() default "";//映射的字典
}
