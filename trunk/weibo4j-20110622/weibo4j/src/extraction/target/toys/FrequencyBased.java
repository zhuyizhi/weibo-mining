package extraction.target.toys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import ljc.seg.HL;
import ljc.seg.SegResult;
import ljc.seg.SegWord;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import sjtu.edu.cn.weibo.weibotools.WeiboV1;

import edu.sjtu.ltlab.word.split.IRSplit;

/**
 * 用于frequency-based method 来抽取target方法的实验
 * @author ucai
 *
 */
public class FrequencyBased {
	/**
	 * 对源文件进行分词，只保留其中的名词和命名实体，写入目标文件中
	 * @param sourFile
	 * @param destFile
	 */
	public static void splitAndWrite(String sourceFile, String destFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(sourceFile)));
			PrintWriter pw = new PrintWriter(new File(destFile));
			IRSplit irSplitter=new IRSplit();
			String tempStr;
			String reg = "\\S*/n[a-z]{0,1}$";
			int counter = 0;
			HashSet<String> stopSet = WeiboV1.getStopWordSet();
			while((tempStr = br.readLine()) != null){
				tempStr = irSplitter.paragraphProcess(WeiboV1.filterAt(tempStr));
				String[] tokens = tempStr.split("  ");
				StringBuilder sb = new StringBuilder();
				for(String token:tokens){
//					使用正则表达式筛选名词类的
					if(token.indexOf("/") != -1)
						if(!stopSet.contains(token.substring(0, token.indexOf("/"))))
							if(token.matches(reg))
								sb.append(token+" ");
				}
				pw.println(sb.toString());
				System.out.println((counter++) + sb.toString());
			}
			pw.flush();
			br.close();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
		}
	}
	
	/**
	 * 仅使用对名词的计数对结果排序
	 * @param dataFile
	 * @param tableName
	 */
	public static void countNumber(String dataFile, String tableName){
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(dataFile)));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				String[] tokens = tempStr.split(" ");
				for(String token:tokens){
					if(hm.containsKey(token))
						hm.put(token, hm.get(token) + 1);
					else
						hm.put(token, 1);
				}
			}
			int size = hm.size();
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			int counter = 0;
			for(String token:hm.keySet()){
				long number = hm.get(token);
				String sql = "insert into " + tableName + "(token, count) values('" + token 
				+ "', '" + number + "');";
//				System.out.println(sql);
				st.executeUpdate(sql);
				System.out.println((++counter) + "/" + size);
//				helper.execureInsertSQL(sql);
			}
			helper.closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 类似于splitAndWrite, 但是只写含adj的微博
	 * 
	 * */
	public static void writeOnlyWithADJ(String sourceFile, String destFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(sourceFile)));
			PrintWriter pw = new PrintWriter(new File(destFile));
			IRSplit irSplitter=new IRSplit();
			String tempStr;
			String reg = "\\S*/n[a-z]{0,1}$";
			int counter = 0;
			HashSet<String> stopSet = WeiboV1.getStopWordSet();
			while((tempStr = br.readLine()) != null){
				tempStr = irSplitter.paragraphProcess(WeiboV1.filterAt(tempStr));
				if(tempStr.contains("/a"))
				{
					String[] tokens = tempStr.split("  ");
					StringBuilder sb = new StringBuilder();
					for(String token:tokens){
//						使用正则表达式筛选名词类的
						if(token.indexOf("/") != -1)
							if(!stopSet.contains(token.substring(0, token.indexOf("/"))))
								if(token.matches(reg))
									sb.append(token+" ");
					}
					pw.println(sb.toString());
				}
				System.out.println((counter++) + tempStr);
			}
			pw.flush();
			br.close();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
		}
	}
	
	public static void getBackGround(){
		try{
			String[] tables = new String[]{"李双江之子打人","李阳","江西湖北交界地震",
					"老人跌倒","邵阳沉船"};
			IRSplit ir = new IRSplit();
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			HashSet<String> stopSet = WeiboV1.getStopWordSet();
			for(String table:tables){
				String sql = "select text from " + table;
				ResultSet rs = st.executeQuery(sql);
				System.out.println(sql);
				int counter = 0;
				while(rs.next()){
					String tempStr = rs.getString(1);
					tempStr = WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(tempStr)));
					System.out.println(++counter);
					tempStr = ir.paragraphProcess(tempStr);
					String[] tokens = tempStr.split("  ");
					for(String token:tokens){
						if(token.indexOf("/") != -1)
							if(!stopSet.contains(token.substring(0, token.indexOf("/")))){
								if(hm.containsKey(token))
									hm.put(token, hm.get(token) + 1);
								else
									hm.put(token, 1);
						}
					}
				}
			}
