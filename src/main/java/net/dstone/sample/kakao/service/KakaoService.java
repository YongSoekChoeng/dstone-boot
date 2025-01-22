package net.dstone.sample.kakao.service;

import net.dstone.common.biz.BaseService;
import net.dstone.common.config.ConfigProperty;
import net.dstone.common.utils.LogUtil;
import net.dstone.sample.kakao.vo.KakaoTokenResponseVo;
import net.dstone.sample.kakao.vo.KakaoUserInfoResponseVo;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Service
@ConfigurationProperties("interface.kakao")
public class KakaoService extends BaseService { 

    private final String KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com/oauth/token";
    private final String KAUTH_USER_URL_HOST ="https://kapi.kakao.com/v2/user/me";


    public String getAccessTokenFromKakao(String code) {

    	String accessToken = "";
    	String refreshToken = "";
    	
    	String clientId = ConfigProperty.getProperty("interface.kakao.client-id");
    	String redirectUri = ConfigProperty.getProperty("interface.kakao.redirect-uri");
    	
		net.dstone.common.utils.WsUtil ws = new net.dstone.common.utils.WsUtil();
		try {
			
			net.dstone.common.utils.WsUtil.Bean wsBean = new net.dstone.common.utils.WsUtil.Bean();
			
			// URL 세팅
			wsBean.url = KAUTH_TOKEN_URL_HOST + "?";
			wsBean.url += "grant_type=authorization_code";
			wsBean.url += "&client_id=" + clientId;
			wsBean.url += "&redirect_uri=" + redirectUri;
			wsBean.url += "&code=" + code;
			wsBean.method = "GET";	
			
			// Header 세팅
			wsBean.setContentType("application/x-www-form-urlencoded;charset=utf-8");
			
			// 호출
			String jsonStr = ws.execute(wsBean);
			info( "ws.StatusCd ===>>>" + ws.StatusCd );
			
			if( ws.StatusCd == net.dstone.common.utils.WsUtil.HTTP_OK ) {
				JSONParser parser = new JSONParser();
				JSONObject element     = (JSONObject)parser.parse(jsonStr);
				accessToken = element.get("access_token").toString();
				refreshToken = element.get("refresh_token").toString();
				info( "accessToken["+accessToken+"]" + " refreshToken["+refreshToken+"]" );
			}else {
				throw new Exception( "KAKAO 인증토큰을 얻는데 실패하였습니다." +  "accessToken["+accessToken+"]" + " refreshToken["+refreshToken+"]"  );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		}
		
        return accessToken;
    }




    public java.util.Map<String, String> getUserInfo(String accessToken) {
    	
    	java.util.Map<String, String> userInfo = new java.util.HashMap<String, String>();

		net.dstone.common.utils.WsUtil ws = new net.dstone.common.utils.WsUtil();
		try {
			
			net.dstone.common.utils.WsUtil.Bean wsBean = new net.dstone.common.utils.WsUtil.Bean();
			
			// URL 세팅
			wsBean.url = KAUTH_USER_URL_HOST ;
			wsBean.method = "POST";	
			
			// Header 세팅
			wsBean.addHeader("Authorization", "Bearer " + accessToken);
			wsBean.setContentType("application/x-www-form-urlencoded;charset=utf-8");
			
			// 호출
			String jsonStr = ws.execute(wsBean);
			info( "ws.StatusCd ===>>>" + ws.StatusCd );
			
			if( ws.StatusCd == net.dstone.common.utils.WsUtil.HTTP_OK ) {
				JSONParser parser = new JSONParser();
				JSONObject element     = (JSONObject)parser.parse(jsonStr);
				
//				{
//					"id": 3886924629,
//					"connected_at": "2025-01-21T00:01:09Z",
//					"kakao_account": {
//						"email_needs_agreement": false,
//						"profile_nickname_needs_agreement": false,
//						"profile": {
//							"is_default_nickname": false,
//							"nickname": "정용석"
//						},
//						"is_email_valid": true,
//						"is_email_verified": true,
//						"has_email": true,
//						"email": "jysn007@gmail.com"
//					},
//					"properties": {
//						"nickname": "정용석"
//					}
//				}
				
				JSONObject kakaoAccount = (JSONObject)element.get("kakao_account");
				JSONObject properties = (JSONObject)element.get("properties");
				userInfo.put("id", element.get("id").toString());
				userInfo.put("email", kakaoAccount.get("email").toString());
				userInfo.put("nickname", properties.get("nickname").toString());

				info( "userInfo ===>>>" + userInfo );
			}else {
				throw new Exception( "KAKAO 개인정보를 얻는데 실패하였습니다."  );
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		}
		
		return userInfo;

    }
}
