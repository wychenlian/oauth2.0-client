package com.hybris.caas.oauth.client.web;


import lombok.NoArgsConstructor;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth/client")
@NoArgsConstructor
public class OauthController {
    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);
    String clientId = null;
    String clientSecret = null;
    String accessTokenUrl = null;
    String userInfoUrl = null;
    String redirectUrl = null;
    String response_type = null;
    String code= null;

    /**
     * Get authorization code.
     * */
    @GetMapping("/authorize")
    public void   getAuthorizationCode(@RequestParam("response_type") String responseType ,
                                       @RequestParam("client_id") String clientId,
                                       HttpServletRequest request, HttpServletResponse response)
    throws IOException{
        clientId = "clientId";
        clientSecret = "clientSecret";
        accessTokenUrl = "authorize";
        redirectUrl = "http://localhost:8081/oauth/client/token";
        response_type = "code";
        System.out.println("-----------客户端/Get Authorize Code By ClientId Begin--------------------------------------------------------------------------------");

        OAuthClient oAuthClient =new OAuthClient(new URLConnectionClient());
        String requestUrl = null;
        try {
            //构建oauthd的请求。设置请求服务地址（accessTokenUrl）、clientId、response_type、redirectUrl
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .authorizationLocation(accessTokenUrl)
                    .setResponseType(response_type)
                    .setClientId(clientId)
                    .setRedirectURI(redirectUrl)
                    .buildQueryMessage();
            requestUrl = accessTokenRequest.getLocationUri();
            System.out.println(requestUrl);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }
        System.out.println("-----------客户端/Get Authorize Code By ClientId End--------------------------------------------------------------------------------");

        response.sendRedirect("http://localhost:8082/oauth/server/"+requestUrl);
    }

    //接受客户端返回的code，提交申请access token的请求
    @RequestMapping("/token")
    public void getAccessTokenByCode(HttpServletRequest request,HttpServletResponse response)throws OAuthProblemException ,IOException{
        System.out.println("-----------客户端/Get AccessToken By Code Begin--------------------------------------------------------------------------------");
        clientId = "clientId";
        clientSecret = "clientSecret";
        accessTokenUrl="http://localhost:8082/oauth/server/token";
        userInfoUrl = "userInfoUrl";
        redirectUrl = "http://localhost:8081/oauth/client/userInfo";
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        code = httpRequest.getParameter("code");
        System.out.println(code);
        OAuthClient oAuthClient =new OAuthClient(new URLConnectionClient());
        try {
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .tokenLocation(accessTokenUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setCode(code)
                    .setRedirectURI(redirectUrl)
                    .buildQueryMessage();
            //去服务端请求access token，并返回响应
            OAuthAccessTokenResponse oAuthResponse =oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
            //获取服务端返回过来的access token
            String accessToken = oAuthResponse.getAccessToken();
            //查看access token是否过期
            Long expiresIn =oAuthResponse.getExpiresIn();
            System.out.println("客户端/callbackCode方法的token：：："+accessToken);
            System.out.println("----------客户端/Get AccessToken By Code End--------------------------------------------------------------------------------");
            response.sendRedirect("http://localhost:8081/oauth/client/userInfo?accessToken="+accessToken);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }
    }


    //接受服务端传回来的access token，由此token去请求服务端的资源（用户信息等）
    @RequestMapping("/userInfo")
    public String getUserInfoByAccessToken(String accessToken) {
        System.out.println("---------客户端/Get UserInfo by AccessToken Begin----------------------------------------------------------------");
        userInfoUrl = "http://localhost:8082/userInfo";
        System.out.println("accessToken:"+accessToken);
        OAuthClient oAuthClient =new OAuthClient(new URLConnectionClient());

        try {

            OAuthClientRequest userInfoRequest =new OAuthBearerClientRequest(userInfoUrl)
                    .setAccessToken(accessToken).buildQueryMessage();
            OAuthResourceResponse resourceResponse =oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            System.out.println("---------客户端/Get UserInfo by AccessToken End----------------------------------------------------------------------------------");
            return  resourceResponse.getBody();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
        System.out.println("---------客户端/accessToken----------------------------------------------------------------------------------");
        return null;
    }
}
