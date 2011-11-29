package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class Visualize {
	

	
	public static void visualize(String termIndexFile, String topicTermDistributionFile, String resultFile){
		try{
			BufferedReader brTerm = new BufferedReader
			(new InputStreamReader(new FileInputStream( termIndexFile), "gb2312"));
			BufferedReader brDis = new BufferedReader
			(new InputStreamReader(new FileInputStream( topicTermDistributionFile), "gb2312"));
			HashMap<String, Integer> strMap = new HashMap<String, Integer>();
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream( resultFile), "UTF-8"));
			
			String[] resStr = null;
			String tempStr;
			while((tempStr = brDis.readLine()) != null){
				String[] dis = tempStr.split(",");
				if(resStr == null)
					resStr = new String[dis.length];
				for(int i=0; i<resStr.length; i++){
					resStr[i] = resStr[i] + ", " + dis[i];
				}
			}
			int count = 0;
			while((tempStr = brTerm.readLine()) != null){
				resStr[count] = tempStr + " : " + resStr[count];
				count++;
			}

			for(String str:resStr){
				pw.println(str);
			}
			
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String path = "E:/programs/weibo4j-20110622/weibo4j" +
				"/experiment/november.second/llda/llda-lishuangjiang-smallNameSet/01000/";
		visualize(path + "term-index.txt", path + "topic-term-distributions.csv", path + "myTopicDis.txt");
	}
}
