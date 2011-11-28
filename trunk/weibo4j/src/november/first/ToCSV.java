package november.first;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Hashtable;

public class ToCSV {
	public static Hashtable<String, HashSet<String>> labelTable = new Hashtable<String, HashSet<String>>();
	public static String[] labels = new String[]{
		"李双江/nh", "李刚/nh", "李天一/nh",  "苏楠/nh", "社会/n", "真理/n","李阳/nh","彭先生/nh"};
	public static String[][] labelNames = new String[][]{
		{"李双江/nh", "爹/n", "爸/n", "双江/ns", "父/n","老子/n"},
		{"李刚/nh"},
		{"李天一/nh", "子/n", "儿子/n", "孩子/n"},
		{"苏楠/nh","苏浩/nh"},
		{"社会/n"},
		{"真理/n"},
		{"李阳/nh"},
		{"彭先生/nh", "彭氏/nh"}
	};
	
	public static HashSet<String> nameSet = new HashSet();
	
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
	

	
	public static void  test1(){
		try{
			String fileName = "son_LDA.txt";
			String outputFile = "sonCSV.csv";
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			while((tempStr = br.readLine()) != null && number < 10000){
				if(tempStr.length() > 4){
					pw.println((number++) + "," + tempStr);
				}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void withLabels(){
		try{
			String fileName = "LDAFile.csv";
			String outputFile = "labeledSonCSV.csv";
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			while((tempStr = br.readLine()) != null && number < 10000){
				if(tempStr.length() > 4){
//					pw.println((number++) + "," + tempStr);
					StringBuffer sb = new StringBuffer();
					for(String str : labels){
						
						if(tempStr.contains(str)){
//							System.out.println(tempStr + " ______ " + str);
							if(sb.length() > 0)
								sb.append(" ");
							sb.append(str);
						}
					}
					if(sb.length() > 0){
						pw.println(sb.toString() + "," + tempStr);
					}
				}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void withLabelsV2(){
		try{
			String fileName = "LDAFile_length1.csv";
			String outputFile = "labeledSonCSV_length1.csv";
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			while((tempStr = br.readLine()) != null && number < 10000){
					StringBuffer sb = new StringBuffer();
					for(String str : labels){
						
						if(tempStr.contains(str)){
							tempStr = tempStr.replaceAll(str, "");
							if(sb.length() > 0)
								sb.append(" ");
							sb.append(str);
						}
					}
					
					if(sb.length() > 0){
						pw.println(sb.toString() + "," + tempStr);
					}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String stripKnownNames(String rawStr){
		try{
			String[] strs = rawStr.split(" ");
			for(String str:nameSet){
				rawStr = rawStr.replaceAll(str, "");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rawStr;
	}
	
	public static void withLabelsV3(){
		try{
			String fileName = "LDAFile.csv";
			String outputFile = "labeledSonCSV_nameSet.csv";
			BufferedReader br = new BufferedReader
			(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter
					(new FileOutputStream(outputFile), "UTF-8"));
			String tempStr ="";
			int number = 1;
			while((tempStr = br.readLine()) != null){
					StringBuffer sb = new StringBuffer();
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
						tempStr = stripKnownNames(tempStr);
						pw.println(sb.toString() + "," + tempStr);
					}
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		ToCSV tc = new ToCSV();
		withLabelsV3();
	}
}
