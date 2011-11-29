package cn.edu.sjtu.howNet;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class wordSim {
  String w1;
  String w2;

  Hashtable howNetWords;

  double ALPHA=0.15;
  double BETA[]={0.45, 0.4, 0.1, 0.05};

  public wordSim(Hashtable t,String w1,String w2)
  {
	howNetWords=t;
    this.w1=w1;
    this.w2=w2;
  }

  public double calculate()
  {
    ArrayList sence1=(ArrayList)howNetWords.get(w1);
    ArrayList sence2=(ArrayList)howNetWords.get(w2);
    if(sence1==null||sence2==null)
      return 0.0;
    double max=0.0;
    for(int i=0;i<sence1.size();i++) {
      for(int j=0;j<sence2.size();j++)
      {
        howNetWord w1=(howNetWord)sence1.get(i);
        howNetWord w2=(howNetWord)sence2.get(j);
        double score=ALPHA*(w1.Type.equals(w2.Type)?1:0);
        int m=w1.Sences.length;
        int n=w2.Sences.length;
        
        double score1 = 0.0;
        int len = (m<n?m:n);
        
        for(int k = 0;k < len && k < BETA.length; k++)
        {
        	if(len == 1)
        	{
        		score1 += primitivesim(w1.Sences[k],w2.Sences[k]);
        	}
        	else
        	{
        		score1 += this.BETA[k] * primitivesim(w1.Sences[k],w2.Sences[k]);
        	}
        }
        score += (1-ALPHA)*score1;
        if(max<score)
        {
          max=score;
        }
      }
    }
    return max;
  }

  private double primitivesim(String s1, String s2)
  {
    if(s1.equals(s2))
      return Math.random()*0.1+0.9;
    else
      return 0.1*Math.random();
  }
}