package ljc.seg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


final public class HL {
	/**
	 * Native method: 初始化海量分词系统.
	 * <p>
	 * 1 注意必须是Windows格式路径,不包含字典文件名的路径,相对路径绝对路径均可, 不能为null 如果为null,分词系统会出错<br>
	 * 2 Both of the file HLSSplit.dll and HLSplitWord.Dat should in the same
	 * directory<br>
	 * 
	 * @param dictPath
	 *            The directory that contains <I>HLSplitWord.Dat</I> Ex:
	 *            <code>if(init("d:\\dictPath")==false) return false;</code>
	 */
	public native static boolean init(final String dictPath);

	/**
	 * Native method: 退出海量分词系统. Eg. <code>HL.exit();</code>
	 */
	public native static void exit();

	/**
	 * Native method: 打开海量分词句柄.
	 * <p>
	 * Open a split thread for spliting<br>
	 * 
	 * @return long: To indicate a thread handle Eg.<code> long handle = HL.openSplit();</code>
	 */
	public native static long openSplit();

	/**
	 * Native method: 关闭海量分词句柄
	 * <p>
	 * Close a split thread when spliting completed<br>
	 * 
	 * @param handle
	 *            <B>Thread handle for spliting</B> eg.
	 *            <code>HL.close(handle)</code>
	 * @see #openSplit()
	 */
	public native static void closeSplit(long handle);

	/**
	 * Native method: 对一段字符串分词.
	 * <p>
	 * This is one of the most important methods for spliting<br>
	 * it depends that you initilize the HL library and open a split.<br>
	 * 
	 * @param handle
	 *            <b>The thread handle previously created for spliting</b>
	 * @param text
	 *            <b>A String to split</b>
	 * @param calFlag
	 *            <b>Extra caculate flag. The flags are static properties of
	 *            class HL.</b>
	 * @return boolean: To indicate whether the split procedure is succeful or
	 *         not. eg.
	 *         <code> boolean flag = HL.split(handle,text,HL.OPT_POS|HL.OPT_KEYWORD);</code>
	 */
	public native static boolean split(long handle, String text, int calFlag);

	/**
	 * Native method: 获得分词结果个数.
	 * 
	 * @param handle:
	 *            <b>The thread handle previously created for spliting</b>
	 * @return int: The word count splited eg.<code>int cnt = HL.getWordCnt(handle);</code>
	 */
	public native static int getWordCnt(long handle);

	/**
	 * Native method: 获取指定的分词结果.
	 * 
	 * @param handle
	 *            <b>The thread handle previously created for spliting</b>
	 * @param index
	 *            <b>The index of the segmented word in the result</b>
	 * @return SegWord : A structure to store the word's information eg.<code> SegWord seg = HL.getWordAt(handle,i);</code>
	 */
	public native static SegWord getWordAt(long handle, int index);

	/**
	 * Native method: 装载用户自定义词典.
	 * 
	 * @param usrDictName
	 *            <b>Full path of the user's directory</b>
	 * @return boolean : indicate whether user dictionary be loaded successful
	 *         eg.<code> boolean flag = HL.openUsrDict(usrDictName);</code>
	 */
	public native static boolean openUsrDict(final String usrDictName);

	/**
	 * Native method: 卸载用户自定义词典.
	 * 
	 * @return boolean : indicate whether release the user's dictionary
	 *         successful
	 */
	public native static boolean freeUsrDict();

	/**
	 * Native method: 获取关键词个数.
	 * 
	 * @param handle
	 *            <b>The thread handle previously created for spliting</b>
	 * @return int : keyword count be splited eg.
	 * @see #getKeywordAt
	 */
	public native static int getKeywordCnt(long handle);

