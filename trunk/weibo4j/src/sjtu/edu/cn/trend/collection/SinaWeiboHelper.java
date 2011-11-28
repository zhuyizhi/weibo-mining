package sjtu.edu.cn.trend.collection;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import weibo4j.Status;

public class SinaWeiboHelper extends MySQLDatabaseHelperDynamic {
	
	protected String keywordTable = "话题词表";
	protected String retweetTable = "转发次数";
	
//	protected String keywordTable = "trend";
	public SinaWeiboHelper(String port, String databaseName, String user,
			String password, String otherCommand) {
		super(port, databaseName, user, password, otherCommand);
	}
	
	public SinaWeiboHelper(){
		port = "3306";
		DATABASE_NAME = "新浪微博";
		user = "root";
		password = "admin";
		otherCommand = "&useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true";
	}
	
	public  void checkConnection(){
		try{
			if(con == null || (! con.isValid(3))){
				getConnected();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将keyword填入话题词表
	 * @param keyword
	 */
	public void insertKeyWords(String keyword){
		try{
			checkConnection();
			Calendar c = Calendar.getInstance();
			String now = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + 
			c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) +
			":" + c.get(Calendar.SECOND);
			String psSQL = "insert into " + keywordTable + " (trendName, createTime) values (?, ?)";
			System.out.println(psSQL);
			
			java.sql.Date date = new java.sql.Date(c.getTimeInMillis());
			
			PreparedStatement ps = con.prepareStatement(psSQL);
			ps.setString(1, keyword);
			ps.setDate(2, date);
			
			ps.execute();
			ps.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			super.closeConnection();
		}
	}
	
	public List<String> getKeywords(){
		List<String> strList = new ArrayList<String>();
		try{
			checkConnection();
			String sql = "select * from " + keywordTable ;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String keyword = rs.getString(1);
				strList.add(keyword);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			super.closeConnection();
		}
		
		return strList;
	}
	
	public List<String> getKeywordsAfter(Calendar calendar){
		List<String> strList = new ArrayList<String>();
		try{
			checkConnection();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String date = "'" + year + "-" + month + "-" + day + "'";
			String sql = "select * from " + keywordTable + " where createTime > " + date;
			
			System.out.println(sql);
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				String keyword = rs.getString(1);
				strList.add(keyword);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			super.closeConnection();
		}
		return strList;
	}
	
	public void createTrendTable(String tableName){
		checkConnection();
		try{
//			String sql = "create table " + tableName + " (trendName VARCHAR(50) NULL, createTime" +
//					" DATE NULL) ";
			String sqlStr = "create table " + tableName +" ( " + "userID BIGINT NOT NULL, " +
					"textID BIGINT NOT NULL, "+
					"created_at DATE NULL, " +
					"text VARCHAR(400) NULL, " +
					"source VARCHAR(400) NULL, " +
					"truncated VARCHAR(400) NULL, " +
					"in_reply_to_status_id BIGINT, " +
					"in_reply_to_user_id BIGINT, " +
					"original_pic VARCHAR(400), " +
					"retweeted_status BIGINT," +
					"id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL)";
			
			
			System.out.println(sqlStr);
			
			super.executeSQL(sqlStr);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			super.closeConnection();
		}
	}
	
	public boolean checkExistence(Status status, String trendName){
		checkConnection();
		try{
			String sql = "select * from " + trendName + " where textID = " + status.getId();
			ResultSet rs = super.execureQuerySQL(sql);
			
			if(rs.next())
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
		
		return false;
	}
	
	public void insertTrendRelatedWeibo(String trendName, Status status){
		if(! checkExistence(status, trendName)){
			try{
				checkConnection();
				String sql = "insert into " + trendName + " (userID,textID,created_at,text,source," +
				"truncated,in_reply_to_status_id,in_reply_to_user_id, " +
				" original_pic, retweeted_status) values (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps=con.prepareStatement(sql); 
				
				ps.setLong(1,status.getUser().getId());
				ps.setLong(2, status.getId());
				
				java.util.Date date = status.getCreatedAt();
				java.sql.Date sqlDate = new java.sql.Date(date.getYear(), date.getMonth() + 1, date.getDate());
				ps.setDate(3, sqlDate);
				ps.setString(4, status.getText());
				ps.setString(5, status.getSource());
				ps.setBoolean(6, status.isTruncated());
				ps.setLong(7,status.getInReplyToStatusId());
				ps.setLong(8, status.getInReplyToUserId());
				ps.setString(9, status.getOriginal_pic());
				if(status.isRetweet())//若是retweet
					ps.setLong(10, status.getRetweeted_status().getId());
				else
					ps.setLong(10, -1);//-1表示没有retweet
				
				ps.execute();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				super.closeConnection();
			}
		}
	}
	
	
	/**
	 * 插入完不关connection
	 * @param trendName
	 * @param status
	 */
	public void insertRetweetStatusWithoutCloseConnection(String trendName, Status status){
		
			try{
				checkConnection();
				String sql = "insert into " + trendName + " (userID,textID,created_at,text,source," +
				"truncated,in_reply_to_status_id,in_reply_to_user_id, " +
				" original_pic, retweeted_status) values (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps=con.prepareStatement(sql); 
				
				ps.setLong(1,status.getUser().getId());
				ps.setLong(2, status.getId());
				
				java.util.Date date = status.getCreatedAt();
				java.sql.Date sqlDate = new java.sql.Date(date.getYear(), date.getMonth() + 1, date.getDate());
				ps.setDate(3, sqlDate);
				ps.setString(4, status.getText());
				ps.setString(5, status.getSource());
				ps.setBoolean(6, status.isTruncated());
				ps.setLong(7,status.getInReplyToStatusId());
				ps.setLong(8, status.getInReplyToUserId());
				ps.setString(9, status.getOriginal_pic());
				if(status.isRetweet())//若是retweet
					ps.setLong(10, status.getRetweeted_status().getId());
				else
					ps.setLong(10, -1);//-1表示没有retweet
				
				ps.execute();
				ps.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		
	}
	
	

	public List<String> getExistTableName(){
		List<String> tables = new ArrayList<String>();
		try{
			checkConnection();
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getTables(null, null, null, new String[]{"TABLE"});
			while(rs.next()){
				tables.add(rs.getString(3));//column 3 of the description is name of the table
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
		return tables;
	}

	public ResultSet getAllWeibo(String trendName){
		checkConnection();
		ResultSet rs = null;
		try{
			String sqlString = "Select * from " + trendName;
			System.out.println(sqlString);
			Statement st = con.createStatement();
			rs = st.executeQuery(sqlString);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			closeConnection();
		}
		return rs;
	}
	
	public void updateRetweetTable(HashMap<Long, Long> counter){
		checkConnection();
		try{
			Statement st = con.createStatement();
			Set<Long> idSet = counter.keySet();
			for(Long id : idSet){
				Long count = counter.get(id);
				String sqlQuery = "select * from " + retweetTable + " where textID = " + id;
				
				ResultSet rs = st.executeQuery(sqlQuery);
				if(rs.next()){//已经有该微博
					Long oldCount = rs.getLong(2);
					String sqlUpdate = "update " + retweetTable + " set retweetCount = '" + 
					(oldCount + count) + "' where textID = '" + id + "'";
					
					
					System.out.println(sqlUpdate);
					st.executeUpdate(sqlUpdate);
				}else{//需要插入新微博
					String sqlInsert = "insert into " + retweetTable + "(textID, retweetCount) values( " +
					id + ", " + count + ") ";
					st.execute(sqlInsert);
				}
			}
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}

	
	
	public void updateRetweetTableClear(HashMap<Long, Long> counter){
		checkConnection();
		try{
			Statement st = con.createStatement();
			Set<Long> idSet = counter.keySet();
			for(Long id : idSet){
				Long count = counter.get(id);
				String sqlQuery = "select * from " + retweetTable + " where textID = " + id;
				
				ResultSet rs = st.executeQuery(sqlQuery);
				if(rs.next()){//已经有该微博
					Long oldCount = rs.getLong(2);
					String sqlUpdate = "update " + retweetTable + " set retweetCount = '" + 
					count + "' where textID = '" + id + "'";
					
					
					System.out.println(sqlUpdate);
					st.executeUpdate(sqlUpdate);
				}else{//需要插入新微博
					String sqlInsert = "insert into " + retweetTable + "(textID, retweetCount) values( " +
					id + ", " + count + ") ";
					st.execute(sqlInsert);
				}
			}
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			closeConnection();
		}
	}
	
	
	public void refreshRetweetTableClear(HashMap<Long, Long> counter, HashMap<Long, java.sql.Date> createdAt
			, String tableName){
		checkConnection();
		try{
			Statement st = con.createStatement();
			PreparedStatement ps = con.prepareStatement("insert into " + retweetTable
					 + "(textID, retweetCount, comeFrom, date) " + "values(?, ?, ?, ?)" );
			Set<Long> idSet = counter.keySet();
			for(Long id : idSet){
				Long count = counter.get(id);
				String sqlQuery = "select * from " + retweetTable + " where textID = " + id;
				
				ResultSet rs = st.executeQuery(sqlQuery);
				if(rs.next()){//已经有该微博
					String sqlUpdate = "update " + retweetTable + " set retweetCount = '" + 
					count + "' where textID = '" + id + "'";
					
					
//					System.out.println(sqlUpdate);
					st.executeUpdate(sqlUpdate);
				}else{//需要插入新微博
//					String sqlInsert = "insert into " + retweetTable + "(textID, retweetCount ) values( " +
//					id + ", " + count + ") ";
					ps.setLong(1, id);
					ps.setLong(2, count);
					ps.setString(3, tableName);
					ps.setDate(4, createdAt.get(id));
					ps.execute();
//					st.execute(sqlInsert);
				}
			}
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			closeConnection();
		}
	}
	
	public static void main(String[] args){
		SinaWeiboHelper helper = new SinaWeiboHelper();
		
//		helper.insertKeyWords("李阳");
		
//		List<String> strList = new ArrayList<String> ();
//		Calendar cd = Calendar.getInstance();
//		cd.set(Calendar.MONTH, Calendar.MARCH);
//		strList = helper.getKeywordsAfter(cd);
//		for(String str : strList){
//			System.out.println(str);
//		}
		
//		helper.createTrendTable("李阳");
//		helper.getExistTableName();
		helper.createTrendTable("lishuangjiang");
	}
}





/**
 * 			String sqlStr = "create table " + tableName +"( " + "userID bigint NOT NULL, " +
					"textID BIGINT NOT NULL, "+
					"created_at DATE NULL, " +
					"text VARCHAR(400) NULL, " +
					"source VARCHAR(400) NULL, " +
					"truncated VARCHAR(400) NULL, " +
					"in_reply_to_status_id BIGINT, " +
					"in_reply_to_user_id BIGINT, " +
					"in_reply_to_screen_name VARCHAR(400), " +
					"thumbnail_pic VARCHAR(400), " + 
					"bmiddle_pic VARCHAR(400), " +
					"original_pic VARCHAR(400), " + 
					"geo VARCHAR(400), " + 
					"retweeted_status BIGINT";
*/