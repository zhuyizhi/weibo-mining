package sjtu.edu.cn.trend.collection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import experiment.one.preprocess.RuleBase;

import sjtu.edu.cn.weibo.weibotools.WeiboV1;
import weibo4j.User;
import weibo4j.Weibo;


import ICTCLAS.I3S.AC.ICTCLAS50;

public class WriteParsedResultIntoDatabase {
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	public HashSet<String> stopWordSet = new HashSet<String>();
	public Weibo weibo;
	static{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
	public WriteParsedResultIntoDatabase() throws Exception{
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
		BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
		String word;
		while((word = br.readLine()) != null){
			stopWordSet.add(word);
		}
	}
	
	public ICTCLAS50 ic50;
	public void init() {
		try{
			ic50 = new ICTCLAS50();
			String argu = ".";
			//初始化
			if (ic50.ICTCLAS_Init(argu.getBytes("UTF-8")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			ic50.ICTCLAS_SetPOSmap(2);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将分词结果写入数据库
	 * @param tableName
	 */
	public void updateTable(String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			HashMap<Long, String> map = new HashMap<Long, String>();
			
			int counter  = 0;
			String sql = "select textID,text from " + tableName ;
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String text = rs.getString("text");
				text = WeiboV1.filterAt(WeiboV1.filterURL(WeiboV1.filterHashTag(text)));
				byte[] bts = ic50.ICTCLAS_ParagraphProcess(text.getBytes("UTF-8"), 0, 1);
				String processText = new String(bts, 0, bts.length, "UTF-8");
				map.put(rs.getLong("textID"), processText);
				System.out.println("分词  " + (++counter));
			}
			st.close();
			
			counter = 0;
			PreparedStatement ps = helper.con.prepareStatement(
					"update " + tableName + " set parsedResult = ? where textID = ?");
			for(Long key:map.keySet()){
				ps.setString(1, map.get(key));
				ps.setLong(2, key);
				ps.addBatch();
				if((counter % 500) == 0)
					ps.executeBatch();
				System.out.println("插入  " + (++counter));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将符合手动拟定的几个模板的微博排除
	 * @param tableName
	 */
	public void updateTable_2(String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			HashMap<Long, Boolean> map = new HashMap<Long, Boolean>();
			HashSet<Long> set = new HashSet<Long>();
			
			int counter  = 0;
			String sql = "select textID,text from " + tableName ;
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int total = 0;
			while(rs.next()){
				String text = rs.getString("text");
				if(RuleBase.matchAnyRule(text))
				{
					set.add(rs.getLong("textID"));
					total++;
				}
			}
			st.close();
			
			PreparedStatement ps = helper.con.prepareStatement(
					"update " + tableName + " set isRuled = 1 where textID = ?");
			for(Long key:set){
				ps.setLong(1, key);
				ps.addBatch();
				if((counter % 500) == 0)
					ps.executeBatch();
				System.out.println("插入  " + (++counter));
			}
			System.out.println(total);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将分词结果组装成去掉停用词后的结果再加入数据库
	 * @param tableName
	 */
	public void updateTable_3(String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			HashMap<Long, String> map = new HashMap<Long, String>();
			
			int counter  = 0;
			String sql = "select textID,parsedResult from " + tableName ;
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String text = rs.getString("parsedResult");
				if(text != null){
					String[] tokens = text.split(" ");
					StringBuilder sb = new StringBuilder();
					for(String token:tokens){
						if(token.indexOf("/") != -1)
							if(!stopWordSet.contains(token.substring(0, token.indexOf("/"))))
									sb.append(token+" ");
					}
					
					map.put(rs.getLong("textID"), sb.toString());
					System.out.println("得到  " + (++counter));
				}
			}
			st.close();
			
			counter = 0;
			PreparedStatement ps = helper.con.prepareStatement(
					"update " + tableName + " set parsedResultNoStopWord = ? where textID = ?");
			for(Long key:map.keySet()){
				ps.setString(1, map.get(key));
				ps.setLong(2, key);
				ps.addBatch();
				if((counter % 500) == 0)
					ps.executeBatch();
				System.out.println("插入  " + (++counter));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 去掉多于一个#的数据
	 * @param tableName
	 */
	public void updateTable_4(String tableName){//去除多于一个#的
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			HashSet<Long> set = new HashSet<Long>();
			
			int counter  = 0;
			String sql = "select textID,text from " + tableName ;
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int total = 0;
			while(rs.next()){
				String text = rs.getString("text");
				if(WeiboV1.countHashTag(text) > 1)
				{
					set.add(rs.getLong("textID"));
					total++;
				}
			}
			st.close();
			
			PreparedStatement ps = helper.con.prepareStatement(
					"update " + tableName + " set isRuled = 1 where textID = ?");
			for(Long key:set){
				ps.setLong(1, key);
				ps.addBatch();
				if((counter % 500) == 0)
					ps.executeBatch();
				System.out.println("插入  " + (++counter));
			}
			System.out.println(total);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 更新每条微博的用户信息的数据
	 * 太慢了，目前没用
	 * @param tableName
	 */
	public  void updateUserProfile(String tableName){
		try{
			HashSet<Long> userSet = new HashSet<Long>();
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			String sql = "select distinct(userID) from " + tableName ;
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int total = 0;
			while(rs.next()){
				Long uID = rs.getLong("userID");
				userSet.add(uID);
				total++;
			}
			st.close();
			System.out.println("查询完毕，共 " + total);
			
			int counter = 0;
//			Random r = new Random();
//			PreparedStatement ps = helper.con.prepareStatement(
//					"insert into user_profile " +
//					"(userID, users_follower, users_follow, users_tweets, isVerified) values(?, ?, ?, ?, ?)");
			
			Statement st2 = helper.con.createStatement();
			for(Long id:userSet){
				try{
					User u = weibo.showUser(id.toString());
					String sql2 = "insert into user_profile(userID, users_follower, " +
							"users_follow, users_tweets, isVerified) values(" + id + ", " + u.getFollowersCount()
							 + ", " + u.getFriendsCount() + ", " + u.getStatusesCount();
					if(u.isVerified())
						sql2 += ", 1)";
					else
						sql2 += ", 0)";
					System.out.println(sql2);
					st2.executeUpdate(sql2);
					System.out.println("插入 " + (++counter));
					Thread.sleep(1600);
				}catch(Exception e){
					Thread.sleep(10000);
				}

				
//				ps.setLong(1, id);
//				ps.setInt(2, u.getFollowersCount());
//				ps.setInt(3, u.getFriendsCount());
//				ps.setInt(4, u.getStatusesCount());
//				if(u.isVerified())
//					ps.setInt(5, 1);
//				else
//					ps.setInt(5, 0);
				
//				ps.executeUpdate();

//				if((r.nextInt() % 7) == 0)
//				{
//					int rt = weibo.getRateLimitStatus().getRemainingHits();
//					System.out.println("rt = " + rt);
//					if( rt < 200);
//					{
//						Thread.sleep(10000);
//					}
//				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 使用LDA结果标注垃圾微博
	 * @throws Exception
	 */
	public void updateLDAGarbage()throws Exception{
		String tableName = "lishuangjiang";
		int garbageIndex = 0;
		String fileId = "E:/programs/weibo4j-20110622/weibo4j/lishuangjiang_tmt_id.txt";
		String topic_doc = "E:/tools/stanford topic modeling toolbox" +
				"/lda-lishuangjiang-for-garbage/document-topic-distributions.csv";
		double t = 0.7;
		
		BufferedReader brf=new BufferedReader
		(new InputStreamReader(new FileInputStream(fileId), "UTF-8"));
		BufferedReader brt=new BufferedReader
		(new InputStreamReader(new FileInputStream(topic_doc), "gb2312"));
		SinaWeiboHelper helper = new SinaWeiboHelper();
		helper.checkConnection();
		PreparedStatement ps = helper.con.prepareStatement("update " + tableName + " " +
				"set isGarbage = 1 where textID = ?");
		
		int counter = 0;
		String tempStr;
		while((tempStr = brt.readLine()) != null){
			long id = Long.parseLong(brf.readLine());
			String[] args = tempStr.split(",");
			if(Double.parseDouble(args[garbageIndex + 1]) > t){
				ps.setLong(1, id);
				ps.execute();
				System.out.println(++counter);
			}
			
		}
	}
	
	
	
	
	public static void main(String[] args) throws Exception{
		WriteParsedResultIntoDatabase w = new WriteParsedResultIntoDatabase();
		w.init();
////		w.updateTable("李双江之子打人", "lishuangjiang_parse");
////		w.updateTable("lishuangjiang","");
////		w.updateTable_2("lishuangjiang");
//		w.updateUserProfile("lishuangjiang");
//		SinaWeiboHelper helper = new SinaWeiboHelper();
//		helper.checkConnection();
//		String sql = "select textID, parsedResult from lishuangjiang where isRuled = 0";
//		Statement st = helper.con.createStatement();
//		ResultSet rs = st.executeQuery(sql);
//		int total = 0;
//		PrintWriter pw = new PrintWriter
//		(new OutputStreamWriter( new FileOutputStream("lishuangjiang_tmt_id.txt"), "gb2312"));
//		while(rs.next()){
//			pw.println(rs.getLong("textID"));
//		}
//		w.updateLDAGarbage();
//		w.updateTable_4("lishuangjiang");
//		w.updateTable("rolled_girl_copy");
//		w.updateTable_2("rolled_girl_copy");
//		w.updateTable_3("rolled_girl_copy");
		w.updateTable_4("rolled_girl_copy");
		
	}
}
