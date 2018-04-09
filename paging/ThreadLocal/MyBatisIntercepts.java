package net.integration.framework.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.PropertyException;

import net.integration.framework.page.Page;
import net.integration.framework.util.OrderInfoUtils;
import net.integration.framework.util.PageInfoUtils;
import net.integration.framework.util.ReflectUtils;
import net.integration.framework.util.StringUtils;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MyBatisIntercepts implements Interceptor {

	private static String dialect = ""; // 数据库方言
	
	private List<Filter> filterSqlIds=new ArrayList<Filter>();//此sql不会走拦截器
	
	class Filter{
		private String sqlId;
		private int type;//^以什么开始，$以什么技术，*包含;1=^,2=$，3=*
		public boolean isFilterSqlId(String id){
			if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(sqlId)){
				if(type==1){
					return id.startsWith(sqlId);
				}else if(type==2){
					return id.endsWith(sqlId);
				}else if(type==3){
					return id.contains(sqlId);
				}else{
					return id.equals(sqlId);
				}
			}
			return false;
		}
		public void setSqlId(String sqlId) {
			this.sqlId = sqlId;
		}
		public void setType(int type) {
			this.type = type;
		}
		
	}
	
	private boolean checkSqlId(String sqlId){
		boolean flag=true;
		for(Filter filter : filterSqlIds){
			if(filter.isFilterSqlId(sqlId)){//如果找到匹配的则返回false，阻止拦截器执行判断里的sql
				flag=false;
				break;
			}
		}
		return flag;
	}

	public Object intercept(Invocation ivk) throws Throwable {
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectUtils.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectUtils.getValueByFieldName(delegate, "mappedStatement");
			String sqlId = mappedStatement.getId();
			sqlId=sqlId.substring(sqlId.lastIndexOf(".")+1, sqlId.length());
			if(this.checkSqlId(sqlId)){//检查是否包含要过滤的sqlId
				Page page = PageInfoUtils.get();
				String order = OrderInfoUtils.get();
				if (page != null) {
					if(!page.isUse()){//防止在一个request中page被使用多次
						BoundSql boundSql = delegate.getBoundSql();
						String sql = boundSql.getSql();
						Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
						PreparedStatement countStmt = null;
						ResultSet rs = null;
						try {
							Connection connection = (Connection) ivk.getArgs()[0];
							String countSql = "select count(*) from (" + sql + ") tmp_count"; // 记录统计
							countStmt = connection.prepareStatement(countSql);
							ReflectUtils.setValueByFieldName(boundSql, "sql", countSql);
							DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
							parameterHandler.setParameters(countStmt);
							rs = countStmt.executeQuery();
							int count = 0;
							if (rs.next()) {
								count = ((Number) rs.getObject(1)).intValue();
							}
							page.setTotalRows(count);
							page.setUse(true);
						} finally {
							try {
								rs.close();
							} catch (Exception e) {
							}
							try {
								countStmt.close();
							} catch (Exception e) {
							}
						}
						//设置排序
						if(null != order) {
							sql = generateOrderSql(sql, order);
						}
						//设置分页
						String pageSql = generatePageSql(sql, page.getStart(), page.getPageSize());
						ReflectUtils.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
					}
				} else {
					//设置排序
					if(null != order) {
						RoutingStatementHandler statementHandler1 = (RoutingStatementHandler) ivk.getTarget();
						BaseStatementHandler delegate1 = (BaseStatementHandler) ReflectUtils.getValueByFieldName(statementHandler1, "delegate");
						BoundSql boundSql = delegate1.getBoundSql();
						String orderSql = boundSql.getSql();
						orderSql = generateOrderSql(orderSql, order);
						ReflectUtils.setValueByFieldName(boundSql, "sql", orderSql); // 将排序sql语句反射回BoundSql.
					}
				}
			}
			
		}
		return ivk.proceed();
	}

	/**
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, int offset, int limit) {
		if (StringUtils.isEmpty(dialect)) throw new NullPointerException("数据库方言为空！");
		if (null == sql) throw new NullPointerException("sql不能为空！");
		StringBuffer pageSql = new StringBuffer(sql.length() + 88);
		if (dialect.equalsIgnoreCase("mysql")) {
			pageSql.append(sql);
			pageSql.append(" limit " + offset + "," + limit);
		} else if (dialect.equalsIgnoreCase("oracle")) {
			pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ");
			pageSql.append(sql);
			pageSql.append(" ) row_ ) where rownum_ >= " + offset
					+ " and rownum_ <= " + limit);
		} else if (dialect.equalsIgnoreCase("db2")) {
			throw new RuntimeException("DB2没有！");
		}
		return pageSql.toString();
	}

	/**
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generateOrderSql(String sql, String order) {
		if (null == sql) throw new NullPointerException("sql不能为空！");
		StringBuffer resultSql = new StringBuffer();
		resultSql.append(sql);
		
		boolean contains = isContains(sql,"\\)\\s+ORDER\\s+BY");
		if(!contains){
			contains= isContains(sql,"\\s+ORDER\\s+BY");
		}
		
		if(contains){
			resultSql.append(" , " +order);
		}else{
			resultSql.append(" order by " +order);
		}
		
		return resultSql.toString();
	}
	
	private static boolean isContains(String lineText,String word){
		  Pattern pattern=Pattern.compile(word,Pattern.CASE_INSENSITIVE);
		  Matcher matcher=pattern.matcher(lineText);
		  return matcher.find();
	}
	
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException(
						"数据库方言没有找到，请到mybatis的config配置文件查看是否配置方言!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		
		String sqlIds = p.getProperty("filterSqlIds");
		if(StringUtils.isNotEmpty(sqlIds)){
			String[] split = sqlIds.split(",");
			for (String id : split) {
				if(StringUtils.isNotEmpty(id)){
					int type=0;
					String sqlId=id;
					if(id.startsWith("^")){
						type=1;
						sqlId=sqlId.substring(1);
					}else if(id.endsWith("$")){
						type=2;
						sqlId=sqlId.substring(0,sqlId.length()-1);
					}else if(id.startsWith("*")){
						type=3;
						sqlId=sqlId.substring(1);
					}
					
					Filter filter = new Filter();
					filter.setSqlId(sqlId);
					filter.setType(type);
					filterSqlIds.add(filter);
					
				}
				
			}
		}
	}
}
