package experiment.one.preprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;

import experiment.util.WeiboV1;

import ICTCLAS.I3S.AC.ICTCLAS50;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;

public class PrepareGarbageFile {
	public ICTCLAS50 ic50;
	public void init() {
		try{
			ic50 = new ICTCLAS50();
			String argu = ".";
			//初始化
			if (ic50.ICTCLAS_Init(argu.getBytes("UTF-8")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			ic50.ICTCLAS_SetPOSmap(2);
		}catch(Exception e){
			e.printStackTrace();
		}



		
		
	}
	public void changeToLDAFormat_ICTCLAS(String sourceFile, String destFile) {
		try{
				BufferedReader br=new BufferedReader
				(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
				LinkedList<String> docs=new LinkedList<String>();
				String tempDoc;
				int num=0;
				while((tempDoc=br.readLine())!=null){
					if(tempDoc.length() > 0 && !RuleBase.matchAnyRule(tempDoc)){
						docs.add(tempDoc);
						System.out.println(++num);
					}
				}		
				System.out.println("size : "+docs.size());
				StringBuffer sb=new StringBuffer();
				PrintWriter pw = new 
				PrintWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
				int counter=0;
				
				pw.println(docs.size());
				for(String str:docs){
					str =  WeiboV1.filterAt(WeiboV1.filterHashTag(WeiboV1.filterURL(str)));
					byte[] bts = ic50.ICTCLAS_ParagraphProcess(str.getBytes("UTF-8"), 0, 1);
					String processText = new String(bts, 0, bts.length, "UTF-8");
//					System.out.println(processText);
					pw.println(processText);
					counter++;
					if((counter % 5000) == 0)
						System.out.println(counter);
				}
				pw.flush();
				pw.close();
				System.out.println("size : "+docs.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getNounRuledFromDatabase(String tableName, String destFile)
	throws Exception{
		SinaWeiboHelper helper = new SinaWeiboHelper();
		helper.checkConnection();
		String sql = "select parsedResultNoStopWord from " + tableName + 
		" where isRuled = 0 and isGarbage = 0";
		Statement st = helper.con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		PrintWriter pw = new 
		PrintWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
		int total = 0;
		while(rs.next()){
			String text = rs.getString("ParsedResultNoStopWord");
			pw.println(text);
		}
		st.close();
		pw.close();
	}
	
	
	public static void main(String[] args)throws Exception{
//		PrepareGarbageFile pgf = new PrepareGarbageFile();
//		pgf.init();
		String path = 
			"E:/programs/weibo4j-20110622/weibo4j/experiment/november.second/garbage collection/";
//		pgf.changeToLDAFormat_ICTCLAS(path + "lishuangjiang_origin.txt", path + "FileForLDA_lishuangjiang.txt");
		
		
		getNounRuledFromDatabase("rolled_girl_copy", path + "rolled_girl_gcRawFile.txt");
		//		try{
//			String file = path + "FileForLDA_lishuangjiang.txt";
//			BufferedReader br=new BufferedReader
//			(new InputStreamReader(new FileInputStream(file), "gb2312"));
//			PrintWriter pw = new 
//			PrintWriter(new OutputStreamWriter(new FileOutputStream(path + "utf-8File.txt"), "UTF-8"));
//			String tempStr;
//			while((tempStr = br.readLine()) != null){
////				System.out.println(tempStr);
//				pw.println(new String(tempStr.getBytes("gb2312"), 0, tempStr.getBytes("gb2312").length, "UTF-8"));
//			}
//			pw.close();
//		}catch(Exception e){
//			
//		}
	
	}
}
