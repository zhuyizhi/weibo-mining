package mytest;

import weibo4j.Query;
import weibo4j.Weibo;

public class SearchTest {
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	
	{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
	public Weibo weibo;
	
	public SearchTest(){
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
	}
	
	public static void main(String[]args)throws Exception{
		Query query = new Query();
		query.setQ("姚明");
		SearchTest st = new SearchTest();
		st.weibo.search(query);
	}
}
