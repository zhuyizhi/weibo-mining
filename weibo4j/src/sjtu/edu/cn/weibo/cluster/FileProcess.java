package sjtu.edu.cn.weibo.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import sjtu.edu.cn.weibo.weibotools.WeiboV1;

public class FileProcess {
	static SinaWeiboHelper helper = new SinaWeiboHelper();
	public List<String> stopWordList = new ArrayList<String>();
	public FileProcess(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String word;
			while((word = br.readLine()) != null){
				stopWordList.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getProcessedWeibo(String str){
		return WeiboV1.filterURL(WeiboV1.filterHashTag(str));
	}
	
	public static String getProcessedWeiboWithoutRetweet(String str){
		return WeiboV1.filterRetweet(WeiboV1.filterURL(WeiboV1.filterHashTag(str)));
	}
	
	public static void writeAllWeiboToFile(String tableName, String destFile){
		try{
			helper.checkConnection();
			ResultSet rs = helper.getAllWeibo(tableName);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter( new FileOutputStream(destFile), "UTF-8"));
			while(rs.next()){
				pw.println(getProcessedWeibo(rs.getString("text")));
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeRandomWeiboToFile(String tableName, String destFile, int number){
		try{
			Random r = new Random();
			int counter = number;
			helper.checkConnection();
			ResultSet rs = helper.getAllWeibo(tableName);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter( new FileOutputStream(destFile), "UTF-8"));
			while(rs.next()){
				if(r.nextInt() % 7 == 0)
				{
					pw.println(getProcessedWeibo(rs.getString("text")));
					if((--counter) <= 0)
						break;
				}
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeRandomWeiboToFile_SamplerVersion(String tableName, String destFile, int number){
		try{
			helper.checkConnection();
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery("select * from " + tableName + " order by rand() limit " + number); 
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter( new FileOutputStream(destFile), "UTF-8"));
			while(rs.next()){
				pw.println(rs.getString("text"));
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Integer> getRandList(int begin, int end, int randSize){
		ArrayList<Integer> randList = new ArrayList<Integer>();
		Random r = new Random();
		while(randList.size() < randSize){
			Integer integer = begin + ((int)(r.nextDouble() * (end - begin)));
			if(!randList.contains(integer)){
				randList.add(integer);
			}
		}
		
		return randList;
	}
	
	public static void writerRandomWeiboToFile_FromFile(String sourceFile, String destFile, int number){
		try{
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter( new FileOutputStream(destFile), "UTF-8"));
			ArrayList<String> arrStr = new ArrayList<String>();
			
			String tempStr;
			while((tempStr = br.readLine()) != null){
				arrStr.add(tempStr);
			}
			int length = arrStr.size();
			ArrayList<Integer> randList = getRandList(0, length, number);
			Object[] strArr = arrStr.toArray();
			for(int i:randList){
				pw.println(strArr[i]);
			}
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeAllWeiboToFileWithoutRetweet(String tableName, String destFile){
		try{
			helper.checkConnection();
			ResultSet rs = helper.getAllWeibo(tableName);
			PrintWriter pw = new PrintWriter(new FileWriter(destFile));
			while(rs.next()){
				pw.println(getProcessedWeiboWithoutRetweet(rs.getString("text")));
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeAllWeiboToFileByDate (String tableName, String destFile, String date){
		try{
			helper.checkConnection();
//			ResultSet rs = helper.getAllWeibo(tableName);
			Statement st = helper.con.createStatement();
			String sql = "select * from " + tableName + " where created_at = '" + date + "' ";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			PrintWriter pw = new PrintWriter(new FileWriter(destFile));
			while(rs.next()){
				pw.println(getProcessedWeibo(rs.getString("text")));
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeAllWeiboToFileByDateWithoutRetweet (String tableName, String destFile, String date){
		try{
			helper.checkConnection();
//			ResultSet rs = helper.getAllWeibo(tableName);
			Statement st = helper.con.createStatement();
			String sql = "select * from " + tableName + " where created_at = '" + date + "' ";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			PrintWriter pw = new PrintWriter(new FileWriter(destFile));
			while(rs.next()){
				pw.println(getProcessedWeiboWithoutRetweet(rs.getString("text")));
			}
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[]args){
//		writeAllWeiboToFile("射洪黑熊失踪", "bear.txt");
//		writeAllWeiboToFile("李阳","liyang.txt");
//		writeAllWeiboToFileWithoutRetweet("李阳","liyang_noRetweet.txt");
//		writeAllWeiboToFileByDate("李双江之子打人", "son_10.txt", "2011-10-10");
//		writeAllWeiboToFileByDate("李阳", "liyang_07.txt", "2011-10-07");
//		writeAllWeiboToFile("老人跌倒","laoren.txt");
//		String tableName = args[0];
//		Integer topicNumber = Integer.parseInt(args[1]), iterNumber = Integer.parseInt(args[2]);
//		writeAllWeiboToFile(tableName, "weibo.txt");
//		writeRandomWeiboToFile("老人跌倒", "laoren1\\test2.txt", 50);
//		writeRandomWeiboToFile("早产儿被当死婴丢厕所", "laoren1\\zao.txt", 50);
//		writeAllWeiboToFile("早产儿被当死婴丢厕所", "zao.txt");
//		writeRandomWeiboToFile_SamplerVersion("李双江之子打人", "lishuangjiang_sample100.txt", 100);
//		String source = 
//			"E:/programs/weibo4j-20110622/weibo4j/experiment/11.2/WithNoRetweet/weibos.txt";
		String path = "E:/programs/weibo4j-20110622/weibo4j/experiment/sandBox/";
//		String res = "sample100——2.txt";
		writerRandomWeiboToFile_FromFile(path + "girl_noR.txt", path + "girl_noR_Sample1.txt", 100);
	
//		writeAllWeiboToFileWithoutRetweet("rolled_girl_copy", "girl_noR.txt");
	}
}
