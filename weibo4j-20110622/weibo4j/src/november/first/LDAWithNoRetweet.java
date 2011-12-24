package november.first;

import java.lang.reflect.Method;

import lab.sjtu.LDA;
import edu.sjtu.cn.mylda.ToLDAFormat;
import edu.sjtu.cn.mylda.ToLDAFormat;
import sjtu.edu.cn.weibo.weibotools.FileProcess;

/**
 * 删除所有与转发有关的信息做LDA
 * @author ucai
 *
 */
public class LDAWithNoRetweet {
	public static String path = "experiment\\11.2\\WithNoRetweet\\";
	public static void main(String[]args){
		try{
			String tableName = "lishuangjiang";
			String weiboFile = path + "weibos.txt";
			String LDAFile = path + "LDAFile.txt";
			
			int wordLength = 0;//words with length no larger wordLength will be abandoned 
//			when transfered to LDA format e.g set to 1 means only keep words with length 2 or more.
			
//			//first of all, get all weibo from database with no retweet information
//			FileProcess.writeAllWeiboToFileWithoutRetweet(tableName, weiboFile );
			
//			//then, transfer the weibo file to LDA format
//			ToLDAFormat.changeToLDAFormat(weiboFile, LDAFile, wordLength);
//			
			String alpha = "0.5";
			String ntopics = "100";
			String niters = "2000";
			String savestep = niters;
			String twords = "200";
			String dir = path;
			String[] arg = new String[]{"-est", "-dfile", "LDAFile.txt", "-alpha", alpha, 
					"-ntopics", ntopics, "-niters", niters, "-savestep", savestep, "-twords", twords
					, "-dir", dir};
			//using java reflection to invoke LDA method
			Method lda = LDA.class.getMethod("main", new Class[]{String[].class});
			lda.invoke(null, new Object[]{arg});
//			System.out.println(LDAFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
