package net.integration.framework.util;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletActionContext{

	public static ServletContext getServletContext(){
		return RequestUtil.getOriginalRequest().getServletContext();
	}
	
	/**
	 * 此requent为spring接管后的requent
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return RequestUtil.getRequest();
	}


	/**
	 * 此requent为原始requent
	 * 
	 * @return
	 */
	public static HttpServletRequest getOriginalRequest() {
		return RequestUtil.getOriginalRequest();
	}

	
	public static HttpServletResponse getResponse() {
		return RequestUtil.getResponse();
	}
	
	public static void main(String[] args) {
	}
}
