package november.second;

import java.lang.reflect.Method;

import lab.sjtu.LDA;
import edu.sjtu.cn.mylda.ToLDAFormat;

public class WrapLDA {
	public static String path = "experiment\\sandBox\\";
//	public static String path = 
//		"experiment\\november.second\\garbage collection\\";
	public static void main(String[]args){
		try{
//			String fileName = "invisableLDAFile_2.txt";
//			String tableName = "lishuangjiang";
			String weiboFile =  "FileForLDA_lishuangjiang.txt";
			String testFile = "mytest.txt";
//			String weiboFile =  "mytest.txt";
//			String weiboFile = "invisableLDAFile_2.txt";
//			String LDAFile = path + fileName;
			
			int wordLength = 0;//words with length no larger wordLength will be abandoned 
//			when transfered to LDA format e.g set to 1 means only keep words with length 2 or more.
			
//			//first of all, get all weibo from database with no retweet information
//			FileProcess.writeAllWeiboToFileWithoutRetweet(tableName, weiboFile );
			
//			//then, transfer the weibo file to LDA format
//			ToLDAFormat.changeToLDAFormat(weiboFile, LDAFile, wordLength);
//			
			String alpha = "0.5";
			String ntopics = "3";
			String niters = "1000";
			String savestep = "500";
			String twords = "50";
			String dir = path;
//			String[] arg = new String[]{"-est", "-dfile", weiboFile, "-alpha", alpha, 
//					"-ntopics", ntopics, "-niters", niters, "-savestep", savestep, "-twords", twords
//					, "-dir", dir};
			String[] arg = new String[]{"-inf", "-dfile", testFile, "-model", "model-final"
					, "-dir", dir};
			//using java reflection to invoke LDA method
			Method lda = LDA.class.getMethod("main", new Class[]{String[].class});
			lda.invoke(null, new Object[]{arg});
//			System.out.println(LDAFile);
//			System.out.println(weiboFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
