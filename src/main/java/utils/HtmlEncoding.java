package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HtmlEncoding {

	public static String encodeURIComponent(String s) {
	    String result;

	    try {
	        result = URLEncoder.encode(s, "UTF-8")
	                .replaceAll("\\+", "%20")
	                .replaceAll("\\%21", "!")
	                .replaceAll("\\%27", "'")
	                .replaceAll("\\%28", "(")
	                .replaceAll("\\%29", ")")
	                .replaceAll("\\%7E", "~")
	                .replaceAll("\\%dot", ".");
	    } catch (UnsupportedEncodingException e) {
	        result = s;
	    }

	    return result;
	}
}
