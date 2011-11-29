package cn.edu.sjtu.howNet;

import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.ArrayList;

//import util.WordReader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class howNetReader {
  public Hashtable howNetWords=new Hashtable();

  public static void main(String[] args) {
	  howNetReader hr = new howNetReader();
	  
//	  String[][] words = hr.readWords("word.txt");
	  String[][] word = hr.readWords("word.txt");//新添的
	//	for(int i=0; i<words.length; i++) {
	//		double simi = hr.Similarity(words[i][0],words[i][1]);
	//		System.out.println(words[i][0] + " " + words[i][1] + " " + simi);
	//	}
		
//		String[] word = WordReader.readWords("word2.txt");//这行被自己注释掉了
		long time = System.currentTimeMillis();
		System.out.print("     ");
		for(int i=0; i<word.length; i++) 
			System.out.print(word[i] + "   ");
		System.out.println();
		for(int i=0; i<word.length; i++) {
			System.out.print(word[i] + "   ");
			for(int j=0; j<word.length; j++) {
				double simi = hr.Similarity(word[i],word[j]);//错误。原来的只有(String ,String)
				System.out.print(simi + "   ");
			}
			System.out.println();
		} 
		System.out.println( (double)System.currentTimeMillis() - time);
	  
  }
  private double Similarity(String[] strings, String[] strings2) {
	// TODO Auto-generated method stub
	return 0;
}
/**
   * for test
   * @param url
   * @return
   */
  public String[][] readWords(String url) {
		 java.io.BufferedReader br = null;
		   try {
		     br = new java.io.BufferedReader(new java.io.FileReader(url));
		   }
		   catch (java.io.FileNotFoundException ex) {
		     System.out.println("File Not Found!");
		   }
		   
		   int count = 0;
		   String[][] words = new String[15][2]; 
		   for(int i=0; i<words.length; i++) {
			   String temp = null;
			   count = 0;
			      try {
			        temp = br.readLine();
			      }
			      catch (java.io.IOException ex) {
			      }
			      if (temp != null) {
			    	  java.util.StringTokenizer st = new java.util.StringTokenizer(temp);
			    	  while (st.hasMoreTokens())
			    		  words[i][count++] = st.nextToken();
			      }
			      
		   }
		   
		     return words;
	}
  
  public howNetReader()
  {
    BufferedReader br=ReadFile("Hownet/glossary.dat");
    while (true) {
      String temp = null;
      try {
        temp = br.readLine();
      }
      catch (IOException ex) {
      }
      if (temp == null)
        break;
      StringTokenizer st = new StringTokenizer(temp);
      int lineindex=0;
      howNetWord w=new howNetWord();
      while (st.hasMoreTokens())
      {
        if(lineindex==0)//the word
          w.Word=st.nextToken();
        else if(lineindex==1)
          w.Type=st.nextToken();
        else
        {
          w.Sences=st.nextToken().split(",");
        }
        lineindex++;
        //  System.out.println(st.nextToken());
      }
      PutIntoTable(w);
    }
  }

  public double Similarity(String s1, String s2)
  {
    wordSim ws=new wordSim(howNetWords,s1,s2);
    return ws.calculate();
  }

  private void PutIntoTable(howNetWord w)
  {
    //first check existence
    if(howNetWords.get(w.Word)==null)//not found, create new list
    {
      ArrayList wordlist=new ArrayList();
      wordlist.add(w);
      howNetWords.put(w.Word,wordlist);
    }
    else
    {
      ((ArrayList)howNetWords.get(w.Word)).add(w);
    }
  }

  public BufferedReader ReadFile(String Filename)
  {
    BufferedReader br = null;
   try {
     br = new BufferedReader(new FileReader(Filename));
   }
   catch (FileNotFoundException ex) {
     System.out.println("File Not Found!");
   }
   return br;
  }

}