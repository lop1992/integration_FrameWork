package net.integration.framework.util;

import net.integration.framework.page.Page;

/**
 * 分页工具类
 * 用于设置page和获取page对象
 */
public final class PageInfoUtils {
	private static ThreadLocal<Page> PAGEINFOLOCAL = new ThreadLocal<Page>();

	public static Page get() {
		return PAGEINFOLOCAL.get();
	}

	public static void set(Page page) {
		PAGEINFOLOCAL.set(page);
	}

	public static void clear() {
		PAGEINFOLOCAL.remove();
	}

}
