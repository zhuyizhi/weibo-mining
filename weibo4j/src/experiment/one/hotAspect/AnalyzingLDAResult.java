package experiment.one.hotAspect;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;

import november.second.LabelScorePair;

public class AnalyzingLDAResult {
	public static void analyze() throws Exception{
		String spath = 
			"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/hotAspect/00200/";
		Similarity simi = new Similarity("term-index_UTF-8.txt", "topic-term-distributions.csv",spath);
		Double[][] ttm = simi.ttMatrix;
		String path = "E:/programs/weibo4j-20110622/weibo4j" +
		"/experiment/real_one/hotAspect/";
		HashMap<Integer, String> idToWord = new HashMap<Integer, String>();
		PrintWriter pw = new PrintWriter
		(new OutputStreamWriter(new FileOutputStream(path + "analyzeLDA_roll.file"), "gb2312"));
		for(String str:simi.wordIndexMap.keySet()){
			idToWord.put(simi.wordIndexMap.get(str), str);
		}
//		for(int key:idToWord.keySet()){
//			pw.println(key + ":" + idToWord.get(key));
//		}

		for(Double[] topic:ttm){
			PriorityQueue<LabelScorePair> pq = new PriorityQueue<LabelScorePair>(); 
			for(int i = 0; i < topic.length - 1; i++){
				String token = idToWord.get(i);
//				if(token == null)
//					System.out.println(i + ":" + topic.length);
				if(topic[i] > 50 && FrequentNoun.isAspect(token)){
					LabelScorePair lsp = 
						new LabelScorePair(idToWord.get(i), Double.toString(topic[i]));
					pq.add(lsp);
				}
			}
			LabelScorePair lsp;
			while((lsp = pq.poll()) != null){
				if(lsp.score < 4000)
					break;
//				pw.print(lsp.label + ":" + lsp.score + ", ");
				pw.print(lsp.label + ", ");
			}
			pw.println();
		}
		pw.close();
	}
	
	public static void main(String[] args) throws Exception{
		analyze();
	}
}
