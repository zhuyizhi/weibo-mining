package experiment.one.sentiment.expression;

import java.util.HashMap;
import java.util.PriorityQueue;

import november.second.LabelScorePair;

/**
 * use the adjacent sentiment words to match hot aspect 
 * @author ucai
 *
 */
public class GetPairByAdjacent {
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
	
	public static HashMap<String, PriorityQueue<LabelScorePair>> getResult(){
		HashMap<String, PriorityQueue<LabelScorePair>> map = new HashMap
		<String, PriorityQueue<LabelScorePair>>();
		
		
		for(String label:labels){
			
		}
		
		return null;
	}
	
	
}
