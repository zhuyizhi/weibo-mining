package sjtu.edu.cn.trend.collection;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import weibo4j.Status;

public class WeiboCollectController {
	
	public static void main(String[] args){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			Set<String> keywordHistory = new HashSet<String>();
			GetTrendWeibo gtw = new GetTrendWeibo();
			
			List<String> tableName = helper.getExistTableName();
			for(String tn : tableName){
				keywordHistory.add(tn);
			}
			
			int exceptionNumber = 0;
			
			while(true){
				try{
					List<String> aliveKeywords = helper.getKeywords();
					for(String keyword : aliveKeywords){
						if(!keywordHistory.contains(keyword)){//if the keyword is a new one, then we should create a new table
							helper.createTrendTable(keyword);
							keywordHistory.add(keyword);
						}
						
						List<Status> sts = gtw.getRelatedStatus(keyword);
						for(Status st : sts){
							helper.insertTrendRelatedWeibo(keyword, st);
						}
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
}