//			结束之后往background里添加
			for(String token:hm.keySet()){
				String sql = "insert into background(token, count) values(?,?)";
				PreparedStatement ps = helper.con.prepareStatement(sql);
				ps.setString(1, token);
				ps.setInt(2, hm.get(token));
				System.out.println("token = " + token);
				ps.execute();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void getBackGroundFromSQLServer(){
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
		     con = java.sql.DriverManager.getConnection(conURL,userName,password);
		     Statement st = con.createStatement();
		     HashMap<String, Integer> hm = new HashMap<String, Integer>();
		     HashSet<String> stopSet = WeiboV1.getStopWordSet();		     
		     ResultSet rs = st.executeQuery("select text from text");
			 IRSplit ir = new IRSplit();
			 SinaWeiboHelper helper = new SinaWeiboHelper();
			 helper.checkConnection();
			 int counter = 0;
		     while(rs.next()){
					String tempStr = rs.getString(1);
					tempStr = WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(tempStr)));
					System.out.println(++counter);
					tempStr = ir.paragraphProcess(tempStr);
					String[] tokens = tempStr.split("  ");
					for(String token:tokens){
						if(token.indexOf("/") != -1)
							if(!stopSet.contains(token.substring(0, token.indexOf("/")))){
								if(hm.containsKey(token))
									hm.put(token, hm.get(token) + 1);
								else
									hm.put(token, 1);
						}
					}
		     }


			for(String token:hm.keySet()){
				String sql = "insert into background_copy(token, count) values(?,?)";
				PreparedStatement ps = helper.con.prepareStatement(sql);
				ps.setString(1, token);
				ps.setInt(2, hm.get(token));
				System.out.println("token = " + token);
				ps.execute();
				ps.close();
			}
		     
//		     System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getBackGroundFromSQLServerV2(){
		try{
			 java.sql.Connection  con = null;
		     String userName = "sa";
		     String password = "69015870";
		     String conURL="jdbc:sqlserver://127.0.0.1:1433;DatabaseName=newSinaWeibo;";
		     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		     con = java.sql.DriverManager.getConnection(conURL,userName,password);
		     Statement st = con.createStatement();
		     HashMap<String, Integer> hm = new HashMap<String, Integer>();
		     HashSet<String> stopSet = WeiboV1.getStopWordSet();		     
		     ResultSet rs = st.executeQuery("select text from text");
			 IRSplit ir = new IRSplit();
			 SinaWeiboHelper helper = new SinaWeiboHelper();
			 helper.checkConnection();
			 int counter = 0;
		     while(rs.next()){
					String tempStr = rs.getString(1);
					tempStr = WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(tempStr)));
					System.out.println(++counter);
					tempStr = ir.paragraphProcess(tempStr);
					String[] tokens = tempStr.split("  ");
					for(String token:tokens){
						if(token.indexOf("/") != -1)
							if(!stopSet.contains(token.substring(0, token.indexOf("/")))){
								Statement s = helper.con.createStatement();
								PreparedStatement qps = helper.con.prepareStatement(
										"select * from background_copy where token = ?");
								qps.setString(1, token);
								ResultSet r = qps.executeQuery();
//								System.out.println("select * from background_copy where token = '" +token + "'");
//								ResultSet r = s.executeQuery("select * from background_copy where token = '" +token + "'" );
								PreparedStatement ps = helper.con.prepareStatement("insert into background_copy(token," +
										"count) values(?,?)");
								ps.setString(1, token);
								if(r.next())
								{
									int count = r.getInt("count");
									ps.setInt(2, count+1);
								}else{
									ps.setInt(2, 1);
								}
								ps.execute();
								ps.close();
								r.close();
								s.close();
								qps.close();
						}
					}
		     }


