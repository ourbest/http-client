package com.cutt.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;

/**
 * Created at 11-1-18
 *
 * @author yonghui.chen
 */
public interface AuthHandler {
    void handle(HttpUriRequest request, List<NameValuePair> params);
}
