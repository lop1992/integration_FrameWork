 package net.integration.framework.util;
 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * <b>描述：</b>Map工具
 * @version v1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class MapUtil {
	public static Logger log= Logger.getLogger(MapUtil.class);
	
	/**
	 * 把map的key转换为list
	 * @param map
	 * @return List<String>
	 */
	public static List<String> getMapKey(Map map) {
		List list = new ArrayList();
		Set keySet = map.keySet();
		Iterator tr = keySet.iterator();
		while (tr.hasNext()) {
			Object obj = tr.next();
			String key = obj.toString();
			list.add(key);
		}
		return list;
	}

	/**
	 * 设置ids
	 * @param map
	 * @return
	 */
	public static List<String> setIds(Map map){
		return setStrASArray(map,"_ids");
	}
	
	/**
	 * 把“1,2,3,4”字符串转换为list
	 * @param map
	 * @param str
	 * @return
	 */
	public static List<String> setStrASArray(Map map,String str){
		Set keySet = map.keySet();
		Iterator tr = keySet.iterator();
		List<String> list=null;
		while (tr.hasNext()) {
			String key = tr.next().toString();
			if(StringUtils.isNotEmpty(key)){
				int lastIndex=key.lastIndexOf(str);
				if(lastIndex!=-1){
					String newKey=key.substring(0, lastIndex);
					if(StringUtils.isEmpty(newKey)){
						newKey=key;
					}
					String[] values = map.get(key).toString().split(",");
					list=Arrays.asList(values);
					map.remove(key);
					map.put(newKey, list);
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * 把map的key转换为String[]
	 * @param map
	 * @return
	 */
	public static String[] getMapKeyToString(Map map) {
		String[] s = new String[map.size()];
		Set keySet = map.keySet();
		Iterator tr = keySet.iterator();
		int i = 0;
		while (tr.hasNext()) {
			Object key = tr.next();
			String keys = key.toString();
			s[i] = keys;
			i++;
		}
		return s;
	}

	/**
	 * 根据key获取map中的值；如果map中没有key，则返回空字符串("")
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getMapValue(Map map, String key) {
		String s = "";
		if(map.containsKey(key)){
			return (String) map.get(key);
		}
		return s;
	}
	
	/**
	 * 根据key获取map中的值并且转换为list
	 * @param map
	 * @param key
	 * @return
	 */
	public static List<String> getMapValues(Map map, String key) {
		if(map.containsKey(key)){
			try {
				return (List<String>) map.get(key);
			} catch (Exception e) {
				log.debug("此键'"+key+"'的值不是List", e);
			}
		}
		return null;
	}
	
	/**
	 * 获取list对应的下标值，如果没有返回字符串"null"
	 * @param list
	 * @param index 下标
	 * @return
	 */
	public static String getListValue(List<String> list, int index) {
		index=Math.abs(index);
		if(list==null || list.isEmpty() || list.size()<index){
			return "null";
		}
		
		return list.get(index);
	}	
	
	/**
	 * 获取此key在list中的下标
	 * @param key
	 * @param list
	 * @return
	 */
	public static int getKeyIndex(String key,List<String> list){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(key)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 查看str是否包含key
	 * @param key
	 * @param list
	 * @return
	 */
	public static boolean containsKey(String key,String str){
		if(key==null || str==null){
			return false;
		}
		return str.equals(key);
	}	
	
	/**
	 * 根据key查询list中是否存在，不区分大小写
	 * @param key
	 * @param list
	 * @return
	 */
	public static boolean listEqualsIgnoreCase(String key,List<String> list){
		if(list==null || list.isEmpty() || StringUtils.isEmpty(key)){
			return false;
		}
		for (String str : list) {
			if(StringUtils.isNotEmpty(str) && str.equalsIgnoreCase(key)){
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * 根据key查询list中是否存在，不区分大小写
	 * @param key
	 * @param list
	 * @return
	 */
	public static boolean listEqualsIgnoreCase(Integer key,List<Integer> list){
		if(list==null || list.isEmpty() || key==null){
			return false;
		}
		for (Integer obj : list) {
			if(obj!=null && obj==key){
				return true;
			}
		}
		return false;
	}	
	
}

