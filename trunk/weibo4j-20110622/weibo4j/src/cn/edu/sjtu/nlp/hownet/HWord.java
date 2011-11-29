package cn.edu.sjtu.nlp.hownet;

import java.util.ArrayList;
import java.util.Iterator;

public class HWord {
	private String word;
	private String syntax;
    private ArrayList senses;
    
    public HWord(String word,String syntax){
        this.syntax = syntax;
        this.word = word;
        senses = new ArrayList();
    }
    
    public String getWord() {
	    return word;
	}
    
    public void setWord(String word) {
	    this.word = word;
	}
    
    public String getSyntax() {
	  return syntax;
	}
  
  public void setSyntax(String syntax) {
	  this.syntax = syntax;
  }
  
  public String[] getSenses() {
	  String[] sense = new String[senses.size()];
	  Iterator it = senses.iterator();
	  
	  int i=0;
	  while(it.hasNext()) {
		 sense[i++] = (String)it.next(); 
	  }
	  
	  return sense;
  }
  
  public void addSense(String sense) {
	  senses.add(sense);
  }
  

}
