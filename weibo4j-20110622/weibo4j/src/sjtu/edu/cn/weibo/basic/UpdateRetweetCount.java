package sjtu.edu.cn.weibo.basic;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import weibo4j.Count;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * This class is designed to update retweet number of the microblog stored in the database 
 * @author ucai
 *
 */
public class UpdateRetweetCount {
	
	
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	
	{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
	public Weibo weibo;
	
	public UpdateRetweetCount(){
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
	}
	
	public static void updateTable(String tableName){
		try{
			SinaWeiboHelper helper  = new SinaWeiboHelper();
			HashMap<Long, Long> counter = new HashMap<Long, Long>();
			String sqlString = "select * from " + tableName;
			System.out.println(sqlString);
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sqlString);
			
//			ResultSet rs = helper.execureQuerySQL(sqlString);
			
			while(rs.next()){
				long retweetID = rs.getLong(10);
//				System.out.println("retweet id = " + retweetID);
				if(retweetID != -1){
					if(counter.containsKey(retweetID)){
						Long oldNumber = counter.get(retweetID);
						counter.remove(retweetID);
						counter.put(retweetID, oldNumber++);
					}else{
						counter.put(retweetID, new Long(1));
					}
				}

			}//从数据库中读出所有的微博，并更新引用计数
			
			helper.updateRetweetTable(counter);
			
		}catch(Exception e){
			
		}
	}
	
	public  void updateTableByAPI(String tableName){
		try{
			SinaWeiboHelper helper  = new SinaWeiboHelper();
			HashMap<Long, Long> counter = new HashMap<Long, Long>();
			HashMap<Long, java.sql.Date> createdAt = new HashMap<Long, java.sql.Date>();
			String sqlString = "select * from " + tableName;
			System.out.println(sqlString);
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sqlString);
				
			int i = 0;
			int ite = 0;
			StringBuilder sb = new StringBuilder();
			while(rs.next()){
				if(++i < 100){
					sb.append(rs.getLong(2) + ",");
					java.sql.Date date = rs.getDate(3);
					createdAt.put(rs.getLong(2), date);
				}else{
					sb.append(rs.getLong(2) );
					java.sql.Date date = rs.getDate(3);
					createdAt.put(rs.getLong(2), date);
					
					String ids = sb.toString();
					System.out.println("ids = " + ids);
					sb = new StringBuilder();
					
					List<Count> countList = weibo.getCounts(ids);
					
					for(Count c : countList){
						counter.put(c.getID(), c.getRt());
					}					
					helper.refreshRetweetTableClear(counter, createdAt, tableName);
					counter.clear();
					
					System.out.println("ok!!!!! size = " + countList.size());
					System.out.println("iteration = " + (++ite));
					Thread.sleep(5000);
					i = 0;
					
				}
			}//从数据库中读出所有的微博，并更新引用计数
			
			String ids = sb.toString();
			List<Count> countList = weibo.getCounts(ids);
			for(Count c : countList){
				counter.put(c.getID(), c.getRt());
			}
			helper.refreshRetweetTableClear(counter, createdAt, tableName);
			
//			helper.updateRetweetTable(counter);
			
		}catch(Exception e){
			
		}
	}
	
	public static void main(String[]args) throws WeiboException{
		UpdateRetweetCount urc = new UpdateRetweetCount();
//		urc.weibo.getCounts("1748636151");
		urc.updateTableByAPI("李阳");
	}
}
