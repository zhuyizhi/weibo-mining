package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Analysis {
	static String path = 
		"E:\\programs\\weibo4j-20110622\\weibo4j\\experiment\\sandBox\\";
	
	public static void getFile(String sourceFile, String resultFile, String word){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + sourceFile), "UTF-8"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + resultFile), "UTF-8"));
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				if(tempStr.contains(word))
					pw.println(tempStr);
			}
			
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void w(){
		String path1 = "E:/programs/weibo4j-20110622/weibo4j/experiment/november.second/backDir/";
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path1 + "TopicFile_OnlyNoun.txt"), "gb2312"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path1 + "TopicFile_OnlyNoun_noPOS.txt"), "gb2312"));
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
//				if(tempStr.contains(word))
//					pw.println(tempStr);
				pw.println(tempStr.replaceAll("/\\S+", ""));
			}
			
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void w2(){
		String path1 = "E:/programs/weibo4j-20110622/weibo4j/experiment/november.second/backDir/";
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path1 + "TopicFile.txt"), "gb2312"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path1 + "TopicFile_OnlyNoun.txt"), "gb2312"));
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
//				if(tempStr.contains(word))
//					pw.println(tempStr);
				pw.println(tempStr.replaceAll("\\S+/[^n]\\S*", ""));
			}
			
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
//		getFile("weibos.txt", "嚣张.txt", "嚣张");
		w();
	}
}
