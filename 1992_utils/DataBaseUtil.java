package net.integration.framework.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * <b>描述<b>：数据库工具包
 * @version v1.0
 */
public class DataBaseUtil {

	private static Logger log = Logger.getLogger(DataBaseUtil.class);
	public static enum DataBase{MYSQL,DM,ORACLE,KINGBASE,SQLSERVER,DB2};
	
	/**
	 * 获取DataSource
	 * @return
	 */
	public static DataSource getDataSource(){
//		DataSource dataSource=(DataSource) JNDIUtil.lookup(JNDIProperties.getLocal());
		DataSource dataSource = ApplicationContextUtil.getBean(DataSource.class);
		return dataSource;
	}
	
	/**
	 * 获取数据库连接
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		Connection conn=null;
		try {
			conn=getDataSource().getConnection();
		} catch (Exception e) {
			log.debug("使用jndi获取Connection报错，转为使用JdbcUtil获取");
			conn=JdbcUtil.getConnection();
		}
		
		return conn;
	}
	
	/**
	 * 根据Connection获取数据库类型
	 * @return DataBase{MYSQL,DM,ORACLE}
	 */
	public static DataBase getDataBaseType(){
		try {
			Connection con = DataBaseUtil.getConnection();
			String dbName = con.getMetaData().getDatabaseProductName();
			if(StringUtils.isNotEmpty(dbName)){
				dbName=dbName.toUpperCase();
			}
			if(dbName.contains("DM")){
				return DataBase.DM;
			}else if(dbName.contains("ORACLE")){
				return DataBase.ORACLE;
			}else if(dbName.contains("KINGBASE")){
				return DataBase.KINGBASE;
			}else if(dbName.contains("SQLSERVER")){
				return DataBase.SQLSERVER;
			}else if(dbName.contains("DB2")){
				return DataBase.DB2;
			}
		} catch (SQLException e) {
			log.debug("获取数据库类型失败，默认为mysql", e);
			e.printStackTrace();
		}
		return DataBase.MYSQL;
	}
	
	/**
	 * 根据数据库类型获取sql-》sql为查询数据库表的主键
	 * @return sql
	 */
	public static String getQueryPKSQL(){
		String sql="";
		DataBase dataBaseType = getDataBaseType();
		if(dataBaseType==DataBase.MYSQL){
			sql = "SELECT C.COLUMN_NAME AS CNAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS T, INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS C "
					+ "WHERE T.TABLE_NAME = C.TABLE_NAME AND T.TABLE_NAME = ? AND T.CONSTRAINT_TYPE = 'PRIMARY KEY'";
		}else if(dataBaseType==DataBase.DM){
			sql = "SELECT CU.COLUMN_NAME AS CNAME FROM ALL_CONS_COLUMNS CU,ALL_CONSTRAINTS AU WHERE CU.CONSTRAINT_NAME = AU.CONSTRAINT_NAME AND AU.CONSTRAINT_TYPE = 'P' AND AU.TABLE_NAME = ?";
		}else if(dataBaseType==DataBase.ORACLE){
			sql="SELECT COL.COLUMN_NAME AS CNAME FROM USER_CONSTRAINTS CON,USER_CONS_COLUMNS COL WHERE CON.CONSTRAINT_NAME = COL.CONSTRAINT_NAME AND CON.CONSTRAINT_TYPE = 'P' AND COL.TABLE_NAME =?";
		}else if(dataBaseType==DataBase.DB2){
			
		}else if(dataBaseType==DataBase.KINGBASE){
			
		}else if(dataBaseType==DataBase.SQLSERVER){
			
		}
		return sql;
	}
	
	/**
	 * 根据数据库类型转换date
	 * @param str
	 * @return
	 */
	public static String getSqlDateInDB(String str){
		String sql="";
		DataBase dataBaseType = getDataBaseType();
		if(dataBaseType==DataBase.MYSQL){
			sql ="'"+str+"'";
		}else if(dataBaseType==DataBase.DM){
			sql ="to_date('"+str+"','yyyy-MM-dd')";
		}else if(dataBaseType==DataBase.ORACLE){
			
		}
		return sql;
	}
	
	/**
	 * 根据数据库类型转换date
	 * @param str
	 * @return
	 */
	public static String getSqlDateTimeInDB(String str){
		String sql="";
		DataBase dataBaseType = getDataBaseType();
		if(dataBaseType==DataBase.MYSQL){
			sql =str;
		}else if(dataBaseType==DataBase.DM){
			sql ="to_date('"+str+"','yyyy-MM-dd HH24:mi:ss')";
		}else if(dataBaseType==DataBase.ORACLE){
			
		}
		return sql;
	}	

	public static void close(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
