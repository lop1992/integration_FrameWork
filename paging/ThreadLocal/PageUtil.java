package net.integration.framework.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.integration.framework.util.OrderInfoUtils;
import net.integration.framework.util.PageInfoUtils;
import net.integration.framework.util.RequestUtil;
import net.integration.framework.util.StringUtils;

/**
 * <b>描述<b>：开启分页/获取分页page/清除分页page<br><br>
 * 
 * 开启分页page<br>
 * 如果在controller中要使用分页查询，那么在查询语句前可以调用this.startPage(),也可以直接PageUtil.startPage();<br>
 * 如果在service等其他地方则只能PageUtil.startPage()开启分页。<br><br>
 * 
 * 获取开启的分页page<br>
 * 如果在controller中要获取开启的分页page，那么在查询语句结束后可以调用this.getPage(),也可以直接PageUtil.getPage();<br>
 * 如果在service等其他地方则只能PageUtil.getPage()获取开启的分页page。<br><br>
 * 
 * 清除分页page<br>
 * 如果在controller中要清除分页page，可以调用this.endPage(),也可以直接PageUtil.endPage();<br>
 * 如果在service等其他地方则只能PageUtil.endPage()清除分页page。<br><br>
 * 
 * 
 * @version v1.0
 */
public class PageUtil {
	/**
	 * 开启分页Page
	 * @return 返回开启的分页Page
	 */
	public static Page startPage() {
		HttpServletRequest request = RequestUtil.getRequest();
		Map<String, String[]> parameterMap =request.getParameterMap();
		String currentPage = null;
		String pageSize = null;
		String sort = null;
		String order = null;
		String isExport = request.getParameter("isExport");
		if (isExport != null && !"".equals(isExport) && "1".equals(isExport)) { // 导出EXCEL
			Page page = new Page();
			page.setCurrentPage(-1);
			page.setPage(false);
			PageInfoUtils.set(page);
			return page;
		}else{
			Page page = new Page();
			page.setPage(true);
			if(parameterMap.get("currentPage") != null){
				currentPage = parameterMap.get("currentPage")[0];
				page.setCurrentPage(Integer.parseInt(currentPage));
			}
			if(parameterMap.get("pageSize") != null){
				pageSize = parameterMap.get("pageSize")[0];
				page.setPageSize(Integer.parseInt(pageSize));
			}
			if(parameterMap.get("sort") != null){
				sort = parameterMap.get("sort")[0];
				page.setSort(sort);
			}
			if(parameterMap.get("order") != null){
				order = parameterMap.get("order")[0];
				page.setOrder(order);
			}
			
			if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)){
				if(sort.contains(",")){
					OrderInfoUtils.set(sort.split(","),order.split(","));
				}else{
					OrderInfoUtils.set(sort+" "+order);
				}
				
			}
			
			request.setAttribute("page", page);
			PageInfoUtils.set(page);
			return page;
		}
	}
	
	/**
	 * 获取分页对象
	 * @return 开启的分页对象
	 */
	public static Page getPage() {
		return PageInfoUtils.get();
	}
	
	/**
	 * 结束分页
	 */
	public static void endPage() {
		PageInfoUtils.clear();
	}
}
