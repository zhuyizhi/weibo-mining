package sjtu.edu.cn.weibo.basic;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import weibo4j.Weibo;

/**
 * used to demonstrate result
 * @author ucai
 *
 */
public class ShowResult {
	public static Weibo weibo = new Weibo();
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	public static final String retweetTable = "转发次数";
	static {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		    weibo.setToken(token, tokenSecret);
	}
	public static void showStatus(long statusID, String tableName){
		SinaWeiboHelper helper = new SinaWeiboHelper();
		try{
			
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from " + tableName + " where textID = " + statusID);
			while(rs.next()){
				System.out.println(rs.getString(4));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			helper.closeConnection();
		}
	}
	
	public static void showDayTopStatus(String tableName, String date, int top){
		SinaWeiboHelper helper = new SinaWeiboHelper();
		try{
			PrintWriter pw = new PrintWriter(new FileWriter("result.txt"));
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			String sql = "select * from " + retweetTable + " where comeFrom = " +
			"'" + tableName + "' and date = '" + date + "' order by retweetCount desc ";
//			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			int counter = 0;

			ArrayList<Long> ids = new ArrayList<Long>();
			HashMap <Long, Long> retweetMap = new HashMap<Long, Long>();
			while(++counter <= top && rs.next()){
				ids.add(rs.getLong(1));
				retweetMap.put(rs.getLong(1), rs.getLong(2));
//				System.out.println(rs.getLong(1));
			}
			PreparedStatement ps = helper.con.prepareStatement("select * from " + tableName + " where " +
			" textID = ?");
			
			for(Long id : ids){
				ps.setLong(1, id);
//				System.out.println("id = " + id);
				ps.execute();
				ResultSet rs2 = ps.getResultSet();
				while(rs2.next()){
					System.out.println(rs2.getString(4) + "转发次数: " + retweetMap.get(id));
					pw.println(rs2.getString(4) + "转发次数: " + retweetMap.get(id));
				}
			}
			
			pw.flush();
			pw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			helper.closeConnection();
		}
	}
	
	
	public static void main(String[]args){
//		long id = Long.parseLong("3355880662478034");
//		showStatus( id , "李双江之子打人");
//		id = Long.parseLong("3355964327782836");
//		showStatus( id , "李双江之子打人");
		showDayTopStatus("家暴", "2011-10-08", 50);
	}
}
