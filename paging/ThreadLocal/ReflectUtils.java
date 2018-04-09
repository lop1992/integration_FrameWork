package net.integration.framework.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class ReflectUtils {

	public static void main(String[] args) {
		// getMethods(ReflectUtils.class);
	}
	
	/**
	 * 获得参数化类型的泛型类型，取第一个参数的泛型类型，（默认去的第一个）
	 * @param clazz 参数化类型
	 * @return 泛型类型
	 */
	@SuppressWarnings("unchecked")
	public static Class getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}
	
	/**
	 * 根据参数索引获得参数化类型的泛型类型，（通过索引取得）
	 * @param clazz 参数化类型
	 * @param index 参数索引
	 * @return 泛型类型
	 */
	@SuppressWarnings("unchecked")
	public static Class getClassGenricType(
			final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public String getId(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			String id = null;
			for (Field field : fields) {
				if (field.isAnnotationPresent(javax.persistence.Id.class)) {
					String fieldName = field.getName();
					String getMethodName = "get"
							+ StringUtils.firstToUpperCase(fieldName);
					Method getMethod = obj.getClass().getMethod(getMethodName,
							new Class[] {});
					id = (String) getMethod.invoke(obj, new Object[] {});
					break;
				}
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String setId(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			String id = null;
			for (Field field : fields) {
				if (field.isAnnotationPresent(javax.persistence.Id.class)) {
					String fieldName = field.getName();
					String setMethodName = "set"
							+ StringUtils.firstToUpperCase(fieldName);
					Method setMethod = obj.getClass().getMethod(setMethodName,
							new Class[] { field.getType() });
					id = StringUtils.createUUID();
					setMethod.invoke(obj, new Object[] { id });// 调用对象的setXXX方法
					break;
				}
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getTableName() {
		Table table = this.getClass().getAnnotation(
				javax.persistence.Table.class);
		if (null != table) {
			return table.name();
		}
		return null;
	}

	/**
	 * 通过构造函数实例化对象
	 * 
	 * @param className
	 *            类的全路径名称
	 * @param parameterTypes
	 *            参数类型
	 * @param initargs
	 *            参数值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object constructorNewInstance(String className,
			Class[] parameterTypes, Object[] initargs) {
		try {
			Constructor<?> constructor = (Constructor<?>) Class.forName(
					className).getDeclaredConstructor(parameterTypes); // 暴力反射
			constructor.setAccessible(true);
			return constructor.newInstance(initargs);
		} catch (Exception ex) {
			throw new RuntimeException();
		}

	}

	/**
	 * 暴力反射获取字段值
	 * 
	 * @param fieldName
	 *            属性名
	 * @param obj
	 *            实例对象
	 * @return 属性值
	 */
	public static Object getFieldValue(String propertyName, Object obj) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 暴力反射获取字段值
	 * 
	 * @param propertyName
	 *            属性名
	 * @param object
	 *            实例对象
	 * @return 字段值
	 */
	public static Object getProperty(String propertyName, Object object) {
		try {

			PropertyDescriptor pd = new PropertyDescriptor(propertyName,
					object.getClass());
			Method method = pd.getReadMethod();
			return method.invoke(object);

			// 其它方式
			/*
			 * BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			 * PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			 * Object retVal = null; for(PropertyDescriptor pd : pds){
			 * if(pd.getName().equals(propertyName)) { Method methodGetX =
			 * pd.getReadMethod(); retVal = methodGetX.invoke(object); break; }
			 * } return retVal;
			 */
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以字符串形式存在的,它支持属性连缀操作:如,.对象.属性
	 * 
	 * @param propertyName
	 *            属性名
	 * @param object
	 *            实例对象
	 * @return 字段值
	 */
	public static Object getBeanInfoProperty(String propertyName, Object object) {
		try {
			return BeanUtils.getProperty(object, propertyName);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以字符串形式存在的
	 * 
	 * @param object
	 *            实例对象
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            字段值
	 * @return
	 */
	public static void setBeanInfoProperty(Object object, String propertyName,
			String value) {
		try {
			BeanUtils.setProperty(object, propertyName, value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以对象属性的实际类型
	 * 
	 * @param propertyName
	 *            属性名
	 * @param object
	 *            实例对象
	 * @return 字段值
	 */
	public static Object getPropertyUtilByName(String propertyName,
			Object object) {
		try {
			return PropertyUtils.getProperty(object, propertyName);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 通过BeanUtils工具包获取反射获取字段值,注意此值是以对象属性的实际类型,这是PropertyUtils与BeanUtils的根本区别
	 * 
	 * @param object
	 *            实例对象
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            字段值
	 * @return
	 */
	public static void setPropertyUtilByName(Object object,
			String propertyName, Object value) {
		try {
			PropertyUtils.setProperty(object, propertyName, value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 设置字段值
	 * 
	 * @param obj
	 *            实例对象
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            新的字段值
	 * @return
	 */
	public static void setProperties(Object object, String propertyName,
			Object value) throws IntrospectionException,
			IllegalAccessException, InvocationTargetException {
		PropertyDescriptor pd = new PropertyDescriptor(propertyName,
				object.getClass());
		Method methodSet = pd.getWriteMethod();
		methodSet.invoke(object, value);
	}

	/**
	 * 设置字段值
	 * 
	 * @param propertyName
	 *            字段名
	 * @param obj
	 *            实例对象
	 * @param value
	 *            新的字段值
	 * @return
	 */
	public static void setFieldValue(Object obj, String propertyName,
			Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 设置字段值
	 * 
	 * @param className
	 *            类的全路径名称
	 * @param methodName
	 *            调用方法名
	 * @param parameterTypes
	 *            参数类型
	 * @param values
	 *            参数值
	 * @param object
	 *            实例对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object methodInvoke(String className, String methodName,
			Class[] parameterTypes, Object[] values, Object object) {
		try {
			Method method = Class.forName(className).getDeclaredMethod(
					methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(object, values);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}
	
	/** 
     * 获取obj对象fieldName的属性值 
     * @param obj 
     * @param fieldName 
     * @return 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static Object getValueByFieldName(Object obj, String fieldName)  
            throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = getFieldByFieldName(obj, fieldName);  
        Object value = null;  
        if(field!=null){  
            if (field.isAccessible()) {  
                value = field.get(obj);  
            } else {  
                field.setAccessible(true);  
                value = field.get(obj);  
                field.setAccessible(false);  
            }  
        }  
        return value;  
    }  
    
    /** 
     * 获取obj对象fieldName的Field 
     * @param obj 
     * @param fieldName 
     * @return 
     */  
    public static Field getFieldByFieldName(Object obj, String fieldName) {  
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass  
                .getSuperclass()) {  
            try {  
                return superClass.getDeclaredField(fieldName);  
            } catch (NoSuchFieldException e) {  
            }  
        }  
        return null;  
    }  
    
    /** 
     * 设置obj对象fieldName的属性值 
     * @param obj 
     * @param fieldName 
     * @param value 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static void setValueByFieldName(Object obj, String fieldName,  
            Object value) throws SecurityException, NoSuchFieldException,  
            IllegalArgumentException, IllegalAccessException {  
        Field field = obj.getClass().getDeclaredField(fieldName);  
        if (field.isAccessible()) {  
            field.set(obj, value);  
        } else {  
            field.setAccessible(true);  
            field.set(obj, value);  
            field.setAccessible(false);  
        }  
    }

	/**
	 * @param <T>
	 *            具体对象
	 * @param fileds
	 *            要进行比较Bean对象的属性值集合(以属性值为key,属性注释为value,集合从数据库中取出)
	 * @param oldBean
	 *            源对象
	 * @param newBean
	 *            新对象
	 * @return 返回二个Bean对象属性值的异同
	 */
//	public static <T> String compareBeanValue(Map<String, String> fileds,
//			T oldBean, T newBean) {
//
//		StringBuilder compares = new StringBuilder();
//		String propertyName = null;
//		Object oldPropertyValue = null;
//		Object newPropertyValue = null;
//
//		StringBuilder descrips = new StringBuilder();
//		for (Map.Entry<String, String> entity : fileds.entrySet()) {
//			// 获取新旧二个对象对应的值
//			propertyName = entity.getKey().toLowerCase();
//			oldPropertyValue = getProperty(propertyName, oldBean);
//			newPropertyValue = getProperty(propertyName, newBean);
//
//			if (null == oldPropertyValue && null == newPropertyValue) {
//				continue;
//			}
//			if ("".equals(oldPropertyValue) && "".equals(newPropertyValue)) {
//				continue;
//			}
//			if (null == oldPropertyValue) {
//				oldPropertyValue = "";
//			}
//			if (null == newPropertyValue) {
//				newPropertyValue = "";
//			}
//
//			if (oldPropertyValue.equals(newPropertyValue)) {
//				continue;
//			}
//			compares.append("字段注释: ").append(entity.getValue()).append("】")
//					.append("原属性值\"");
//			if (StringUtils.isEmpty(oldPropertyValue + "")) {
//				oldPropertyValue = " ";
//			}
//			compares.append(oldPropertyValue).append("\"现属性值\"");
//			if (StringUtils.isEmpty(newPropertyValue + "")) {
//				newPropertyValue = " ";
//			}
//			compares.append(newPropertyValue).append("\";");
//		}
//		return compares.toString();
//	}

}
