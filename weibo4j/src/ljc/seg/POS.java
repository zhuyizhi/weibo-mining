package ljc.seg;


final public class POS {

	/**
	 * ���ݴ� ������
	 */
	public static final int A = 0x40000000; 

	/**
	 * ����� ��������
	 */
	public static final int B = 0x20000000; 

	/**
	 * ���� ������
	 */
	public static final int C = 0x10000000; 

	/**
	 * ���� ������
	 */
	public static final int D = 0x08000000; 

	/**
	 * ̾�� ̾����
	 */
	public static final int E = 0x04000000; 

	/**
	 * ��λ�� ��λ����
	 */
	public static final int F = 0x02000000; 

	/**
	 * ����
	 */
	public static final int I = 0x01000000; 

	/**
	 * ϰ��
	 */
	public static final int L = 0x00800000; 

	/**
	 * ���� ������
	 */
	public static final int M = 0x00400000; 

	/**
	 * ������
	 */
	public static final int MQ = 0x00200000; 

	/**
	 * ���� ������
	 */
	public static final int N = 0x00100000; 

	/**
	 * ������
	 */
	public static final int O = 0x00080000; 

	/**
	 * ���
	 */
	public static final int P = 0x00040000; 

	/**
	 * ���� ������
	 */
	public static final int Q = 0x00020000; 

	/**
	 * ���� ������
	 */
	public static final int R = 0x00010000; 

	/**
	 * ������
	 */
	public static final int S = 0x00008000; 

	/**
	 * ʱ���
	 */
	public static final int T = 0x00004000; 

	/**
	 * ���� ������
	 */
	public static final int U = 0x00002000; 

	/**
	 * ���� ������
	 */
	public static final int V = 0x00001000; 

	/**
	 * ������
	 */
	public static final int W = 0x00000800; 

	/**
	 * ��������
	 */
	public static final int X = 0x00000400; 

	/**
	 * ������ ��������
	 */
	public static final int Y = 0x00000200;  

	/**
	 * ״̬��
	 */
	public static final int Z = 0x00000100;  

	/**
	 * ����
	 */
	public static final int NR = 0x00000080;  

	/**
	 * ����
	 */
	public static final int NS = 0x00000040;  

	/**
	 * ��������
	 */
	public static final int NT = 0x00000020;  

	/**
	 * �����ַ�
	 */
	public static final int NX = 0x00000010;  

	/**
	 * ����ר��
	 */
	public static final int NZ = 0x00000008;  

	/**
	 * ǰ�ӳɷ�
	 */
	public static final int H = 0x00000004;  

	/**
	 * ��ӳɷ�
	 */
	public static final int K = 0x00000002;  	
	
	/**
	 * Ӣ����ĸ�����ֵ����
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
			return "MX"; //Ӣ����ĸ�����ֵ����
			
        default: 
        	return Integer.toHexString(value);//����������16����ֵ
		}		
	}

}
