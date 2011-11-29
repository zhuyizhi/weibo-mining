package cn.edu.sjtu.nlp.hownet;

import java.util.HashMap;
import java.util.ArrayList;

public class WordSimilarity {
	private HashMap hownet;
	private Taxonomy taxonomy;
	private String tags = Hownet.tags;
	
	private double ALPHA = 1.6;
	private double BETA[]={0.5, 0.2, 0.17, 0.13};
	private double delta = 0.2;
	
	//private boolean DEBUG = false;
	private ArrayList indepentSememe1,indepentSememe2;
	private ArrayList relativeSememe1,relativeSememe2;
	private ArrayList tagSememe1,tagSememe2;
	
	public WordSimilarity(HashMap hownet) {
		this.hownet = hownet;
		init();
	}
	
	private void init() {
		taxonomy = new Taxonomy();
		
		indepentSememe1 = new ArrayList();
		indepentSememe2 = new ArrayList();
		relativeSememe1 = new ArrayList();
		relativeSememe2 = new ArrayList();
		tagSememe1 = new ArrayList();
		tagSememe2 = new ArrayList();	
	}
	
	public double getSimilarity(String word1, String word2) {
		double similar = 0,max = 0;
		String[] senses1 = null,senses2 = null;
		if(hownet.containsKey(word1) && hownet.containsKey(word2)) {
			senses1 = ( (HWord)hownet.get(word1) ).getSenses();
			senses2 = ( (HWord)hownet.get(word2) ).getSenses();
		}else return 0.01; // cannot get from hownet;
		
		for(int i=0; i<senses1.length; i++)
			for(int j=0; j<senses2.length; j++) {
				max = conceptSimilarity(senses1[i],senses2[j]);
				if( similar < max ) similar = max;
			}
		
		return similar;
	}
	
	private double conceptSimilarity(String sense1, String sense2) {
		double total = 0;
		double[] similar = new double[4];	
		String[] concept1,concept2;
		String temp;
		
		concept1 = sense1.split(",");
		for(int p=1; p<concept1.length; p++) {
			temp = concept1[p];
			int index = tags.indexOf(temp.charAt(0));
			if(index != -1) tagSememe1.add(temp);
			else 
				if ( temp.contains("=") ) relativeSememe1.add(temp);
				else indepentSememe1.add(temp);
		}
		
		concept2 = sense2.split(",");
		for(int p=1; p<concept2.length; p++) {
			temp = concept2[p];
			int index = tags.indexOf(temp.charAt(0));
			if(index != -1) tagSememe2.add(temp);
			else 
				if ( temp.contains("=") ) relativeSememe2.add(temp);
				else indepentSememe2.add(temp);
		}
		
		similar[0] = sememeSimilar(concept1[0],concept2[0]);
	    similar[1] = indepentSimilar(indepentSememe1,indepentSememe2);
	    similar[2] = relativeSimilar(relativeSememe1,relativeSememe2);
	    similar[3] = tagSimilar(tagSememe1,tagSememe2);
	   
		double modify = 1;
		for(int i=0; i<BETA.length; i++){
			modify = 1;
			for(int j=0; j<=i; j++) modify = similar[j] * modify;
			total = BETA[i] * modify + total ;
		}
		
		return total;
	}

	private double sememeSimilar(String sememe1, String sememe2) {
		double distance = 1;
		if( sememe1.equals(sememe2)) distance = 0;
		else {
			int index1 = sememe1.indexOf("|");
			int index2 = sememe2.indexOf("|");
			distance = taxonomy.getTaxonomyDistance(sememe1.substring(index1+1,sememe1.length()),sememe2.substring(index2+1,sememe2.length()));
		}
		
		return ALPHA / ( ALPHA + distance) ;
	}
		
