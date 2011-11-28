package sjtu.edu.cn.corpus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import sjtu.edu.cn.trend.collection.SinaWeiboHelper;

/**
 * used to delete duplicate records from a table
 * @author ucai
 *
 */
public class DeleteDuplicate {
	
	/**
	 * assume there existing a destTable in the database
	 * @param sourceTable
	 * @param destTable
	 */
	public static void deleteDuplicateExist(String sourceTable, String destTable){
		try{
			HashSet<Long> idSets = new HashSet<Long>();
			SinaWeiboHelper helper = new SinaWeiboHelper();
			ResultSet rs = helper.getAllWeibo(sourceTable);
			
			int counter = 0;
			while(rs.next()){
				long id = rs.getLong("textID");
				if(!idSets.contains(id)){
					idSets.add(id);
					
					long userID = rs.getLong("userID");
					java.sql.Date created_at = rs.getDate("created_at");
					String text = rs.getString("text");
					String original_pic = rs.getString("original_pic");
					long retweeted_status = rs.getLong("retweeted_status");
					
					PreparedStatement ps = helper.con.prepareStatement("insert into " 
							+ destTable + "(userID, textID, created_at, text, original_pic, retweeted_status) "
									+ "values(?, ?, ?, ?, ?, ?)");
					
					ps.setLong(1, userID);
					ps.setLong(2, id);
					ps.setDate(3, created_at);
					ps.setString(4, text);
					ps.setString(5, original_pic);
					ps.setLong(6, retweeted_status);
					
					ps.execute();
					
					System.out.println(++counter);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		deleteDuplicateExist("李双江之子打人", "lishuangjiang");
	}
}
