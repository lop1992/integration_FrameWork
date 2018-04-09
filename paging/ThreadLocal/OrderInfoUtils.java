package net.integration.framework.util;


/**
 * 排序工具类
 * 用于设置排序字段
 */
public final class OrderInfoUtils {
	private static ThreadLocal<String> ORDERINFOLOCAL = new ThreadLocal<String>();

	public static String get() {
		return ORDERINFOLOCAL.get();
	}

	/**
	 * 自定义拼装排序sql语句
	 * @param order 已经写好的sql语句
	 */
	public static void set(String sql) {
		ORDERINFOLOCAL.set(sql);
	}
	
	/**
	 * 多排序支持
	 * @param sort 排序字段数组
	 * @param order 排序字段对应的asc/desc数组
	 */
	public static void set(String[] sort,String[] order) {
		StringBuffer sb = new StringBuffer();
		if(sort!=null && order!=null && sort.length==order.length && sort.length!=0){
			for (int i = 0; i < sort.length; i++) {
				if(i==0){
					sb.append(sort[i]+" "+order[i]);
				}else{
					sb.append(" , "+sort[i]+" "+order[i]);
				}
				
			}
		}
		ORDERINFOLOCAL.set(sb.toString());
	}

	public static void clear() {
		ORDERINFOLOCAL.remove();
	}

}
