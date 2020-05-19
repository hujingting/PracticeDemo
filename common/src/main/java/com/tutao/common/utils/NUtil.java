package com.tutao.common.utils;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.app.download.MD5;
import com.app.model.RuntimeData;
import com.app.model.net.HTTPCaller;
import com.app.model.net.NameValuePair;
import com.tutao.common.model.RuntimeData;
import com.tutao.common.network.HTTPCaller;

import java.util.Arrays;
import java.util.List;

public class NUtil {


    public static String get(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.contains("?")) {
            url = url + "&";
        } else {
            url = url + "?";
        }
        url += HTTPCaller.Instance().getCommonFieldString();
        MLog.e("hdp", "url:" + url);
        if (RuntimeData.getInstance().getDeviceId() != null) {
            String dno = RuntimeData.getInstance().getDeviceId();
            Uri uri = Uri.parse(url);
            String jni_dno = uri.getQueryParameter("dno");
            if (TextUtils.isEmpty(jni_dno)) {
                url += "&dno=" + dno;
            }
        } else {
            String dno=getDno();
            Uri uri = Uri.parse(url);
            String jni_dno = uri.getQueryParameter("dno");
            if (TextUtils.isEmpty(jni_dno)) {
                url += "&dno=" + dno;
            }
            RuntimeData.getInstance().setDeviceId(dno);
        }
        return getGetKey(url);
    }

    public static String getGetKey(String url) {
        String h = "";
        String passUrl = "";
        passUrl = url.substring(url.indexOf("?") + 1, url.length());
        passUrl = DecryptUtil.URLDecoder(passUrl);
        String commonFieldString[] = passUrl.split("&");
        Arrays.sort(commonFieldString);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < commonFieldString.length; i++) {
            stringBuffer.append(commonFieldString[i]);
            if (i != commonFieldString.length - 1) {
                stringBuffer.append("&");
            }
        }
        h = MD5.getMd5Value(stringBuffer.toString());
        h = MD5.getMd5Value(h).toLowerCase();
        return url + "&h=" + h;
    }

    private static NameValuePair getDnoPair(List<NameValuePair> form) {
        for (NameValuePair item : form) {
            if (item.getName().equals("dno")) {
                return item;
            }
        }
        return null;
    }

    public static void post(List<NameValuePair> form) {
        form.addAll(HTTPCaller.Instance().getCommonField());
        String dno = RuntimeData.getInstance().getDeviceId();
        if (!TextUtils.isEmpty(dno)) {
            NameValuePair jni_dno_pair = getDnoPair(form);
            if (jni_dno_pair == null) {
                NameValuePair nameValuePair = new NameValuePair("dno", dno);
                form.add(nameValuePair);
            }
        } else {
            NameValuePair jni_dno_pair = getDnoPair(form);
            if (jni_dno_pair != null && !TextUtils.isEmpty(jni_dno_pair.getValue())) {

                RuntimeData.getInstance().setDeviceId(jni_dno_pair.getValue());
            } else {
                String dnow = getDno();
                NameValuePair nameValuePair = new NameValuePair("dno", dnow);
                form.add(nameValuePair);
                RuntimeData.getInstance().setDeviceId(dnow);
            }
        }
        getPostKey(form);
    }
    public static String ASCIISort(String str) {
        int total = 0;
        for (int i = 0; i < str.length(); i++) {
            //字符串处理每次读取一位。
            total = total + str.charAt(i);
        }
        total = total % 255;
        return byteToHex(total);
    }

    /**
     * 将一个整形化为十六进制，并以字符串的形式返回
     */
    private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteToHex(int n) {
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }

    /**
     * post请求加密
     *
     * @param form
     */
    public static void getPostKey(List<NameValuePair> form) {
        String[] test = new String[form.size()];
        String h = "";
        for (int i = 0; i < form.size(); i++) {
            NameValuePair nameValuePair = form.get(i);
            test[i] = nameValuePair.getName() + "=" + nameValuePair.getValue();
        }
        Arrays.sort(test);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < test.length; i++) {
            sb.append(test[i]);
            if (i != test.length - 1) {
                sb.append("&");
            }
        }
        h = MD5.getMd5Value(sb.toString());
        h = MD5.getMd5Value(h).toLowerCase();
        form.add(new NameValuePair("h", h));
    }

    /**
     * 新的dno生成规则
     * @return
     */

    public static String getDno() {
        String ei = "";
        String mac = "";
        String dno = "";
        String md5 = "";
        Context context = RuntimeData.getInstance().getContext();
        ei = Util.getAndroidId(context);
        if (!TextUtils.isEmpty(ei)) {
            ei = Base64.encodeToString(ei.getBytes(), Base64.DEFAULT);
        }
        mac = Util.getMacAddress(RuntimeData.getInstance().getContext());
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(ei)) {
            sb.append(ei);
        }
        if (!TextUtils.isEmpty(mac)) {
            sb.append(mac);
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            md5 = MD5.getMd5Value(sb.toString());
            dno = ASCIISort(md5);
        }
        return (md5 + dno).toLowerCase();
    }

}
