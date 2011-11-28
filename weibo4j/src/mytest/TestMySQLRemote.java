package mytest;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestMySQLRemote {
	public static void main(String[] args) throws Exception{
		java.sql.Connection  con = null;
	    Class.forName("com.mysql.jdbc.Driver"); 
	    Statement stmt=null;
	    String SQLstr="",SQLstr2="";
	    String geo;
	    con = DriverManager.getConnection( "jdbc:mysql://192.168.1.109:3306/" +
	    		"新浪微博?user=root&password=ucai&useUnicode=true&characterEncoding=" +
	    		"utf-8&rewriteBatchedStatements=true" ); 
	    if(con != null)
	    	System.out.println("succeed");
	    stmt = con.createStatement();
	   ResultSet rs = stmt.executeQuery("select text from lishuangjiang limit 100");
	   while(rs.next()){
		   System.out.println(rs.getString("text"));
	   }
	}
}
