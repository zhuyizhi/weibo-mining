package sjtu.edu.cn.trend.collection;

import java.util.Calendar;
import java.util.List;

public class KeywordControllerV2 {
	private static final String keywordTable = "话题词表";
	private SinaWeiboHelper helper = new SinaWeiboHelper();
	/**
	 * 将keyword填入"话题词表"，这样在下次爬行的时候就会爬取这个词了
	 * @param keyword
	 */
	public void addKeyword(String keyword){
		try{
			helper.insertKeyWords(keyword);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addProcess(){
		GetTrendWeibo gtw = new GetTrendWeibo();
		List<String> keyList = gtw.getHourlyTrend();
		
		List<String> allKeyList = helper.getKeywords();
		
		for(String key:keyList){
			if(!allKeyList.contains(key)){//this is a new trend, need to create a new table
				helper.insertKeyWordsV2(key);
				helper.createTrendTableV2(key);
			}
		}
	}
	
	public void deleteProcess(){
		//start to check out of date trend
		Calendar c = Calendar.getInstance();
		c.set(c.DAY_OF_MONTH, c.get(c.DAY_OF_MONTH) - 7);
		helper.invalidKeywordsBefore(c);
	}
	
	public void scheduler(){
		try{
			while(true){
				try{
					addProcess();
					deleteProcess();
					
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("error in keyword add&&delete ");
				}
				System.out.println("scheduler....");
				Thread.sleep(3600000);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error in keyword controller scheduler");
		}

	}
	
	public static void main(String[]args){
		KeywordControllerV2 kc = new KeywordControllerV2();
		kc.scheduler();
	}
}
