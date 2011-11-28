package extraction.target.toys;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;

public class GetNEs {
	public static void getNEs(String tableName, int thresholdInt, String filePath){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from wordfrequency where count > '" + thresholdInt + "' order by count desc");
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(filePath), "UTF-8"));
			while(rs.next()){
				String token = rs.getString("token");
				String reg = "\\S+/n[z,i,s]+";
				if(token.matches(reg) ){
					pw.println(token + " " + rs.getInt("count"));
				}
			}
			pw.flush();
			pw.close();
			rs.close();
			st.close();
			helper.closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//	public 
	
	public static void main(String[]args){
//		String tableName = args[0];
//		String threshold = args[1];
//		getNEs(tableName, Integer.parseInt(threshold), "otherNEs.txt");
		getNEs("laoren_wordfrequency", 100, "laoren1\\otherNEs.txt");
	}
}
