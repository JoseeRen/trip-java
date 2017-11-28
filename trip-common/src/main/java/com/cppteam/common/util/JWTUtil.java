package com.cppteam.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Base64;


/**
 * JWT加解密工具
 * @author happykuan
 * 
 */
public class JWTUtil {
	
	private static final String SECRET = "hdcpptd123.";
	private static final String HEADER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9";  // {"typ":"JWT","alg":"HS256"} base64加密(jwt 第一部分)
	
	/**
	 * 生成jwt token
	 * @param id
	 * @return
	 */
	public static String generateToken(String id) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		String payload = jsonObject.toString();
		String payloadEncoded = Base64.getEncoder().encodeToString(payload.getBytes());			// 对payload进行base64加密(jwt 第二部分)
		String encodedString = JWTUtil.HEADER + "." + payloadEncoded;							// base64加密后的header和base64加密后的payload使用.连接组成字符串，
		String signature = DigestUtils.sha256Hex((encodedString + JWTUtil.SECRET).getBytes());	// 然后通过header中声明的加密方式进行加盐secret组合(jwt 第三部分)
		return JWTUtil.HEADER + "." + payloadEncoded + "." + signature; 
	}
	
	/**
	 * 根据jwt token获取id
	 * @param token
	 * @return 成功返回id, 失败返回null
	 */
	public static String validToken(String token) {

	    if(StringUtils.isBlank(token)) {
	        return null;
        }

		String[] arr = token.split("\\.");
		String payloadEncoded = arr[1];
		String idJson = new String(Base64.getDecoder().decode(payloadEncoded));
		JSONObject jsonObject = new JSONObject(idJson);
        String id = jsonObject.getString("id");

        String newToken = JWTUtil.generateToken(id);
		
		if (newToken.equals(token)) {
			return id;
		} else {
			return null;
		}
	}
}
