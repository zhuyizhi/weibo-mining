package ljc.seg;

import java.util.ArrayList;


public class SegResult {
	/**
	 * words array list that are segmented
	 */
	ArrayList<SegWord> words = null;
	/**
	 * keyword array list that are caculated when OPT_KEYWORD flag was set
	 */
	ArrayList<SegWord> keywords = null;
	/**
	 * finger byte array when OPT_FINGER flag was set
	 */
	byte[]             finger   = null;
	
	/**
	 * @return Returns the finger.
	 */
	public byte[] getFinger() {
		return finger;
	}

	/**
	 * @param finger The finger to set.
	 */
	public void setFinger(byte[] finger) {
		this.finger = finger;
	}

	/**
	 * @return Returns the keywords.
	 */
	public ArrayList<SegWord> getKeywords() {
		return keywords;
	}

	/**
	 * @return Returns the words.
	 */
	public ArrayList<SegWord> getWords() {
		return words;
	}

	/**
	 * Add a new segword
	 * @param word
	 */
	protected void addSegWord(SegWord word){
		if(words==null) words = new ArrayList<SegWord>();
		words.add(word);
	}
	/**
	 * Add a new keyword
	 * @param word
	 */
	protected void addKeyword(SegWord word){
		if(keywords==null) keywords = new ArrayList<SegWord>();
		keywords.add(word);
	}

}
