package experiment.one.preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.aliasi.classify.BinaryLMClassifier;
import com.aliasi.classify.JointClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

public class GarbageClassifier {
	
//	public static File gFile = new File("garbage.txt");
//	public static String CATEGORY = "sugar";
	public static int NGRAM_SIZE = 1;
	
	public static void trainModel(String sourceFile, String modelFile){
		try{
			BinaryLMClassifier classifier = 
				new BinaryLMClassifier(new NGramProcessLM(NGRAM_SIZE), 1);
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				classifier.train("garbage", tempStr, 0);
			}
			
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(modelFile));
			classifier.compileTo(os);
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean testModel(String modelFile, String testStr){
		try{
			ObjectInputStream oi = new ObjectInputStream
			(new FileInputStream(modelFile));
			LMClassifier c = (LMClassifier) oi.readObject();
			oi.close();
			
			JointClassification jc = c.classifyJoint
			(testStr.toCharArray(), 0, testStr.length());
			System.out.println(jc.score(0));
			if(jc.score(0) < -2.5)
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static void main(String[] args) throws Exception{
		String path = 
			"F:/programs/from lab/weibo4j-20110622/weibo4j/experiment/november.second/garbage collection/";
		trainModel(path + "manual_garbage.txt", path + "classifier.model");
//		String str1 = "姜府呀局李双江之子打人毙叮统情人节康 我要减肥了起拘票喜";
		String str1 = "给大家推荐一个热门话题！如果你也有兴趣，快来和我一起聊吧";
//		String str2 = "李双江之子打人案同犯父亲因伪造车牌被拘   现在已经把矛头对象套牌车，李将军真是厉害啊！";
//		String str3 = " 涪陵/ns 长江/ns 后浪/n 前浪/n 后浪/n 大浪淘沙/i 滩滩/ns ";
//		String str4 = "给/p 大家/r 推荐/v 一个/mq 热门/a 话题/n ！/w 如果/c 你/r 也/d 有/v 兴趣/n ，/w 快/d 来/v 和/cc 我/r 一起/d 聊/v 吧/y ！/w ";
		String str2 = "要想查找数字，字母或数字，空白是很简单的，因为已经有了对应这些字符集合的元字符，但是如果你想匹配没有预定义元字符的字符集合(比如元音字母a,e,i,o,u),应该怎么办？";
		System.out.println(testModel(path + "classifier.model", str1));
		System.out.println(testModel(path + "classifier.model", str2));
//		System.out.println(testModel(path + "classifier.model", str3));
//		System.out.println(testModel(path + "classifier.model", str4));
	}
}
