package com.tutao.common.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.tutao.common.model.RuntimeData;
import com.tutao.common.utils.BaseUtil;
import com.tutao.common.utils.MLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;


/**
 * @author guopeng
 * @ClassName: HTTPCaller
 * @Description: TODO(HTTP请求发起和数据解析转换)
 * @date 2014 2014年9月18日 下午9:24:50
 */

public class HTTPCaller {

    private static HTTPCaller _instance = null;
    private OkHttpClient client;// 实例化对象
    private Context context = null;
    private Map<String, Call> requestHandleMap = null;//以URL为KEY存储的请求
    private StringBuilder errorSB = null;
    private CacheControl cacheControl = null;

    private HttpConfig httpConfig = new HttpConfig();//配置信息

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CallbackMessage data = (CallbackMessage) msg.obj;
            data.callback();
        }
    };

    private HTTPCaller() {
        requestHandleMap = Collections.synchronizedMap(new WeakHashMap<String, Call>());
        cacheControl = new CacheControl.Builder().noStore().noCache().build();
    }

    //单例模式
    public static HTTPCaller Instance() {
        if (_instance == null) {
            _instance = new HTTPCaller();
        }
        return _instance;
    }

    //创建新的实例
    public static HTTPCaller newInstance() {
        return new HTTPCaller();
    }

    public HTTPCaller setContext(Context ctx) {
        this.context = ctx;
        if (context == null) {
            MLog.e(httpConfig.getTagName(), "HTTPCaller的context是NULL");
        }
        return this;
    }

    public HTTPCaller setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;

        this.httpConfig.setUserAgent(BaseUtil.getUserAgent(RuntimeData.getInstance().getAppConfig()));

//        if(RuntimeData.getInstance().getAppConfig().httpdns){
//            client = new OkHttpClient.Builder()
//                    .connectTimeout(httpConfig.getConnectTimeout(), TimeUnit.SECONDS)
//                    .writeTimeout(httpConfig.getWriteTimeout(), TimeUnit.SECONDS)
//                    .readTimeout(httpConfig.getReadTimeout(), TimeUnit.SECONDS)
//                    .dns(OkHttpDns.getInstance(RuntimeData.getInstance().getContext()))
//                    .build();
//        }else{
        client = new OkHttpClient.Builder()
                .connectTimeout(httpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .build();
//        }

        return this;
    }

//    public void downloadFile(String url, String saveFilePath, ProgressUIListener progressUIListener) {
//        downloadFile(url, saveFilePath, null, RuntimeData.getInstance().getHeaders(), progressUIListener, true);
//    }

    /**
     * 断点续传下载
     *
     * @param url                下载地址
     * @param saveFilePath       保存路径
     * @param startBytes         断点位置
     * @param progressUIListener
     */
//    public void downloadFileWithRange(String url, String saveFilePath, String startBytes, ProgressUIListener progressUIListener) {
//        downloadFile(url, saveFilePath, startBytes, RuntimeData.getInstance().getHeaders(), progressUIListener, true);
//    }

    /**
     * 取消下载
     *
     * @param url 下载地址
     */
    public void downloadFilePause(String url) {
        url = deParameter(url);
        Call call = requestHandleMap.remove(url);
        if (call != null) {
            call.cancel();
        }
    }

//    public void downloadFile(String url, String saveFilePath, String startBytes, Header[] header, ProgressUIListener progressUIListener, boolean autoCancel) {
//        if (checkAgent()) {
//            return;
//        }
//        add(url, downloadFileSendRequest(url, saveFilePath, startBytes, header, progressUIListener), autoCancel);
//    }

