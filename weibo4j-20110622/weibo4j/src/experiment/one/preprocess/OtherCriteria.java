package experiment.one.preprocess;

import experiment.util.WeiboV1;

public class OtherCriteria {
	public static int MINIMUM_WORD = 3;
	public static int MINIMUM_FOLLOWER = 3;
	public static int MINIMUM_MICROBLOG = 5; //作者的微博数量小于这个个数或者粉丝数少于上面的数的，删掉
	public static int MINIMUM_HASHTAG = 2; //含有的话题个数大于这个个数的，删掉
	
	public static boolean ruledByOtherCriteria(String rawText, int follower, int tweetNum){
		if(follower < MINIMUM_FOLLOWER)
			return true;
		if(tweetNum < MINIMUM_MICROBLOG)
			return true;
		String cleanText = WeiboV1.filterAt(WeiboV1.filterHashTag
				(WeiboV1.filterURL(WeiboV1.filterRetweet(rawText))));
		if(cleanText.length() < MINIMUM_WORD)
			return true;
		
		if(WeiboV1.countHashTag(rawText) > MINIMUM_HASHTAG)
			return true;
		
		return false;
	}
	

}

//同时含有多于一个流行话题的也去除