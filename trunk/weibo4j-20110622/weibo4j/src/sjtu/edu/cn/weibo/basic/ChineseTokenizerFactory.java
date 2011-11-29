package sjtu.edu.cn.weibo.basic;
import com.aliasi.tokenizer.*;
import com.aliasi.tokenizer.Tokenizer;

import java.io.StringReader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.cn.smart.*;
import org.apache.lucene.util.Version;
public class ChineseTokenizerFactory implements TokenizerFactory {
	public Tokenizer tokenizer(char[] ch ,int start, int length ){
		return new ChineseTokenizer(ch, start, length);
	}
	class ChineseTokenizer extends Tokenizer{
		private SmartChineseAnalyzer analyzer=null;
		private TokenStream stream;
		private TermAttribute ta=null;
		@SuppressWarnings("deprecation")
		public ChineseTokenizer(char[] ch,int offset,int length){
			analyzer=new SmartChineseAnalyzer();
			String text=new String(ch);
			if((offset!=0)||(length!=ch.length)){
				text=text.substring(offset, length);
			}
			stream=analyzer.tokenStream("sentence",new StringReader(text));
			ta=(TermAttribute) stream.getAttribute(TermAttribute.class);
		}
		public String nextToken(){
			try{
				if(stream.incrementToken())
					return ta.term();
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}
}