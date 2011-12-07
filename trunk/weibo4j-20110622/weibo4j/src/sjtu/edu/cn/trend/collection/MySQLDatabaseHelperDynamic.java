package sjtu.edu.cn.trend.collection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDatabaseHelperDynamic {
	public String port = "3306";
	public String DATABASE_NAME = "新浪微博";
	public String user = "root";
	public String password = "ucai";
	public String otherCommand = "&useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true";
	
	public Connection con ;
	
	public MySQLDatabaseHelperDynamic(String port, String databaseName, String user, String password, String otherCommand){
		this.port = port;
		this.DATABASE_NAME = databaseName;
		this.user = user;
		this.password = password;
		this.otherCommand = otherCommand;
	}
	
	public MySQLDatabaseHelperDynamic(String port, String databaseName, String user, String password){
		this.port = port;
		this.DATABASE_NAME = databaseName;
		this.user = user;
		this.password = password;
		this.otherCommand = "";
	}
	
	public MySQLDatabaseHelperDynamic(){
		
	}
	
	public  void getConnected(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			String connectString = "jdbc:mysql://202.120.38.155:"+port+"/"+DATABASE_NAME+"?user="+user
			+"&password="+password+otherCommand;
			con = DriverManager.getConnection(connectString);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try{
			if(con != null && con.isValid(3))
				con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void executeSQL(String sqlString ){
		try{
			Statement st = con.createStatement();
			st.execute(sqlString);
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	
	public void execureInsertSQL(String sqlString){
		try{
			Statement st = con.createStatement();
			st.executeUpdate(sqlString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ResultSet execureQuerySQL(String sqlString){
		ResultSet rs = null;
		try{
			Statement st = con.createStatement();
			rs = st.executeQuery(sqlString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}	
	
}
