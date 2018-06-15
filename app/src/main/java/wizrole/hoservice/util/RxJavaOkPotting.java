package wizrole.hoservice.util;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wizrole.hoservice.Constant;

import static java.security.spec.MGF1ParameterSpec.SHA1;


/**
 * Created by Administrator on 2017/2/22.
 * 网络请求
 * okhttp+rxjava
 */

public class RxJavaOkPotting {

    private Request request;
    private OkHttpClient client;
    private Call call;
    public static final String NET_ERR="net_err";
    private static RxJavaOkPotting okPotting;
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String base_url;
    public static String getBase_url() {
        return Constant.base_Url;
    }

    private RxJavaOkPotting(String base_url) {
        this.base_url = base_url;
        client = new OkHttpClient();
        client.newBuilder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public void OnCancel() {
        call.cancel();
    }

    public static synchronized RxJavaOkPotting getInstance(String base_url) {
        if (okPotting == null) {
            okPotting = new RxJavaOkPotting(base_url);
        }
        RxJavaOkPotting.base_url = base_url;
        return okPotting;
    }

    private Request get(String url) {
        request = new Request.Builder()
                .url(base_url + url)
                .get()
                .build();
        return request;
    }

    private Request ComGet(String url) {
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return request;
    }

    public void ComAsk(String url, Callback callback) {
        call = client.newCall(ComGet(url));
        call.enqueue(callback);
    }

    /**
     * Host: api.cn.ronghub.com
     App-Key: uwd1c0sxdlx2
     Nonce: 14314
     Timestamp: 1408710653491
     Signature: 45beb7cc7307889a8e711219a47b7cf6a5b000e8
     Content-Type: application/x-www-form-urlencoded
     * @param url
     * @param formBody
     * @return
     */

    //获取13位字符串格式的时间戳
    public  String getTime13() {
        long time = System.currentTimeMillis();
        String str13 = String.valueOf(time);
        return str13;
    }


    private Request post(String url, FormBody formBody) {
        request = new Request.Builder()
                .url(base_url)
                .post(formBody)
                .build();
        return request;
    }

    private Request json(String base_url, String json) {
        RequestBody formBody = new FormBody.Builder()
                .add("param", json)
                .build();
        request = new Request.Builder()
                .url(base_url)
                .post(formBody)
                .build();
        return request;
    }

    /**
     * get
     * @param url
     * @param subscriber
     */
    public void Ask(String url, Subscriber subscriber) {
        call = client.newCall(get(url));
        Asynchronous(call, subscriber);
    }

    /**
     * post
     * @param url
     * @param json
     * @param subscriber
     */
    public void Ask(String url, String json, Subscriber subscriber) {
        call = client.newCall(json(url, json));
        Asynchronous(call, subscriber);
    }

    /**
     * json 请求
     * @param url
     * @param formBody
     * @param subscriber
     */
    public void Ask(String url, FormBody formBody, Subscriber subscriber) {
        call = client.newCall(post(url, formBody));
        Asynchronous(call, subscriber);
    }


    private void Asynchronous(Call call, final Subscriber subscriber) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                subscriber.onNext(NET_ERR);
                subscriber.onCompleted();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
            }
        });
    }
}