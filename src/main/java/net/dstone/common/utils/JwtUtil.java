package net.dstone.common.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	private static String SECRETE_KEY_FOR_JWT = "jysn007db2admin!";
	
	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param body
	 * @return
	 */
	public static String getJwt(Map<String, Object> body) {
		return getJwt(new HashMap<String, Object>(), body, SECRETE_KEY_FOR_JWT);
	}
	
	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param body
	 * @param secretKey
	 * @return
	 */
	public static String getJwt(Map<String, Object> body, String secretKey) {
		return getJwt(new HashMap<String, Object>(), body, secretKey);
	}
	
	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param header
	 * @param body
	 * @return
	 */
	public static String getJwt(Map<String, Object> header, Map<String, Object> body) {
		String jwt = getJwt(header, body, SECRETE_KEY_FOR_JWT);
		return jwt;
	}
	
	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param header
	 * @param body
	 * @param secretKey
	 * @return
	 */
	public static String getJwt(Map<String, Object> header, Map<String, Object> body, String secretKey) {
		String jwt = getJwt(header, body, "", "", SignatureAlgorithm.HS256 , secretKey);
		return jwt;
	}

	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param header
	 * @param body
	 * @param secretKey
	 * @return
	 */
	public static String getJwt(Map<String, Object> header, Map<String, Object> body, Key secretKey) {
		String jwt = getJwt(header, body, "", "", SignatureAlgorithm.HS256 , secretKey);
		return jwt;
	}
	
	/**
	 * Jwt(Json Web Token)생성 메서드
	 * @param header
	 * @param body
	 * @param issuer
	 * @param audience
	 * @param alg
	 * @param keyObj
	 * @return
	 */
	public static String getJwt(Map<String, Object> header, Map<String, Object> body, String issuer, String audience, SignatureAlgorithm alg, Object keyObj) {
		String jwt = "";
		io.jsonwebtoken.JwtBuilder builder = null;
        try {
        	builder = io.jsonwebtoken.Jwts.builder();
        	if( header != null && header.size() > 0 ) {
        		Iterator<String> iter = header.keySet().iterator();
        		while( iter.hasNext() ) {
        			String itemKey = iter.next();
        			builder.setHeaderParam(itemKey, header.get(itemKey));
        		}
        	}
        	if( body != null && body.size() > 0 ) {
        		Iterator<String> iter = body.keySet().iterator();
        		while( iter.hasNext() ) {
        			String itemKey = iter.next();
        			builder.claim(itemKey, body.get(itemKey));
        		}
        	}
        	if( !StringUtil.isEmpty(issuer) ) {
        		builder.setIssuer(issuer);
        	}
        	if( !StringUtil.isEmpty(audience) ) {
        		builder.setAudience(audience);
        	}
        	
            java.util.Date issDt = new java.util.Date();
            java.util.Date expDt = org.apache.commons.lang3.time.DateUtils.addHours(issDt, 1);
            
        	builder.setIssuedAt(issDt);
        	builder.setExpiration(expDt);
        	
        	// 대칭키 (HMAC)일 경우 - HS256, HS384, HS512 등... ==>> SecretKey 또는 byte[]
        	if( alg.getValue().startsWith("HS") ) {
        		if( keyObj instanceof String ) {
        			String keyStr = (String)keyObj;
        			jwt = builder.signWith(alg, keyStr).compact();
        		}else if( keyObj instanceof byte[] ) {
        			byte[] keyBytes = (byte[])keyObj;
        			jwt = builder.signWith(alg, keyBytes).compact();
        		}else {
        			throw new Exception("Algorithm과 키타입을 확인하세요. 대칭키 (HMAC)일 경우 - HS256, HS384, HS512 등... ==>> keyObj는 String 또는 byte[] 이어야 합니다.");
        		}
        	// 비대칭키일 경우 - RS256, RS384, RS512, ES256, ES384, ES512 등... ==>> PrivateKey
        	}else {
        		if( keyObj instanceof java.security.Key ) {
        			java.security.Key key = (java.security.Key)keyObj;
        			jwt = builder.signWith(alg, key).compact();
        		}else {
        			throw new Exception("Algorithm과 키타입을 확인하세요. 비대칭키일 경우 - RS256, RS384, RS512, ES256, ES384, ES512 등... ==>> keyObj는 PrivateKey 이어야 합니다.");
        		}
        	}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
		return jwt;
	}
	
	/**
	 * Jwt(Json Web Token)복호화 메서드
	 * @param jwt
	 * @param itemKey
	 * @return
	 */
	public static String getJwtDec(String jwt, String itemKey) {
		return getJwtDec(jwt, itemKey, SECRETE_KEY_FOR_JWT);
	}
	
	/**
	 * Jwt(Json Web Token)복호화 메서드
	 * @param jwt
	 * @param itemKey
	 * @param secretKey
	 * @return
	 */
	public static String getJwtDec(String jwt, String itemKey, String secretKey) {
		String jwtDec = "";
        try {
        	Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        	jwtDec = claims.getBody().get(itemKey).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return jwtDec;
	}
	/**
	 * Jwt(Json Web Token)복호화 메서드
	 * @param jwt
	 * @param itemKey
	 * @param key
	 * @return
	 */
	public static String getJwtDec(String jwt, String itemKey, Key key) {
		String jwtDec = "";
        try {
        	Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
        	jwtDec = claims.getBody().get(itemKey).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return jwtDec;
	}
	

	/**
	 * PEM에서 PKCS#8 개인키 추출
	 * @param path
	 * @return
	 */
	public static String loadPrivateKeyPem(String path) throws Exception {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }

	/**
	 * PEM에서 PKCS#8 개인키 추출
	 * @param keyPem
	 * @return
	 */
	public static RSAPrivateKey loadPrivateKeyFromPem(String keyPem) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }
}
