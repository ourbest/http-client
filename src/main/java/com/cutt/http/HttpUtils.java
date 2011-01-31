package com.cutt.http;

import com.cutt.http.oauth.AccessToken;
import com.cutt.http.oauth.OAuth;
import com.cutt.http.oauth.OAuthHandler;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created at 2010-11-25
 *
 * @author yonghui.chen
 */
public class HttpUtils {
    public static final String IPHONE_AGENT = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.05 Mobile/8A293 Safari/6531.22.7";
    public static final String FIREFOX_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
    public static String USER_AGENT = FIREFOX_AGENT;

    static int ip = 131;

    private static final String UTF_8 = "UTF-8";
    public static boolean proxy;

    public static String shorten(String longURL) throws IOException, JSONException {
        AccessToken at = new AccessToken("1/bZC8QmbzXDrFiv6p7LQX17ADiwUZ4ak7cXPYEUB3n_U", "9obFaGOL1gw903IRpfvQnpN8");
        OAuthHandler google = new OAuthHandler(new OAuth("www.bituibang.com", "YJNbJ0KSRP3qS5zVan6RXE7d"), at);
        String url = "https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyD41_DF3Oe2L_N80ODHeOLdB2WRsh1WUHU";

        JSONObject jo = new JSONObject();
        jo.put("longUrl", longURL);
        ServerResponse ret = HttpUtils.postJSON(url, jo.toString(), google);
        return new JSONObject(ret.getBody()).getString("id");
    }

