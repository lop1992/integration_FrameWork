package net.integration.framework.page;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {
	private static final long serialVersionUID = 1L;

	private int totalRows = 0; // 记录总数

	private int totalPages = 0; // 总页数

	private int pageSize = 10; // 每页显示数据条数，默认为10条记录

	private int currentPage = 1; // 当前页数

	private int endPage = 1; // 结束页数
	
	private int start;
	
	private boolean isUse;//是否使用；
	
	private int showPages = 5;//显示页数按钮数
	
	private int showPageStart;//显示页数按钮起始数
	
	private int showPageEnd;//显示页数按钮结束数
	
	private int end;

	private boolean hasPrevious = false; // 是否有上一页

	private boolean hasNext = false; // 是否有下一页

	private String filterString;  //封装查询传参
	
	private String sort;//排序字段
	
	private String order;//排序类型：升序/降序	
	
	private boolean isPage = false;
	
	private String sql;
	private boolean entityOrField;

	private List<?> listData;//分页数据
	
	public Page() {

	}

	public void init(int totalRows, int pageSize) {
		this.totalRows = totalRows;
		this.pageSize = pageSize;
		totalPages = ((totalRows + pageSize) - 1) / pageSize;
		setEndPage(totalRows, pageSize);
		refresh(); // 刷新当前页面信息

	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	private void setEndPage(int totalRows, int pageSize) {
		int pageNumber = totalRows / pageSize;
		int pageRemainder = totalRows % pageSize;
		if (pageRemainder > 0) {
			this.endPage = pageNumber + 1;
		} else {
			this.endPage = pageNumber;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if(currentPage<1){
			currentPage=1;
		}
		this.currentPage = currentPage;
		refresh();

	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		refresh();
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		refresh();
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		this.totalPages=(int) Math.ceil((double)this.totalRows/this.pageSize);
		if(this.currentPage>this.totalPages){
			this.currentPage=this.totalPages;
		}
		refresh();
	}

	// 跳到第一页
	public void first() {
		currentPage = 1;
		this.setHasPrevious(false);
		refresh();
	}

	// 取得上一页（重新设定当前页面即可）

	public void previous() {
		currentPage--;
		refresh();
	}

	// 取得下一页

	public void next() {
		if (currentPage < totalPages) {
			currentPage++;
		}
		refresh();
	}

	// 跳到最后一页

	public void last() {
		currentPage = totalPages;
		this.setHasNext(false);
		refresh();
	}

	public boolean isHasNext() {
		return hasNext;
	}

	/**
	 * 
	 * @param hasNext
	 *            The hasNext to set.
	 * 
	 */

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	/**
	 * 
	 * @param hasPrevious
	 *            The hasPrevious to set.
	 * 
	 */

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	// 刷新当前页面信息

	public void refresh() {
		if (totalPages <= 1) {
			hasPrevious = false;
			hasNext = false;
		} else if (currentPage == 1) {
			hasPrevious = false;
			hasNext = true;
		} else if (currentPage == totalPages) {
			hasPrevious = true;
			hasNext = false;
		} else {
			hasPrevious = true;
			hasNext = true;
		}
		
	}

	public int getEndPage() {
		int pageNumber = totalRows / pageSize;
		int pageRemainder = totalRows % pageSize;
		if (pageRemainder > 0) {
			this.endPage = pageNumber + 1;
		} else {
			this.endPage = pageNumber;
		}
		return endPage;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isPage() {
		return isPage;
	}

	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}

	public int getStart() {
		int start = (currentPage - 1) * pageSize;
        if (start < 1) {
        	start = 0;
        }
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	public int getShowPages() {
		return showPages;
	}

	public void setShowPages(int showPages) {
		this.showPages = showPages;
	}

	public int getShowPageStart() {
		if(currentPage>3){
			showPageStart = currentPage-2;
		} else {
			showPageStart = 1;
		}
		if(currentPage > 5 && currentPage > this.getTotalPages() - 2) {
			showPageStart = this.getTotalPages() - 4;
		}
		return showPageStart;
	}

	public void setShowPageStart(int showPageStart) {
		this.showPageStart = showPageStart;
	}

	public int getShowPageEnd() {
		showPageEnd = this.getShowPageStart()+showPages-1;
		if(showPageEnd > this.getTotalPages()) {
			showPageEnd = this.getTotalPages();
		}
		return showPageEnd;
	}

	public void setShowPageEnd(int showPageEnd) {
		this.showPageEnd = showPageEnd;
	}

	/**
	 * @return isUse
	 */
	public boolean isUse() {
		return isUse;
	}

	/**
	 * @param isUse 要设置的 isUse
	 */
	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
	public List<?> getListData() {
		return listData;
	}

	public void setListData(List<?> listData) {
		this.listData = listData;
	}
	
}
