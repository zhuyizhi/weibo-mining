package sjtu.edu.cn.trend.collection;

public class KeywordController {
	
	private static final String keywordTable = "话题词表";
	private static final SinaWeiboHelper helper = new SinaWeiboHelper();
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
	
	public static void main(String[]args){
		KeywordController kc = new KeywordController();
//		kc.addKeyword("老人跌倒");
//		kc.addKeyword("家暴");
//		kc.addKeyword("李双江之子打人");
//		kc.addKeyword("射洪黑熊失踪");
//		kc.addKeyword("江西湖北交界地震");
//		kc.addKeyword("邵阳沉船");
//		kc.addKeyword("拒载");
//		kc.addKeyword("早产儿被当死婴丢厕所");
		kc.addKeyword("NBA停摆结束");
	}
}
