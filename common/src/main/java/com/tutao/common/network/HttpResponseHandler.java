package com.tutao.common.network;

import android.text.TextUtils;

import com.app.model.RuntimeData;
import com.app.util.DecryptUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author guopeng
 * @ClassName: HttpResponseHandler
 * @Description: TODO(HTTP回调类)
 * @date 2014 2014年9月21日 下午5:42:04
 */
public abstract class HttpResponseHandler implements Callback {

    private boolean isStream = false;

    public HttpResponseHandler() {

    }

    public HttpResponseHandler(boolean isStream) {
        this.isStream = isStream;
    }

    public void onFailure(Call call, IOException e) {
        if (e == null) {
            onFailure(-1, new byte[]{});
        } else {
            if (e.getMessage() != null) {
                onFailure(-1, e.getMessage().getBytes());
            } else {
                onFailure(-1, new byte[]{});
            }

        }
    }

    public void onResponse(Call call, Response response) throws IOException {
        int code = response.code();
        if (isStream) {
            if (code > 299) {
                onFailure(response.code(), null);
            } else {
                onStream(code, response.body().byteStream());
            }
        } else {
            byte[] body = response.body().bytes();
            if (code > 299) {
                if (RuntimeData.getInstance().getAppConfig().isEncryption&&!TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)) {//服务器加密了需要解密字段
                    try {
                        byte[] bodyUp = DecryptUtil.desDecrypt(body, RuntimeData.getInstance().getAppConfig().key);
                        if (bodyUp == null) {
                            onFailure(response.code(), body);
                        } else {
                            onFailure(response.code(), bodyUp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onFailure(response.code(), body);
                    }
                } else {
                    onFailure(response.code(), body);
                }
            } else {
                Headers headers = response.headers();
                Header[] hs = new Header[headers.size()];
                for (int i = 0; i < headers.size(); i++) {
                    hs[i] = new Header(headers.name(i), headers.value(i));
                }
                if (RuntimeData.getInstance().getAppConfig().isEncryption&&!TextUtils.isEmpty(RuntimeData.getInstance().getAppConfig().key)) {//服务器加密了需要解密字段
                    try {
                        byte[] bodyUp = DecryptUtil.desDecrypt(body, RuntimeData.getInstance().getAppConfig().key);
                        if (bodyUp == null) {
                            onSuccess(code, hs, body);
                        } else {
                            onSuccess(code, hs, bodyUp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onSuccess(code, hs, body);
                    }
                } else {
                    onSuccess(code, hs, body);
                }
            }
        }
    }

    public void onFailure(int status, byte[] data) {

    }

    public void onProgress(int bytesWritten, int totalSize) {
    }

    public void onStream(int statusCode, InputStream is) {
    }

    ;

    public abstract void onSuccess(int statusCode, Header[] headers, byte[] responseBody);

}
