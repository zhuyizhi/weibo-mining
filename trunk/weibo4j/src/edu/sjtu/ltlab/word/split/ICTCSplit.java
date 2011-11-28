package edu.sjtu.ltlab.word.split;


import com.xjt.nlp.word.ICTCLAS;

public class ICTCSplit implements Splitter {
	private ICTCLAS split;

	public ICTCSplit() {
		split = ICTCLAS.getInstance();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjtu.ltlab.ia.word.split.Splitter#paragraphProcess(java.lang.String)
	 */
	public String paragraphProcess(String sParagraph) {
		String icResult = split.paragraphProcess(sParagraph);

		return icResult;
	}

}
