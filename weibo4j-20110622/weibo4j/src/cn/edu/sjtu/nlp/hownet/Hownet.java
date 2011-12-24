package cn.edu.sjtu.nlp.hownet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Hownet {
	private static HashMap hownet = new HashMap();
	private static String url = "Hownet/glossary.dat";
	
	public static String tags = "~!@#$%^&*+?";
	
	/*
	public static void main(String[] args) {
		Hownet.setURL("Hownet/glossary.dat");
		Hownet.getHownet();// TODO Auto-generated method stub

	}
	*/
	public static void setURL(String url) {
		Hownet.url = url;
	}
	
	static boolean DEBUG = true;
	public static HashMap getHownet() {
	   System.out.println("Hownet is builting......");
	   
	   BufferedReader br = null;
	   try {
//		   br = new BufferedReader(new FileReader(url));
		   br = new BufferedReader(new InputStreamReader
				   (new FileInputStream(url), "gb2312"));
	   } catch (FileNotFoundException ex) {
		   System.err.println("File Not Found!");
		   System.err.println(ex);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   String line;
	   String[] split = null;
	   HWord word,temp;
	   try {
		   while(true) {
			   line = br.readLine();
			   if(line == null) break;
			   split = line.split("\\s+");
			   word = new HWord(split[0],split[1]);
			   word.addSense(split[2]);
			   
			   if(hownet.containsKey(split[0])) {
				   temp = (HWord)hownet.get(split[0]);
				   temp.addSense(split[2]);
				 
			   }else {
				   hownet.put(split[0],word);
			   }
			   
			 
			}
	   } catch (IOException ex) {
		   System.err.println(ex);
	   }
	   
	   System.out.println("Hownet dicitonary is built up: " + hownet.size());
	   return hownet;

	}

	public static boolean isPerson(String word){
		String[] senses = ( (HWord)hownet.get(word) ).getSenses();
		for(String sense : senses){
			List<String> s = Arrays.asList(sense.split(","));
			if(s.contains("human|人"))
				return true;
		}

		return false;
	}
	
	public static void main(String[] args){
//		HashMap hownet = Hownet.getHownet();
		getHownet();
		System.out.println((isPerson("汽车")));
	}
}
