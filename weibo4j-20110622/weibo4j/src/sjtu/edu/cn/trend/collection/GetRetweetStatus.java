package sjtu.edu.cn.trend.collection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import weibo4j.Status;
import weibo4j.Weibo;

public class GetRetweetStatus {
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
//	public static Weibo weibo = new Weibo();
	public Weibo weibo;
	static{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
//	    weibo.setToken(token, tokenSecret);
	}
	public GetRetweetStatus(){
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
	}
	/**
	 * first version of updateByTable , simultaneously search the table and get the retweeted
	 * status.
	 * 
	 * cost too much time, hence is deprecated
	 * @param tableName
	 */
	public void updateByTable(String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			ResultSet rs = helper.getAllWeibo(tableName);
			
			while(rs.next()){
				long rId = rs.getLong("retweeted_status");
				if(rId != -1)
				{
					try{
						Status st = weibo.showStatus(rId);
//						st.getRetweeted_status().
						helper.insertRetweetStatusWithoutCloseConnection("retweet_table", st);
					}catch(Exception e){
						e.printStackTrace();
					}
					Thread.sleep(3000);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * second version of updateByTable, first search the original table to get a list of 
	 * distinctive ids, then search for the Retweeted status.
	 * @param tableName
	 */
	public void updateByTableV2(String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			String sql = "select retweeted_status from " + tableName
			+ " where retweeted_status != -1";
			ResultSet rs = st.executeQuery(sql);
			HashSet<Long> lists = new HashSet<Long>();
			while(rs.next()){
				lists.add(rs.getLong(1));
			}
			
			
			//search the retweet_table for existing ids
			String sql2 = "select textID from retweet_table";
			ResultSet rs2 = st.executeQuery(sql2);
			HashSet<Long> existLists = new HashSet<Long>();
			while(rs2.next()){
				existLists.add(rs2.getLong("textID"));
			}
			
			
			for(Long id:lists){
				if(!existLists.contains(id)){
					try{
						Status s = weibo.showStatus(id);
						helper.insertRetweetStatusWithoutCloseConnection("retweet_table", s);
					}catch(Exception e){
						e.printStackTrace();
					}
					Thread.sleep(1000);
				}

			}
			
//			while(rs.next()){
//				long rId = rs.getLong("retweeted_status");
//				if(rId != -1)
//				{
//					try{
//						Status st = weibo.showStatus(rId);
//						helper.insertRetweetStatusWithoutCloseConnection("retweet_table", st);
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					Thread.sleep(3000);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args)throws Exception{
		GetRetweetStatus grs = new GetRetweetStatus();
		grs.updateByTableV2("李双江之子打人");
	}
}
