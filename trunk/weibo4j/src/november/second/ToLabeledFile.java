package november.second;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import experiment.one.sentiment.SentiTools;

public class ToLabeledFile {
	public static Hashtable<String, HashSet<String>> labelTable = new Hashtable<String, HashSet<String>>();
//	public static String[] labels = new String[]{
//		"李双江/nh", "李刚/nh", "李天一/nh",  "苏楠/nh", "社会/n", "真理/n","李阳/nh","彭先生/nh"};
//	public static String[][] labelNames = new String[][]{
//		{"李双江/nh", "爹/n", "爸/n", "双江/ns", "父/n","老子/n"},
//		{"李刚/nh"},
//		{"李天一/nh", "子/n", "儿子/n", "孩子/n"},
//		{"苏楠/nh","苏浩/nh"},
//		{"社会/n"},
//		{"真理/n"},
//		{"李阳/nh"},
//		{"彭先生/nh", "彭氏/nh"}
//	};
	
//	public static String[] labels = new String[]{
//		"李双江/nh", "李天一/nh"};
//	public static String[][] labelNames = new String[][]{
//		{"李双江/nh", "爹/n", "爸/n", "双江/ns", "父/n","老子/n"},
//		{"李天一/nh", "子/n", "儿子/n", "孩子/n"},
//	};
	
	public static String[] labels = new String[]{
		"媒体", "苏楠奥迪", "儿子老子", "李阳", "犯罪道歉", 
		"名人法律", "钱家牛", "孩子父母", "家长", "李天宝马", "前程",
		"李双江", "减肥"};
	public static String[][] labelNames = new String[][]{
		{"媒体/n","牌/n"},
		{"车牌/n","司机/n","苏楠/nr","北京市/ns","警方/n","北京/ns","奥迪/nz","山西/ns"},
		{"儿子/n","老子/n"},
		{"李阳/nr","上联/n","冲锋枪/n","李刚/nr","爸/n","下联/n","横批/n","爹/n"},
		{"犯罪/vn","和解/vn","道歉/vn","教养/n","嫌疑人/n"},
		{"名人/n","法律/n","事情/n","中国/ns","教育/vn","官/n"},
		{"钱/n","家/n","牛/n"},
		{"孩子/n","父母/n","父亲/n"},
		{"夫妇/n","人/n","家长/n"},
		{"宝马/nz","车/n","责任/n","李天/nr","枪/n"},
		{"前程/n","环境/n","真理/n","根源/n","社会/n","青春/n"},
		{"事/n","李/nr","事件/n","双江/ns"},
		{"减肥/vn"}
	};
	
	
	public static String path = "experiment//november.second//Labeled LDA//";
	public static HashSet<String> nameSet = new HashSet();


	public ArrayList<String> sentimentList = new ArrayList<String>();
	
