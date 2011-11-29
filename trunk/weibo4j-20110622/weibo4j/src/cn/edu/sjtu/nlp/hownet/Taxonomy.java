package cn.edu.sjtu.nlp.hownet;

import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;


public class Taxonomy {

	private HashMap semeMap;
	private int[] index;
	private String[] sememe;
	
	private String url = "Hownet/WHOLE.DAT";
	private int N = 1618;
	
	public Taxonomy() {
		semeMap = new HashMap();
		index = new int[N];
		sememe = new String[N];
		
		System.out.println("Taxonomy is loading......");
		init();
		System.out.println("Taxonomy is ready");
	}
	
	private void init() {
		java.io.BufferedReader br = null;
		try {
		    br = new java.io.BufferedReader(new java.io.FileReader(url));
		} catch (java.io.FileNotFoundException ex) {
		    System.out.println("File Not Found!");
		}
		
		Vector vec = new Vector();
		String temp = null;
		try {
			while(true) { 
				temp = br.readLine();
				if(temp == null) break;
				vec.add(temp);
			}
		}catch (java.io.IOException ex) {
			System.err.println(ex);
		} 
		
		StringTokenizer words = null;
		for(int i=0,j=0; i<vec.size() ; i++,j++) {
			words = new  StringTokenizer( (String)vec.get(i) );
			words.nextToken();
			temp = words.nextToken();
			sememe[i] = temp.substring(temp.indexOf("|")+1, temp.length());
			if(!semeMap.containsKey(sememe[i]))semeMap.put(sememe[i], new Integer(i));  //redifintion problem, this part has not solved
			index[i] = Integer.valueOf(words.nextToken()).intValue() ;
		}
		 
	}
	
	public double getTaxonomyDistance(String s1, String s2) {
		Integer i1 = (Integer)semeMap.get(s1);
		Integer i2 = (Integer)semeMap.get(s2);
		if(i1 == null || i2 == null) return 6.4;
		else {
			Vector parent1 = new Vector();
			Vector parent2 = new Vector();
			
			int index1 = i1.intValue(),index2 = i2.intValue();
			while(index[index1] != index1) {
				parent1.add(new Integer(index1));
				index1 = index[index1];
			}
			parent1.add(new Integer(index1));
		
			while(index[index2] != index2) {
				parent2.add(new Integer(index2));
				index2 = index[index2];
			}
			parent2.add(new Integer(index2));
			
			for(int i=0; i<parent1.size(); i++) {
				int j = parent2.indexOf(parent1.get(i));
				if( j >= 0) {
					return  i+j ;
				}
			}
			
			return 20;
		}
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	/*
	public static void main(String[] args) {
		Taxonomy taxonomy = new Taxonomy();
		taxonomy.getTaxonomyDistance("事件","事情");// TODO Auto-generated method stub

	} */
	

}
