package sjtu.edu.cn.corpus;

import edu.sjtu.ltlab.word.split.IRSplit;

public class Analysis {
	public static void getBackGround(){
		try{
			 java.sql.Connection  con = null;
		     //String url = "jdbc:sqlserver://";
		     //String serverName= "DANTEL_LAB";
		     //String portNumber = "1433";
		     //String databaseName= "newSinaWeibo";
		     String userName = "sa";
		     String password = "69015870";
		     String conURL="jdbc:sqlserver://127.0.0.1:1433;DatabaseName=newSinaWeibo;";
		     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		     con=java.sql.DriverManager.getConnection(conURL,userName,password);
		     
		     
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		
	}
}
