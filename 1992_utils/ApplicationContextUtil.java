package net.integration.framework.util;



import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	// Spring应用上下文环境   
    private static ApplicationContext applicationContext;   
    
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		if(context!=null){
			ApplicationContextUtil.applicationContext=context;
		}
	}

	/**
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**  
     * 获取对象  
     * 这里重写了bean方法，起主要作用  
     * @param name  
     * @return 泛型 一个以所给名字注册的bean的实例  ,如果没有返回null
     */  
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {   
    	Object bean = null;
    	try {
    		bean = applicationContext.getBean(name);
		} catch (Exception e) {
		}
        return (T)bean ;   
    }

    /**
     * 获取对象  
     * 这里重写了bean方法，起主要作用  
     * @param name  
     * @return 泛型 一个以所给名字注册的bean的实例  ,如果没有返回null
     * @throws BeansException 此方法会抛出异常
     */
    @SuppressWarnings("unchecked")
	public static <T> T getBeanThrowException(String name) throws BeansException{   
    	Object bean = applicationContext.getBean(name);
        return (T)bean ;   
    }
    
    /**  
     * 获取对象  
     * 这里重写了bean方法，起主要作用  
     * @param className   
     * @return 泛型 一个以所给名字注册的bean的实例  ,如果没有返回null
     */  
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> className) {   
    	Object bean = null;
    	try {
    		bean = applicationContext.getBean(className);
		} catch (Exception e) {
		}
        return (T)bean ;   
    }
	
    /**
     * 获取对象  
     * 这里重写了bean方法，起主要作用  
     * @param className  
     * @return 泛型 一个以所给名字注册的bean的实例  ,如果没有返回null
     * @throws BeansException 此方法会抛出异常
     */
	@SuppressWarnings("unchecked")
	public static <T> T getBeanThrowException(Class<?> className) throws BeansException{
    	Object bean = applicationContext.getBean(className);
        return (T)bean ;   
    }
}
