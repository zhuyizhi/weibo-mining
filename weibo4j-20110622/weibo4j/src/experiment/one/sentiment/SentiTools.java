package experiment.one.sentiment;

public class SentiTools {
	public static boolean isADJ(String str){
		if(str.indexOf("/") != -1)
			if(str.substring(str.indexOf("/"), str.length()).equals("/a") ||
					str.substring(str.indexOf("/"), str.length()).equals("/ad") ||
					str.substring(str.indexOf("/"), str.length()).equals("/an"))
				return true;
			
		return false;
	}
	
	
	public static boolean isV(String str, boolean containVN){
		if(str.indexOf("/") != -1)
			if(str.substring(str.indexOf("/"), str.length()).equals("/v") ||
					str.substring(str.indexOf("/"), str.length()).equals("/vd") ||
					(containVN && str.substring(str.indexOf("/"), str.length()).equals("/vn")))
				return true;
			
		return false;
	}
	
	public static boolean isAspect(String token){
		if(token.indexOf("/") != -1)
			if(token.substring(token.indexOf("/"), token.length()).equals("/n") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nr")	||
					token.substring(token.indexOf("/"), token.length()).equals("/ns") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nt") ||
					token.substring(token.indexOf("/"), token.length()).equals("/nz") ||
					token.substring(token.indexOf("/"), token.length()).equals("/vn"))
				return true;
		return false;
	}
	
	public static boolean isL(String token){
		if(token.indexOf("/") != -1)
			if(token.substring(token.indexOf("/"), token.length()).equals("/l"))
				return true;
		return false;
	}
}
