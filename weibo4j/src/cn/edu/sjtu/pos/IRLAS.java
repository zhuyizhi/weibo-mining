package cn.edu.sjtu.pos;


/**
 * 哈工大中文分词和词性标注接口
 * 
 * @author 小土豆
 * 
 */
public class IRLAS {
	private static IRLAS instance = null;
	
	static {
		System.loadLibrary("SJTU_IRLAS");
	}

	/**
	 * 
	 */
	public IRLAS() {
		LoadSegRes();
	}

	/**
	 * @return
	 */
	public synchronized static IRLAS getInstance() {
		if (instance == null) {
			instance = new IRLAS();
		}
		return instance;
	}

	/**
	 * 
	 *
	 */
	public synchronized native void LoadSegRes();

	/**
	 * 
	 *
	 */
	public synchronized native void ReleaseSegRes();

	/**
	 * 分词和词性标注,使用哈工大标注集
	 * 
	 * @param txt
	 * @return
	 */
	public synchronized native String wordSegment(String txt);
	
}
