package edu.sjtu.ltlab.word.split;


import cn.edu.sjtu.pos.IRLAS;


public class IRSplit implements Splitter{
	private IRLAS split ;
	
	public IRSplit() {
		split = IRLAS.getInstance();
	}
	

	/* (non-Javadoc)
	 * @see edu.sjtu.ltlab.ia.word.split.Splitter#paragraphProcess(java.lang.String)
	 */
	public String paragraphProcess(String sParagraph) {
		String irResult = split.wordSegment(sParagraph);
		
		return irResult;
	}
	
}
