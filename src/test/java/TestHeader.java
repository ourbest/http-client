import com.cutt.http.FileParameter;
import com.cutt.http.HttpUtils;
import com.cutt.http.ServerResponse;
import com.cutt.http.oauth.AccessToken;
import com.cutt.http.oauth.OAuth;
import com.cutt.http.oauth.OAuthHandler;
import org.apache.http.HttpException;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;

import static com.cutt.http.HttpUtils.params;
import static com.cutt.http.HttpUtils.strParams;

/**
 * Created at 11-1-26
 *
 * @author yonghui.chen
 */
public class TestHeader {
    public static void main(String[] args) throws HttpException, FileNotFoundException {
        testSina();
    }

    private static void test163() throws HttpException {
        //        XAuthHandler handler = new XAuthHandler("1", "2");
//        ServerResponse ret = HttpUtils.fetch("http://api.t.163.com/oauth/access_token",
//                HttpUtils.strParams("a", "n"), true, handler);
//        System.out.println(ret);
        OAuthHandler o2 = new OAuthHandler(new OAuth("kiWxO5xicQNyKcGk", "QIDHhw4lRGJmJyHVUZPmartOyVr8281f"));
//        XAuthHandler xAuthHandler = new XAuthHandler("tsina.dev@cutt.com", "keygatebeijing");
/*
        OAuthHandler oAuthHandler = new OAuthHandler(new ApiKey("kiWxO5xicQNyKcGk", "QIDHhw4lRGJmJyHVUZPmartOyVr8281f"));
*/
//        List<NameValuePair> parameters = xAuthHandler.getParameters("http://api.t.163.com/oauth/access_token");
        ServerResponse response = HttpUtils.fetch("http://api.t.163.com/oauth/access_token",
                strParams("x_auth_username", "tsina.dev@cutt.com", "x_auth_password", "keygatebeijing", "x_auth_mode", "client_auth"), true, o2);
        System.out.println("response = " + response);
    }

    private static void sinaXauth() throws HttpException {
        ServerResponse response;
        OAuth apiKey = new OAuth("1841617959", "168c7af5bcf6fee9cac1e803f6aff68b");
/*
        xAuthHandler = new XAuthHandler(apiKey, "chyh@msn.com", "chenyh");
        response = HttpUtils.fetch("http://api.t.sina.com.cn/oauth/access_token", params(), true, xAuthHandler);
        System.out.println("response = " + response);
*/
    }

    private static void testSina() throws HttpException, FileNotFoundException {
        OAuth apiKey = new OAuth("1841617959", "168c7af5bcf6fee9cac1e803f6aff68b");
        AccessToken userKey = new AccessToken("3930c0c0347cca06f4f3b7f687e1f159", "5fbf02184b62e0f32f91ec84966e789a");

        OAuthHandler authHandler = new OAuthHandler(apiKey, userKey);
        ServerResponse fetch = HttpUtils.fetch(
                "http://api.t.sina.com.cn/account/verify_credentials.json", authHandler);
        System.out.println("fetch = " + fetch);

//        imageTest(authHandler);
    }

    private static void imageTest(OAuthHandler authHandler) throws HttpException, FileNotFoundException {
        ServerResponse ret = HttpUtils.post("http://api.t.sina.com.cn/statuses/upload.json",
                params(new BasicNameValuePair("status", "发个图片"),
                        new FileParameter("pic", new File("test.png"))), authHandler);

        System.out.println(ret);
    }
}
