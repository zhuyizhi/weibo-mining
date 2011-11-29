package extraction.target.toys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeSet;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;

public class GetRankedNouns {
	public static void getRankedNouns(String sourceNEs, String sourceLDA, 
			String destFile, String tableFreq, String tableBackG, double logarithm){
		try{
			TreeSet<TokenPair> ts = new TreeSet<TokenPair>();
			BufferedReader br=new BufferedReader(new InputStreamReader
					(new FileInputStream(sourceNEs), "UTF-8"));
			BufferedReader br2=new BufferedReader(new InputStreamReader
					(new FileInputStream(sourceLDA), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(destFile), "UTF-8"));
			
			System.out.println("get other nes...");
			String tempStr;
			while((tempStr = br.readLine()) != null){
				String[] strs = tempStr.split(" ");
				TokenPair tp =new TokenPair(strs[0], Integer.parseInt(strs[1]));
				ts.add(tp);
			}
			br.close();
			
			System.out.println("get LDA nouns...");
			SinaWeiboHelper helper = new SinaWeiboHelper();
			helper.checkConnection();
			PreparedStatement ps = helper.con.prepareStatement("select count from " + tableFreq + 
					" where token = ? ");
			while((tempStr = br2.readLine()) != null){
				ps.setString(1, tempStr);
				ResultSet rs = ps.executeQuery();
				int freq = 0;
				if(rs.next())
					freq = rs.getInt("count");
				TokenPair tp =new TokenPair(tempStr, freq);
				ts.add(tp);
				rs.close();
			}
			br2.close();
			ps.close();
			
	        System.out.println("get dfs from background...");
		    String userName = "sa";
		    String password = "69015870";
		    String conURL="jdbc:sqlserver://192.168.1.105:1433;DatabaseName=wordMap;";
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		    java.sql.Connection con = java.sql.DriverManager.getConnection(conURL,userName,password);
		    if(con != null) System.out.println("connected successfully");
		    PreparedStatement ps2 = 
			 con.prepareStatement("select df from " + tableBackG + " where word = ?");
		    
		    for(TokenPair tp:ts){
		    	ps2.setString(1, tp.token);
		    	ResultSet rs = ps2.executeQuery();
		    	int df = 0;
		    	if(rs.next()){
		    		df = rs.getInt("df");
		    	}
		    	tp.score = scoreFunction(tp.score, df, logarithm);
		    }
			
		    System.out.println("writing into file...");
		    for(TokenPair tp:ts){
		    	pw.println(tp.token + " : " + tp.score);
		    }
		    pw.flush();
		    pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static double scoreFunction(double freq, int df, double para){
		double loge2 = Math.log(para);
		double logs = ((Math.log(df + 1))/loge2);
//		double logs = df + 1;
		return freq/( logs  + 0.5);
	}
	public static void main(String[]args){
		getRankedNouns("laoren1\\otherNEs.txt", "laoren1\\LDAnouns.txt", "laoren1\\finalResult.txt", 
				"laoren_wordfrequency", "wordListLarge", 1.2);
	}
}
