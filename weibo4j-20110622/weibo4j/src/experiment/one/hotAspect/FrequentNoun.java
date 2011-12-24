package experiment.one.hotAspect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import november.second.LabelScorePair;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;

/**
 * 返回文集中出现的最频繁的一组名词
 * @author ucai
 *
 */
public class FrequentNoun {
	public static String path = 
		"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/hotAspect/";
	
	public HashSet<String> stopWordSet = new HashSet<String>();
	public FrequentNoun(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String word;
			while((word = br.readLine()) != null){
				stopWordSet.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 根据北大的词性标准，与实验室的那个不一样。将名动词也作为对象之一
	 * @param token
	 * @return
	 */
	public static boolean isAspect(String token){
		if(token.indexOf("/") != -1)
			if(token.substring(token.indexOf("/"), token.length()).equals("/n") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nr")	||
					token.substring(token.indexOf("/"), token.length()).equals("/ns") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nt") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nz") ||
					token.substring(token.indexOf("/"), token.length()).equals("/vn"))
				return true;
		return false;
	}
	
	
	public static boolean isName(String token){
		if(token.indexOf("/") != -1)
			if(	token.substring(token.indexOf("/"), token.length()).equals("/nr"))
				return true;
		return false;
	}
	
	public void getMostFrequentNouns_FileVersion(String tableName, String destFile){
		PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>();
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			String sql = "select parsedResultNoStopWord from " + tableName +
					" isGarbage = 0 and isRuled = 0";
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			
			//从数据库收集计数信息
			while(rs.next()){
				String text = rs.getString("parsedResultNoStopWord");
				if(text != null){
					String[] strs = text.split(" ");
					for(String str:strs){
						if(isAspect(str))
						{
							Integer c;
							if(strMap.containsKey(str))
							{
								c = strMap.get(str) + 1;
								strMap.put(str, c);
							}else
								strMap.put(str, 1);
						}
					}
				}
			}
			
			//得到排序队列
			for(String key:strMap.keySet()){
				LabelScorePair lsp = new LabelScorePair(key, strMap.get(key).toString());
				pq.add(lsp);
			}
			
			//写入文件
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + destFile), "UTF-8"));
			LabelScorePair l;
			while((l = pq.poll()) != null){
				pw.println(l.label + ":" + l.score);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public PriorityQueue<LabelScorePair> getMostFrequentNouns_MemVersion(String tableName){
		PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>();
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			String sql = "select parsedResultNoStopWord from " + tableName +
			" where isGarbage = 0 and isRuled = 0";
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			
			int counter = 0;
			//从数据库收集计数信息
			while(rs.next()){
				String text = rs.getString("parsedResultNoStopWord");
				if(text != null){
					String[] strs = text.split(" ");
					for(String str:strs){
						if(isAspect(str))
						{
							Integer c;
							if(strMap.containsKey(str))
							{
								c = strMap.get(str) + 1;
								strMap.put(str, c);
							}else
								strMap.put(str, 1);
						}
					}
				}
				System.out.println(counter++);
			}
			System.out.println(strMap.size() + " nouns get");
			//得到排序队列
			for(String key:strMap.keySet()){
				LabelScorePair lsp = new LabelScorePair(key, strMap.get(key).toString());
				pq.add(lsp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return pq;
	}
	
	public static void main(String[] args){
		FrequentNoun fn = new FrequentNoun();
		fn.getMostFrequentNouns_MemVersion("lishuangjiang");
	}
}