//    private Call downloadFileSendRequest(String url, final String saveFilePath, String startBytes, Header[] header, final ProgressUIListener progressUIListener) {
//        Request.Builder builder = new Request.Builder();
//        builder.url(url);
//        builder.get();
//        return execute(builder, startBytes, header, new DownloadFileResponseHandler(url, saveFilePath, startBytes, progressUIListener));
//    }

    private Call execute(Request.Builder builder, String startBytes, Header[] header, Callback callback) {
        boolean hasUa = false;
        if (header == null) {
            builder.header("Connection", "close");
            builder.header("Accept", "*/*");
        } else {
            for (Header h : header) {
                builder.header(h.getName(), h.getValue());
                if (!hasUa && h.getName().equals("User-Agent")) {
                    hasUa = true;
                }
            }
        }
        if (!hasUa) {
            builder.removeHeader("User-Agent").addHeader("User-Agent", BaseUtil.encodeHeadInfo(this.httpConfig.getUserAgent()));
        }
        //  Range参数是用来下载文件断点续传使用的，不影响其他请求
        if (!TextUtils.isEmpty(startBytes)) {
            builder.addHeader("Range", "bytes=" + startBytes + "-");
        }
        Request request = builder.cacheControl(cacheControl).build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        MLog.i(request.method() + " " + request.url().toString());
        return call;
    }

    //同步执行
    private byte[] execute(Request.Builder builder, Header[] header) {
        boolean hasUa = false;
        if (header == null) {
            builder.header("Connection", "close");
            builder.header("Accept", "*/*");
        } else {
            for (Header h : header) {
                builder.header(h.getName(), h.getValue());
                if (!hasUa && h.getName().equals("User-Agent")) {
                    hasUa = true;
                }
            }
        }
        if (!hasUa) {
            builder.removeHeader("User-Agent").addHeader("User-Agent", BaseUtil.encodeHeadInfo(this.httpConfig.getUserAgent()));
        }
        Request request = builder.cacheControl(cacheControl).build();
        MLog.i(request.method() + " " + request.url().toString());

        Call call = client.newCall(request);
        byte[] body = null;
        try {
            Response response = call.execute();
            body = response.body().bytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    public Call delete(String url, Header[] header, HttpResponseHandler callback) {
        return this.delete(url, header, callback, true);
    }

    public Call delete(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback) {
        return this.delete(url, header, form, callback, true);
    }

    public Call delete(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback, boolean sign) {
        try {
            if (form == null)
                form = new ArrayList<>(2);
            if (sign)
                NUtil.post(form);//加密处理
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (NameValuePair item : form) {
                if (item.getValue() == null) {
                    continue;
                }
                formBuilder.add(item.getName(), item.getValue());
            }
            RequestBody requestBody = formBuilder.build();

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.delete(requestBody);

            return execute(builder, null, header, callback);
        } catch (Exception e) {
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }

    public Call delete(String url, Header[] header, HttpResponseHandler callback, boolean sign) {
        if (sign) {
            url = NUtil.get(url);
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.delete();
        return execute(builder, null, header, callback);
    }


    public Call get(String url, Header[] header, HttpResponseHandler callback) {
        return this.get(url, header, callback, true);
    }


    public byte[] getSync(String url, Header[] header) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        return execute(builder, header);
    }

    public byte[] postSync(String url, Header[] header, List<NameValuePair> form) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (NameValuePair item : form) {
            if (item.getValue() == null) {
                continue;
            }
            formBuilder.add(item.getName(), item.getValue());
        }
        RequestBody requestBody = formBuilder.build();

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(requestBody);
        return execute(builder, header);
    }

    //这个方法用于长连接 长连接的key不用url，用自己的key
    public void get(String key, String url, Header[] header, HttpResponseHandler callback, boolean sign) {
        add(key, get(url, header, callback, sign));
    }

    public <T> void get(Class<T> clazz, String url, RequestDataCallback<T> callback) {
        this.get(clazz, url, callback, true);
    }

    public <T> void get(Class<T> clazz, String url, RequestDataCallback<T> callback, boolean autoCancel) {
        this.get(clazz, url, RuntimeData.getInstance().getHeaders(), callback, autoCancel);
    }

    public <T> void get(final Class<T> clazz, final String url, Header[] header, final RequestDataCallback<T> callback, boolean autoCancel) {
        if (checkAgent()) {
            return;
        }
        add(url, get(url, header, new MyHttpResponseHandler(clazz, url, callback)), autoCancel);
    }

    /**
     * @param clazz
     * @param key
     * @param url
     * @param header
     * @param callback
     * @param <T>
     * @deprecated Use {@link #get(Class, String, RequestDataCallback)}  instead.
     */
    @Deprecated
    public <T> void get(final Class<T> clazz, final String key, final String url, Header[] header, final RequestDataCallback<T> callback) {
        if (checkAgent()) {
            return;
        }
        add(url, get(url, header, new MyHttpResponseHandler(clazz, url, callback)), TextUtils.isEmpty(key) ? false : true);
    }

    public Call get(String url, Header[] header, HttpResponseHandler callback, boolean sign) {
        if (sign) {
            url = NUtil.get(url);
        }
        Request.Builder builder = new Request.Builder();
        if (TextUtils.isEmpty(url)) {    //防止崩溃
            MLog.e("HTTPCaller:url为空");
            return null;
        }
        if (RuntimeData.getInstance().getAppConfig().isEncryption && !TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)&&url.indexOf(RuntimeData.getInstance().getCurrentServerUrl()) > -1) {
            String parameter = url.substring(url.indexOf("?") + 1);
            parameter = DecryptUtil.URLDecoder(parameter);
            url = url.substring(0, url.indexOf("?") + 1) + "_p=" + DecryptUtil.desEncryptToURLEncoded(parameter, RuntimeData.getInstance().getAppConfig().key);
        }
        builder.url(url);
        builder.get();
        return execute(builder, null, header, callback);
    }

    public Call post(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback) {
        return this.post(url, header, form, callback, true);
    }

    public Call post(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback, boolean sign) {
        try {
            if (form == null)
                form = new ArrayList<>(2);
            if (sign)
                NUtil.post(form);//加密处理
            FormBody.Builder formBuilder = new FormBody.Builder();
            if (RuntimeData.getInstance().getAppConfig().isEncryption && !TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)&&url.indexOf(RuntimeData.getInstance().getCurrentServerUrl()) > -1) {
                StringBuffer stringBuffer = new StringBuffer();
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    stringBuffer.append(item.getName());
                    stringBuffer.append("=");
                    stringBuffer.append(item.getValue());
                    stringBuffer.append("&");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    String _P = DecryptUtil.desEncryptNotToURLEncoded(stringBuffer.toString(), RuntimeData.getInstance().getAppConfig().key);
                    formBuilder.add("_p", _P);
                }
            } else {
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    formBuilder.add(item.getName(), item.getValue());
                }
            }
            RequestBody requestBody = formBuilder.build();
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(requestBody);

            return execute(builder, null, header, callback);
        } catch (Exception e) {
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }

    public Call put(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback) {
        return this.put(url, header, form, callback, true);
    }

    public Call put(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback, boolean sign) {
        try {
            if (form == null) {
                form = new ArrayList<>(2);
            }
            if (sign) {
                NUtil.post(form);//加密处理
            }

            FormBody.Builder formBuilder = new FormBody.Builder();
            for (NameValuePair item : form) {
                formBuilder.add(item.getName(), item.getValue());
            }
            RequestBody requestBody = formBuilder.build();

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.put(requestBody);

            return execute(builder, null, header, callback);
        } catch (Exception e) {
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }

    public Call postFile(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MediaType mediaType = MediaType.parse("application/octet-stream");
            for (int i = form.size() - 1; i >= 0; i--) {
                NameValuePair item = form.get(i);
                if (item.getName().startsWith("upload_file") || item.getAlias().startsWith("upload_file") || item.isFile()) {
                    File myFile = new File(item.getValue());
                    if (myFile.exists()) {
//                        String fileName = Util.getFileName(item.getValue());
                        builder.addFormDataPart(item.getName(), item.getValue(), RequestBody.create(mediaType, myFile));
                    }
                    form.remove(i);
                }

            }

            NUtil.post(form);

//            for (NameValuePair item : form) {
//                builder.addFormDataPart(item.getName(), item.getValue());
//            }
            if (RuntimeData.getInstance().getAppConfig().isEncryption && !TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)&&url.indexOf(RuntimeData.getInstance().getCurrentServerUrl()) > -1) {
                StringBuffer stringBuffer = new StringBuffer();
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    stringBuffer.append(item.getName());
                    stringBuffer.append("=");
                    stringBuffer.append(item.getValue());
                    stringBuffer.append("&");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    String _P = DecryptUtil.desEncryptNotToURLEncoded(stringBuffer.toString(), RuntimeData.getInstance().getAppConfig().key);
                    builder.addFormDataPart("_p", _P);
                }
            } else {
                for (NameValuePair item : form) {
                    builder.addFormDataPart(item.getName(), item.getValue());
                }
            }
            RequestBody requestBody = builder.build();
            Request.Builder requestBuider = new Request.Builder();
            requestBuider.url(url);
            requestBuider.post(requestBody);
            return execute(requestBuider, null, header, callback);

        } catch (Exception e) {
            if (MLog.debug) {
                e.printStackTrace();
                Log.e("XX", e.toString());
            }
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }

    public Call postFile(String url, Header[] header, List<NameValuePair> form, HttpResponseHandler callback, boolean sign) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MediaType mediaType = MediaType.parse("application/octet-stream");
            for (int i = form.size() - 1; i >= 0; i--) {
                NameValuePair item = form.get(i);
                if (item.getName().startsWith("upload_file") || item.getAlias().startsWith("upload_file") || item.isFile()) {
                    File myFile = new File(item.getValue());
                    if (myFile.exists()) {
//                        String fileName = Util.getFileName(item.getValue());
                        builder.addFormDataPart(item.getName(), item.getValue(), RequestBody.create(mediaType, myFile));
                    }
                    form.remove(i);
                }

            }
            if (sign) {
                NUtil.post(form);
            } else {
                form.addAll(HTTPCaller.Instance().getCommonField());
            }


