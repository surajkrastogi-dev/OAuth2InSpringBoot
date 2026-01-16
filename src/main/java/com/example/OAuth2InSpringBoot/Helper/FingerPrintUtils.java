package com.example.OAuth2InSpringBoot.Helper;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class FingerPrintUtils {
	
	public static String generateFingerPrint(String deviceId,String ipAddress,String userAgent) {
		String raw = deviceId+"|"+ipAddress+"|"+userAgent;
		return DigestUtils.sha256Hex(raw);
	}
	

	public static String generateFingerPrintOther(String deviceId, String ipAddress, String userAgent) {
	    String raw = deviceId + "|" + ipAddress + "|" + userAgent;
	    return DigestUtils.sha256Hex(raw.getBytes(StandardCharsets.UTF_8));
	}


}
