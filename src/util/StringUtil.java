package util;

import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static String zeros(int number) {
		return CharBuffer.allocate(number).toString().replace('\0', '0');
	}

	public static String SHA256(String input){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String timestampConverter(long timestamp) {
		Date date = new Date(timestamp * 1000);
		Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateFormat.format(date);
	}


}
