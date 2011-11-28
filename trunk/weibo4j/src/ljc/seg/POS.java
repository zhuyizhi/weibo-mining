package ljc.seg;


final public class POS {

	/**
	 * 形容词 形语素
	 */
	public static final int A = 0x40000000; 

	/**
	 * 区别词 区别语素
	 */
	public static final int B = 0x20000000; 

	/**
	 * 连词 连语素
	 */
	public static final int C = 0x10000000; 

	/**
	 * 副词 副语素
	 */
	public static final int D = 0x08000000; 

	/**
	 * 叹词 叹语素
	 */
	public static final int E = 0x04000000; 

	/**
	 * 方位词 方位语素
	 */
	public static final int F = 0x02000000; 

	/**
	 * 成语
	 */
	public static final int I = 0x01000000; 

	/**
	 * 习语
	 */
	public static final int L = 0x00800000; 

	/**
	 * 数词 数语素
	 */
	public static final int M = 0x00400000; 

	/**
	 * 数量词
	 */
	public static final int MQ = 0x00200000; 

	/**
	 * 名词 名语素
	 */
	public static final int N = 0x00100000; 

	/**
	 * 拟声词
	 */
	public static final int O = 0x00080000; 

	/**
	 * 介词
	 */
	public static final int P = 0x00040000; 

	/**
	 * 量词 量语素
	 */
	public static final int Q = 0x00020000; 

	/**
	 * 代词 代语素
	 */
	public static final int R = 0x00010000; 

	/**
	 * 处所词
	 */
	public static final int S = 0x00008000; 

	/**
	 * 时间词
	 */
	public static final int T = 0x00004000; 

	/**
	 * 助词 助语素
	 */
	public static final int U = 0x00002000; 

	/**
	 * 动词 动语素
	 */
	public static final int V = 0x00001000; 

	/**
	 * 标点符号
	 */
	public static final int W = 0x00000800; 

	/**
	 * 非语素字
	 */
	public static final int X = 0x00000400; 

	/**
	 * 语气词 语气语素
	 */
	public static final int Y = 0x00000200;  

	/**
	 * 状态词
	 */
	public static final int Z = 0x00000100;  

	/**
	 * 人名
	 */
	public static final int NR = 0x00000080;  

	/**
	 * 地名
	 */
	public static final int NS = 0x00000040;  

	/**
	 * 机构团体
	 */
	public static final int NT = 0x00000020;  

	/**
	 * 外文字符
	 */
	public static final int NX = 0x00000010;  

	/**
	 * 其他专名
	 */
	public static final int NZ = 0x00000008;  

	/**
	 * 前接成分
	 */
	public static final int H = 0x00000004;  

	/**
	 * 后接成分
	 */
	public static final int K = 0x00000002;  	
	
	/**
	 * 英文字母和数字的组合
	 */
	public static final int MX= 0x00200010;

	/**
	 * Static method to convert a pos value to corresponding string indicator
	 * @param value
	 * @return
	 */
	public static String ToString(int value) {
		switch (value) {
		case 0x40000000:
			return "A";

		case 0x20000000:
			return "B";

		case 0x10000000:
			return "C";

		case 0x08000000:
			return "D";

		case 0x04000000:
			return "E";

		case 0x02000000:
			return "F";

		case 0x01000000:
			return "I";

		case 0x00800000:
			return "L";

		case 0x00400000:
			return "M";

		case 0x00200000:
			return "MQ";

		case 0x00100000:
			return "N";

		case 0x00080000:
			return "O";

		case 0x00040000:
			return "P";

		case 0x00020000:
			return "Q";

		case 0x00010000:
			return "R";

		case 0x00008000:
			return "S";

		case 0x00004000:
			return "T";

		case 0x00002000:
			return "U";

		case 0x00001000:
			return "V";

		case 0x00000800:
			return "W";

		case 0x00000400:
			return "X";

		case 0x00000200:
			return "Y";

		case 0x00000100:
			return "Z";

		case 0x00000080:
			return "NR";

		case 0x00000040:
			return "NS";

		case 0x00000020:
			return "NT";

		case 0x00000010:
			return "NX";

		case 0x00000008:
			return "NZ";

		case 0x00000004:
			return "H";

		case 0x00000002:
			return "K";
			
		case 0x00200010:
			return "MX"; //英文字母和数字的组合
			
        default: 
        	return Integer.toHexString(value);//其他情况输出16进制值
		}		
	}

}
