package mytest;

import java.util.List;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Trend;
import weibo4j.Trends;
import weibo4j.Weibo;

public class TopicRelatedTest {
	public static void main(String[] args){
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        
        try{
        	String token = "40ddebeb62103224e67d5ce336c49546";
        	String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
        	Weibo weibo = new Weibo();
        	weibo.setToken(token, tokenSecret);
        	Paging paging = new Paging();
        	paging.count(50);
        	List<Status> sts = weibo.getTrendStatus("动车事故", paging);
//        	weibo.getDailyTrends();
        	List<Trends> trends = weibo.getTrendsHourly(0);
        	for(Trends tds : trends){
        		System.out.println("trends..............");
        		Trend[] tdArray = tds.getTrends();
        		for(Trend td : tdArray){
        			System.out.println(td.getQuery() + "  " + td.getName());
        		}
        	}
        	
        	for(Status st : sts){
        	
        		System.out.println(st.getCreatedAt()+st.getText());
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
}
