package sjtu.edu.cn.trend.collection;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import weibo4j.Status;

public class WeiboCollectControllerV2 {

	public static void collect(){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			GetTrendWeibo gtw = new GetTrendWeibo();
			
			int exceptionNumber = 0;
			
			while(true){
				try{
					List<String> aliveKeywords = helper.getAliveKeywords();
					for(String keyword : aliveKeywords){
						
						List<Status> sts = gtw.getRelatedStatus(keyword);
						for(Status st : sts){
//							helper.insertTrendRelatedWeibo(keyword, st);
							helper.insertTrendRelatedWeiboV2(keyword, st);
						}
						
						Thread.sleep(2000);
					}
				}catch(Exception e){
					e.printStackTrace();
					exceptionNumber++;
				}
				
				System.out.println("exceptionNumber = " + exceptionNumber);
				Thread.sleep(60000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		collect();
	}
}
