package wizrole.hoservice.util;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wizrole.hoservice.interface_base.DownLoadListener;

/**
 * Created by ${liushengping} on 2017/9/29.
 * 何人执笔？
 * 文件下载
 */

public class DownLoadFile {
    private Call call;
    private Request request;
    private OkHttpClient client;
    public DownLoadListener downLoadListener;

    public DownLoadFile(DownLoadListener downLoadListener) {
        this.downLoadListener = downLoadListener;
        client = new OkHttpClient();
        client.newBuilder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    private Request get(String url) {
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return request;
    }

    public void OnCancel() {
        call.cancel();
    }

    private String getSdPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    //下载地址，文件地址  文件名
    public void DownLoad(String url, String file_path,String fileName) {
        final File file = new File(file_path,fileName);
        if (file.exists()) {
            //文件已存在
            handler.sendEmptyMessage(4012);
        }else {//文件不存在，去下载
            File file_down=new File(file_path);
            if(!file_down.exists()){//先判断文件夹是否存在
                file_down.mkdir();
            }
            call = client.newCall(get(url));
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.obtainMessage(4013).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {//更新下载进度
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            handler.obtainMessage(4010, progress).sendToTarget();
                        }
                        fos.flush();
                        handler.obtainMessage(4011).sendToTarget();//下载成功
//                        if (downLoadListener != null) {
//                            downLoadListener = null;
//                        }
                    } catch (Exception e) {
                        handler.obtainMessage(4013).sendToTarget();//下载失败
//                        if (downLoadListener != null) {
//                            downLoadListener = null;
//                        }
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            handler.obtainMessage(4013).sendToTarget();//下载失败
//                            if (downLoadListener != null) {
//                                downLoadListener = null;
//                            }
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {

                        }
                    }
                }
            });
        }
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (downLoadListener != null) {
                if (msg.what == 4010) {//进度
                    downLoadListener.onSchedule(Integer.parseInt(msg.obj.toString()));
                } else if (msg.what == 4011) {//成功
                    downLoadListener.onSuccess();
                } else if (msg.what == 4013) {//失败
                    downLoadListener.onError();
                }else if(msg.what==4012){
                    downLoadListener.onExists();
                }
            }
        }
    };
}
