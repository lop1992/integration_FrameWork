package net.integration.framework.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 名称：<br/>
 * 描述： 
 * @version 1.0.0.0
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

	/**字段/代码 {@value}*/
	public abstract String code();

	/**字段名字 {@value}*/
	public abstract String name();

	/**字段类型 {@value}*/
	public abstract String type();

	/**长度 {@value}*/
	public abstract int length();

	/**精度 {@value}*/
	public abstract int precision();

	/**格式 {@value}*/
	public abstract String dataFormat();

	/**是否必填 {@value}*/
	public abstract boolean isRequired();

	/**是否是字典字段 {@value}*/
	public abstract boolean dict() default false;

	/**字典表明 {@value}*/
	public abstract String dictName() default "";

	/**字典字段，根据bs获取tbattr {@value}*/
	public abstract String bs() default "";

	/**字典表中要返回的字段 {@value}*/
	public abstract String tbattr() default "";

	/**对应类中的属性名称 {@value}*/
	public abstract String csAttr() default "";
}
