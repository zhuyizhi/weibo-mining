package experiment.one.sentiment.expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;
import sjtu.edu.cn.weibo.weibotools.WeiboV1;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class PrepareForGeneration {
	public static String path = "E:/programs/weibo4j-20110622/weibo4j" +
			"/experiment/real_one/sentiment/expression/";
	public static String sentiDic = "E:/programs/weibo4j-20110622/weibo4j/sentidics/" +
	"HowNet_sentiDic_small_ForSplit.txt";
	ICTCLAS50 ic = new ICTCLAS50();
	public PrepareForGeneration(){
		try{
			ic.ICTCLAS_Init(".".getBytes("UTF-8"));
			ic.ICTCLAS_ImportUserDictFile
			(sentiDic.getBytes("UTF-8"), ICTCLAS50.eCodeType.CODE_TYPE_UTF8.ordinal());
			ic.ICTCLAS_SaveTheUsrDic();
			ic.ICTCLAS_SetPOSmap(2);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String split(String rStr){
		try{
			byte[] bts = ic.ICTCLAS_ParagraphProcess(rStr.getBytes("UTF-8"), 
					ICTCLAS50.eCodeType.CODE_TYPE_UTF8.ordinal(), 1);
			return new String(bts, 0 , bts.length, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void getSubjectiveWindows(String tableName, String resultFile){
		try{
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			String sql = "select text from " + tableName 
			+ " where isGarbage = 0 and isRuled = 0";
			Statement st = helper.con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			System.out.println("get" );
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"));
			Set<String> sset = WeiboV1.getStopWordSet();
			int counter = 0;
			while(rs.next()){
				System.out.println(counter ++ );
				String tempStr = rs.getString("text");
				tempStr = WeiboV1.filterAt(
						WeiboV1.filterURL(WeiboV1.filterHashTag(tempStr)));
				String sStr = split(tempStr);
				if(sStr.contains("/sent"))
				{
					String[] tokens = sStr.split(" ");
					boolean[] flags = new boolean[tokens.length];
					for(int i = 0; i < tokens.length; i++){
						if(tokens[i].contains("/sent"))
						{
							flags[i] = true;
							if(i - 1 >= 0)
								flags[i - 1] = true;
							if(i - 2 >= 0)
								flags[i - 2] = true;
							if(i + 1 < tokens.length)
								flags[i + 1] = true;
							if(i + 2 < tokens.length)
								flags[i + 2] = true;
						}
					}
					
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < flags.length; i++){
						if(flags[i] && !tokens[i].contains("/w") 
								&& !sset.contains(offPOS(tokens[i])))
							sb.append(offPOS(tokens[i]) + " ");
					}
					pw.println(sb.toString());
				}
			}
			
			pw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)throws Exception{
		PrepareForGeneration pg = new PrepareForGeneration();
		pg.getSubjectiveWindows("lishuangjiang", path + "subTrainning.txt");
//		System.out.println(pg.split("这是板板六十四"));
		pg.ic.ICTCLAS_Exit();
	}
	
	public static String offPOS(String token){
		if(token.indexOf("/") != -1)
			return token.substring(0, token.indexOf("/"));
		else
			return token;
	}
}
