package november.second;

public class LabelScorePair implements Comparable{
	public String label;
	public Double score;
	public LabelScorePair(String label, String score){
		this.label = label;
		this.score = Double.parseDouble(score);
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(score < ((LabelScorePair)o).score)
			return 1;
		else if(score == ((LabelScorePair)o).score)
			return 0;
		return -1;
	}
	
}
