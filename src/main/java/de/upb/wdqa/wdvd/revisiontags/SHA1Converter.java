package de.upb.wdqa.wdvd.revisiontags;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHA1Converter {
	static final Logger logger = LoggerFactory.getLogger(SHA1Converter.class);
	
	static public String base16to36 (String base16){
		byte[] sha1 = parseByte(16, base16);
		
		return getBase36(sha1);
	}
	
	static public byte[] parseByte16(String base16){
		return parseByte(16, base16);
	}
	
	static public byte[] parseByte36(String base36){
		return parseByte(36, base36);
	}
	
	static private byte[] parseByte(int base, String sha1){
		byte[] result = null;
		if (sha1 != null){
			if(!sha1.equals("")){
				try {
					BigInteger bi = new BigInteger(sha1, base);
					result = bi.toByteArray();
				
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			else{
				result = new byte[0];
			}
		}
		return result;
		
	}
	
	static public String getBase16(byte[] bytes){
		return getBase(16, bytes);
	}
	
	static public String getBase36(byte[] bytes){
		return getBase(36, bytes);
	}
	
	static private String getBase(int base, byte[] bytes){
		String result;
		
		if (bytes != null){
			if(bytes.length != 0){
				BigInteger bi = new BigInteger(1, bytes);
				String tmp = bi.toString(base);
				
				int numberOfDigits = (int) Math.ceil(160.0 / (Math.log(base) / Math.log(2.0)));
				
				result = StringUtils.leftPad(tmp, numberOfDigits, '0');
			}
			else{
				result = "";
			}
		}
		else{
			result = null;
		}
		
		return result;		
	}
}
