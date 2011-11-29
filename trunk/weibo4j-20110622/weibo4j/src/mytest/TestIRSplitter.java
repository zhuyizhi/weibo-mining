package mytest;

import edu.sjtu.ltlab.word.split.IRSplit;

public class TestIRSplitter {
	public static void main(String[]args)throws Exception{
//		ICTCSplit ictSplitter=new ICTCSplit();
		IRSplit irSplitter=new IRSplit();
//		Scanner sc=new Scanner(new File(doc));
//		StringBuffer sb=new StringBuffer();
//		while(sc.hasNext())
//		{
//			sb.append(sc.nextLine());
//		}
//		String temps=sb.toString();
		String temps= " 【疯狂英语否认李阳妻子遭家暴】 疑似妻子再曝李阳“家暴细节” - 京华时报·京华网  疯狂啊! 《传李阳外籍妻子遭家暴 头部耳朵多处受伤(组图)》（ 来自@头条新闻）" +
				"李阳做人也太差劲 //@有报天天读:回覆@上海赵长天:家暴，研究中？ //@上海赵长天:李阳在电视节目上公开说和美籍妻子结婚是为了研究美国的教育，这太不靠谱了。";
		String res=irSplitter.paragraphProcess(temps);
		System.out.println(res);
//		String[] strs=res.split("  ");
//		for(String s:strs)
//			System.out.println(s);
	}
}
