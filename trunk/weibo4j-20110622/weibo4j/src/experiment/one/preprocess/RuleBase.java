package experiment.one.preprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RuleBase {
	public static String[] rules = new String[]{
		".+（\\s*分享自 .*）\\s*",                                        //消除分享新闻的微博
		".+（\\s*来自.*）\\s*",
		"^发表了一篇.+",													//去掉发表博客的通知信息
		"^发表了博文.+",
		"^我.*投票.+",													//去掉投票的
		"^分享一个投票.+",
		"^这个投票不错.+",
		"^转载.*《.+》.+",												//去掉转载的
		"^[转载].+",			
		"^//.+"	,														//去掉纯转发的
		"^【.+】$",
		"\\s*",															//去掉只含空格符的
		".*包邮.*",														//去掉淘宝信息
		".*淘宝.*",
		".*Tmall.*"
	};
	
	public static void ruleBasedProcess(String sourceFile, String destFile){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
			
			int[] hit = new int[rules.length];
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				boolean flag = true;
				for(int i = 0; i < rules.length; i++){
					if(tempStr.matches(rules[i]))
					{
						flag = false;
						hit[i]++;
						break;
					}
				}
				if(flag)
					pw.println(tempStr);
			}
			pw.close();
			
			int total = 0;
			for(int i = 0; i < rules.length; i++){
				System.out.println(rules[i] + "   hits:" + hit[i]);
				total += hit[i];
			}
			System.out.println(total);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean matchAnyRule(String str){
		for(String rule:rules){
			if(str.matches(rule))
				return true;
		}
		return false;
	}
	public static boolean testRules(String str, String pattern){
		if(str.matches(pattern))
			return true;
		else
			return false;
	}
	
	public static void main(String[] args){
		String pattern = rules[7];
		String str = "转载《打人归来》： ——李双江 日落西山门牙飞， 儿子打人把家归把家归， 会堂的车证映鲜血， 哀嚎的哭声满天飞， 哭声飞到天上去， 李双江听了心欢喜， 夸咱们儿子干的好， 夸少爷打法属第一， mi sao la mi sao， la sao mi dao ruai， 夸少爷打法属第一";
		System.out.println(testRules(str, pattern));
		
		ruleBasedProcess("lishuangjiang_origin.txt", "process.txt");
	}
}
