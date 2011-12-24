package experiment.one.hotAspect;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import november.second.LabelScorePair;

import cn.edu.sjtu.nlp.hownet.Hownet;
import cn.edu.sjtu.nlp.hownet.WordSimilarity;

import com.aliasi.spell.EditDistance;
import com.aliasi.stats.Statistics;
import com.aliasi.util.Distance;

import experiment.one.lucene.SearchTable;
import experiment.util.Stat;

public class Similarity implements Distance{
	public HashMap<String, Integer> wordIndexMap;
	public String path = "E:/programs/weibo4j-20110622/weibo4j" +
	"/experiment/real_one/hotAspect/lda-lishuangjiang-t10-new/01500/";
	public String ttFile = "topic-term-distributions.csv";
	public String termIndexFile = "term-index.txt";
	public Double[][] ttMatrix;
	public int topicNumber = 10;
//	public static double smoothParameter = 0.1;
	public double topicParameter = 0.5;
	public double semanticParameter = 0.4;
	public double stringParameter = 0.1;
	public HashMap hownet ;
	public WordSimilarity wsi;

	
	public Similarity(String path, int topicNumber){
		this("term-index.txt", "topic-term-distributions.csv", path, topicNumber);
	}
	
	public Similarity(String termIndexFile, String ttFile, String path, int topicNumber){
		this.ttFile = ttFile;
		this.termIndexFile = termIndexFile;
		this.path = path;
		this.topicNumber = topicNumber;
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path + termIndexFile), 
							"GBK"));
			String tempStr;
			int counter = 0;
			this.wordIndexMap = new HashMap<String, Integer>();
			while((tempStr = br.readLine()) != null){
				int size = wordIndexMap.size();
				this.wordIndexMap.put(tempStr, counter++);
				int newSize = wordIndexMap.size();
				if(newSize == size)
					System.out.println(size + " : " + tempStr);
//				System.out.println(tempStr);
			}
