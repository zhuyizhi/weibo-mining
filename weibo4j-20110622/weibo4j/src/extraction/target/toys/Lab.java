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
	
	
	public static void main(String[]args) throws Exception{
//		stopwords();
		String[] strs = new String[]{"】/wp", "~/ws", "★☆/ws", "s/w", "看看/v"};
		for(String w:strs)
			punc(w);
	}
}
