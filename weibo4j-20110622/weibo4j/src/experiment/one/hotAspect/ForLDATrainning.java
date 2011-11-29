package experiment.one.hotAspect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import sjtu.edu.cn.weibo.weibotools.WeiboV1;

public class ForLDATrainning {
	public static String path = 
		"E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/hotAspect/";
	
	public HashSet<String> stopWordSet = new HashSet<String>();
	public ForLDATrainning(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("stopwords.txt")));
			String word;
			while((word = br.readLine()) != null){
				stopWordSet.add(word);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void prepareLDAFile(String tableName, String LDAFile){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(LDAFile), "gb2312"));
			
			int counter  = 0;
			String sql = "select parsedResultNoStopWord from " + tableName +
			" where isRuled = 0 and isGarbage = 0";
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int total = 0;
			while(rs.next()){
				String text = rs.getString("parsedResultNoStopWord");
				if((text = WeiboV1.filterBlank(text) )!= null)
				{
					pw.println(text);
					System.out.println(++total);
				}
			}
			st.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		prepareLDAFile("lishuangjiang", path + "lishuangjiang_LDAFile.csv");
	}
	
}
