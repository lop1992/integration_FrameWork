package net.integration.framework.util;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 名称：JDBC工具类<br>
 * 描述：提供Connection连接方法
 * @version 1.0.0.0
 */
public class JdbcUtil {

	private static String url;
	private static String user;
	private static String password;
	private static String driverClassName;
	private static String remarks;
	private static String useInformationSchema;

	private static Properties prop;
	private static Properties props;
	private static String dataType;
	static {
		prop = new Properties();
		InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		url = prop.getProperty("sjk.connection.url");
		user = prop.getProperty("sjk.connection.username");
		password = prop.getProperty("sjk.connection.password");
		driverClassName = prop.getProperty("sjk.connection.driverClassName");
		remarks = prop.getProperty("sjk.connection.remarks");
		remarks=remarks==null?"false":remarks;
		useInformationSchema = prop.getProperty("sjk.connection.useInformationSchema");
		useInformationSchema=useInformationSchema==null?"false":useInformationSchema;
		dataType=prop.getProperty("database.type");
	}

	static {
		try {
			if(null != driverClassName && !"".equals(driverClassName)) {
				Class.forName(driverClassName);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>获取数据库连接</b>
	 * 参数：需要在根路径中配置jdbc.properties文件<br>
	 * 如：
	 * driverClassName=com.mysql.jdbc.Driver
	 * url=jdbc:mysql://localhost:3306/scgx_qxyy?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
	 * user=root
	 * password=123456
	 * remarks=true
	 * useInformationSchema=true
	 * @return 返回数据库连接Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		//根据链接的数据库类型,区分获取的数据信息
		if(StringUtils.isNotBlank(dataType)&& dataType.equalsIgnoreCase("MYSQL")){
			props.setProperty("remarks", remarks); // 设置可以获取remarks信息
			props.setProperty("useInformationSchema", useInformationSchema);// 设置可以获取tables
		}else if(StringUtils.isNotBlank(dataType)&& dataType.equalsIgnoreCase("Oracle")){
			props.put("remarksReporting",useInformationSchema);
			props.put("ResultSetMetaDataOptions",1);
		}
		return DriverManager.getConnection(url, props);
	}

	/**
	 * <b>获取数据库连接</b>
	 * 参数：driverClassName连接驱动，url数据库连接地址，user数据库用户名，password数据库密码，返回：Connection。
	 * @param driverClassName连接驱动，如：com.mysql.jdbc.Driver
	 * @param url数据库连接地址，如：jdbc:mysql://localhost:3306/scgx_qxyy?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
	 * @param user数据库用户名，如：root
	 * @param password数据库密码，如：123456
	 * @return 返回数据库连接Connection
	 * @throws SQLException
	 */
	public static Connection getConnection(String driverClassName, String url, String user, String password) throws SQLException {
		props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		props.setProperty("remarks", "true"); // 设置可以获取remarks信息
		props.setProperty("useInformationSchema", "true");// 设置可以获取tables
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(url, props);
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

    public static void main(String[] args) throws SQLException {
    	System.out.println(getConnection());
    	System.out.println(JdbcUtil.url);
	}
}
