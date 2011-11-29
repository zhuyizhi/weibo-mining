/**
 * 
 */
package weibo4j.examples.account;

import weibo4j.RateLimitStatus;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetRateLimitStatus {

	public static final String token = "40ddebeb62103224e67d5ce336c49546";
	public static final String tokenSecret = "51f0adf2216bb5e7a3a9dce5aa37d29f";
	/**
	 * 获取当前用户API访问频率限制
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
		try {
			Weibo weibo = new Weibo();
//			weibo.setToken(args[0], args[1]);
			weibo.setToken(token, tokenSecret);
			RateLimitStatus limitStatus = weibo.rateLimitStatus();
			
			System.out.println(limitStatus.toString());
			System.out.println(limitStatus.getRemainingHits());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}