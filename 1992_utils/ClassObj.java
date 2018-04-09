package net.integration.framework.util;

import java.lang.reflect.Field;


/**
 * 名称：“类处理”类<br>
 * 描述：提供一些关于类的处理方法
 * @Version 1.0.0.0
 */
public class ClassObj {

	/**
	 * <b>返回实例化对象</b>
	 * 参数：classPath为类路径 ，返回：Class 实例化对象 。
	 * @param classPath 类路径
	 * @return 实例化对象
	 */
	@SuppressWarnings("rawtypes")
	public static Class get(String classPath){
		Class obj=null;
		try {
			obj=Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * <b>返回类属性值集合</b>
	 * 参数：classPath为类路径 ，返回：Field[] 类属性值集合 。
	 * @param classPath 类路径
	 * @return 类属性集合
	 */
	public static Field[] getClassField(String classPath){
		return ClassObj.get(classPath).getFields();
	}
	
	/**
	 * <b>返回类属性值</b>
	 * 参数：classPath为类路径 ，返回：String 类属性值 。
	 * @param classPath 类路径
	 * @param fieldName 类属性名
	 * @return 类属性值
	 */
	public static String getClassFieldValue(String classPath,String fieldName){
		String fieldValue=null;
		try {
			fieldValue=ClassObj.get(classPath).getField(fieldName).get(fieldName).toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return fieldValue;
	}
	
	/**
	 * <b>返回类属性值</b>
	 * 参数：classPath为类路径 ，返回：String 类属性值 。
	 * @param classPath 类路径
	 * @param fieldName 类属性名
	 * @return 类属性值
	 */
	public static String getClassFieldValue(Class<?> clzz,String fieldName){
		String fieldValue=null;
		try {
			Class<?> clz=clzz;
			while(clz!=Object.class){
				Field[] declaredFields = clz.getDeclaredFields();
				for (Field field : declaredFields) {
					if(field.getName().equals(fieldName)){
						field.setAccessible(true);
						fieldValue=(String) field.get(clz.newInstance());
						field.setAccessible(false);
						break;
					}
				}
				clz=clz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldValue;
	}

	/**
	 * <b>返回类属性值</b>
	 * 参数：classPath为类路径 ，返回：String 类属性值 。
	 * @param classPath 类路径
	 * @param fieldName 类属性名
	 * @return 类属性值
	 */
	public static String getClassFieldValue(Object obj,String fieldName){
		String fieldValue=null;
		try {
			Class<?> clz=obj.getClass();
			while(clz!=Object.class){
				Field[] declaredFields = clz.getDeclaredFields();
				for (Field field : declaredFields) {
					if(field.getName().equals(fieldName)){
						field.setAccessible(true);
						fieldValue=(String) field.get(obj);
						field.setAccessible(false);
						break;
					}
				}
				clz=clz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldValue;
	}
	/**
	 * <b>返回类属性值</b>
	 * 参数：classPath为类路径 ，返回：String 类属性值 。
	 * @param classPath 类路径
	 * @param fieldName 类属性名
	 * @return 类属性值
	 */
	public static void setClassFieldValue(Object obj,String fieldName,Object value){
		try {
			Class<?> clz=obj.getClass();
			while(clz!=Object.class){
				Field[] declaredFields = clz.getDeclaredFields();
				for (Field field : declaredFields) {
					if(field.getName().equals(fieldName)){
						field.setAccessible(true);
						field.set(obj, value);
						field.setAccessible(false);
						break;
					}
				}
				clz=clz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Class<?> clzz=null;
		try {
			String url="net.integration.framework.fileUpDownload.model.FileBean";
			clzz=Class.forName(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object obj = clzz.newInstance();
		ClassObj.setClassFieldValue(obj, "fileSaveType", "123");
		System.out.println(ClassObj.getClassFieldValue(obj, "fileSaveType"));
//		System.out.println(ClassObj.getClassFieldValue(clzz, "fileSaveTable"));
	}
}
