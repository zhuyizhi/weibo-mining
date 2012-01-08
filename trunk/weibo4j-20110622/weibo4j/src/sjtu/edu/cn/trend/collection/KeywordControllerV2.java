package sjtu.edu.cn.trend.collection;

import java.util.Calendar;
import java.util.List;

public class KeywordControllerV2 {
	private static final String keywordTable = "话题词表";
	private SinaWeiboHelper helper = new SinaWeiboHelper();
	private int NUMBER_AFTER_TWO_DAY = 5000;
	private int NUMBER_EVERY_DAY = 3000;
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
		System.out.println("start 2");
		//get unqualified table invalid
		helper.invalidKeywordsLessThan(2, this.NUMBER_AFTER_TWO_DAY);
		helper.invalidKeywordsEveryday(this.NUMBER_EVERY_DAY);
		
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
				Thread.sleep(7200000);//改为两个小时唤醒一次了
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