//			hashMap的大小小于实际的token个数。观察发现有超过size的value，即中间有些string值因为散列重合掉了
//			发现如果使用gb2312读入的时候就会产生这种情况，所以只好将原文本存为utf-8的重新读取
			ttMatrix = Stat.getTopicTermDsitribution(path + ttFile, 
					topicNumber, this.wordIndexMap.size());
			
			hownet = Hownet.getHownet();
			wsi = new WordSimilarity(hownet);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Similarity(){
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path + termIndexFile), 
							"GBK"));
			String tempStr;
			int counter = 0;
			this.wordIndexMap = new HashMap<String, Integer>();
			while((tempStr = br.readLine()) != null){
				this.wordIndexMap.put(tempStr, counter++);
			}

			ttMatrix = Stat.getTopicTermDsitribution(path + ttFile, 
					topicNumber, this.wordIndexMap.size());
			
			hownet = Hownet.getHownet();
			wsi = new WordSimilarity(hownet);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Similarity(double tp, double sp, double ssp){
		this();
		this.topicParameter = tp;
		this.semanticParameter = sp;
		this.stringParameter = ssp;
	}
	
	public static HashMap<String, Integer> getWordIndexMap(String termIndexFile){
		HashMap<String, Integer> wi = new HashMap<String, Integer>();
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(termIndexFile), "GBK"));
			String tempStr;
			int counter = 0;
			while((tempStr = br.readLine()) != null){
				wi.put(tempStr, counter++);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return wi;
	}

	public double getTopicalDivergence(String word1, String word2){
		double simi = 0;
//		System.out.println(word1 + "::::" + word2);
		try{
			int index1 = this.wordIndexMap.get(word1);
			int index2 = this.wordIndexMap.get(word2);
//			System.out.println(index1 + "::::" + index2);
			double[] d1 = new double[this.topicNumber];
//			System.out.println(d1.length);
			double[] d2 = new double[this.topicNumber];
			int total1 = 0, total2 = 0;
			for(int i = 0; i < this.topicNumber; i++){
				d1[i] = this.ttMatrix[i][index1];
				d2[i] = this.ttMatrix[i][index2];
				total1 += d1[i];
				total2 += d2[i];
			}
//			smooth. average 10% of the occurrences to each topic
//			for(int i = 0; i < this.topicNumber; i++){
//				d1[i] = d1[i] * 0.9 + total1 * this.smoothParameter / this.topicNumber; 
//				d2[i] = d2[i] * 0.9 + total2 * this.smoothParameter / this.topicNumber; 
//			}
			for(int i = 0; i < d1.length; i++){
				d1[i] /= total1;
				d2[i] /= total2;
			}
//			for(int i = 0; i < d1.length; i++){
//				System.out.println(d1[i] + "  " + d2[i]);
//			}
			simi = Statistics.jsDivergence(d1, d2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return simi;
	}

	public void testTopicalDivergence() throws Exception{
		FrequentNoun fn = new FrequentNoun();
		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("lishuangjiang");
		System.out.println(pq.size());
		System.out.println("get frequent Nouns");
		HashSet<String> sets = new HashSet<String>();
		LabelScorePair cl;
		while((cl = pq.poll()) != null){
			if(cl.score < 1000)
				break;
			
			sets.add(cl.label);
		}
		
		for(String str:sets){
			System.out.print(str + "  :  ");
			for(String str2:sets){
				System.out.print("(" + str2 + ") " + getTopicalDivergence(str, str2) + "  ");
			}
			System.out.println();
		}
	}
	
	/**
	 * use Hownet to calculate semantic similarity. use 1 - simi to get distance.
	 * words pair  that contain word can't find in Hownet will  be return 0.01 similarity
	 * hence 0.99 distance. 
	 * @param word1
	 * @param word2
	 * @return
	 */
	public double getSemanticDistance(String word1, String word2){
		double simi = wsi.getSimilarity(word1.substring(0, word1.indexOf("/")), 
				word2.substring(0, word2.indexOf("/")));
		return 1 - simi;
	}
	
	public void testSemanticSimilarity() throws Exception{
		FrequentNoun fn = new FrequentNoun();
		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("lishuangjiang");
		System.out.println(pq.size());
		System.out.println("get frequent Nouns");
		HashSet<String> sets = new HashSet<String>();
		LabelScorePair cl;
		while((cl = pq.poll()) != null){
			if(cl.score < 1000)
				break;
			
			sets.add(cl.label);
		}
		
		for(String str:sets){
			System.out.print(str + "  :  ");
			for(String str2:sets){
				System.out.print("(" + str2 + ") " + getSemanticDistance(str, str2) + "  ");
			}
			System.out.println();
		}
	}
	
	/**
	 * use edit distance. normalized by the longer word's length.
	 * @param word1
	 * @param word2
	 * @return real value between 0 and 1
	 */
	public static double getStringDistance(String word1, String word2){
		String w1 = word1.substring(0, word1.indexOf("/"));
		String w2 = word2.substring(0, word2.indexOf("/"));
		int distance = EditDistance.editDistance(w1, w2 ,true);
		int length = w1.length() > w2.length() ? w1.length() : w2.length();
		return (double)distance/length;
	}
	
	/**
	 * contain all three criterion
	 * @return
	 */
	public double getDistance(String word1, String word2){
		double tSimi = this.getTopicalDivergence(word1, word2);
		double sSimi = this.getSemanticDistance(word1, word2);
		double strSimi = getStringDistance(word1, word2);
		
		double simi = 1;
		if(sSimi == 0.99){
			simi = tSimi * (topicParameter + semanticParameter * 0.8) 
			+ strSimi * (stringParameter + semanticParameter * 0.2);
			System.out.println(word1 + ":" + word2 + " = " + simi);
			return simi; 
		}else{
			simi =  tSimi * topicParameter + sSimi * semanticParameter + strSimi * stringParameter; 
			System.out.println(word1 + ":" + word2 + " = " + simi);
			return simi;
		}
	}
	
	public void testDistance() throws Exception{
		FrequentNoun fn = new FrequentNoun();
		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("lishuangjiang");
		System.out.println(pq.size());
		System.out.println("get frequent Nouns");
		HashSet<String> sets = new HashSet<String>();
		LabelScorePair cl;
		while((cl = pq.poll()) != null){
			if(cl.score < 1000)
				break;
			
			sets.add(cl.label);
		}
		
//		for(String str:sets){
//			System.out.print(str + "  :  ");
//			for(String str2:sets){
//				System.out.print("(" + str2 + ") " + getDistance(str, str2) + "  ");
//			}
//			System.out.println();
//		}
//		
		PriorityQueue<LabelScorePair> pq2 = new PriorityQueue<LabelScorePair>();
		for(String str:sets){
			for(String str2:sets){
//				System.out.print("(" + str2 + ") " + getDistance(str, str2) + "  ");
				LabelScorePair ls = new LabelScorePair(str+":"+str2, 
						Double.toHexString(getDistance(str, str2)));
				pq2.add(ls);
			}
		}
		while((cl = pq2.poll()) != null){
			System.out.println(cl.label + "    " + cl.score);
		}
	}
	

	
	public static void main(String[] args) throws Exception{
//		String path = "E:/programs/weibo4j-20110622/weibo4j" +
//				"/experiment/real_one/hotAspect/lda-lishuangjiang-t10-new/01500/";
//		getTopicTermDsitribution(path + "topic-term-distributions.csv", 28997, 10);
//		Similarity sim = new Similarity();
//		System.out.println(sim.getTopicalDivergence("李/nr", "双江/ns"));
//		sim.testTopicalDivergence();
//		sim.testSemanticSimilarity();
//		sim.getStringSimilarity("李阳/nr", "李双江/nr");
//		System.out.println(Similarity.getStringDistance("儿子/nr", "老子/nr"));
//		sim.testDistance();
//		System.out.println(sim.getTopicalDivergence("青春/n", "根源/n"));
		
		String path = 
			"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/hotAspect/00200/";
		Similarity sim = new Similarity("term-index_UTF-8.txt", "topic-term-distributions.csv",path, 10);
	}

	@Override
	public double distance(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return getDistance((String)arg0, (String)arg1);
	}
}
