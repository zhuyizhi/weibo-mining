/**
 *
 */
package weibo4j.examples.timeline;

import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetPublicTimeline {

	/**
	 * 获取最新更新的公共微博消息
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	try {
			//获取前20条最新更新的公共微博消息
    		Weibo weibo = new Weibo();
    		String Access_token="601c372ed47c64eb9f7b3cdf0fb0acb4";
    		String Access_token_secret="32f13f171f9a437b1ef1a0fa75966667";
    		String Access_token2="02e65b23c9e441aff3ef48d7c01290bb";
    		String Access_token_secret2="245e77bcebc53225ce9a492338264e81";
			weibo.setToken(Access_token,Access_token_secret);
			 List<Status> statuses =weibo.getPublicTimeline();
			for (Status status : statuses) {
	            System.out.println(status.getUser().getName() + ":" +
	                               status.getText() + ":" +
	                               status.getCreatedAt());
	        }
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