    public static ServerResponse postJSON(String url, String json, AuthHandler authHandler) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(json));
        post.addHeader("Content-Type", "application/json");
        HttpClient client = getClient();
        ServerResponse resp;
        try {
            authHandler.handle(post, null);
            HttpResponse response = client.execute(post);
            resp = new ServerResponse(response.getStatusLine().getStatusCode(),
                    new String(extractGZipContent(response), getCharset(response.getEntity())));
            resp.setHeaders(response.getAllHeaders());
        } finally {
            client.getConnectionManager().shutdown();
        }
        return resp;
    }

    public static ServerResponse get(String url) throws HttpException {
        return fetch(url);
    }

    public static ServerResponse get(String url, AuthHandler authHandler) throws HttpException {
        return fetch(url, authHandler);
    }

    public static ServerResponse get(String url, List<NameValuePair> params) throws HttpException {
        return fetch(url, params, false);
    }

    public static ServerResponse get(String url, List<NameValuePair> params, AuthHandler authHandler) throws HttpException {
        return fetch(url, params, false, authHandler);
    }

    public static ServerResponse get(String url,
                                     List<NameValuePair> params,
                                     String username,
                                     String password) throws HttpException {
        return fetchURL(url, params, username, password, false, false, null);
    }

    public static ServerResponse get(String url,
                                     List<NameValuePair> params,
                                     String username,
                                     String password,
                                     AuthHandler authHandler) throws HttpException {
        return fetchURL(url, params, username, password, false, false, authHandler);
    }

    public static ServerResponse post(String url, List<NameValuePair> params) throws HttpException {
        return fetch(url, params, true);
    }

    public static ServerResponse post(String url, List<NameValuePair> params, AuthHandler authHandler) throws HttpException {
        return fetch(url, params, true, authHandler);
    }

    public static ServerResponse post(String url,
                                      List<NameValuePair> params,
                                      String username,
                                      String password) throws HttpException {
        return fetchURL(url, params, username, password, true, false, null);
    }

    public static ServerResponse post(String url,
                                      List<NameValuePair> params,
                                      String username,
                                      String password,
                                      AuthHandler authHandler) throws HttpException {
        return fetchURL(url, params, username, password, true, false, authHandler);
    }

    // 简单get
    public static ServerResponse fetch(String url) throws HttpException {
        return fetch(url, strParams(), null, null, false);
    }

    public static ServerResponse fetch(String url, AuthHandler authHandler) throws HttpException {
        return fetch(url, strParams(), null, null, false, authHandler);
    }

    // 无认证信息的方法
    public static ServerResponse fetch(String url,
                                       List<NameValuePair> params,
                                       boolean post) throws HttpException {
        return fetch(url, params, null, null, post);
    }

    public static ServerResponse fetch(String url,
                                       List<NameValuePair> params,
                                       boolean post,
                                       AuthHandler authHandler) throws HttpException {
        return fetch(url, params, null, null, post, authHandler);
    }

    // 用户名密码是用basic认证的用户名密码
    public static ServerResponse fetch(String url,
                                       List<NameValuePair> params,
                                       String username,
                                       String password,
                                       boolean post) throws HttpException {
        return fetchURL(url, params, username, password, post, false, null);
    }

    public static ServerResponse fetch(String url,
                                       List<NameValuePair> params,
                                       String username,
                                       String password,
                                       boolean post,
                                       AuthHandler authHandler) throws HttpException {
        return fetchURL(url, params, username, password, post, false, authHandler);
    }

    // get 二进制内容
    public static ServerResponse fetchRaw(String url) throws HttpException {
        return fetchURL(url, strParams(), null, null, false, true, null);
    }

    public static ServerResponse fetchRaw(String url, AuthHandler authHandler) throws HttpException {
        return fetchURL(url, strParams(), null, null, false, true, authHandler);
    }

    public static ServerResponse delete(String url, List<NameValuePair> params,
                                        String loginName, String password) throws HttpException {
        return delete(url, params, loginName, password, null);
    }

    // REST中的delete method
    public static ServerResponse delete(String url, List<NameValuePair> params,
                                        String loginName, String password,
                                        AuthHandler authHandler) throws HttpException {
        HttpClient client = getClient();
        try {
            setProxy(client);

            String query = URLEncodedUtils.format(params, UTF_8);
            if (url.indexOf('?') == -1) {
                url += "?" + query;
            } else {
                if (url.charAt(url.length() - 1) == '&') {
                    url += query;
                } else {
                    url += "&" + query;
                }
            }
            HttpDelete delete = new HttpDelete(url);
            if (loginName != null) {
                delete.addHeader("Authorization",
                        "Basic " + BASE64Encoder.encode((loginName + ":" + password).getBytes()));
            }
            if (authHandler != null) {
                authHandler.handle(delete, null);
            }
            HttpResponse response = client.execute(delete);
            ServerResponse resp = new ServerResponse(response.getStatusLine().getStatusCode(),
                    new String(extractGZipContent(response), getCharset(response.getEntity())));
            resp.setHeaders(response.getAllHeaders());
            return resp;
        } catch (ClientProtocolException e) {
            throw new HttpException(e.getMessage(), e);
        } catch (IOException e) {
            throw new HttpException(e.getMessage(), e);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    // 获取redirect的url，仅取一次重定向
    public static String getTargetURL(String url) throws IOException {
        HttpClient client = getClient();

        HttpGet g = new HttpGet(url);
        client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

        HttpResponse response = client.execute(g);
        int statusCode = response.getStatusLine().getStatusCode();
        response.getEntity().consumeContent();
        if (statusCode >= 300 && statusCode < 400) {
            return response.getFirstHeader("Location").getValue();
        }
        return url;
    }

    // 获取redirect的url，直到最后的重定向
    public static String getFinalTargetURL(String url) throws IOException {
        HttpClient client = getClient();

        HttpGet g = new HttpGet(url);
        client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

        HttpResponse response = client.execute(g);
        int statusCode = response.getStatusLine().getStatusCode();
        response.getEntity().consumeContent();
        if (statusCode >= 300 && statusCode < 400) {
            String ru = response.getFirstHeader("Location").getValue();
            return getFinalTargetURL(ru);
        }
        return url;
    }

    // 两个创建键值对的方法
    // params("a", "1", "b", "2") 即 a=1&b=2
    public static List<NameValuePair> strParams(String... params) {
        if (params == null || params.length == 0) return Collections.emptyList();
        if (params.length % 2 == 1) throw new IllegalArgumentException("参数必须是偶数个");
        List<NameValuePair> ret = new ArrayList<NameValuePair>(params.length / 2);
        for (int i = 0; i < params.length / 2; i++) {
            ret.add(new BasicNameValuePair(params[i * 2], params[i * 2 + 1]));
        }
        return ret;
    }

    public static List<NameValuePair> params(NameValuePair... param) {
//        return new ArrayList<NameValuePair>(Arrays.asList(param));
        return Arrays.asList(param);
    }

    // 内部方法
    private static HttpClient getClient() {
        HttpClient client;// = new DefaultHttpClient();
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        supportedSchemes.register(new Scheme("http",
                PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));
        HttpParams params = new BasicHttpParams();
//        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(100));
        HttpProtocolParams.setContentCharset(params, UTF_8);

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
                supportedSchemes);

        client = new DefaultHttpClient(ccm, params);
        client.getParams().setParameter(HttpProtocolParams.USER_AGENT, USER_AGENT);
        return client;
    }


    // 内部实现
    private static ServerResponse fetchURL(String url, List<NameValuePair> params,
                                           String username, String password,
                                           boolean post, boolean bin,
                                           AuthHandler authHandler) throws HttpException {
        HttpClient client = getClient();
        try {
            HttpUriRequest request;

            setProxy(client);
            boolean multipart = false;
            if (post) {
                HttpPost p = new HttpPost(url);

                for (NameValuePair param : params) {
                    if (param instanceof FileParameter) {
                        multipart = true;
                        break;
                    }
                }

                p.addHeader("Accept-Encoding", "gzip, deflate");
                if (!multipart) {
                    p.setEntity(new UrlEncodedFormEntity(params, UTF_8));
                } else {
                    MultipartEntity entity = new MultipartEntity();
                    for (NameValuePair param : params) {
                        if (param instanceof FileParameter) {
                            entity.addPart(param.getName(), ((FileParameter) param).toBody());
                        } else {
                            entity.addPart(param.getName(), new StringBody(param.getValue(), Charset.forName(UTF_8)));
                        }
                    }
                    p.setEntity(entity);
                }
                request = p;
            } else {
                String query = URLEncodedUtils.format(params, UTF_8);
                if (query.length() > 0) {
                    if (url.indexOf('?') == -1) {
                        url += "?" + query;
                    } else {
                        if (url.charAt(url.length() - 1) == '&') {
                            url += query;
                        } else {
                            url += "&" + query;
                        }
                    }
                }
                request = new HttpGet(url);
                request.addHeader("Accept-Encoding", "gzip, deflate");
            }
            if (authHandler != null) authHandler.handle(request, params);

            HttpContext context = new BasicHttpContext();
            if (username != null) {
                request.addHeader("Authorization",
                        "Basic " + BASE64Encoder.encode((username + ":" + password).getBytes()));
            }
            HttpResponse response = client.execute(request, context);
            if (post) {
                int statusCode = response.getStatusLine().getStatusCode();
                switch (statusCode) {
                    case HttpStatus.SC_MOVED_TEMPORARILY:
                    case HttpStatus.SC_MOVED_PERMANENTLY:
                    case HttpStatus.SC_TEMPORARY_REDIRECT:
                    case HttpStatus.SC_SEE_OTHER:
                        Header locationHeader = response.getFirstHeader("location");

                        String location = locationHeader.getValue();

                        URI uri;
                        try {
                            uri = new URI(location);
                        } catch (URISyntaxException ex) {
                            throw new ProtocolException("Invalid redirect URI: " + location, ex);
                        }
                        HttpParams params2 = response.getParams();
                        // rfc2616 demands the location value be a complete URI
                        // Location       = "Location" ":" absoluteURI
                        if (!uri.isAbsolute()) {
                            if (params2.isParameterTrue(ClientPNames.REJECT_RELATIVE_REDIRECT)) {
                                throw new ProtocolException("Relative redirect location '"
                                        + uri + "' not allowed");
                            }
                            // Adjust location URI
                            HttpHost target = (HttpHost) context.getAttribute(
                                    ExecutionContext.HTTP_TARGET_HOST);
                            if (target == null) {
                                throw new IllegalStateException("Target host not available " +
                                        "in the HTTP context");
                            }

                            HttpRequest req = (HttpRequest) context.getAttribute(
                                    ExecutionContext.HTTP_REQUEST);

                            try {
                                URI requestURI = new URI(req.getRequestLine().getUri());
                                URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
                                uri = URIUtils.resolve(absoluteRequestURI, uri);
                            } catch (URISyntaxException ex) {
                                throw new ProtocolException(ex.getMessage(), ex);
                            }
                        }
                        HttpGet get = new HttpGet(uri);
                        if (authHandler != null) authHandler.handle(request, null);
                        response = client.execute(get, context);
                } //end of switch
            }
            ServerResponse resp = bin ?
                    new ServerResponse(response.getStatusLine().getStatusCode(),
                            extractGZipContent(response)) :
                    new ServerResponse(response.getStatusLine().getStatusCode(),
                            new String(extractGZipContent(response), getCharset(response.getEntity())));
            resp.setHeaders(response.getAllHeaders());
            return resp;
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), e);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }


    private static String getCharset(HttpEntity entity) {
        String charset = EntityUtils.getContentCharSet(entity);
        if (charset == null) {
            charset = UTF_8;
        }
        return charset;
    }

    private static byte[] extractGZipContent(HttpResponse response) throws IOException {
        InputStream in = response.getEntity().getContent();
        if (response.containsHeader("Content-Encoding")) {
            Header header = response.getFirstHeader("Content-Encoding");
            if (header.getValue().toLowerCase().indexOf("gzip") != -1) {
                in = new GZIPInputStream(in);
            }
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream(10240);
        byte[] buffer = new byte[1024 * 4];
//        long count = 0;
        int n;
        while (-1 != (n = in.read(buffer))) {
            output.write(buffer, 0, n);
//            count += n;
        }
        return output.toByteArray();
    }

    private static void setProxy(HttpClient client) {
        if (proxy) {
            if (ip > 239) {
                ip = 130;
            }
            String proxy = "218.240.62." + ip++;
            HttpHost proxyHost = new HttpHost(proxy, 13218, "http");
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
        }
    }

}
