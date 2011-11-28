package edu.sjtu.cn.mylda;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

import sjtu.edu.cn.weibo.weibotools.WeiboV1;

import edu.sjtu.ltlab.word.split.ICTCSplit;
import edu.sjtu.ltlab.word.split.IRSplit;


public class ToLDAFormat {
//	public static TestSmartChineseAnalyzer ts=new TestSmartChineseAnalyzer();
//	public static ICTCSplit ir = new ICTCSplit();
	public static IRSplit ir = new IRSplit();
	public static void changeToLDAFormat(String sourceFile, String destFile, int wordLength)throws Exception{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
//		HashSet<String> docs=new HashSet<String>();
		LinkedList<String> docs=new LinkedList<String>();
		String tempDoc;
		int num=0;
		while((tempDoc=br.readLine())!=null){
			docs.add(tempDoc);
			System.out.println(++num);
		}		
		System.out.println("size : "+docs.size());
//		如果使用HashSet，可以去重，但是会打乱顺序
		StringBuffer sb=new StringBuffer();
//		PrintWriter pw=new PrintWriter(new File(destFile));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
		int counter=0;
		
		HashSet<String> stopWordTable = new HashSet<String> ();
		BufferedReader brStopWord = 
			new BufferedReader(new InputStreamReader(new FileInputStream("stopwords.txt"), "UTF-8"));
		String word;
		while((word = brStopWord.readLine()) != null){
			stopWordTable.add(word);
//			System.out.println("stop word ---" + word + "---");
		}
//		System.out.println("size: " + stopWordTable.size());
		
		pw.println(docs.size());
		for(String str:docs){
//			System.out.println(doc.get(fieldName));
//			String processText=ts.getSplittedResult(str);
			str =  WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(str)));
			String processText = ir.paragraphProcess(str);
			
//			2011.9.10 添加停用词处理
			String[] strArray = processText.split(" ");
			StringBuilder sb2 = new StringBuilder(); 
			for(String w : strArray ){
//				if(! stopWordTable.contains(w) && w.length() > 1 ){
//				2011.10.24 filter stop words, size shorter than wordLength, and  all the punctuation
				if(w.indexOf("/") != -1)
					if(! stopWordTable.contains(w.substring(0, w.indexOf("/"))) 
							&& (w.substring(0, w.indexOf("/")).length() > wordLength)
							&& !(w.endsWith("/w") || w.endsWith("/wp") || w.endsWith("/ws"))){
//					processText = processText.replace(w, "");
						sb2.append(w + " ");
						
//						System.out.println(w + "----"  + w.substring(0, w.indexOf("/")) + "----  " + stopWordTable.contains(w.substring(0, w.indexOf("/"))));
					}
			}
			
			sb.append(sb2.toString());
			sb.append("\r\n");
			counter++;
			if((counter%5000)==0)
			{
				String r1 = sb.toString();
				pw.write(r1);
				sb=new StringBuffer();
				System.out.println("第"+counter+"个微博被写入");
				pw.flush();
			}
		}
		pw.write(sb.toString());
//		pw.print(sb.toString());
		pw.flush();
		pw.close();
		System.out.println("size : "+docs.size());
	}
	
	
	public static void changeToLDAFormat_ICTCLAS(String sourceFile, String destFile, int wordLength) {
		try{
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
				LinkedList<String> docs=new LinkedList<String>();
				String tempDoc;
				int num=0;
				while((tempDoc=br.readLine())!=null){
					docs.add(tempDoc);
					System.out.println(++num);
				}		
				System.out.println("size : "+docs.size());
//				如果使用HashSet，可以去重，但是会打乱顺序
				StringBuffer sb=new StringBuffer();
//				PrintWriter pw=new PrintWriter(new File(destFile));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
				int counter=0;
				
				HashSet<String> stopWordTable = new HashSet<String> ();
				BufferedReader brStopWord = 
					new BufferedReader(new InputStreamReader(new FileInputStream("stopwords.txt"), "UTF-8"));
				String word;
				while((word = brStopWord.readLine()) != null){
					stopWordTable.add(word);
				}
				
				pw.println(docs.size());
				for(String str:docs){
//					System.out.println(doc.get(fieldName));
//					String processText=ts.getSplittedResult(str);
					str =  WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(str)));
					String processText = ir.paragraphProcess(str);
					
//					2011.9.10 添加停用词处理
					String[] strArray = processText.split(" ");
					StringBuilder sb2 = new StringBuilder(); 
					for(String w : strArray ){
//						if(! stopWordTable.contains(w) && w.length() > 1 ){
//						2011.10.24 filter stop words, size shorter than wordLength, and  all the punctuation
						if(w.indexOf("/") != -1)
							if(! stopWordTable.contains(w.substring(0, w.indexOf("/"))) 
									&& (w.substring(0, w.indexOf("/")).length() > wordLength)
									&& !(w.endsWith("/w") || w.endsWith("/wp") || w.endsWith("/ws"))){
//							processText = processText.replace(w, "");
								sb2.append(w + " ");
								
//								System.out.println(w + "----"  + w.substring(0, w.indexOf("/")) + "----  " + stopWordTable.contains(w.substring(0, w.indexOf("/"))));
							}
					}
					
					sb.append(sb2.toString());
					sb.append("\r\n");
					counter++;
					if((counter%5000)==0)
					{
						String r1 = sb.toString();
						pw.write(r1);
						sb=new StringBuffer();
						System.out.println("第"+counter+"个微博被写入");
						pw.flush();
					}
				}
				pw.write(sb.toString());
//				pw.print(sb.toString());
				pw.flush();
				pw.close();
				System.out.println("size : "+docs.size());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args)throws Exception{
//		changeToLDAFormat("liyang_noRetweet.txt", "./liyang_noRetweet_LDA_multilple_duplicate.txt", 0);
		changeToLDAFormat("girl_noR.txt", "girl.txt", 0);
//		changeToLDAFormat("weibo.txt", "LDAFormat.txt", Integer.parseInt(args[0]));
	}
}
