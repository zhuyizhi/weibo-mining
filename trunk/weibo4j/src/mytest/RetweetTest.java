package mytest;

import java.util.List;

import weibo4j.Emotion;
import weibo4j.Paging;
import weibo4j.RetweetDetails;
import weibo4j.Status;
import weibo4j.Weibo;

public class RetweetTest {
	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	
	{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
	public Weibo weibo;
	
	public RetweetTest(){
		weibo = new Weibo();
		weibo.setToken(token, tokenSecret);
	}
	
	public static void testRetweet(){
		try{
			RetweetTest  rt = new RetweetTest();
			Weibo wb = rt.weibo;
//			rt.weibo.getUserTimeline(new Paging(10));
//			System.out.println(rt.weibo.getUserId());
//			rt.weibo.getFollowers();
//			wb.getFollowersStatuses();
			List<Status> sl = wb.getUserTimeline();
			for(Status s : sl){
				System.out.println(s.getId() + ":" + s.getText());
				RetweetDetails rd = s.getRetweetDetails();
				Status st = s.getRetweeted_status();
				s.getInReplyToStatusId();
//				s.getRetweetDetails();
				if(st != null)
				{
					System.out.println(st.getId() + "--" + rd.getRetweetId() + ":" + st.getText());
					System.out.println(s.getInReplyToStatusId() + "---" + s.getInReplyToScreenName());
				}
			}
			System.out.println(wb.getCounts("3356829996950853, 3356767531309178"));
			System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testGetWeiboByID(){
		RetweetTest  rt = new RetweetTest();
		Weibo wb = rt.weibo;
		long id = 3356829996950853L;
		try{
			Status s = wb.showStatus(id);
			Status rs = s.getRetweeted_status();
			System.out.println(s.getText() + " 后面是转发内容：" + rs.getText());
//			List<Emotion> ls = wb.getEmotions();
//			for(Emotion e:ls ){
//				System.out.println(e);
//			}
//			System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void main(String[]args) throws Exception{
		testGetWeiboByID();
		
	}
}