	/**
	 * initiation
	 * @param dicFile
	 */
	public ToLabeledFile(String dicFile ){
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(dicFile), "UTF-8"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				sentimentList.add(tempStr);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	{
		int i;
		for (i = 0; i < labels.length; i++){
			HashSet<String> names = new HashSet<String>();
			for(String name:labelNames[i]){
				names.add(name);
			}
			labelTable.put(labels[i], names);
		}
		
		for(i = 0; i<labels.length ; i++){
			for(String name:labelNames[i]){
				nameSet.add(name);
			}
		}
	}
	
	/**
	 * strip the known names specified in the name set
	 * @param rawStr
	 * @return
	 */
	public static String stripKnownNames(String rawStr){
		try{
			for(String str:nameSet){
				rawStr = rawStr.replaceAll(str, "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rawStr;
	}
	
	public static String keepOnlyAdjVL(String rawStr){
		String[] strs = rawStr.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String str:strs){
			if(str.indexOf("/") != -1)
				if(SentiTools.isADJ(str) || SentiTools.isV(str, true) || SentiTools.isL(str))
				{
					if(sb.length() > 0)
						sb.append(" " + str);
					else
						sb.append(str);
				}
		}
		return sb.toString();
	}
	
	public String keepOnlyDicWords(String rawStr){
		String[] strs = rawStr.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String str:strs){
			if(str.indexOf("/") != -1)
				if(SentiTools.isADJ(str) || SentiTools.isV(str, true) || SentiTools.isL(str))
					if(this.sentimentList.contains(str.substring(0, str.indexOf("/"))))
					{
						if(sb.length() > 0)
							sb.append(" " + str);
						else
							sb.append(str);
					}
		}
		return sb.toString();
	}
	
	/**
	 * get training data
	 * @param sourceFile
	 * @param resultFile
	 * @param latentTopicNumber
	 */
	public static void toLabeledFile(String sourceFile, String resultFile, int latentTopicNumber){
		try{
			String fileName = sourceFile;
			String outputFile = resultFile;
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "gb2312"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			
			//construct an array of latent topic labels
			ArrayList<String> labelList = new ArrayList<String>();
			for(int i=0; i < latentTopicNumber; i++){
				labelList.add("topic_" + i);
			}
			
			StringBuilder latentSB =new StringBuilder();
			for(int i=0; i < latentTopicNumber; i++){
				if(latentSB.length() > 0)
					latentSB.append(" topic_" + i);
				else
					latentSB.append("topic_" + i);
			}
			
			while((tempStr = br.readLine()) != null){
					StringBuffer sb = new StringBuffer();
//					sb.append(latentSB);
					for(String str : labels){
						for(String name:labelTable.get(str))
						{
							if(tempStr.contains(name))
							{
								if(sb.length() > 0)
								{
									sb.append(" ");
								}
								sb.append(str);
								break;
							}
						}
					}
					
					if(sb.length() > 0){
						tempStr = stripKnownNames(tempStr);//改动
						String resStr;
						if(latentSB.length() > 0)
							resStr = latentSB.toString() + " " + sb.toString() + "," + tempStr;
						else
							resStr = sb.toString() + "," + tempStr;
						pw.println(resStr);
					}

			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getTestData(String sourceFile, String resultFile){
		try{
			String fileName = sourceFile;
			String outputFile = resultFile;
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			
			String tempStr;
			while((tempStr = br.readLine()) != null){
				boolean flag = false;
				for(String str : labels){
					for(String name:labelTable.get(str))
					{
						if(tempStr.contains(name))
						{
							flag = true;
							break;
						}
					}
				}
				if(flag == false){
					pw.println(tempStr);
				}
			}
			
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toLabeledFileOnlyAdjVL(String sourceFile, String resultFile, int latentTopicNumber){
		try{
			String fileName = sourceFile;
			String outputFile = resultFile;
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "gb2312"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			
			//construct an array of latent topic labels
			ArrayList<String> labelList = new ArrayList<String>();
			for(int i=0; i < latentTopicNumber; i++){
				labelList.add("topic_" + i);
			}
			
			StringBuilder latentSB =new StringBuilder();
			for(int i=0; i < latentTopicNumber; i++){
				if(latentSB.length() > 0)
					latentSB.append(" topic_" + i);
				else
					latentSB.append("topic_" + i);
			}
			
			while((tempStr = br.readLine()) != null){
					StringBuffer sb = new StringBuffer();
//					sb.append(latentSB);
					for(String str : labels){
						for(String name:labelTable.get(str))
						{
							if(tempStr.contains(name))
							{
								if(sb.length() > 0)
								{
									sb.append(" ");
								}
								sb.append(str);
								break;
							}
						}
					}
					
					if(sb.length() > 0){
						tempStr = keepOnlyAdjVL(stripKnownNames(tempStr));
						if(tempStr.length() > 0)
						{
							String resStr;
							if(latentSB.length() > 0)
								resStr = latentSB.toString() + " " + sb.toString() + "," + tempStr;
							else
								resStr = sb.toString() + "," + tempStr;
							pw.println(resStr);
						}
					}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void toLabeledFileOnlyDictSenti(String sourceFile, String resultFile, int latentTopicNumber){
		try{
			String fileName = sourceFile;
			String outputFile = resultFile;
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "gb2312"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			
			//construct an array of latent topic labels
			ArrayList<String> labelList = new ArrayList<String>();
			for(int i=0; i < latentTopicNumber; i++){
				labelList.add("topic_" + i);
			}
			
			StringBuilder latentSB =new StringBuilder();
			for(int i=0; i < latentTopicNumber; i++){
				if(latentSB.length() > 0)
					latentSB.append(" topic_" + i);
				else
					latentSB.append("topic_" + i);
			}
			
			while((tempStr = br.readLine()) != null){
					StringBuffer sb = new StringBuffer();
//					sb.append(latentSB);
					for(String str : labels){
						for(String name:labelTable.get(str))
						{
							if(tempStr.contains(name))
							{
								if(sb.length() > 0)
								{
									sb.append(" ");
								}
								sb.append(str);
								break;
							}
						}
					}
					
					if(sb.length() > 0){
						tempStr = keepOnlyDicWords(stripKnownNames(tempStr));
						if(tempStr.length() > 0)
						{
							String resStr;
							if(latentSB.length() > 0)
								resStr = latentSB.toString() + " " + sb.toString() + "," + tempStr;
							else
								resStr = sb.toString() + "," + tempStr;
							pw.println(resStr);
						}
					}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		try{
			String newPath = "E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/sentiment/";
			String dicPath = "E:/programs/weibo4j-20110622/weibo4j/sentidics/";
			ToLabeledFile tbf = new ToLabeledFile( dicPath + "HowNet_sentiDic_smaller.txt");
			System.out.println(tbf.sentimentList.size());
//			toLabeledFileOnlyAdjVL(newPath + "lishuangjiang_LDAFile_ready.csv", newPath + "trainningData_lishuangjiang_c13_OnlyAdjVL.txt", 0);
			tbf.toLabeledFileOnlyDictSenti(newPath + "lishuangjiang_LDAFile_ready.csv", 
					newPath + "trainningData_lishuangjiang_c13_OnlyDicSenti_smallerSenti.txt", 0);
//			getTestData(path + "TopicFile.txt", path + "testData.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
