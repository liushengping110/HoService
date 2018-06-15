package wizrole.hoservice.fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.activity.AboutUsActivity;
import wizrole.hoservice.activity.LoginActivity;
import wizrole.hoservice.activity.MainActivity;
import wizrole.hoservice.activity.PerInforActivity;
import wizrole.hoservice.activity.SettingActivity;
import wizrole.hoservice.activity.UserAdviceActivity;
import wizrole.hoservice.activity.VersionUpDataActivity;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.ui.PopupWindowPotting;
import wizrole.hoservice.util.BitmapToBase;
import wizrole.hoservice.util.ImageChange;
import wizrole.hoservice.util.ScreenListener;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.util.Tol_util;
import wizrole.hoservice.view.RoundImageView;


/**
 * Created by a on 2017/8/5.
 */

public class Fragment_me extends Fragment implements View.OnClickListener{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    private View view;
    public PopupWindowPotting popupWindowPotting;
    public TextView text_xc;
    public TextView text_xj;
    public TextView text_cancle;
    public TextView text_me_login;
    public RoundImageView img_header;
    public ImageView img_bg;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";//头像名称
    private File tempFile;
    public boolean  status=true;
    public static Handler handler;
    private LinearLayout lin_advice,lin_about_us,lin_set,lin_version;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_me , null);
            //初始化控件
            initUI();
            isCreateView = true;
        }
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 9999:
                        initHeader();
                        break;
                }
            }
        };
        return view;
    }

    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if(!isLoadData){
            //加载数据操作
            initHeader();
            setListener();
            isLoadData=true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }
    public void initUI(){
        img_header=(RoundImageView)view.findViewById(R.id.img_header);
        img_bg=(ImageView)view.findViewById(R.id.img_bg);
        text_me_login=(TextView)view.findViewById(R.id.text_me_login);
        lin_advice=(LinearLayout)view.findViewById(R.id.lin_advice);
        lin_about_us=(LinearLayout)view.findViewById(R.id.lin_about_us);
        lin_version=(LinearLayout)view.findViewById(R.id.lin_version);
        lin_set=(LinearLayout)view.findViewById(R.id.lin_set);
    }

    public Bitmap bitmap_bg;
    public Bitmap bitmap;
    public void initHeader(){
        if(SharedPreferenceUtil.getLoginState(getActivity())==1){//已登录
            if (!SharedPreferenceUtil.getPersonHeader(getActivity()).equals("")) {//用户已设置头像
                bitmap_bg= BitmapToBase.base64ToBitmap(SharedPreferenceUtil.getPersonHeader(getActivity()));
                img_bg.setImageDrawable(ImageChange.BoxBlurFilter(bitmap_bg));
                img_header.setImageBitmap(bitmap_bg);
            }else{//已登录，未设置头像
                bitmap_bg= readBitMap(getActivity(), R.drawable.header_bg);
                img_bg.setImageDrawable(ImageChange.BoxBlurFilter(bitmap_bg));
                bitmap= readBitMap(getActivity(), R.drawable.header);
                img_header.setImageBitmap(bitmap);
            }
            //设置用户名
            PersonInfor infor=(PersonInfor)SharedPreferenceUtil.getBean(getActivity());
            text_me_login.setText(infor.getName());
        }else{//未登录
            bitmap_bg= readBitMap(getActivity(), R.drawable.header_bg);
            img_bg.setImageDrawable(ImageChange.BoxBlurFilter(bitmap_bg));
            bitmap=readBitMap(getActivity(),R.drawable.header);
            img_header.setImageBitmap(bitmap);
            text_me_login.setText(getString(R.string.login));
        }
    }
    public void initPopupWindow(){
        if (popupWindowPotting==null){
            popupWindowPotting=new PopupWindowPotting(getActivity(),1) {
                @Override
                protected int getLayout() {
                    return R.layout.dialog_select_photo;
                }
                @Override
                protected void initUI(){
                    text_xc=$(R.id.text_xc);
                    text_xj=$(R.id.text_xj);
                    text_cancle=$(R.id.text_cancle);
                }
                @Override
                protected void setListener() {
                    text_xc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view){//相册
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                            popupWindowPotting.Hide();
                        }
                    });
                    text_xj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view){//相机
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            // 判断存储卡是否可以用，可用进行存储
                            if (hasSdcard()) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                                .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                            }
                            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
                            status=false;
                            popupWindowPotting.Hide();
                        }
                    });
                    text_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view){//取消
                            popupWindowPotting.Hide();
                        }
                    });
                }
            };
        }
    };

    public void setListener(){
        text_me_login.setOnClickListener(this);
        img_header.setOnClickListener(this);
        lin_advice.setOnClickListener(this);
        lin_about_us.setOnClickListener(this);
        lin_set.setOnClickListener(this);
        lin_version.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 9999:
                        initHeader();
                        break;
                }
            }
        };
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {//相册回调数据
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if(requestCode==PHOTO_REQUEST_CAMERA){//相机回调数据
            if(resultCode==-1){
                if (hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_FILE_NAME);
                    crop(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_sd), Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == PHOTO_REQUEST_CUT) {//裁剪回调数据
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                img_bg.setImageDrawable(ImageChange.BoxBlurFilter(bitmap));
                img_header.setImageBitmap(bitmap);
                String img_infor= BitmapToBase.bitmapToBase64(bitmap);
                SharedPreferenceUtil.savePersonHeader(getActivity(),img_infor);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1----不设置就自由比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_me_login://登录
                if(SharedPreferenceUtil.getLoginState(getActivity())!=1){
                    intent=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    intent=new Intent(getActivity(), PerInforActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.img_header://头像
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    requestPermission();//先申请权限
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_advice://意见反馈
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), UserAdviceActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_about_us://关于我们
                intent=new Intent(getActivity(), AboutUsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.lin_set://设置中心
                if(SharedPreferenceUtil.getLoginState(getActivity())==1) {
                    intent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_version://版本更新
                intent=new Intent(getActivity(), VersionUpDataActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求授权
     */
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){ //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
        }else{//已授权
            if(popupWindowPotting==null){
                initPopupWindow();
            }
            popupWindowPotting.Show(view);
        }
    }

    /**
     * 权限申请返回结果
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults  申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //同意权限申请
                    //权限通过
                    if(popupWindowPotting==null){
                        initPopupWindow();
                    }
                    popupWindowPotting.Show(view);
                }else { //拒绝权限申请
                    Toast.makeText(getActivity(),getString(R.string.caram_no),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 1; // 原图的五分之一，设置为2则为二分之一
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
}
