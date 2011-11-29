package experiment.one.hotAspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.HashSet;

import cn.edu.sjtu.nlp.hownet.Hownet;
import cn.edu.sjtu.nlp.hownet.WordSimilarity;
import november.second.LabelScorePair;

public class AspectWithSimilarity {
	public static boolean isNE(String token){
		if(token.indexOf("/") != -1 )
			if(token.substring(token.indexOf("/"), token.length()).equals("/nr") ||
					token.substring(token.indexOf("/"), token.length()).equals("/ns"))
				return true;
		
		return false;
	}
	
	public static void testSimilarityBaseMethod()throws Exception{
		FrequentNoun fn = new FrequentNoun();
		PriorityQueue<LabelScorePair> pq = fn.getMostFrequentNouns_MemVersion("lishuangjiang");
		System.out.println(pq.size());
		System.out.println("get frequent Nouns");
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		HashMap hownet = Hownet.getHownet();
		WordSimilarity wsi = new WordSimilarity(hownet);
//		for(Object s:hownet.keySet()){
//			System.out.println(s);
//		}
		
		
		LabelScorePair cl;
		while((cl = pq.poll()) != null){
			if(cl.score < 1000)
				break;
			if(! isNE(cl.label)){
				HashSet<String> hs = new HashSet<String>();
				hs.add(cl.label);
				map.put(cl.label, hs);
			}
		}
		
		for(String key:map.keySet()){
			for(String key2:map.keySet()){
				double simi = wsi.getSimilarity(key.substring(0, key.indexOf("/")), 
						key2.substring(0, key2.indexOf("/")));
				System.out.println("counting " + key + ":" + key2 + "    simi=" + simi);
				if(simi > 0.5)
					map.get(key).add(key2);
			}
		}
		
		for(String key:map.keySet()){
			System.out.println("key : " + key);
			for(String str:map.get(key)){
				System.out.println("            " + str);
			}
		}
	}
	
	
	public static void main(String[] args)throws Exception{
		testSimilarityBaseMethod();
	}
}
