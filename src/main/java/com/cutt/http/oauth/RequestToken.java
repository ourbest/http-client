/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.cutt.http.oauth;


import com.cutt.http.HttpUtils;
import com.cutt.http.ServerResponse;
import org.apache.http.HttpException;

import static com.cutt.http.HttpUtils.strParams;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 *         representing unauthorized Request Token which is passed to the service provider when acquiring the authorized Access Token
 */
public class RequestToken extends OAuthToken {
    private static final long serialVersionUID = -8214365845469757952L;

    public RequestToken(ServerResponse res) throws OAuthException {
        super(res);
    }

    public RequestToken(String res) {
        super(res);
    }

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
    }

    public String getAuthenticationURL(String url) {
        return url + "?oauth_token=" + getToken();
    }

    public AccessToken getAccessToken(OAuthHandler handler, String url, String pin) throws OAuthException {
        try {
            handler.setAccessToken(this);
            handler.setOauthVerifier(pin);
            return new AccessToken(HttpUtils.fetch(url, strParams(/*"oauth_verifier", pin*/), true, handler));
        } catch (HttpException e) {
            throw new OAuthException();
        }
//        return new AccessToken(httpRequest(accessTokenURL, new PostParameter[0], true));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestToken that = (RequestToken) o;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result;
        return result;
    }
}
