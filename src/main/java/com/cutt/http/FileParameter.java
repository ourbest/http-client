package com.cutt.http;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created at 11-1-26
 *
 * @author yonghui.chen
 */
public class FileParameter implements NameValuePair {
    private String name;
    private InputStream in;
    private File file;

    public FileParameter(String name, File file) throws FileNotFoundException {
        this.name = name;
        this.file = file;
    }

    public FileParameter(String name, byte[] data) {
        this.name = name;
        this.in = new ByteArrayInputStream(data);
    }

    public FileParameter(String name, InputStream in) {
        this.name = name;
        this.in = in;
    }

    @Override
    public String getName() {
        return name;
    }

    public InputStream getData() {
        return in;
    }

    @Override
    public String getValue() {
        return "bin";
    }

    public ContentBody toBody() {
        if (file != null) {
            return new FileBody(file);
        } else {
            return new InputStreamBody(in, name);
        }
    }
}
