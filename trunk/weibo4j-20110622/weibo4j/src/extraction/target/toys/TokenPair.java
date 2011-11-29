package extraction.target.toys;

public class TokenPair implements Comparable{
	String token;
	double score;
	public TokenPair(String token, double score){
		this.token = token;
		this.score = score;
	}
	public TokenPair(){
		this.token = null;
		this.score = 0;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(this.score < ((TokenPair)o).score)
			return 1;
		else if(this.score > ((TokenPair)o).score )
			return -1;
		return 0;
	}
	
}