	/**
	 * Native method: 获取指定的关键词.
	 * 
	 * @param handle
	 *            <b>The thread handle previously created for spliting</b>
	 * @param index
	 *            <b>The index of the keyword in the result</b>
	 * @return SegWord : a structure contains the segmented word information eg.
	 *         <code>
	 * for(int i=0;i<HL.getKeywordCnt(handle);i++){
	 * 	  SegWord seg = HL.getKeywordAt(handle,i);
	 * }
	 * </code>
	 */
	public native static SegWord getKeywordAt(long handle, int index);

	/**
	 * Native method: 获得语义指纹.
	 * 
	 * @param handle
	 *            <b>The thread handle previously created for spliting</b>
	 * @return a byte array(which size is 16) to present the finger of the text
	 */
	public native static byte[] getFingerM(long handle);

	/**
	 * 默认计算标识：仅进行分词
	 */
	public final static byte OPT_DEFAULT = 0x0;

	/**
	 * 计算关键词附加标识
	 */
	public final static byte OPT_KEYWORD = 0x1;

	/**
	 * 计算文章语义指纹标识
	 */
	public final static byte OPT_FINGER = 0x2;

	/**
	 * 计算词性标识
	 */
	public final static byte OPT_POS = 0x4;

	/**
	 * 输出面向检索的分词结果
	 */
	public final static byte OPT_SEARCH = 0x8;
	
	public static String POSTAG = "|";

	private static final int MAX_SIZE = 512 * 1024 * 1024; // 可以分词的最大文本大小:默认10k

	private static final String line_sep = System.getProperty("line.separator");
	
	
	
	/**
	 * Load HL.dll when using class HL.
	 */
	static {
		System.loadLibrary("HL");
		init();
	}

	private static HL instance = null;

	/**
	 * 简化的初始化方法:默认在加载当前工作目录下的HLSplitWord.dat文件.
	 * 
	 * @return a boolean flag to indicate whether it's initilized successful or
	 *         not
	 */
	public static boolean init() {
		String curPath = System.getProperty("user.dir");
		return init(curPath);
	}

	/**
	 * 
	 */
	private HL() {
		init();
	}

	/**
	 * @return
	 */
	public synchronized static HL getInstance() {
		if (instance == null) {
			instance = new HL();
		}
		return instance;
	}
	
	/**
	 * 用单一线程对一段文本进行分词.
	 * <p>
	 * 不用打开分词句柄和关闭分词句柄
	 * </p>
	 * 
	 * @param text
	 *            <b>The string text that to be segmented</b>
	 * @param calFlag
	 *            <b>extra caculate flag</b>
	 * @return SegResult : A SegResult object that contains all the information.
	 *         eg.<code>SegResult result = HL.splitText(text,HL.OPT_POS);</code>
	 */
	public static SegResult splitText(String text, int calFlag) {
		long handle = HL.openSplit();
		if (HL.split(handle, text, calFlag) == false)
			return null;
		SegResult result = new SegResult();
		for (int i = 0; i < HL.getWordCnt(handle); i++) {
			SegWord word = HL.getWordAt(handle, i);
			result.addSegWord(word);
		}
		if ((calFlag & HL.OPT_KEYWORD) != 0) {//
			for (int i = 0; i < HL.getKeywordCnt(handle); i++) {
				SegWord word = HL.getKeywordAt(handle, i);
				result.addKeyword(word);
			}
		}
		if ((calFlag & HL.OPT_FINGER) != 0) {//
			result.setFinger(HL.getFingerM(handle));
		}
		HL.closeSplit(handle);
		return result;
	}