//			for(String token:hm.keySet()){
//				String sql = "insert into background_copy(token, count) values(?,?)";
//				PreparedStatement ps = helper.con.prepareStatement(sql);
//				ps.setString(1, token);
//				ps.setInt(2, hm.get(token));
//				System.out.println("token = " + token);
//				ps.execute();
//				ps.close();
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * for every microblog, choose the most frequent noun as the target
	 * @param sourceFile
	 * @param tableName
	 */
	public static void findTarget(String sourceFile, String tableName){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from wordfrequency");
			HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
			while(rs.next()){
				wordMap.put(rs.getString("token"), rs.getInt("count"));
				
			}
			BufferedReader br = new BufferedReader(new FileReader(new File(sourceFile)));
			String tempStr;
			PreparedStatement ps = helper.con.prepareStatement("insert into " + tableName + "(split, target) values(?,?)");
			int counter = 0;
			while((tempStr = br.readLine()) != null){
				String[] tokens = tempStr.split(" ");
				if(tokens.length > 0){
					int maxCount = 0;
					String target = "";
					for(String token:tokens){
						if(wordMap.get(token) > maxCount)
						{
							maxCount = wordMap.get(token);
							target = token;
						}
					}
					ps.setString(1, tempStr);
					ps.setString(2, target);
					ps.execute();
				}
				System.out.println(++counter);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * get four kinds of special nouns that has a frequency larger than 100
	 */
	public static void getSpecialNouns(){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from wordfrequency order by count desc");
			List<String> l = new ArrayList<String>();
			while(rs.next()){
				if(rs.getInt("count") < 100)
					break;
				String token = rs.getString("token");
				String reg = "\\S+/n[z,i,s,h]*";
				if(token.matches(reg) ){
					l.add(token);
				}
			}
			for(String t:l){
				System.out.println(t);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testIJC(){
		HL.init();
		try{
//			String text =new String("遭塔利班绑架的德国人质现身录像求助".getBytes(), "gb2312");
			String text = "遭塔利班绑架的德国人质现身录像求助";
			SegResult result= HL.splitText(text, HL.OPT_KEYWORD);
			
			List<SegWord> keyword = result.getKeywords();
			for(SegWord seg : keyword)
				System.out.println(seg.word + seg.weight);
			PrintWriter pw = new PrintWriter(new File("test.txt"));
			for(SegWord seg : keyword)
				pw.println(seg.word + seg.weight);
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void testConnectSQLServer(){
		try{
		     String userName = "sa";
		     String password = "69015870";
		     String conURL="jdbc:sqlserver://192.168.1.105:1433;DatabaseName=wordMap;";
		     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		     java.sql.Connection con = java.sql.DriverManager.getConnection(conURL,userName,password);
		     if(con != null) System.out.println("connected successfully");
		     
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * get the top people names, and write into the file specified by file path
	 * @param threshold
	 * @param filePath
	 */
	public static void getTopNames(int threshold, String filePath, String freqTable){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from " + freqTable + " order by count desc");
			List<String> l = new ArrayList<String>();
			while(rs.next()){
				if(rs.getInt("count") < threshold)
					break;
				String token = rs.getString("token");
				String reg = "\\S+/nh";
				if(token.matches(reg) ){
					l.add(token);
				}
			}
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(filePath), "UTF-8"));
			for(Object token:l.toArray()){
				pw.println(token);
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
	
	/**
	 * get named entities other than people names. Use the formula
	 * 
	 * freq/(log(bfreq+1) + 1) 
	 * 
	 * where freq is the frequency in the current corpus and bfreq is the frequency in the background
	 * corpus. This is a formula that similar to TF-IDF。
	 * 
	 * @param thresholdInt cut off the frequency in current corpus 
	 * @param thresholdDouble the threshold of the formula
	 * @param filePath
	 */
	public static void getOtherNEs(int thresholdInt,double thresholdDouble, String filePath){

		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from wordfrequency where count > '" + thresholdInt + "' order by count desc");
			
		     String userName = "sa";
		     String password = "69015870";
		     String conURL="jdbc:sqlserver://192.168.1.105:1433;DatabaseName=wordMap;";
		     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		     java.sql.Connection con = java.sql.DriverManager.getConnection(conURL,userName,password);
		     if(con != null) System.out.println("connected successfully");
			 PreparedStatement ps = 
				 con.prepareStatement("select df from wordListLarge where word = ?");
			 
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(filePath), "UTF-8"));
			List<String> l = new ArrayList<String>();
			TreeSet<TokenPair> tr = new TreeSet<TokenPair>();
			double loge2 = Math.log(1.2);
			while(rs.next()){
				String token = rs.getString("token");
				String reg = "\\S+/n[z,i,s]+";
				if(token.matches(reg) ){
					String word;
					int freq = rs.getInt("count");
					if(token.indexOf("/") != -1)
						word = token.substring(0, token.indexOf("/"));
					else
						word = token;
					ps.setString(1, word);
					ResultSet crs = ps.executeQuery();	
					int df = 0;
					if(crs.next()){
						df = crs.getInt("df");
					}
					double logs = ((Math.log(df + 1))/loge2);
//					double logs = df + 1;
					Double score = freq/( logs  + 0.4);
					if(score > thresholdDouble)
					{
						System.out.println(word + " " +" df=" + df + "  " + logs);
						TokenPair tp = new TokenPair();
						tp.score = score;
						tp.token = token;
						tr.add(tp);
						
//						pw.println(token + " : " + score);
					}
//						l.add(token);
						
				}
			}

//			for(Object token:l.toArray()){
//				pw.println(token);
//			}
			TokenPair tp;
			while( (tp = tr.pollFirst()) != null ){
				pw.println(tp.token + " : " + tp.score);
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

	
	
	
	public static void main(String[]args){
//		splitAndWrite("son.txt" , "splitNounsSecond.txt");
//		countNumber("splitNounsSecond.txt");
//		writeOnlyWithADJ("son.txt", "onlyWithADJ.txt");
//		findTarget("onlyWithADJ.txt", "target");
//		getSpecialNouns();
//		getBackGround();
//		getBackGroundFromSQLServer();
//		splitAndWrite("laoren.txt", "laoren_splitNouns.txt");
//		countNumber("laoren_splitNouns.txt", "laoren_wordfrequency");
//		testIJC();
//		testConnectSQLServer();
//		getTopNames(100, "10.23/laoren_people100.txt", "laoren_wordfrequency");
		getOtherNEs(100, 0, "10.23/lishuangjiang_otherNEs100_0e1.5.txt");
//		double s = Math.log(1775);
//		double s1 = s +1;
//		double s2 = 5104 / s1; 
//		System.out.println(s + "  " + s2 );
	}
}

