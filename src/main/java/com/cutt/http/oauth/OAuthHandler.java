package com.cutt.http.oauth;

import com.cutt.http.AuthHandler;
import com.cutt.http.FileParameter;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created at 11-1-27
 *
 * @author yonghui.chen
 */
public class OAuthHandler implements AuthHandler {
    protected OAuth oauth;
    protected OAuthToken accessToken;

    public OAuthHandler(OAuth oauth) {
        this.oauth = oauth;
    }

    public OAuthHandler(OAuth oauth, OAuthToken accessToken) {
        this.oauth = oauth;
        this.accessToken = accessToken;
    }

    public void setAccessToken(OAuthToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void handle(HttpUriRequest request, List<NameValuePair> params) {
        String url = request.getURI().toString();
        List<NameValuePair> signatureBaseParams = new ArrayList<NameValuePair>();
        if (params != null) {
            for (NameValuePair nameValuePair : params) {
                if (!(nameValuePair instanceof FileParameter)) {
                    signatureBaseParams.add(new BasicNameValuePair(nameValuePair.getName(), nameValuePair.getValue()));
                }
            }
        }
/*
        if (request instanceof HttpPost) {
            HttpPost p = (HttpPost) request;
            HttpEntity entity = p.getEntity();
            try {
                List<NameValuePair> pairs = URLEncodedUtils.parse(entity);
                for (NameValuePair nameValuePair : pairs) {
                    if (!(nameValuePair instanceof FileParameter)) {
                        signatureBaseParams.add(new BasicNameValuePair(nameValuePair.getName(), nameValuePair.getValue()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

        String post = oauth.generateAuthorizationHeader(request.getMethod(), url, signatureBaseParams, accessToken);
        request.addHeader(new BasicHeader("Authorization", post));
    }

    public void setOauthVerifier(String oauthVerifier) {
        oauth.setOauthVerifier(oauthVerifier);
    }
}
