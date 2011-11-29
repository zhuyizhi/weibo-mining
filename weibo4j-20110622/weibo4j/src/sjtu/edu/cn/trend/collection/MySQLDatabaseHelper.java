package sjtu.edu.cn.trend.collection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDatabaseHelper {
	private static String port = "3306";
	private static String DATABASE_NAME = "新浪微博";
	private static String user = "root";
	private static String password = "ucai";
	private static String otherCommand = "useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true";
	
	private static Connection con ;
	
	public static Connection getConnected(){
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			String connectString = "jdbc:mysql://192.168.1.109:"+port+"/"+DATABASE_NAME+"?user="+user
			+"&password="+password+"&"+otherCommand;
			con = DriverManager.getConnection(connectString);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	public static void closeConnection(){
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void executeSQL(String sqlString ){
		try{
			Statement st = con.createStatement();
			st.execute(sqlString);
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	
	public static void execureInsertSQL(String sqlString){
		try{
			Statement st = con.createStatement();
			st.executeUpdate(sqlString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void execureQuerySQL(String sqlString){
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sqlString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
}
