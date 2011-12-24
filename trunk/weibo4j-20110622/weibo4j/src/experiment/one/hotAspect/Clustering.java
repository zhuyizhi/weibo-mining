package experiment.one.hotAspect;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import november.second.LabelScorePair;

import com.aliasi.cluster.CompleteLinkClusterer;
import com.aliasi.cluster.Dendrogram;
import com.aliasi.cluster.SingleLinkClusterer;
import com.aliasi.util.Distance;

public class Clustering {
	public static void singleLinkClustering(Set<String> strSet){
		try{
			Similarity sim = new Similarity(0.4,0.5,0.1);
			SingleLinkClusterer sl = new SingleLinkClusterer(sim);
			Dendrogram<String> dend = sl.hierarchicalCluster(strSet);
			Set<Set<String>> clusters = dend.partitionK(15);
			int counter = 0;
			for(Set<String> cluster:clusters){
				System.out.println("cluster " + (++counter) + ": ");
				System.out.print("      ");
				for(String str:cluster){
					System.out.print(str + ", ");
				}
				System.out.println();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testSLC() throws Exception{
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
		singleLinkClustering(sets);
	}
	
	public static void completeLinkClustering(Set<String> strSet){
		try{
//			Similarity sim = new Similarity(0.4,0.5,0.1);
//			String path = 
//				"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/" +
//				"hotAspect/lda-rolled_girl_topic_10/01500/";
			String path = 
				"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/hotAspect/00200/";
//			Similarity sim = new Similarity("term-index_UTF-8.txt", "topic-term-distributions.csv",path);
//			Similarity sim = new Similarity();
			PMISimilarity sim = new PMISimilarity();
			CompleteLinkClusterer cl = new CompleteLinkClusterer(sim);
			Dendrogram<String> dend = cl.hierarchicalCluster(strSet);
			Set<Set<String>> clusters = dend.partitionK(13);
			int counter = 0;
			for(Set<String> cluster:clusters){
//				System.out.println("cluster " + (++counter) + ": ");
//				System.out.print("      ");
				for(String str:cluster){
					System.out.print(str + ",");
				}
				System.out.println();
			}
			System.out.println(dend.prettyPrint());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Set<Set<String>> completeLinkClustering(Set<String> strSet, Distance sim, int k){
		try{
			CompleteLinkClusterer cl = new CompleteLinkClusterer(sim);
			Dendrogram<String> dend = cl.hierarchicalCluster(strSet);
			Set<Set<String>> clusters = dend.partitionK(k);
			printCluster(clusters);
			return clusters;
//			System.out.println(dend.prettyPrint());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Set<Set<String>> completeLinkClustering(Set<String> strSet, Similarity sim, int k){
		try{
			CompleteLinkClusterer cl = new CompleteLinkClusterer(sim);
			Dendrogram<String> dend = cl.hierarchicalCluster(strSet);
			Set<Set<String>> clusters = dend.partitionK(k);
			printCluster(clusters);
			return clusters;
//			System.out.println(dend.prettyPrint());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void printCluster(Set<Set<String>> clusters){
//		int counter = 0;
		for(Set<String> cluster:clusters){
//			System.out.println("cluster " + (++counter) + ": ");
//			System.out.print("      ");
			for(String str:cluster){
				System.out.print(str + ",");
			}
			System.out.println();
		}
	}
	
	public static void printCluster(Set<Set<String>> clusters, PrintWriter pw) throws Exception{
//		int counter = 0;
		for(Set<String> cluster:clusters){
//			System.out.println("cluster " + (++counter) + ": ");
//			System.out.print("      ");
			for(String str:cluster){
				pw.print(str + ",");
			}
			pw.println();
		}
	}
	
	public static void testCLC() throws Exception{
		FrequentNoun fn = new FrequentNoun();
//		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("rolled_girl_copy");
		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("lishuangjiang");
		System.out.println(pq.size());
		System.out.println("get frequent Nouns");
		HashSet<String> sets = new HashSet<String>();
		LabelScorePair cl;
		while((cl = pq.poll()) != null){
			if(cl.score < 500)
				break;
			
			sets.add(cl.label);
		}
		completeLinkClustering(sets);
	}
	
	
	public static void main(String[] args)throws Exception{
//		testSLC();
		testCLC();
	}
}
