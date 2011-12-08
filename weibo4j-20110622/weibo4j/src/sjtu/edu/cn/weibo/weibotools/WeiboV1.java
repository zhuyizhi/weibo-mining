package sjtu.edu.cn.weibo.weibotools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WeiboV1 {
	public static String sinaURL_1="http://sinaurl.cn/";
	public static String sinaURL_2="http://t.cn/";
	public static String reg="("+sinaURL_1+"|"+sinaURL_2+"){1}[\\w*]+";
	public static Pattern pattern=Pattern.compile("("+sinaURL_1+"|"+sinaURL_2+"){1}[\\w*]+");
	public static String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	public static String[] monNum={"01","02","03","04","05","06","07","08","09","10","11","12"};
	public static HashMap<String,Integer> monthMap=new HashMap();
	
	public WeiboV1(){
		for(int i=0;i<12;i++)
		{
			monthMap.put(months[i], i+1);
		}
		System.out.println("WeiboV1 initiated");
	}
//	public static Pattern pattern2 = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
	public static boolean hasURL(String str){
		if(str.contains(sinaURL_1)||str.contains(sinaURL_2))
			return true;
		else
			return false;
	}
	public static String[] getURL(String str){
		Matcher matcher=pattern.matcher(str);
		StringBuffer sb=new StringBuffer();
		int counter=0;
		while(matcher.find())
		{
			sb.append(matcher.group());
			sb.append(" ");
			counter++;
		}
		if(counter==0)
			return null;
		return sb.toString().split(" ");
	}
	public static String filterURL(String str){
		return str.replaceAll(reg, "");
	}
	
	/**
	 * filter out string that surrounds with hashtags
	 * @param str
	 * @return
	 */
	public static String filterHashTag(String str){
		return str.replaceAll("\\#\\S+\\#", "");
	}
	/**
	 * get the number of topics in the tweet
	 * @param str
	 * @return
	 */
	public static int countHashTag(String str){
		String replacement = "  my-special-zhanweifu-haha  ";
		String newStr = str.replaceAll("\\#\\S+?\\#", replacement);
//		System.out.println(newStr);
		int counter = 0;
		int fromIndex = 0;
		while((fromIndex = newStr.indexOf(replacement, fromIndex) )!= -1)
		{
			counter++;
			fromIndex++;
		}
		return counter;
	}
	
	/**
	 * filter blank token like ' /x' . this one is used on the processed text
	 * @param str
	 * @return
	 */
	public static String filterBlank(String str){
		if(str != null)
		{
//			StringBuilder sb = new StringBuilder();
//			String[] tokens = str.split(" ");
//			for(String token:tokens){
//				if(token.ind)
//			}
			return str.replaceAll("/\\S+?", "").replaceAll("/\\S+?", "");
		}
		return str;
	}
	/**
	 * filter the "@" mark and the username after that
	 * @param str
	 * @return
	 */
	public static String filterAt(String str){
		return str.replaceAll("@\\S+", "");
	}
	/**
	 * act on a single token
	 * @param token
	 * @return
	 */
	public static String filterPOS(String token){
		if(token.indexOf("/") != -1)
			token = token.substring(0, token.indexOf("/"));
		return token;
	}
	
	public static List<String> getStopWordList(){
		List<String> stopWordList = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String word;
			while((word = br.readLine()) != null){
				stopWordList.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stopWordList;
	}
	
	public static HashSet<String> getStopWordSet(){
		HashSet<String> stopWordSet = new HashSet<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String word;
			while((word = br.readLine()) != null){
				stopWordSet.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stopWordSet;
	}
	
	public static HashSet<String> getStopWordSet(String filePath){
		HashSet<String> stopWordSet = new HashSet<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
			String word;
			while((word = br.readLine()) != null){
				stopWordSet.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stopWordSet;
	}
/**
 * 
 * @param date
 * @return Calendar形式的日期信息。感觉有误
 */
	public  Calendar processDate(String date){
		Calendar cal=Calendar.getInstance();
		String[] times=date.split(" ");
		for(String str:times)
			System.out.println(str);
		String[] hours=times[3].split(":");
//		for(String str:hours)
//			System.out.println(str);
		//Mon Apr 04 20:41:56 CST 2011
		int year=Integer.parseInt(times[5]);
		int mon=monthMap.get(times[1]).intValue() ;
		int day=Integer.parseInt(times[2]);
		int hour=Integer.parseInt(hours[0]);
		int min=Integer.parseInt(hours[1]);
		int sec=Integer.parseInt(hours[2]);
		cal.set(year,mon,day,hour,min,sec);
		return cal;
	}
	/**
	 * 
	 * @param date
	 * @return int形式的日期，例如20110404
	 */
	public  int processDateInt(String date){
		String[] times=date.split(" ");
		int year=Integer.parseInt(times[5]);
		int mon=monthMap.get(times[1]).intValue() ;
		StringBuilder sb=new StringBuilder();
		sb.append(year);
		sb.append(monNum[mon-1]);
		sb.append(times[2]);
		return Integer.parseInt(sb.toString());
	}

	public static void testFunction2()throws Exception{
		String s="测试微博http://sinaurl.cn/hMAnw，http://t.cn/hMAnw";
		String[] urls=getURL(s);
		for(String url:urls)
			System.out.println(url);
		System.out.println(filterURL(s));
	}
	public static void testProcessDate(String date){
		WeiboV1 wv1=new WeiboV1();
		Calendar cal=wv1.processDate(date);
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		Date dat=cal.getTime();
		System.out.println(dat.getYear()+" "+dat.getMonth()+" "+dat.getDay()+" "+dat.getHours());
		System.out.println(cal.getTime().getTime());
		System.out.println(wv1.processDateInt(date));
	}
	/**
	 * wrote in 2011-9-12. filter out the retweet text in microblog, only keep original text created
	 * by this user 
	 * @param str
	 * @return
	 */
	public static String filterRetweet(String str){
		String pattern = "//@";
		int pos = str.indexOf(pattern);
		
		if(pos != -1)
			return str.substring(0, pos);
		else
			return str;
	}
	
	public static void main(String[] args)throws Exception{
//		testFunction2();
//		testProcessDate("Mon Apr 04 20:41:56 CST 2011");
//		System.out.println(filterHashTag("这是一个包含#hashtag#的"));
//		System.out.println(filterHashTag("这是一个包含#李阳#的"));
//		System.out.println(filterHashTag("这是一个不包含hashtag的"));
//		System.out.println(filterAt("这是原文//@转发"));
//		System.out.println(countHashTag("：：：#世预赛##感念师恩##李双江之子打人之案和解#互粉啊"));
		System.out.println(filterBlank("/x /x /x /x /x 【/w 微/g 博/g 动态/n 】/w 李/nr 双江/ns 之/u 子/g 打/v 人/n 案/x 参与者/n 苏/b 楠/n 身份/n 再/d 引/v 质疑/v 。/w 此前/t 媒体/n 报道/v ：/w 北京市/ns 公安局/n 表示/v ，/w 肇事/vn 车主/n 9月/t 6日/t 晚/tg 已/d 承认/v 为/p 减轻/v 处理/v 、/w 逃避/v 处罚/vn ，/w 编造/v 了/u 是/v 苏浩/nr 同志/n 亲属/n 的/u 理由/n 。/w 经/p 核查/vn ，/w 苏浩/nr 同志/n 与/cc 肇事/vn 车主/n 没有/v 任何/r 关系/n 。/w 奥迪车/n 悬挂/v 车牌号/n 所有/b 人/n 为/p 山西省/ns 劳动/vn 教养/n 工作/vn 管理局/n ，/w 苏/b 所/u 使用/v 的/u 是/v 伪造/v 牌照/n 。/w "));
	}
}
