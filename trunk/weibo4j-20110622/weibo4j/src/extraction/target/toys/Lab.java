package extraction.target.toys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Lab {
	public static void stopwords(){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("stop_words_ch.txt"), "UTF-8"));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("stopwords.txt"), "UTF-8"));
			String tempStr;
			HashSet<String> stopSet = new HashSet<String>();
			while((tempStr = br.readLine()) != null){
				stopSet.add(tempStr);
			}
			System.out.println(stopSet.size());
			
			while((tempStr = br2.readLine()) != null){
				int size = stopSet.size();
				stopSet.add(tempStr);
				if( stopSet.size() > size){
					System.out.println(tempStr);
				}
			}
			System.out.println(stopSet.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void punc(String w){
		if(w.endsWith("/w") || w.endsWith("/wp") || w.endsWith("/ws"))
			System.out.println(true);
		else
			System.out.println(false);
	}
	
	public static void code(){
		String str1 = "責/n";
		String str2 = "個/n";
		System.out.println(str1);
		System.out.println(str2);
	}
	
	public static void countWords(String file) throws Exception {
		BufferedReader br = new BufferedReader(new 
				InputStreamReader(new FileInputStream(file), "GBK"));
		HashSet<String> set = new HashSet<String>();
		
		String temp;
		while((temp = br.readLine()) != null){
			temp = temp.substring(temp.indexOf(",") + 1, temp.length());
			String[] tokens = temp.split(" ");
			for(String token : tokens)
				set.add(token);
		}
		System.out.println(set.size());
	}
	
	public static void main(String[]args) throws Exception{
//		stopwords();
//		String[] strs = new String[]{"】/wp", "~/ws", "★☆/ws", "s/w", "看看/v"};
//		for(String w:strs)
//			punc(w);
//		code();
		String path = "F:/programs/from lab/weibo4j-20110622/weibo4j/experiment/" +
				"real_one/hotAspect/many-experiment/";
		String file = "roll_aspect_120_10llda.csv";
		countWords(path + file);
	}
}
