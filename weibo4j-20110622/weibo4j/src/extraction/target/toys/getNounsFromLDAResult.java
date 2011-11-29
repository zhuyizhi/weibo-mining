package extraction.target.toys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;

public class getNounsFromLDAResult {
	public static void getNouns(String sourceFile, String destFile){
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
			String tempStr;
			HashSet<String> sets = new HashSet<String>();
			while((tempStr = br.readLine()) != null){
//				System.out.println(tempStr + " -----------");
				String[] strs = tempStr.split(" ");
				for(String str:strs){
//					System.out.print(str + "   ");
					if(str.endsWith("/n") || str.endsWith("/nl") || str.endsWith("/nz")){
						sets.add(str.substring(1));
					}
				}
//				System.out.println();
			}
			
			for(String str:sets){
				pw.println(str);
			}
			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void parseStr(){
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader
					(new FileInputStream("LDAnouns.txt"), "UTF-8"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				System.out.println(tempStr.charAt(0) + "--" + tempStr.charAt(1));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		String dirPath = args[0];
//		String filePath = "model-final.twords";
		getNouns(dirPath + "model-final.twords", dirPath + "LDAnouns.txt");
//		parseStr();
	}
}
