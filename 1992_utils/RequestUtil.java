package net.integration.framework.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestUtil {

	private static ThreadLocal<Map<String,Object>> REQUESTLOCAL = new ThreadLocal<Map<String,Object>>();
	
	private static Map<String,Object> init(){
		if(REQUESTLOCAL.get()==null){
			Map<String, Object> map= new HashMap<String, Object>();
			REQUESTLOCAL.set(map);
			return map;
		}
		return REQUESTLOCAL.get();
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) REQUESTLOCAL.get().get("request");
	}

	public static void setRequest(HttpServletRequest request) {
		Map<String,Object> map= init();
		map.put("request", request);
	}

	public static HttpServletRequest getOriginalRequest() {
		return (HttpServletRequest) REQUESTLOCAL.get().get("originalRequest");
	}

	/**
	 * 此requent为原始requent
	 * 
	 * @return
	 */
	public static void setOriginalRequest(HttpServletRequest request) {
		Map<String, Object> map = init();
		map.put("originalRequest", request);
	}
	
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) REQUESTLOCAL.get().get("response");
	}
	
	
	public static void setResponse(HttpServletResponse response) {
		Map<String, Object> map=init();
		map.put("response", response);
	}
	
	public static void clear() {
		REQUESTLOCAL.remove();
	}
	
	/**
	 * 在request中设置值；内部转发必须使用setAttr才能在跳转的方法中获取到值
	 * @param attributeName
	 * @param attributeValues
	 */
	public static void setAttr(String attributeName,Object attributeValues){
		if(getRequest()!=null){
			getRequest().setAttribute(attributeName, attributeValues);
		}
	}
	
	/**
	 * 获取request中的值：内部转发必须使用getAttr才能获取到值
	 * @param attributeName
	 * @return 如果没有值则返回null
	 */
	public static Object getAttr(String attributeName){
		if(getRequest()!=null){
			return getRequest().getAttribute(attributeName);
		}
		return null;
	}
	
	/**
	 * 获取request中的值：内部转发的String必须使用getParameter才能获取到值
	 * @param attributeName
	 * @return 如果没有值则返回null
	 */
	public static String getParameter(String attributeName){
		if(getRequest()!=null){
			return getRequest().getParameter(attributeName);
		}
		return null;
	}
	
	/**
	 * 获取request中attributeName的值
	 * 与getParameter方法相同
	 * @param attributeName
	 * @return
	 */
	public static String getPara(String attributeName){
		return getParameter(attributeName);
	}
	
	/**
	 * 获取request中attributeName的值
	 * @param attributeName
	 * @param defaultValue
	 * @return String 如果获取的值为null或者""时使用defaultValue值
	 */
	public static String getPara(String attributeName,String defaultValue){
		String para = getPara(attributeName);
		if(StringUtils.isEmpty(para)){
			para=defaultValue;
		}
		return para;
	}
	
	
	
	/**
	 * 获取request中attributeName的值，并转换为Integer
	 * @param attributeName
	 * @return Integer 如果值为null，则返回null
	 */
	public static Integer getParaInt(String attributeName){
		return getParaInt(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Integer
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Integer getParaInt(String attributeName,Integer defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value))
			return Integer.parseInt(value.trim());
		return defaultValue;
	}
	

	/**
	 * 获取request中attributeName的值，并转换为Long
	 * @param attributeName
	 * @return Long 如果值为null，则返回null
	 */
	public static Long getParaLong(String attributeName){
		return getParaLong(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Long
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Long 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Long getParaLong(String attributeName,Long defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value))
			return Long.parseLong(value.trim());
		return defaultValue;
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Double
	 * @param attributeName
	 * @return Double 如果值为null，则返回null
	 */
	public static Double getParaDouble(String attributeName){
		return getParaDouble(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Double
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Double 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Double getParaDouble(String attributeName,Double defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value))
			return Double.parseDouble(value.trim());
		return defaultValue;
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Float
	 * @param attributeName
	 * @return Float 如果值为null，则返回null
	 */
	public static Float getParaFloat(String attributeName){
		return getParaFloat(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Float
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Float 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Float getParaFloat(String attributeName,Float defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value))
			return Float.parseFloat(value.trim());
		return defaultValue;
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Char
	 * @param attributeName
	 * @return Character 如果值为null，则返回null
	 */
	public static Character getParaChar(String attributeName){
		return getParaChar(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Character
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Character 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Character getParaChar(String attributeName,Character defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value))
			return value.trim().toCharArray()[0];
		return defaultValue;
	}
	
	
	/**
	 * 获取request中attributeName的值，并转换为Boolean
	 * 注意：值不区分大小写；但是如果值为null，则直接返回null
	 * @param attributeName
	 * @return Boolean 如果值为null，则返回null
	 */
	public static Boolean getParaBoolean(String attributeName){
		return getParaBoolean(attributeName,null);
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Boolean
	 * 注意：值不区分大小写；但是如果值为null，则直接返回defaultValue
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Boolean 如果获取的值为null时使用defaultValue值
	 */
	public static Boolean getParaBoolean(String attributeName,Boolean defaultValue){
		String value = getPara(attributeName);
		if (value!=null){
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}
	
	/**
	 * 获取request中attributeName的值，并转换为Boolean
	 * 注意：值不区分大小写；但是如果值为null，则直接返回defaultValue
	 * @param attributeName
	 * @param defaultValue 默认值
	 * @return Boolean 如果获取的值为null或者""时使用defaultValue值
	 */
	public static Boolean getParaBoolean2(String attributeName,Boolean defaultValue){
		String value = getPara(attributeName);
		if (StringUtils.isNotEmpty(value)){
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}
}