	/**
	 * 用单一线程对文件进行分词和计算词性.
	 * <p>
	 * 不用打开分词句柄和关闭分词句柄
	 * </p>
	 * 
	 * @param src
	 * @param des
	 * @return boolean : boolean flag indicates whether segment successfully
	 * @throws IOException
	 */
	public static boolean splitFile(File src, File des) throws IOException {
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(src));
			writer = new FileWriter(des);
			if (src.length() > MAX_SIZE) {
				ArrayList<File> temp = new ArrayList<File>();
				char[] buffer = new char[MAX_SIZE];
				StringBuffer strBuf = new StringBuffer(MAX_SIZE + 100);
				int len = reader.read(buffer);
				int fileIndex = 0;
				String path = des.getAbsolutePath();
				StringBuffer buf = new StringBuffer();
				for (char c : path.toCharArray()) {
					if (c != '\\' && c != '/' && c != ':')
						buf.append(c);
				}
				String prefix = buf.toString();
				while (len != -1) {// do while not to the end
					if (strBuf.length() == len)
						strBuf.delete(0, len - 1);
					strBuf.append(buffer); // append to strBuf
					if (!isPunc(buffer[len - 1])) {// 最后一个字符不是标点,向前读取直到标点出现
						int c = reader.read();
						while (c != -1 && !isPunc((char) c)) {// 一直找找到一个完整的句子
							strBuf.append((char) c);
							len++;
							c = reader.read();
						}
						if (c != -1)
							strBuf.append((char) c); // c 是一个标点
					}
					fileIndex++;

					File tmpfile = File.createTempFile(prefix
							+ String.valueOf(fileIndex), null, null);
					temp.add(tmpfile);
					splitAndSave(strBuf.toString(), new FileWriter(tmpfile));
					len = reader.read(buffer); // continue to read
				}
				// merge file together
				for (File f : temp) {
					BufferedReader r = new BufferedReader(new FileReader(f));
					String line = r.readLine();
					while (line != null) {
						writer.write(line + line_sep);
						line = r.readLine();
					}
					writer.flush();
					r.close();
					f.delete();
				}
				writer.close();
				return true;
			} // end fi 
			 
			// 单个文件进行分词
			StringBuffer strBuf = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				strBuf.append(line + line_sep);
				line = reader.readLine();
			}
			long handle = HL.openSplit();
			boolean flag = HL.split(handle, strBuf.toString(), HL.OPT_POS); // just
			// pos
			if (flag == false)
				return false; //
			for (int i = 0; i < HL.getWordCnt(handle); i++) {
				SegWord seg = HL.getWordAt(handle, i);
				/*
				 * if(seg.word.equals(line_sep)) //若是回车,则仅仅写入文件回车符
				 * writer.write(line_sep); else if(!seg.word.trim().equals(""))
				 */
				writer.write(seg.word + HL.POSTAG + POS.ToString(seg.pos) + "\r\n");// 每个分开的词之间用空格隔开
			}
			writer.flush();
			writer.close();
			HL.closeSplit(handle);
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	/**
	 * @param text
	 * @param writer
	 * @return
	 * @throws IOException
	 */
	private static boolean splitAndSave(String text, FileWriter writer)
			throws IOException {
		long handle = HL.openSplit();
		boolean flag = HL.split(handle, text, HL.OPT_POS); // just pos
		if (flag == false)
			return false; //
		for (int i = 0; i < HL.getWordCnt(handle); i++) {
			SegWord seg = HL.getWordAt(handle, i);
			/*
			 * if(seg.word.equals(line_sep)) //若是回车,则仅仅写入文件回车符
			 * writer.write(line_sep); else if(!seg.word.trim().equals(""))
			 */
			writer.write(seg.word + HL.POSTAG + POS.ToString(seg.pos) + " ");// 每个分开的词之间用空格隔开
		}
		HL.closeSplit(handle);
		return true;
	}

	/**
	 * @param c
	 * @return
	 */
	private static boolean isPunc(char c) {
		if (c == '。' || c == '？' || c == '！' || c == '.' || c == '?'
				|| c == '!')
			return true;
		else
			return false;
	}

	public static void main(String[] args) throws IOException {
		HL.init();
		String text = "遭塔利班绑架的德国人质现身录像求助";
		SegResult result= HL.splitText(text, HL.OPT_KEYWORD);
		
		List<SegWord> keyword = result.getKeywords();
		for(SegWord seg : keyword)
			System.out.println(seg.word + seg.weight);
		
		
	}

}
