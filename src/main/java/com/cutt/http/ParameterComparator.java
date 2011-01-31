package com.cutt.http;

import org.apache.http.NameValuePair;

import java.util.Comparator;

/**
 * Created at 11-1-27
 *
 * @author yonghui.chen
 */
public class ParameterComparator implements Comparator<NameValuePair> {
    @Override
    public int compare(NameValuePair o1, NameValuePair o2) {
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        return o1.getName().compareTo(o2.getName());
    }
}
