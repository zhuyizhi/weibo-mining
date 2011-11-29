package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class CountADJ {
	static String path = 
		"E:\\programs\\weibo4j-20110622\\weibo4j\\experiment\\sandBox\\";
	public static boolean isADJ(String str){
		if(str.indexOf("/") != -1)
			if(str.substring(str.indexOf("/"), str.length()).equals("/a"))
				return true;
			
		return false;
	}
	
	public static boolean isNoun(String str){
		if(str.indexOf("/") != -1)
			if(str.substring(str.indexOf("/"), str.length()).equals("/n") ||
					str.substring(str.indexOf("/"), str.length()).equals("/nd")	||
					str.substring(str.indexOf("/"), str.length()).equals("/nh") ||
					str.substring(str.indexOf("/"), str.length()).equals("/ns") ||
					str.substring(str.indexOf("/"), str.length()).equals("/nt") ||
					str.substring(str.indexOf("/"), str.length()).equals("/nl") ||
					str.substring(str.indexOf("/"), str.length()).equals("/nz") ||
					str.substring(str.indexOf("/"), str.length()).equals("/ni"))
				return true;

		
		return false;
	}
	
	public static void countADJs(String sourceFile, String resultFile){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + sourceFile), "UTF-8"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				String[] strs = tempStr.split(" ");
				for(String str:strs){
					if(isADJ(str))
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
			
			PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>();
			for(String key:strMap.keySet()){
				LabelScorePair lsp = new LabelScorePair(key, strMap.get(key).toString());
				pq.add(lsp);
			}
			
			
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + resultFile), "UTF-8"));
//			for(String key:strMap.keySet()){
//				pw.println(key + " : " + strMap.get(key));
//			}
			LabelScorePair l;
			while((l = pq.poll()) != null){
				pw.println(l.label + "  :  " + l.score);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void countNouns(String sourceFile, String resultFile){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + sourceFile), "UTF-8"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				String[] strs = tempStr.split(" ");
				for(String str:strs){
					if(isNoun(str))
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
			
			PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>();
			for(String key:strMap.keySet()){
				LabelScorePair lsp = new LabelScorePair(key, strMap.get(key).toString());
				pq.add(lsp);
			}
			
			
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + resultFile), "UTF-8"));
//			for(String key:strMap.keySet()){
//				pw.println(key + " : " + strMap.get(key));
//			}
			LabelScorePair l;
			while((l = pq.poll()) != null){
				pw.println(l.label + ":" + l.score);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void removeTweetsWithNoTopNouns(String sourceFile, String nounListFile, String resFile){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + sourceFile), "UTF-8"));
			BufferedReader brNounList = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + nounListFile), "UTF-8"));
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + resFile), "UTF-8"));
			ArrayList<String> nounList = new ArrayList<String>();
			
			String tempStr;
			while((tempStr = brNounList.readLine()) != null){
				nounList.add(tempStr);
			}
			
			while((tempStr = brSource.readLine()) != null){
				String[] strs = tempStr.split(" ");
				boolean flag = false;
				for(String str:strs){
					if(nounList.contains(str))
					{
						flag = true;
						break;
					}	
				}
				
				if(flag)
					pw.println(tempStr);
			}
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
//		countADJs("invisableLDAFile.txt", "adjCount.txt");
//		System.out.println(isADJ("富/a"));
//		countNouns("TopicFile.txt", "topNouns_TopicFile.txt");
//		removeTweetsWithNoTopNouns("Nouns_TopicFile.txt", "TopicFile_TopNounLists_small.txt", "onlyTweetsWithTopNouns.txt");
//		countNouns("girl_onTopic.txt", "topNouns_girl_onTopic.txt");
		countADJs("girl_onTopic.txt", "topADJs_girl_onTopic.txt");
	}
}
