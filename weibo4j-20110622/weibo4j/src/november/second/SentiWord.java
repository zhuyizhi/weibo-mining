package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SentiWord {
	public static String path = "experiment\\november.second\\sentiment\\";
	public ArrayList<String> sentimentList = new ArrayList<String>();
	
	/**
	 * initiation
	 * @param dicFile
	 */
	public void getDictionary(String dicFile ){
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(dicFile), "UTF-8"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				sentimentList.add(tempStr);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isSentimentWord(String token){
		if(token.contains("/"))
			if(token.substring(token.indexOf("/"), token.length()).equals("/a") ||
					token.substring(token.indexOf("/"), token.length()).equals("/v")||
					token.substring(token.indexOf("/"), token.length()).equals("/vu"))
				if(sentimentList.contains(token.substring(0, token.indexOf("/"))))
					return true;
		return false;
	}
	
	/**
	 * judge if a tweet has sentiment words
	 * @param tweet
	 * @return
	 */
	public boolean containSentiment(String tweet){
		String[] tokens = tweet.split(" ");
		for(String token:tokens){
//			if(token.contains("/"))
//				if(token.substring(token.indexOf("/"), token.length()).equals("/a") ||
//						token.substring(token.indexOf("/"), token.length()).equals("/v")||
//						token.substring(token.indexOf("/"), token.length()).equals("/vu"))
//					if(sentimentList.contains(token.substring(0, token.indexOf("/"))))
//						return true;
			if(isSentimentWord(token))
				return true;
		}
		return false;
	}
	
	/**
	 * get the tweets with at least one sentiment words, and write them into another file
	 * @param testFile
	 * @param sentiFile
	 */
	public void getSentimentTweets(String testFile, String sentiFile){
		try{
			String fileName = testFile;
			String outputFile = sentiFile;
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			
			String tempTweet;
			while((tempTweet = br.readLine()) != null){
				if(containSentiment(tempTweet)){
					pw.println(tempTweet);
				}
			}
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void generateResults(String inferenceFile, String rawLDAFile, String resultFile, int numberOfTopTopics){
		try{
			String fileName = inferenceFile;
			String ldaFile = rawLDAFile;
			String outputFile = resultFile;
			BufferedReader brInference = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "gb2312"));
			BufferedReader brLDA = new BufferedReader
			(new InputStreamReader(new FileInputStream(ldaFile), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			
			String tempStr, tempLDAStr;
			while((tempStr = brInference.readLine()) != null){
				tempLDAStr = brLDA.readLine();
				String[] tupleStr = tempStr.split("\t");
				StringBuilder sentiSB =new StringBuilder();
				for(int i = 1; i<tupleStr.length; i++){//i = 0 is the position of order number
					String[] tokens = tupleStr[i].split(" ");
					if(isSentimentWord(tokens[0]))
//						if(sentimentList.contains(tokens[0].substring(0, tokens[0].indexOf("/"))))
						{
							//then this is a sentiment word
							PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>(); 
							for(int j = 1; j < tokens.length; j++){
								pq.add(new LabelScorePair
										(tokens[j].substring(0, tokens[j].indexOf(":")), 
												tokens[j].substring(tokens[j].indexOf(":") + 1)));
							}
							//now get the sentiment words string
							StringBuilder sb = new StringBuilder(" " + tokens[0]);
							for(int n = numberOfTopTopics; n >0; n--){
								LabelScorePair lbs = pq.remove();
								sb.append(" " + lbs.label + ":" + lbs.score );
							}
							
							sentiSB.append("----------" + sb);
						}
				}
				pw.println(tempLDAStr + sentiSB.toString());
			}
			pw.flush();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		SentiWord sw = new SentiWord();
		sw.getDictionary( path + "sentiDic.txt");
//		sw.getSentimentTweets(path + "testData.txt", path + "sentiTest_2.txt");
		sw.generateResults(path + "sentiTest2.tsv", path + "sentiTest_2.txt", path + "result_smallNameSet_2.txt", 1);
		
//		System.out.println(sw.containSentiment("尊严/n 好/a "));
//		for(String str:sw.sentimentList){
//			System.out.println(str);
//		}
	}
}