	private double indepentSimilar(ArrayList indepent1, ArrayList indepent2) {
		if( (indepent1.size() + indepent2.size())==0) return 1; 
		else {
			double total = 0,temp = 0,max = 0;
			ArrayList minList = indepent1.size() <= indepent2.size() ? indepent1: indepent2;
			ArrayList maxList = indepent1.size() > indepent2.size() ? indepent1: indepent2;
			int index = 0, size = maxList.size();
			String s1,s2;
			
			while( !minList.isEmpty() ) {
				s1 = (String)minList.get(0);
				index = 0;
				max = 0;
				for(int i=0; i<maxList.size(); i++){
					s2 = (String)maxList.get(i);
					temp = sememeSimilar(s1, s2);
					if(temp > max) {
						max = temp;
						index = i;
					}
				}
				
				total = total + max;
				minList.remove(0);
				maxList.remove(index);
				
			}
			
			int remain = maxList.size();
			maxList.clear();
			return ( total + remain*delta ) / size;
		}
	}

	private double relativeSimilar(ArrayList relative1, ArrayList relative2) {
		int size = relative1.size() + relative2.size();
		//if(relative1.size()==0 || relative2.size()==0) { relative1.clear();relative2.clear();return 1; }
		if(size == 0) return 1;
		else {
			double total = 0;
			ArrayList minList = relative1.size() <= relative2.size() ? relative1: relative2;
			ArrayList maxList = relative1.size() > relative2.size() ? relative1: relative2;
			int count = 0 ;
			String s1,s2;
			int index1, index2;
			
			while(!minList.isEmpty()) {
				s1 = (String)minList.get(0);
				index1 = s1.indexOf("=");
				for(int i=0; i<maxList.size(); i++) {
					s2 = (String)minList.get(i);
					index2 = s1.indexOf("=");
					if( s1.substring(0,index1).equals(s2.substring(0,index2)) ) {
						total = total + sememeSimilar(s1, s2);
						maxList.remove(i);
						count ++ ;
						break;
					}
				}
				
				minList.remove(0);
			}

			maxList.clear();
			return ( total + (size - 2*count) * delta ) / (size-count);
		}

	}
	
	private double tagSimilar(ArrayList tag1, ArrayList tag2) {
		int size = tag1.size() + tag2.size();
		//if(tag1.size() == 0 || tag2.size()== 0) { tag1.clear();tag2.clear();return 1; }
		if(size == 0) return 1;
		else {
			double total = 0;
			ArrayList minList = tag1.size() <= tag2.size() ? tag1: tag2;
			ArrayList maxList = tag1.size() > tag2.size() ? tag1: tag2;
			int count = 0 ;
			String s1,s2;
			
			while(!minList.isEmpty()) {
				s1 = (String)minList.get(0);
				for(int i=0; i<maxList.size(); i++) {
					s2 = (String)minList.get(i);
					if(s1.charAt(0) == s2.charAt(0)) {
						total = total + sememeSimilar(s1, s2);
						maxList.remove(i);
						count ++ ;
						break;
					}
				}
				
				minList.remove(0);
			}
			
			maxList.clear();
			return ( total + (size - 2*count) * delta ) / (size-count);
		}

	}
	
	
	public static void main(String[] args) {
		HashMap hownet = Hownet.getHownet();
		WordSimilarity wsi = new WordSimilarity(hownet);
//		if(!wsi.DEBUG) {
//			String[] word = WordReader.readWords("word2.txt");
//			long time = System.currentTimeMillis();
//			System.out.print("     ");
//			for(int i=0; i<word.length; i++) 
//				System.out.print(word[i] + "   ");
//			System.out.println();
//			for(int i=0; i<word.length; i++) {
//				System.out.print(word[i] + "   ");
//				for(int j=0; j<word.length; j++) {
//					double simi = wsi.getSimilarity(word[i],word[j]);
//					System.out.print(simi + "   ");
//				}
//				System.out.println();
//			} 
//		    
//			System.out.println(System.currentTimeMillis() - time);
//		} else {
//			double simi = wsi.getSimilarity("����","��ְ");
//			System.out.println(simi);
//		}
//			for(Object s:hownet.keySet()){
//				System.out.println(s);
//			}
			double simi = wsi.getSimilarity("山西", "北京");
			System.out.println(simi);

	}
	
	

}
