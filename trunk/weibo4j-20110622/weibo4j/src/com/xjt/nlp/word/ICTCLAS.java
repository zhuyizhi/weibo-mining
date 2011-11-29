package com.xjt.nlp.word;

/**
 * <p>Title: Java���ķִ����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author ��ӱ
 * @version 1.0
 */

public class ICTCLAS {

	static {
		System.loadLibrary("ICTCLAS");
	}
	
	private static ICTCLAS instance = null;

	private ICTCLAS() {
		init(0, 2);
	}

	public synchronized static ICTCLAS getInstance() {
		if (instance == null) {
			instance = new ICTCLAS();
		}
		return instance;
	}

	public synchronized native boolean init(int i, int j);

	public synchronized native String paragraphProcess(String sParagraph);

	public synchronized native boolean fileProcess(String source, String target);

	public static void main(String[] args) {
		ICTCLAS split1 = ICTCLAS.getInstance();
		System.out.println(split1.paragraphProcess("�����ҹ���Դ�ɳ�����չ�����󣬹����ƶ��˻�����չ�˵��ս������"));
	}


}