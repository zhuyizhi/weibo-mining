package ljc.seg;

final public class SegWord {
	
	public String word;
	public float  weight;
	public int    pos;	
	
	public String toString() {
		return word + HL.POSTAG + POS.ToString(pos);
	}
}