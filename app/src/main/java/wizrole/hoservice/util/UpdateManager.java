package wizrole.hoservice.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;

/**
 * 版本更新
 */
public class UpdateManager {
	private Dialog downloadDialog;
	private Context mContext;
	//返回的安装包url
//	private String apkUrl = "http://172.16.1.166:8180/ZYServices/hoservice.apk";
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/wizrole/";
	private static final String saveFileName = savePath + "hoservice.apk";

	//返回的版本文件txt地址
//	public static final String txtUrl="http://172.16.1.166:8180/ZYServices/appVersion.txt";
	/* 下载txt安装路径 */
	private static final String saveTxtPath = "/sdcard/wizrole/";
	private static final String saveFileTxtName = saveTxtPath + "hoServiceVersion.txt";
	/* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;//下载进度条
    private static final int DOWN_OVER = 2;//下载apk成功
    private static final int DOWN_TXT = 3;//下载txt文件
    private static final int DOWN_FAIL = 4;//下载apk失败
    private static final int WIFI_ERR = 5;//网络连接失败
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
	public boolean status;
	public UpdateManager(Context context,boolean status) {
		this.mContext = context;
		this.status=status;
	}

	//外部接口让主Activity调用---显示弹出框
	public void checkUpdateInfo(){
		showNoticeDialog();
	}

    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE://下载进度
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:	//下载完成--安装
				downloadDialog.cancel();//关闭对话框
				installApk();//安装
				break;
			case DOWN_TXT:	//下载txt
				String downResult=readFileSdcard(saveFileTxtName);
				checkVerson(downResult,getVersion());
				break;
			case DOWN_FAIL://下载失败
				downloadDialog.cancel();//关闭对话框
				Toast.makeText(mContext,"很抱歉，下载失败", Toast.LENGTH_LONG).show();
				break;
			case WIFI_ERR:
				ToastUtil.MyToast(mContext,"网络连接失败，请检查网络！");
				break;
			}
    	};
    };
	private void showNoticeDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View view=inflater.inflate(R.layout.dialog_update,null);
		builder.setCancelable(false);
		builder.setView(view);
		builder.setTitle("软件版本更新");
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
//		builder.setNegativeButton("以后再说", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
		builder.create().show();
//		noticeDialog = builder.create();
//		noticeDialog.show();
	}
	
	private void showDownloadDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		builder.setTitle("下载中，请稍候...");
		builder.setCancelable(false);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
		downloadDialog = builder.create();
		downloadDialog.show();
		downloadApk();//开始下载
	}
	/**
	 * 下载apk
	 */
	public void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	/**子线程下载*/
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(Constant.apkPath);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
					//更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
						//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}
				while(!interceptFlag);//点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				ToastUtil.MyToast(mContext,"网络连接失败，请检查网络！");
			} catch(IOException e){
				e.printStackTrace();
				ToastUtil.MyToast(mContext,"网络连接失败，请检查网络！");
			}
		}
	};

	/**
	 * 安装apk
	 */
	private void installApk(){
		File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
	}

	/**
	 * 下载txt
	 */
	public boolean  down=true;
	public Thread thread;
	public  void downloadTxt(){
		thread = new Thread(TxtDownLoad);//httpUrlConnect下载
		thread.start();
	}
	/**子线程下载*/
	public Thread TxtDownLoad =new Thread() {
		@Override
		public void run() {
			try {
				URL url = new URL(Constant.versionTxtPath);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				File file = new File(saveTxtPath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileTxtName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					if(numread <= 0){
						mHandler.sendEmptyMessage(DOWN_TXT);
						break;
					}
					fos.write(buf, 0, numread);
				}while (down);
				fos.close();
				is.close();
			}catch (MalformedURLException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(WIFI_ERR);
			} catch(IOException e){
				e.printStackTrace();
				mHandler.sendEmptyMessage(WIFI_ERR);
			}
		}
	};

	//读在/mnt/sdcard/目录下面的文件
	public String readFileSdcard(String fileName){
		String isoString="";
		try {
			File urlFile = new File(fileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			String mimeTypeLine = null ;
			while ((mimeTypeLine = br.readLine()) != null) {
				isoString = str+mimeTypeLine;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isoString;
	}

	/***获取当前应用版本号*/
	public String getVersion() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "ERR";
		}
	}
	/**检测版本号**/
	private UpdateManager mUpdateManager;
	public void checkVerson(String webVerson, String versonNum ){
		if(webVerson==null||webVerson==""){
			if(status){
				Toast.makeText(mContext,"版本号获取失败!", Toast.LENGTH_SHORT).show();
			}
		}else{
			if(!versonNum.equals("ERR")){
				if (!webVerson.equals(versonNum)){  //检测版本号的不同--提示更新
//					mUpdateManager = new UpdateManager(mContext);
//					mUpdateManager.checkUpdateInfo();
					showDownloadDialog();//直接下载 不用询问
				}else{
					if(status){
						ToastUtil.MyToast(mContext,"当前已是最新版本"+versonNum);
					}
				}
			}else{
				if(status){
					Toast.makeText(mContext,"应用程序版本获取失败！"+versonNum, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
