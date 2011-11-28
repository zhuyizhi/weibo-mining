package extraction.target.toys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import edu.sjtu.ltlab.word.split.IRSplit;

import ljc.seg.HL;
import ljc.seg.SegResult;
import ljc.seg.SegWord;

public class TestIJC {
	public static void testIJC(){
		HL.init();
		try{
//			String text =new String("遭塔利班绑架的德国人质现身录像求助".getBytes(), "gb2312");
			String text = "遭塔利班绑架的德国人质现身录像求助";
			SegResult result= HL.splitText(text, HL.OPT_KEYWORD);
			
			List<SegWord> keyword = result.getKeywords();
			String str;
			for(SegWord seg : keyword)
//				System.out.println(seg.word + seg.weight);
				str = seg.word;
//			PrintWriter pw = new PrintWriter(new File("test.txt"));
//			for(SegWord seg : keyword)
//				pw.println(seg.word + seg.weight);
//			pw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void testIJC2(){
		
		try{
			HL.init();
			HL hl = HL.getInstance();
			String[] strs = new String[]{"中国", "啊", "世界首富"};
			for(String token:strs){
				int counter = 25000;
				while((--counter) > 0)
				{
					
					Long handler = HL.openSplit();
					SegResult result= HL.splitText(token, HL.OPT_KEYWORD);
					List<SegWord> keyword = result.getKeywords();
					String str;
					for(SegWord seg : keyword)
					{
						str = seg.word;
						System.out.println(counter);
					}
//						str = seg.word;
//						System.out.println(seg.word);
					HL.closeSplit(handler);
				}
				System.out.println("milstone");
			}
			HL.exit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void compareToIJC2(){
		
		try{
			IRSplit ir = new IRSplit();
			String[] strs = new String[]{"中国", "啊", "世界首富"};
			for(String token:strs){
				int counter = 25000;
				while((--counter) > 0)
				{
					
//					Long handler = HL.openSplit();
//					SegResult result;
//					result= HL.splitText(token, HL.OPT_KEYWORD);
					String result = ir.paragraphProcess(token);
//					List<SegWord> keyword = result.getKeywords();
//					String str;
//					for(SegWord seg : keyword)
//					{
//						str = seg.word;
//						System.out.println(counter);
//					}
					String[] rs = result.split("  ");
					System.out.println(counter);
//						str = seg.word;
//						System.out.println(seg.word);
//					HL.closeSplit(handler);
				}
				System.out.println("milstone");
			}
//			HL.exit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testIJC3(){
		try{
			HL.splitFile(new File("res.txt"), new File("hailiang.txt"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void transferToGBK() throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("laoren.txt"), "UTF-8"));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("res.txt"), "GB2312"));
		String tempStr;
		while((tempStr = br.readLine()) != null){
			pw.println(tempStr);
		}
		pw.flush();
	}
	
	public static void compareTo(){
		try{
			IRSplit ir =new IRSplit();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("res.txt"), "GB2312"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("qita.txt"), "GB2312"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				pw.println(ir.paragraphProcess(tempStr));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void compareing(){
		try{
			IRSplit ir =new IRSplit();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("res.txt"), "GB2312"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("qita.txt"), "GB2312"));
			String tempStr;
			while((tempStr = br.readLine()) != null){
				pw.println(ir.paragraphProcess(tempStr));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
//		testIJC3();
		try{
//			transferToGBK();
			long time1 = System.currentTimeMillis();
//			testIJC3();
//			testIJC2();
			compareToIJC2();
			long time2 = System.currentTimeMillis();
			System.out.println(time2-time1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