//            for (NameValuePair item : form) {
//                builder.addFormDataPart(item.getName(), item.getValue());
//            }
            if (RuntimeData.getInstance().getAppConfig().isEncryption && !TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)&&url.indexOf(RuntimeData.getInstance().getCurrentServerUrl()) > -1) {
                StringBuffer stringBuffer = new StringBuffer();
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    stringBuffer.append(item.getName());
                    stringBuffer.append("=");
                    stringBuffer.append(item.getValue());
                    stringBuffer.append("&");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    String _P = DecryptUtil.desEncryptNotToURLEncoded(stringBuffer.toString(), RuntimeData.getInstance().getAppConfig().key);
                    builder.addFormDataPart("_p", _P);
                }
            } else {
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    builder.addFormDataPart(item.getName(), item.getValue());
                }
            }
            RequestBody requestBody = builder.build();
            Request.Builder requestBuider = new Request.Builder();
            requestBuider.url(url);
            requestBuider.post(requestBody);
            return execute(requestBuider, null, header, callback);

        } catch (Exception e) {
            if (MLog.debug) {
                e.printStackTrace();
                Log.e("XX", e.toString());
            }
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }

    public Call postFile(String url, Header[] header, List<NameValuePair> form, String byteName, byte[] fileContent, HttpResponseHandler callback, boolean sign) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MediaType mediaType = MediaType.parse("application/octet-stream");
            if(!TextUtils.isEmpty(byteName)){
                builder.addFormDataPart(byteName, byteName, RequestBody.create(mediaType, fileContent));
            }
            for (int i = form.size() - 1; i >= 0; i--) {
                NameValuePair item = form.get(i);
                if (item.getName().startsWith("upload_file") || item.getAlias().startsWith("upload_file") || item.isFile()) {
                    File myFile = new File(item.getValue());
                    if (myFile.exists()) {
//                        String fileName = Util.getFileName(item.getValue());
                        builder.addFormDataPart(item.getName(), item.getValue(), RequestBody.create(mediaType, myFile));
                    }
                    form.remove(i);
                }

            }
            if (sign) {
                NUtil.post(form);
            } else {
                form.addAll(HTTPCaller.Instance().getCommonField());
            }
            if (RuntimeData.getInstance().getAppConfig().isEncryption && !TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)&&url.indexOf(RuntimeData.getInstance().getCurrentServerUrl()) > -1) {
                StringBuffer stringBuffer = new StringBuffer();
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    stringBuffer.append(item.getName());
                    stringBuffer.append("=");
                    stringBuffer.append(item.getValue());
                    stringBuffer.append("&");
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    String _P = DecryptUtil.desEncryptNotToURLEncoded(stringBuffer.toString(), RuntimeData.getInstance().getAppConfig().key);
                    builder.addFormDataPart("_p", _P);
                }
            } else {
                for (NameValuePair item : form) {
                    if (item.getValue() == null) {
                        continue;
                    }
                    builder.addFormDataPart(item.getName(), item.getValue());
                }
            }
            RequestBody requestBody = builder.build();
            Request.Builder requestBuider = new Request.Builder();
            requestBuider.url(url);
            requestBuider.post(requestBody);
            return execute(requestBuider, null, header, callback);

        } catch (Exception e) {
            if (MLog.debug) {
                e.printStackTrace();
                Log.e("XX", e.toString());
            }
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }


    public Call postFile(String url, Header[] header, byte[] fileContent, HttpResponseHandler callback) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MediaType mediaType = MediaType.parse("application/octet-stream");
            builder.addFormDataPart("upload_file", "", RequestBody.create(mediaType, fileContent));
            List<NameValuePair> form = new ArrayList<NameValuePair>(2);
            NUtil.post(form);
            for (NameValuePair item : form) {
                builder.addFormDataPart(item.getName(), item.getValue());
            }
            RequestBody requestBody = builder.build();
            Request.Builder requestBuider = new Request.Builder();
            requestBuider.url(url);
            requestBuider.post(requestBody);
            return execute(requestBuider, null, header, callback);
        } catch (Exception e) {
            if (MLog.debug) {
                e.printStackTrace();
                Log.e("XX", e.toString());
            }
            if (callback != null)
                callback.onFailure(-1, e.getMessage().getBytes());
        }
        return null;
    }


    /**
     * 郭总要求不让用户连代理
     *
     * @return
     */
    private boolean checkAgent() {
        if (httpConfig.isAgent()) {
            return false;
        } else {
            String proHost = android.net.Proxy.getDefaultHost();
            int proPort = android.net.Proxy.getDefaultPort();
            if (proHost == null || proPort < 0) {
                return false;
            } else {
                Log.i(httpConfig.getTagName(), "有代理,不能访问");
                return true;
            }
        }
    }

    public <T> void post(Class<T> clazz, String url, List<NameValuePair> params, RequestDataCallback<T> callback) {
        this.post(clazz, url, RuntimeData.getInstance().getHeaders(), params, callback, true);
    }

    public <T> void post(Class<T> clazz, String url, List<NameValuePair> params, RequestDataCallback<T> callback, boolean autoCancel) {
        this.post(clazz, url, RuntimeData.getInstance().getHeaders(), params, callback, autoCancel);
    }

    public <T> void post(Class<T> clazz, String url, Header[] header, List<NameValuePair> params, RequestDataCallback<T> callback, boolean autoCancel) {
        if (checkAgent()) {
            return;
        }
        add(url, post(url, header, params, new MyHttpResponseHandler(clazz, url, callback)), autoCancel);
    }

    public <T> void delete(Class<T> clazz, String url, List<NameValuePair> params, RequestDataCallback<T> callback) {
        this.delete(clazz, url, RuntimeData.getInstance().getHeaders(), params, callback, true);
    }

    public <T> void delete(Class<T> clazz, String url, List<NameValuePair> params, RequestDataCallback<T> callback, boolean autoCancel) {
        this.delete(clazz, url, RuntimeData.getInstance().getHeaders(), params, callback, autoCancel);
    }

    public <T> void delete(Class<T> clazz, String url, Header[] header, List<NameValuePair> params, RequestDataCallback<T> callback, boolean autoCancel) {
        if (checkAgent()) {
            return;
        }
        add(url, delete(url, header, params, new MyHttpResponseHandler(clazz, url, callback)), autoCancel);
    }


    /**
     * @param clazz
     * @param key
     * @param url
     * @param header
     * @param callback
     * @param <T>
     * @deprecated Use {@link #post(Class, String, List, RequestDataCallback)}   instead.
     */
    public <T> void post(Class<T> clazz, String key, String url, Header[] header, List<NameValuePair> params, RequestDataCallback<T> callback) {
        if (checkAgent()) {
            return;
        }
        add(url, post(url, header, params, new MyHttpResponseHandler(clazz, url, callback)), TextUtils.isEmpty(key) ? false : true);
    }

    public <T> void put(final Class<T> clazz, final String key, final String url, Header[] header, List<NameValuePair> params, final RequestDataCallback<T> callback) {
        add(url, put(url, header, params, new MyHttpResponseHandler(clazz, url, callback)));
    }

    public <T> void delete(final Class<T> clazz, final String key, final String url, Header[] header, final RequestDataCallback<T> callback) {
        add(url, delete(url, header, new MyHttpResponseHandler(clazz, url, callback)));
    }

    public <T> void postFile(Class<T> clazz, String url, List<NameValuePair> form, RequestDataCallback<T> callback) {
        add(url, postFile(url, RuntimeData.getInstance().getHeaders(), form, new MyHttpResponseHandler(clazz, url, null, callback)));
    }


    public <T> void postFile(Class<T> clazz, String url, List<NameValuePair> form, RequestDataCallback<T> callback, boolean sign) {
        add(url, postFile(url, RuntimeData.getInstance().getHeaders(), form, new MyHttpResponseHandler(clazz, url, null, callback), sign));
    }

    public <T> void postFile(Class<T> clazz, String url, List<NameValuePair> form, String byteName, byte[] fileContent, RequestDataCallback<T> callback, boolean sign) {
        add(url, postFile(url, RuntimeData.getInstance().getHeaders(), form, byteName, fileContent, new MyHttpResponseHandler(clazz, url, null, callback), sign));
    }

    /**
     * @param clazz
     * @param key
     * @param url
     * @param header
     * @param form
     * @param progress
     * @param callback
     * @param <T>
     * @deprecated Use {@link #postFile(Class, String, List, RequestDataCallback)}  instead.
     */
    public <T> void postFile(final Class<T> clazz, final String key, final String url, Header[] header, List<NameValuePair> form, final CustomerProgress progress, final RequestDataCallback<T> callback) {
        add(url, postFile(url, header, form, new MyHttpResponseHandler(clazz, url, progress, callback)));
    }

    public <T> void postFile(final Class<T> clazz, final String key, final String url, Header[] header, byte[] fileContent, final CustomerProgress progress, final RequestDataCallback<T> callback) {
        add(url, postFile(url, header, fileContent, new MyHttpResponseHandler(clazz, url, progress, callback)));
    }

    public class DownloadFileResponseHandler implements Callback {
        private ProgressUIListener progressUIListener;
        private String saveFilePath;
        private String startBytes;
        private String url;

        public DownloadFileResponseHandler(String url, String saveFilePath, String startBytes, ProgressUIListener progressUIListener) {
            this.url = url;
            this.startBytes = startBytes;
            this.saveFilePath = saveFilePath;
            this.progressUIListener = progressUIListener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            clear(url);
            try {
                MLog.i(httpConfig.getTagName(), url + " " + -1 + " " + new String(e.getMessage().getBytes(), "utf-8"));
            } catch (UnsupportedEncodingException encodingException) {
                encodingException.printStackTrace();
            }

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            MLog.i(httpConfig.getTagName(), url + " code:" + response.code());
//            clear(url);

            ResponseBody responseBody = ProgressHelper.withProgress(response.body(), progressUIListener);

            //  判断是否为断点续传
            if (TextUtils.isEmpty(startBytes)) {
                //  不是断点续传 直接创建文件并写入
                BufferedSource source = responseBody.source();
                File outFile = new File(saveFilePath);
                outFile.delete();
                outFile.createNewFile();

                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                source.readAll(sink);
                sink.flush();
                source.close();
            } else {
                //  断点续传时，找到之前的文件并继续写入
                InputStream in = responseBody.byteStream();
                FileChannel channelOut = null;
                // 随机访问文件，可以指定断点续传的起始位置
                RandomAccessFile randomAccessFile = null;
                try {
                    randomAccessFile = new RandomAccessFile(saveFilePath, "rwd");
                    channelOut = randomAccessFile.getChannel();
                    // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                    MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, Long.parseLong(startBytes), responseBody.contentLength());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) != -1) {
                        mappedBuffer.put(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        if (channelOut != null) {
                            channelOut.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class MyHttpResponseHandler<T> extends HttpResponseHandler {
        private Class<T> clazz;
        private String url;
        private RequestDataCallback<T> callback;
        private CustomerProgress progress;

        public MyHttpResponseHandler(Class<T> clazz, String url, RequestDataCallback<T> callback) {
            this.clazz = clazz;
            this.url = url;
            this.callback = callback;
        }

        public MyHttpResponseHandler(Class<T> clazz, String url, CustomerProgress progress, RequestDataCallback<T> callback) {
            this.clazz = clazz;
            this.url = url;
            this.callback = callback;
            this.progress = progress;
        }

        @Override
        public void onFailure(int status, byte[] data) {
            // TODO Auto-generated method stub
            super.onFailure(status, data);
            clear(url);
            //添加统计
            addError(url, status, data);
            if (MLog.debug) {
                try {
                    String str = new String(data, "utf-8");
                    MLog.i(httpConfig.getTagName(), url + " " + status + " " + str);
                } catch (Exception e) {
                }
            }
            sendCallback(callback);
        }

        @Override
        public void onSuccess(int status, Header[] header, byte[] data) {
            clear(url);
            try {
                // TODO Auto-generated method stub
                String str = new String(data, "utf-8");
                if (MLog.debug) {
                    MLog.i(httpConfig.getTagName(), url + " " + status + " " + str);
                }
                T t = JSON.parseObject(str, clazz);
                sendCallback(status, t, data, callback);
            } catch (Exception e) {
                if (MLog.debug) {
                    MLog.e("XX", "HTTPCaller:" + e.toString());
                }
                addError(url, status, data);//添加统计
                sendCallback(callback);
            }
        }

        @Override
        public void onProgress(int bytesWritten, int totalSize) {
            if (progress != null) {
                progress.onProgress(bytesWritten, totalSize);
            }
        }
    }

    public void autoCancel(String url) {
        url = deParameter(url);
        Call call = requestHandleMap.remove(url);
//        MLog.i("ansen","call:"+call);
        if (call != null) {
//            MLog.i("ansen","call.cancel()");
            call.cancel();
        }
    }

    private void add(String url, Call call) {
        add(url, call, true);
    }

    /**
     * 保存请求信息
     *
     * @param url        请求url
     * @param call       http请求call
     * @param autoCancel 自动取消
     */
    private void add(String url, Call call, boolean autoCancel) {
        if (!TextUtils.isEmpty(url)) {
            url = deParameter(url);
            if (autoCancel) {
                autoCancel(url);//如果同一时间对api进行多次请求，自动取消之前的
            }
//            MLog.i("ansen","添加url:"+url+" call:"+call);
            requestHandleMap.put(url, call);
        }
    }

    private void clear(String url) {
        Call call = requestHandleMap.remove(deParameter(url));
//        MLog.i("ansen","删除url:"+url+" call:"+call);
    }

    //get请求需要去掉后面的参数
    private String deParameter(String url) {
        if (url.contains("?")) {//get请求需要去掉后面的参数
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    //更新字段值
    public void updateCommonField(String key, String value) {
        httpConfig.updateCommonField(key, value);
    }

    public void removeCommonField(String key) {
        httpConfig.removeCommonField(key);
    }

    public void addCommonField(String key, String value) {
        httpConfig.addCommonField(key, value);
    }

    public String getCommonFieldString() {
        return httpConfig.getCommonFieldString();
    }

    public Map<String, String> getCommonFieldMap() {
        return httpConfig.getCommonFieldMap();
    }
    public List<NameValuePair> getCommonField() {
        return httpConfig.getCommonField();
    }

    private <T> void sendCallback(RequestDataCallback<T> callback) {
        sendCallback(-1, null, null, callback);
    }

    private <T> void sendCallback(int status, T data, byte[] body, RequestDataCallback<T> callback) {
        CallbackMessage<T> msgData = new CallbackMessage<T>();
        msgData.body = body;
        msgData.status = status;
        msgData.data = data;
        msgData.callback = callback;

        Message msg = handler.obtainMessage();
        msg.obj = msgData;
        handler.sendMessage(msg);
    }

    private class CallbackMessage<T> {
        public RequestDataCallback<T> callback;
        public T data;
        public byte[] body;
        public int status;

        public void callback() {
            if (data == null && callback != null) {
                callback.dataCallback(null);
            } else {
                if (callback != null)
                    callback.dataCallback(status, data, body);
//                MLog.d("XX","status:"+status+",data:"+data+",body:"+new String(body));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    //JSON解析模板
    ///////////////////////////////////////////////////////////////////////////////
    private void addError(String url, int status, byte[] data) {
//        if (errorSB == null)
//            errorSB = new StringBuilder();
//        errorSB.setLength(0);
//        errorSB.append(url);
//        errorSB.append("\r\nStatus:");
//        errorSB.append(status);
//        errorSB.append("\r\n");
//        try {
//            if (data != null)
//                errorSB.append(new String(data, "utf-8"));
//        } catch (Exception e) {
//
//        }
//        MobclickAgent.onEvent(context, "9999", errorSB.toString());
    }
}
