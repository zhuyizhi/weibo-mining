package edu.sjtu.ltlab.word.split;

import java.util.HashMap;
import java.util.List;

import ljc.seg.HL;
import ljc.seg.POS;
import ljc.seg.SegResult;
import ljc.seg.SegWord;

public class HLSplit implements Splitter {
	
	private static HashMap<String, String> map = new HashMap<String, String>() ;
	
	public HLSplit() {
		initMap();
	}
	
	private void initMap() {
		map.put(POS.ToString(POS.A), POSGB.a);
		map.put(POS.ToString(POS.B), POSGB.f);
		map.put(POS.ToString(POS.C), POSGB.c);
		map.put(POS.ToString(POS.D), POSGB.d);
		map.put(POS.ToString(POS.E), POSGB.nz);
		map.put(POS.ToString(POS.F), POSGB.nd);
		map.put(POS.ToString(POS.I), POSGB.i);
		map.put(POS.ToString(POS.L), POSGB.i);
		map.put(POS.ToString(POS.M), POSGB.m);
		map.put(POS.ToString(POS.MQ), POSGB.q);
		map.put(POS.ToString(POS.N), POSGB.n);
		map.put(POS.ToString(POS.O), POSGB.o);
		map.put(POS.ToString(POS.P), POSGB.p);
		map.put(POS.ToString(POS.Q), POSGB.q);
		map.put(POS.ToString(POS.R), POSGB.r);
		map.put(POS.ToString(POS.S), POSGB.nl);
		map.put(POS.ToString(POS.T), POSGB.nt);
		map.put(POS.ToString(POS.U), POSGB.u);
		map.put(POS.ToString(POS.V), POSGB.v);
		map.put(POS.ToString(POS.W), POSGB.wp);
		map.put(POS.ToString(POS.X), POSGB.x);
		map.put(POS.ToString(POS.Y), POSGB.e);
		map.put(POS.ToString(POS.Z), POSGB.as);
		map.put(POS.ToString(POS.NR), POSGB.nh);
		map.put(POS.ToString(POS.NS), POSGB.ns);
		map.put(POS.ToString(POS.NT), POSGB.ni);
		map.put(POS.ToString(POS.NX), POSGB.ws);
		map.put(POS.ToString(POS.NZ), POSGB.nz);
		map.put(POS.ToString(POS.H), POSGB.h);
		map.put(POS.ToString(POS.K), POSGB.k);
	}


	/* (non-Javadoc)
	 * @see edu.sjtu.ltlab.ia.word.split.Splitter#paragraphProcess(java.lang.String)
	 */
	public String paragraphProcess(String sParagraph) {
		SegResult result = HL.splitText(sParagraph, HL.OPT_POS);

		List<SegWord> words = result.getWords();
		StringBuilder sb = new StringBuilder();
		int size = words.size();
		for (int i = 0; i < size; i++) {
			SegWord word = words.get(i);
			sb.append( word.word + POSGB.newPOS  + map.get( POS.ToString(word.pos) ) + POSGB.SEPERATOR );
		}

		return sb.toString();
	}
	

}
