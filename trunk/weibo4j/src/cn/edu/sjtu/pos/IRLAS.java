package cn.edu.sjtu.pos;


/**
 * ���������ķִʺʹ��Ա�ע�ӿ�
 * 
 * @author С����
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
	 * �ִʺʹ��Ա�ע,ʹ�ù������ע��
	 * 
	 * @param txt
	 * @return
	 */
	public synchronized native String wordSegment(String txt);
	
}
