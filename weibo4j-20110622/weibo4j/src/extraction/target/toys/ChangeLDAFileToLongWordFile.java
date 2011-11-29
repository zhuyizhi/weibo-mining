package extraction.target.toys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 将原始LDA文件转成只含词长大于2的
 * @author ucai
 *
 */
public class ChangeLDAFileToLongWordFile {
	public static void main(String[] args){
		try{
			BufferedReader brSource = new BufferedReader
			(new InputStreamReader(new FileInputStream
					("E:\\programs\\weibo4j-20110622\\weibo4j\\experiment\\sandBox\\invisableLDAFile.txt"), "UTF-8"));
			
			PrintWriter pw = new PrintWriter
			(new OutputStreamWriter(new FileOutputStream
					("E:\\programs\\weibo4j-20110622\\weibo4j\\experiment\\sandBox\\invisableLDAFile_2.txt"), "UTF-8"));
			
			String tempStr;
			while((tempStr = brSource.readLine()) != null){
				String[] strs = tempStr.split(" ");
				StringBuilder sb = new StringBuilder();
				for(String str:strs){
					if(str.indexOf("/") != -1){
						if(str.substring(0, str.indexOf("/")).length() > 1){
							sb.append(str + " ");
						}
					}
				}
				
				if(sb.length() > 0){
					pw.println(sb.toString());
				}
			}
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
