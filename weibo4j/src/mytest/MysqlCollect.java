package mytest;
//import weibo4j.QueryResult;
//import weibo4j.Tweet;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.Status;

import weibo4j.http.AccessToken;
import weibo4j.http.HttpClient;
import weibo4j.http.ImageItem;
import weibo4j.http.PostParameter;
import weibo4j.http.RequestToken;
import weibo4j.http.Response;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.io.*;
import java.sql.*;

import javax.management.Query;

public class MysqlCollect {
	public static void main(String[]args)throws Exception{
		int excepTimes=0;
		int totalTime=0;
		try{
			 java.sql.Connection  con = null;
		     Class.forName("com.mysql.jdbc.Driver"); 
		    Statement stmt=null;
		    String SQLstr="",SQLstr2="";
		    String geo;
		    System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));//12.7 添加的
		    System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000));//12.7 添加的
		    con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/SinaWeibo3?user=root&password=admin&useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true" ); 
		    //int sss=11;
		    int batchTimes=0; 
			PreparedStatement ps=con.prepareStatement("insert into text(userID,textID,created_at,text,source," +
					"favorited,truncated,in_reply_to_status_id,in_reply_to_user_id,in_reply_to_screen_name,thumbnail_pic, " +
					"bmiddle_pic, original_pic, geo,retweeted_status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
			
			PreparedStatement userSt=con.prepareStatement("insert into user" +
			"(userID,screen_name,location,description,followers_count,friends_count,statuses_count,created_at,verified) values(?,?,?,?,?,?,?,?,?)");
				while(true)
				{
					try{
						batchTimes++;
        		    if(con!=null) System.out.println("server Connected!");
        		    stmt=con.createStatement();	
        		    stmt.setQueryTimeout(60);//2.23添加
        		    Weibo.CONSUMER_KEY="1075591520";
        		    Weibo.CONSUMER_SECRET="00ab18cf31ddec1c5480923fed8f0f76";
        		    
        		    Weibo weibo = new Weibo("yoyozhou19900180@163.com", "69015870");
        		    
        		    
					List<Status> statuses = weibo.getPublicTimeline();
//					List<Status> sts= Status.constructStatuses(get(weibo.getBaseURL() +
//			                "statuses/public_timeline.json?count=200&", true));
//					for(Status st:sts)
//					{
//						System.out.println(st.getText());
//					}
//					System.out.println(sts.size());
		            InputStreamReader   stdin   =   new   InputStreamReader(System.in);//键盘输入 
		            BufferedReader   bufin   =   new   BufferedReader(stdin); 
		            String str=bufin.readLine();
					/*
					 *     public List<Status> getPublicTimeline() throws
            WeiboException {
    	
        return Status.constructStatuses(get(getBaseURL() +
                "statuses/public_timeline.json?count=200&", true));

//      return Status.constructStatuses(get(getBaseURL() +"statuses/public_timeline.xml", true), this);
    }
					 * */
					 //long time1=System.currentTimeMillis();
					for (Status status : statuses) {
						SQLstr="";
						geo=new Double(status.getLatitude()).toString()+" "+new Double(status.getLongitude()).toString();
						ps.setLong(1,status.getUser().getId());
						ps.setLong(2, status.getId());
						ps.setString(3,status.getCreatedAt().toString());
						ps.setString(4, status.getText());
						ps.setString(5, status.getSource());
						ps.setBoolean(6,status.isFavorited());
						ps.setBoolean(7, status.isTruncated());
						ps.setLong(8,status.getInReplyToStatusId());
						ps.setLong(9, status.getInReplyToUserId());
						ps.setString(10, status.getInReplyToScreenName());
						ps.setString(11, status.getThumbnail_pic());
						ps.setString(12, status.getBmiddle_pic());
						ps.setString(13, status.getOriginal_pic());
						ps.setString(14, geo);
						if(status.isRetweet())//若是retweet
							ps.setLong(15, status.getRetweetDetails().getRetweetId());
						else
							ps.setLong(15, -1);//-1表示没有retweet
						ps.addBatch();
						//ps.execute();
						/*SQLstr="insert into text(userID,textID,created_at,text,source," +
						"favorited,truncated,in_reply_to_status_id,in_reply_to_user_id,in_reply_to_screen_name,thumbnail_pic, bmiddle_pic, original_pic, geo,retweeted_status) values ('"+status.getUser().getId()+"', '"
						+status.getId()+"', '"+status.getCreatedAt().toString().replace("'", "''")+"', '"+(new String((status.getText().replace("'", "''")).getBytes(),"utf-8"))+"', '"+
						status.getSource().replace("'", "''")+"','"+status.isFavorited()+"','"+status.isTruncated()+"','"+
						status.getInReplyToStatusId()+"', '"+status.getInReplyToUserId()+"', '"+status.getInReplyToScreenName().replace("'", "''")
						+"', '"+status.getThumbnail_pic().replace("'", "''")+"', '"+status.getBmiddle_pic().replace("'", "''")+"', '"+status.getOriginal_pic().replace("'", "''")+"', '"
						//+geo+"', '"+status.getRetweetDetails().getRetweetId()+"'";//当不是retweet的时候，是没有这个字段的，直接这样写会引发nullpointer异常
						+geo+"',''"+");";
						//+"','')";
	
						stmt.executeUpdate(SQLstr);*/

						userSt.setLong(1,status.getUser().getId() );
						userSt.setString(2, status.getUser().getScreenName());
						userSt.setString(3, status.getUser().getLocation());
						userSt.setString(4, status.getUser().getDescription());
						userSt.setInt(5, status.getUser().getFollowersCount());
						userSt.setInt(6,status.getUser().getFriendsCount());
						userSt.setInt(7,status.getUser().getStatusesCount());
						userSt.setString(8,status.getUser().getCreatedAt().toString());
						userSt.setBoolean(9, status.getUser().isVerified());
						userSt.addBatch();
						//userSt.execute();
							/*SQLstr2="insert into user values('"+status.getUser().getId()+"', '"+status.getUser().getScreenName().replace("'", "''")
							+"', '0', '0', '"+status.getUser().getLocation().replace("'", "''")+"','"+status.getUser().getDescription().replace("'", "''")+"', '"+"tempurl"
							+"', '"+"n"+"','"+status.getUser().getFollowersCount()+"', '"+status.getUser().getFriendsCount()+"', '"+status.getUser().getStatusesCount()
							+"', '"+status.getUser().getFavouritesCount()+"', '"+status.getUser().getCreatedAt().toString().replace("'", "''")+"', '"+status.getUser().isVerified()+"')";//province，city，location的值都没有
							stmt.executeUpdate(SQLstr2);//2.23 改动，不再检查不同的用户，以加快速度*/
						//}
					}
					if(batchTimes>=10)
					{
						System.out.println("inserting...");
						long time1=System.currentTimeMillis();
						ps.executeBatch();
						userSt.executeBatch();
						long time2=System.currentTimeMillis();
						System.out.println(time2-time1);
						//ps.clearBatch();
						//userSt.clearBatch();
						batchTimes=0;
					}
					System.out.println("异常出现次数为:"+excepTimes);
					//ps.close();
					//userSt.close();
					
					 //long time2=System.currentTimeMillis();
					 //totalTime+=time2-time1;
					 //System.out.println(time2-time1);
					Thread.sleep(5000);
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("this happes in while");
						excepTimes++;					
						}
				}
				//con.close();
				//System.out.println(totalTime);
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace(System.out);
			
		}finally{
			
			
		}
		
	}
}
