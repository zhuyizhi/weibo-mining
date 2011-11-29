package mytest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.*;
import weibo4j.http.*;
import weibo4j.util.BareBonesBrowserLaunch;

public class OauthTest {
	public static void main(String[]args){
		try{
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	        
	           Weibo weibo = new Weibo();
	           // set callback url, desktop app please set to null
	           // http://callback_url?oauth_token=xxx&oauth_verifier=xxx
	           //1。根据app key第三方应用向新浪获取requestToken
	           RequestToken requestToken = weibo.getOAuthRequestToken();
	           
	           System.out.println("1.......Got request token成功");
	           System.out.println("Request token: "+ requestToken.getToken());
	           System.out.println("Request token secret: "+ requestToken.getTokenSecret());
	           AccessToken accessToken = null;
	          //2。用户从新浪获取verifier_code 如果是Android或Iphone应用可以callback =json&userId=xxs&password=XXX
	           System.out.println("Open the following URL and grant access to your account:");
	           System.out.println(requestToken.getAuthorizationURL());
	           BareBonesBrowserLaunch.openURL(requestToken.getAuthorizationURL());
	          //3。用户输入验证码授权信任第三方应用
	           BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	           while (null == accessToken) {
	               System.out.print("Hit enter when it's done.[Enter]:");
	               String pin = br.readLine();
	               System.out.println("pin: " + br.toString());
	               try{
	                //4。通过传递requestToken和用户验证码获取AccessToken
	                   accessToken = requestToken.getAccessToken(pin);
	               } catch (WeiboException te) {
	                   if(401 == te.getStatusCode()){
	                       System.out.println("Unable to get the access token.");
	                   }else{
	                       te.printStackTrace();
	                   }
	               }
	           }
	           
	           System.out.println("Got access token.");
	           System.out.println("Access token: "+ accessToken.getToken());
	           System.out.println("Access token secret: "+ accessToken.getTokenSecret());
	          //使用AccessToken来操作用户的所有接口
//	          Weibo weibo=new Weibo();
	           weibo = new Weibo();

	          // 以后就可以用下面accessToken访问用户的资料了
	  weibo.setToken(accessToken.getToken(), accessToken.getTokenSecret());
	           //发布微博
	  Status status = weibo.updateStatus("test message6 ");
	  System.out.println("Successfully updated the status to ["
	    + status.getText() + "].");
	           
	           try {
	   Thread.sleep(3000);
	  } catch (InterruptedException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	           System.exit(0);
		}catch (WeiboException te) {
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit( -1);
       } catch (IOException ioe) {
           System.out.println("Failed to read the system input.");
           System.exit( -1);
       }
	}
}
