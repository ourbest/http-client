package com.cutt.http;

import org.apache.http.Header;

/**
 * Created at 2010-11-25
 *
 * @author yonghui.chen
 */
public class ServerResponse {
    private int code;
    private String body;
    private byte[] binary;
    private Header[] headers;

    public ServerResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public ServerResponse(int code, byte[] binary) {
        this.code = code;
        this.binary = binary;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
