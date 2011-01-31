import com.cutt.http.HttpUtils;
import com.cutt.http.ServerResponse;
import com.cutt.http.oauth.*;
import org.apache.http.HttpException;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author chen
 * @date 11-1-27
 */
public class GoogleOauth {
    public static void main(String[] args) throws HttpException, OAuthException, IOException, JSONException {
        System.out.println(HttpUtils.shorten("http://www.bituibang.com/btui"));
    }

    private static void googlr() throws HttpException, IOException, OAuthException {
    OAuthHandler google = new OAuthHandler(new OAuth("www.bituibang.com", "YJNbJ0KSRP3qS5zVan6RXE7d"));
        ServerResponse serverResponse = HttpUtils.post("https://www.google.com/accounts/OAuthGetRequestToken",
                HttpUtils.strParams("scope", "https://www.googleapis.com/auth/urlshortener",
                        "oauth_callback", "http://www.bituibang.com/abcd"), google);

        System.out.println("serverResponse = " + serverResponse);

        // oauth_verifier=u02IWpjmA6kNVME5DSRsmOOl&oauth_token=4%2FQennLAWT02uxocbcikE6q1fMpF9J
        RequestToken rt = new RequestToken(serverResponse.getBody());
        System.out.println("https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=" + rt.getToken());
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
//        google = new OAuthHandler(new OAuth("www.bituibang.com", "YJNbJ0KSRP3qS5zVan6RXE7d"), rt);
//        rt.getAccessToken(google, "", "u02IWpjmA6kNVME5DSRsmOOl");

        AccessToken t = rt.getAccessToken(google, "https://www.google.com/accounts/OAuthGetAccessToken", s);

        System.out.println("post = " + t);}
}
