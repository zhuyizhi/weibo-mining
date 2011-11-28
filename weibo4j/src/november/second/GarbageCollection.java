package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import lab.sjtu.LDA;

/**
 * Get rid of the garbage microblog from the collection.
 * Conducted by the following procedure:
 * 1. use the lda model to train a 2-topic topic model
 * 2. then manually find the one that summarize garbage microblog
 * 3. cluster by the topic model, classify the microblog by a threshold.
 * 
 * @author ucai
 *
 */
public class GarbageCollection {
	public static Double threshold = 0.7;
	public static String path = "experiment\\november.second\\garbage collection\\";
	public static String topicFile = path + "TopicFile.txt";
	public static String garbageFile = path + "garbageFile.txt";
	/**
	 * 1.first step, train a 2-topic LDA model, start form the file specified by the parameter
	 * 
	 * the source code is copied from the class LDAWithNoRetweet
	 * @param sourceFile path of the file used to train model
	 */
	public static void trainLDAModel(String sourceFile){
		try{
			String alpha = "0.5";
			String ntopics = "5";//临时改成5了
			String niters = "1000";
			String savestep = niters;
			String twords = "100";
			String dir = path;
			
			String[] arg = new String[]{"-est", "-dfile", sourceFile, "-alpha", alpha, 
					"-ntopics", ntopics, "-niters", niters, "-savestep", savestep, "-twords", twords
					, "-dir", dir};
			//using java reflection to invoke LDA method
			Method lda = LDA.class.getMethod("main", new Class[]{String[].class});
			lda.invoke(null, new Object[]{arg});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void classifiedByLDA(String sourceFile, int garbageTopicIndex){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream(path + sourceFile), "UTF-8"));
			BufferedReader brTheta = new BufferedReader
			(new InputStreamReader(new FileInputStream(path  + "model-final.theta"), "UTF-8"));
			
			PrintWriter pwTopic = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(topicFile), "UTF-8"));
			PrintWriter pwGarbage = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(garbageFile), "UTF-8"));
			//skip over the first line of the LDA file, since this line only contains a total number 
			brSource.readLine();
			
			String tweet, assignment;
			int onTopic =0, offTopic = 0;
			while((tweet = brSource.readLine()) != null){
				assignment = brTheta.readLine();
				String[] asss = assignment.split(" ");
				if(Double.parseDouble(asss[garbageTopicIndex]) > threshold)
				{
					pwGarbage.println(tweet);
					offTopic++;
				}
				else
				{
					pwTopic.println(tweet);
					onTopic++;
				}
			}
			
			pwGarbage.println(offTopic);
			pwTopic.println(onTopic);
			
			pwGarbage.close();
			pwTopic.close();
			brSource.close();
			brTheta.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[]args){
		try{
//			trainLDAModel("LDAFile.txt");
//			classifiedByLDA("LDAFile.txt", 0);
//			trainLDAModel("liyang_noRetweet_LDA_multilple_duplicate.txt");
//			classifiedByLDA("liyang_noRetweet_LDA_multilple_duplicate.txt", 1);
//			trainLDAModel("girl.txt");
//			classifiedByLDA("girl.txt", 4);
			classifiedByLDA("FileForLDA_lishuangjiang.txt", 0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
