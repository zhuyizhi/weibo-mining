package experiment.one.hotAspect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;

import experiment.util.POSFilter;
import experiment.util.POSType;
import experiment.util.Stat;
import experiment.util.WeiboV1;

import november.second.LabelScorePair;

public class AnalyzingLDAResult {

	
	public static void analyzeTMTLDAResult(String path, String termIndexFile, String
			 topicTermDistributionFile, int topicNumber) throws Exception{
		try{
//			要从两个文件读入LDA结果，首先将termIndex文件转为utf-8编码的格式
			String newTIFile = termIndexFile + "_utf8";
			BufferedReader br = new BufferedReader(new InputStreamReader(new 
					FileInputStream(path + termIndexFile), "GBK"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new 
					FileOutputStream(path + newTIFile), "UTF-8"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				tempStr = new String(tempStr.getBytes("UTF-8"), "UTF-8");
				pw.println(tempStr);
			}
			pw.close();
			br.close();
			
//			初始化wordmap
			BufferedReader br2 = new BufferedReader(
					new InputStreamReader(new FileInputStream(path + newTIFile ), 
							"UTF-8"));
			int counter = 0;
			HashMap<String, Integer> wordIndexMap = new HashMap<String, Integer>();
			while((tempStr = br2.readLine()) != null){
				int size = wordIndexMap.size();
//				tempStr = new String(tempStr.getBytes("GBK"), "GBK");
				tempStr = new String(tempStr.getBytes("UTF-8"), "UTF-8");
				wordIndexMap.put(tempStr, counter++);
				int newSize = wordIndexMap.size();
				if(newSize == size)
					System.out.println(size + " : " + tempStr);
//				System.out.println(tempStr);
			}
			System.out.println(wordIndexMap.size());
			
//			开始初始化二维数组
			Double[][] ttMatrix = Stat.getTopicTermDsitribution
			(path + topicTermDistributionFile, topicNumber, wordIndexMap.size());
			
			
//			建立一个id-->word的map
			HashMap<Integer, String> idToWord = new HashMap<Integer, String>();
			for(String str:wordIndexMap.keySet()){
				idToWord.put(wordIndexMap.get(str), str);
			}
			
//			对于每一个话题，建立一个优先级队列来存放高排名的名词。这里只排序
//			在该topic中出现次数大于50的名词.结果存到目录下的分析文件里，输出每个话题的前10个单词
			PrintWriter pw2 = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + "analyzeLDA.file"), "gb2312"));
			double topicThreshold = 50.0;
			for(Double[] topic:ttMatrix){
				PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>(); 
				
				for(int i = 0; i < (topic.length - 1); i++){
					String token = idToWord.get(i);
					System.out.println(i + ":" + topic.length );
					if(topic[i] > topicThreshold && FrequentNoun.isAspect(token)){
						LabelScorePair lsp = 
							new LabelScorePair(idToWord.get(i), Double.toString(topic[i]));
						pq.add(lsp);
					}
				}
				
				LabelScorePair lsp;
				int count = 10;
				while((lsp = pq.poll()) != null && count-- > 0){
//					if(lsp.score < 4000)
//						break;
//					pw2.print(lsp.label + ":" + lsp.score + ", ");
					pw2.print(lsp.label + ", ");
				}
				pw2.println();
			}
			pw2.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void analyzeTMTLDAPOS(String path, String termIndexFile, String topicTermDistributionFile, 
			int topicNumber, POSType type, double threshold, int outputNumber, int wordLength, boolean withScore) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new 
					FileInputStream(path + termIndexFile), "GBK"));
			
			int counter = 0;
			String tempStr;
			HashMap<String, Integer> wordIndexMap = new HashMap<String, Integer>();
			while((tempStr = br.readLine()) != null)
				wordIndexMap.put(tempStr, counter++);
			
			Double[][] ttMatrix = Stat.getTopicTermDsitribution
			(path + topicTermDistributionFile, topicNumber, wordIndexMap.size());
			
//			建立一个id-->word的map
			HashMap<Integer, String> idToWord = new HashMap<Integer, String>();
			for(String str:wordIndexMap.keySet())
				idToWord.put(wordIndexMap.get(str), str);
//			依次处理每个话题
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(path + type.toString() + "_" + wordLength + "_analyzeLDA.file"), "GBK"));
			POSFilter filter = new POSFilter();
			LabelScorePair lsp;
			for(Double[] topic:ttMatrix){
				PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>(); 
				for(int i = 0; i < topic.length - 1; i++){
					String token = idToWord.get(i);
					if(topic[i] > threshold && POSFilter.match(token, type)){
						lsp = new LabelScorePair(idToWord.get(i), Double.toString(topic[i]));
						pq.add(lsp);
					}
				}
				
				int count = outputNumber;
				while((lsp = pq.poll()) != null && count > 0)
					if(WeiboV1.filterPOS(lsp.label).length() >= wordLength){
						if(withScore)
							pw.print(lsp.label + ":" + lsp.score + ", ");
						else
							pw.print(lsp.label + ",");
						count--;
					}
					
				pw.println();
			}
			pw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void analyzeTMTLDAPOS(String path, int topicNumber, 
			POSType type, double threshold, int outputNumber, int wordLength, boolean withScore) {
		String termIndexFile = "term-index.txt";
		String topicTermDistributionFile = "topic-term-distributions.csv";
		analyzeTMTLDAPOS(path, termIndexFile, topicTermDistributionFile, 
				topicNumber, type, threshold, outputNumber, wordLength , withScore);
	}
	
	public static void main(String[] args) throws Exception{
//		String path = "C:/Users/ucai/Desktop/llda/lda-f43bd2b8-10-949382c4" +
//				"/00950/";
//		String path = "C:/Users/ucai/Desktop/llda/llda-lishuangjiang-smallNameSet/01000/";
		String termIndexFile = "term-index.txt";
		String topicTermDistributionFile = "topic-term-distributions.csv";
		int topicNumber = 10;
//		analyzeTMTLDAResult(path, termIndexFile, 
//				 topicTermDistributionFile, topicNumber);
//		
		double threshold = 50;
		int outputNumber = 5;
//		POSType type = POSType.N;
		POSType type = POSType.SENTIMENT;
		POSType type2  = POSType.Person;
		boolean withScore = false;
		int wordLength = 1;
		String path = "C:/Users/ucai/Desktop/llda/llda-lishuangjiang_c13_allwords/01000/";
		
		analyzeTMTLDAPOS(path, topicNumber, type2, threshold, outputNumber, wordLength, 
				withScore);
	}
}
