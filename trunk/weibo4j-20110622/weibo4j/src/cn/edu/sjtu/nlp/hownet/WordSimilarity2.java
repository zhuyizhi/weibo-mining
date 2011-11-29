//package cn.edu.sjtu.nlp.hownet;
//
//import gate.creole.ontology.*;
//import gate.creole.ontology.jena.JenaOntologyImpl;
//import gate.creole.*;
//
//import java.net.*;
//
//import java.util.HashMap;
//import java.util.ArrayList;
//
//import util.*;
//
//public class WordSimilarity2 {
//	private HashMap hownet;
//	JenaOntologyImpl ontology;
//	private String tags = Hownet.tags;
//	
//	private double ALPHA = 1.6;
//	private double BETA[]={0.5, 0.2, 0.17, 0.13};
//	private double delta = 0.2;
//	
//	private boolean DEBUG = false;
//	
//	public WordSimilarity2(HashMap hownet) {
//		this.hownet = hownet;
//		
//		init();
//	}
//	
//	private void init() {
//		getOntology();
//	}
//	
//	private void getOntology()  {
//		URL url = null;
//		try {
//			url = new URL("file:\\D:\\Program Files\\GATE-3.1\\plugins\\Ontology_Tools\\resources\\Hownet.owl");
//		} catch(MalformedURLException urle) {
//			System.err.print(urle);
//		}
//		
//		System.out.println("Ontology is loading......");
//		ontology = new JenaOntologyImpl();
//		ontology.setOwlLiteFileURL(url);
//		ontology.setURL(url);
//		try {
//			ontology.init();
//		} catch (ResourceInstantiationException e) {
//			System.err.println(e);
//		}
//		
//		System.out.println("Ontology is built up.");
//	}
//	
//	ArrayList indepentSememe1 = new ArrayList(),indepentSememe2 = new ArrayList();
//	ArrayList relativeSememe1 = new ArrayList(),relativeSememe2 = new ArrayList();
//	ArrayList tagSememe1 = new ArrayList(),tagSememe2 = new ArrayList();
//	
//	public double getSimilarity(String word1, String word2) {
//		double similar = 0,max = 0;
//		String[] senses1 = ( (HWord)hownet.get(word1) ).getSenses();
//		String[] senses2 = ( (HWord)hownet.get(word2) ).getSenses();
//		
//		for(int i=0; i<senses1.length; i++)
//			for(int j=0; j<senses2.length; j++) {
//				max = conceptSimilarity(senses1[i],senses2[j]);
//				if( similar < max ) similar = max;
//			}
//		
//		return similar;
//	}
//	
//	private double conceptSimilarity(String sense1, String sense2) {
//		//reinitList();
//		
//		double total = 0;
//		double[] similar = new double[4];	
//		String[] concept1,concept2;
//		String temp;
//		
//		concept1 = sense1.split(",");
//		for(int p=1; p<concept1.length; p++) {
//			temp = concept1[p];
//			int index = tags.indexOf(temp.charAt(0));
//			if(index != -1) tagSememe1.add(temp);
//			else 
//				if ( temp.contains("=") ) relativeSememe1.add(temp);
//				else indepentSememe1.add(temp);
//		}
//		
//		concept2 = sense2.split(",");
//		if(DEBUG) {
//			print.array(concept1);
//			print.array(concept2);
//		}
//		for(int p=1; p<concept2.length; p++) {
//			temp = concept2[p];
//			int index = tags.indexOf(temp.charAt(0));
//			if(index != -1) tagSememe2.add(temp);
//			else 
//				if ( temp.contains("=") ) relativeSememe2.add(temp);
//				else indepentSememe2.add(temp);
//		}
//		
//		if(DEBUG) {
//			print.arrayList(indepentSememe1);
//	    	print.arrayList(indepentSememe2);
//	        System.out.println("==========");
//	   	    print.arrayList(relativeSememe1);
//	     	print.arrayList(relativeSememe2);
//	     	System.out.println("==========");
//    	    print.arrayList(tagSememe1);
//	   		print.arrayList(tagSememe2);
//	   		System.out.println("==========");
//		}
//		
//		similar[0] = sememeSimilar(concept1[0],concept2[0]);
//	    similar[1] = indepentSimilar(indepentSememe1,indepentSememe2);
//	   
//	 
//	    similar[2] = relativeSimilar(relativeSememe1,relativeSememe2);
//	    similar[3] = tagSimilar(tagSememe1,tagSememe2);
//	    if(DEBUG) {
//	    	System.out.println("indep " + similar[1]);
//	 	    System.out.println("relative " + similar[2]);
//	 	    System.out.println("basic" + similar[0]);
//	 	    System.out.println("tag " + similar[3]);
//	    }
//	   
//		double modify = 1;
//		for(int i=0; i<BETA.length; i++){
//			modify = 1;
//			for(int j=0; j<=i; j++) modify = similar[j] * modify;
//			total = BETA[i] * modify + total ;
//		}
//		
//		return total;
//	}
//	
//	private double sememeSimilar(String sememe1, String sememe2) {
//		double distance = 1;
//		if( sememe1.equals(sememe2)) distance = 0;
//		else {
//			//System.out.println(sememe1 + "==" + sememe2 + sememe2.length());
//			int index1 = sememe1.indexOf("|");
//			int index2 = sememe2.indexOf("|");
//			TClass t1 = ontology.getClassByName(sememe1.substring(index1+1,sememe1.length()));
//			TClass t2 = ontology.getClassByName(sememe2.substring(index2+1,sememe2.length()));;
//			if(t1 != null && t2 != null) {
//			  distance = ontology.getTaxonomicDistance(t1,t2);
//			 // System.out.println(t1.getName() + " " + t2.getName() + " " + distance);
//			  if(distance == 0) distance += 20;
//			}else distance = 6.4;
//			  
//		}
//		
//		return ALPHA / ( ALPHA + distance) ;
//	}
//	
//	private double indepentSimilar(ArrayList indepent1, ArrayList indepent2) {
//		if( (indepent1.size() + indepent2.size())==0) return 1; 
//		else {
//			//if(indepent1.size() ==0 || indepent2.size() == 0) return 1;
//			double total = 0,temp = 0,max = 0;
//			ArrayList minList = indepent1.size() <= indepent2.size() ? indepent1: indepent2;
//			ArrayList maxList = indepent1.size() > indepent2.size() ? indepent1: indepent2;
//			int index = 0, size = maxList.size();
//			String s1,s2;
//			
//			while( !minList.isEmpty() ) {
//				s1 = (String)minList.get(0);
//				index = 0;
//				max = 0;
//				for(int i=0; i<maxList.size(); i++){
//					s2 = (String)maxList.get(i);
//					temp = sememeSimilar(s1, s2);
//					if(temp > max) {
//						max = temp;
//						index = i;
//					}
//				}
//				
//				total = total + max;
//				minList.remove(0);
//				maxList.remove(index);
//				
//			}
//			
//			int remain = maxList.size();
//			maxList.clear();
//			return ( total + remain*delta ) / size;
//		}
//	}
//
//	private double relativeSimilar(ArrayList relative1, ArrayList relative2) {
//		int size = relative1.size() + relative2.size();
//		if(size==0) return 1;
//		else {
//			//if(relative1.size() ==0 || relative1.size() == 0) return 1;
//			double total = 0;
//			ArrayList minList = relative1.size() <= relative2.size() ? relative1: relative2;
//			ArrayList maxList = relative1.size() > relative2.size() ? relative1: relative2;
//			int count = 0 ;
//			String s1,s2;
//			int index1, index2;
//			
//			while(!minList.isEmpty()) {
//				s1 = (String)minList.get(0);
//				index1 = s1.indexOf("=");
//				for(int i=0; i<maxList.size(); i++) {
//					s2 = (String)minList.get(i);
//					index2 = s1.indexOf("=");
//					if( s1.substring(0,index1).equals(s2.substring(0,index2)) ) {
//						total = total + sememeSimilar(s1, s2);
//						maxList.remove(i);
//						count ++ ;
//						//System.out.println(s1 + " " + s2 + " " + sememeSimilar(s1, s2));
//						break;
//					}
//				}
//				
//				minList.remove(0);
//			}
//
//			maxList.clear();
//			return ( total + (size - 2*count) * delta ) / (size-count);
//		}
//
//	}
//	
//	private double tagSimilar(ArrayList tag1, ArrayList tag2) {
//		int size = tag1.size() + tag2.size();
//		if(size == 0) return 1;
//		else {
//			//if(tag1.size() ==0 || tag2.size() == 0) return 1;
//			double total = 0;
//			ArrayList minList = tag1.size() <= tag2.size() ? tag1: tag2;
//			ArrayList maxList = tag1.size() > tag2.size() ? tag1: tag2;
//			int count = 0 ;
//			String s1,s2;
//			
//			while(!minList.isEmpty()) {
//				s1 = (String)minList.get(0);
//				for(int i=0; i<maxList.size(); i++) {
//					s2 = (String)minList.get(i);
//					if(s1.charAt(0) == s2.charAt(0)) {
//						total = total + sememeSimilar(s1, s2);
//						//minList.remove(0);
//						maxList.remove(i);
//						count ++ ;
//						break;
//					}
//				}
//				
//				minList.remove(0);
//			}
//			
//			maxList.clear();
//			return ( total + (size - 2*count) * delta ) / (size-count);
//		}
//
//	}
//	
//	
//	public static void main(String[] args) {
//		HashMap hownet = Hownet.getHownet();
//		WordSimilarity2 wsi = new WordSimilarity2(hownet);
//
//		if(!wsi.DEBUG) {
//			String[] word = WordReader.readWords("word2.txt");
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
//		} else {
//			double simi = wsi.getSimilarity("��ѡ","����");
//			System.out.println(simi);
//		}
//		
//			
//
//	}
//	
//	
//
//}

