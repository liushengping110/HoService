package wizrole.hoservice.activity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.ObstetricsGynecology;
import wizrole.hoservice.interface_base.DownLoadListener;
import wizrole.hoservice.util.DownLoadFile;
import wizrole.hoservice.util.NetUtils;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;

/**
 * Created by ${liushengping} on 2017/9/29.
 * 何人执笔？
 * 妇产科学
 */

public class DeparentScienActivity extends BaseActivity implements DownLoadListener,OnLoadCompleteListener
,OnDrawListener,OnPageChangeListener{

//    @BindView(R.id.lin_back)LinearLayout lin_back;
//    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_pdf_num)TextView text_pdf_num;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pdfView)PDFView pdfView;
    public  String savePath = "/sdcard/wizrole/PDF/";
    public  String saveFileName;
    public String pdfurl;
    public ProgressDialog  dialog;
    public int now_page;//当前页
    public int totalNum;//总页数
    public boolean status=false;//只执行一次的
    public boolean status_press=true;
    public ObstetricsGynecology gynecology;
    @Override
    protected int getLayout() {
        return R.layout.activity_deparentscien;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        gynecology=(ObstetricsGynecology)getIntent().getSerializableExtra("gynecology");
        pdfurl= Constant.ip_pdf+gynecology.getChapterUrl();
        saveFileName=savePath+gynecology.getChapterName()+".pdf";
        initDialog();//初始化dialog
        onWrite();
    }

    public void initDialog(){
        //添加弹出的对话框
        dialog = new ProgressDialog(this) ;
        dialog.setTitle(getString(R.string.wxts)) ;
        dialog.setMessage(getString(R.string.pdf_download)) ;
        //将进度条设置为水平风格，让其能够显示具体的进度值
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL) ;
        dialog.setCancelable(false) ; //用了这个方法之后，直到图片下载完成，进度条才会消失（即使在这之前点击了屏幕）
    }

    @Override
    public void onSchedule(int progress) {
        dialog.setProgress(progress);
    }

    @Override
    public void onSuccess() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onError() {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onExists() {
        handler.sendEmptyMessage(2);
    }


    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://下载失败
                    dialog.dismiss();
                    pdfView.setVisibility(View.INVISIBLE);
                    text_no_data.setVisibility(View.VISIBLE);
                    ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.wifi_err));
                    break;
                case 1://下载成功
                    dialog.dismiss();
                    ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.download_success));
                    setPdfView(new File(saveFileName),1);
                    break;
                case 2://本地存在
                    setPdfView(new File(saveFileName),1);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPdf();
            }
        });
    }

    /**
     * 加载pdf文件
     */
    private void loadPdf() {
        File file = new File(saveFileName);
        if (file.exists()) {//已经存在，在sd卡本地读取
            setPdfView(file,1);
        } else {//不存在，从网络下载
            if (NetUtils.isNetworkConnected(DeparentScienActivity.this)) {//判断网络连接
                //下载
                dialog.show();
                new DownLoadFile(this).DownLoad(pdfurl,savePath,gynecology.getChapterName()+".pdf");
            } else {
                handler.sendEmptyMessage(0);
            }
        }
    }

    /**
     * 设置pdf属性
     * @param file
     */
    private void setPdfView(File file,int setPage) {
        showProgress();
        pdfView.setVisibility(View.VISIBLE);
        text_no_data.setVisibility(View.INVISIBLE);
        pdfView.setFitsSystemWindows(true);
        pdfView.setFocusableInTouchMode(false);
        pdfView.fromFile(file)
                .onLoad(this)
                //.swipeVertical(true)设置pdf文档垂直翻页，默认是左右滑动翻页
                .onDraw(this)
                .onPageChange(this)
                .defaultPage(setPage)
                .showMinimap(true)//是否显示缩放小地图
                .load();
    }
    //加载完成
    @Override
    public void loadComplete(int nbPages) {
        hideProgress();
        if(!status){//第一次加载
            text_pdf_num.setVisibility(View.VISIBLE);
            text_pdf_num.setText(1+"/"+nbPages);
        }else{//记住此页，回到此页
            text_pdf_num.setVisibility(View.VISIBLE);
            text_pdf_num.setText(SharedPreferenceUtil.getPdfPagerNum(DeparentScienActivity.this)+"/"+nbPages);
        }
//        //是否回到之前的页码
//        if(SharedPreferenceUtil.getPdfPagerNum(DeparentScienActivity.this)!=0){//如果上次记住了
//            if(!status){//只执行一次，不然会在下一个回调中死循环
//                status=true;
//                RemmberPage(1,"是否回到上次查看的页码？");
//            }
//        }
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }
    //pdf翻页
    @Override
    public void onPageChanged(int page, int pageCount) {
        now_page=page;
        totalNum=pageCount;
        text_pdf_num.setText(page+"/"+pageCount);
    }


    final public static int REQUEST_CODE_CAMERA = 123;//相
    final public static int REQUEST_CODE_AUDIO = 456;//声音
    /**写入权限*/
    public void onWrite() {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {//提示用户手动设置权限--回调
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
                return;
            } else {//当程序已设置了允许权限，直接通过
                onRead();
            }
    }
    /**读取权限**/
    public void onRead(){
            int checkAudioPhonePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
            if(checkAudioPhonePermission != PackageManager.PERMISSION_GRANTED){//提示用户手动设置权限--回调
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_AUDIO);
                return;
            }else{//当程序已设置了允许权限，直接通过
                loadPdf();
            }
    }

    /**
     * 权限通过回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//写入允许后的回调--开始设置读取权限
                    onRead();
                } else {
                    status_press=false;
                    MyToast(MyApplication.getContextObject().getString(R.string.write_fail));
                    finish();
                }
                break;
            case REQUEST_CODE_AUDIO://音频获取权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//读取允许后的回调--
                    loadPdf();
                } else {
                    status_press=false;
                    MyToast(MyApplication.getContextObject(). getString(R.string.read_fail));
                    finish();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    /**展示登录进度视图*/
    private ProgressDialog progressDialog;
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.pdf_onopen));
    }
    /**隐藏登录进度视图*/
    public void hideProgress() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    //是否记住查看页码
    public void RemmberPage(final int type, String  message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.wxts));
        builder.setMessage(message);
        builder.setIcon(R.drawable.hos_ec);
        builder.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(type==1){//进入
                    if(SharedPreferenceUtil.getPdfPagerNum(DeparentScienActivity.this)!=0){//说明有报错，记住
                        setPdfView(new File(saveFileName),SharedPreferenceUtil.getPdfPagerNum(DeparentScienActivity.this));
                    }
                }else{// ---2  退出
                    SharedPreferenceUtil.savePdfPagerNum(DeparentScienActivity.this,now_page);//保存
                }
                builder.create().cancel();
                if (type==2){//退出
                    finish();
                }
            }
        });
        builder.setNeutralButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().cancel();
                if(type==1){//进入提示  回到之前
                    SharedPreferenceUtil.clearPdfPagerNum(DeparentScienActivity.this);
                }else{//退出
                    SharedPreferenceUtil.clearPdfPagerNum(DeparentScienActivity.this);
                    finish();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(status_press){
//                RemmberPage(2,"是否保存当前页码，以便下次回到此处");
//            }
//        }
//        return false;
//    }
}
