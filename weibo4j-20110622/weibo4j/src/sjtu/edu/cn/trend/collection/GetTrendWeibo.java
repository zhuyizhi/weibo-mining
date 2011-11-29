package sjtu.edu.cn.trend.collection;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Trend;
import weibo4j.Trends;
import weibo4j.Weibo;

public class GetTrendWeibo {
//	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String token = "601c372ed47c64eb9f7b3cdf0fb0acb4";
//	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	public static final String tokenSecret = "32f13f171f9a437b1ef1a0fa75966667";
	
	{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
	public Weibo weibo;
	
	public GetTrendWeibo(){
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
	}
	
	public List<Status> getRelatedStatus(String trend){
		List<Status> sts = new ArrayList<Status>();
		try{
        	Paging paging = new Paging();
        	paging.count(50);
        	
        	sts = weibo.getTrendStatus(trend, paging);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sts;
	}
    
	public List<String> getHourlyTrend(){
		List<String> trendNameList = new ArrayList<String>();
		try{
			List<Trends> trends = weibo.getTrendsHourly(0);
			
        	for(Trends tds : trends){
        		Trend[] tdArray = tds.getTrends();
        		for(Trend td : tdArray){
        			trendNameList.add(td.getName());
        		}
        	}
		}catch(Exception e){
			e.printStackTrace();
		}
		return trendNameList;
	}
}
