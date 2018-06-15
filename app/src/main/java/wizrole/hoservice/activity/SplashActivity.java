package wizrole.hoservice.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.interface_base.ReturnData;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.util.Tol_util;

/**
 * Created by a on 2016/12/6.
 * 动画引导页面
 */

public class SplashActivity extends BaseActivity  {

    public String  content;
    private AlphaAnimation animation;
    private ImageView img_splash;
    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }


    @Override
    protected void initData() {
        String message=getIntent().getStringExtra("pushmessage");//获取通知入口
        //判断是否从通知栏点击进入--
        if(message!=null) {
            SharedPreferenceUtil.saveNotidic(this,1);
        }
        img_splash = (ImageView) findViewById(R.id.img_splash);
        initAnination();
        isLogin();
    }

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



    @Override
    protected void setListener() {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**动画结束时执行操作*/
            @Override
            public void onAnimationEnd(Animation animation) {
                if (login) {//如果是自动登录 ，动画结束后  跳转到验证页面
                    if (!SharedPreferenceUtil.getMama(SplashActivity.this).toString().equals("")) {//如果设置了密码锁
                        Intent intent = new Intent(SplashActivity.this, GestureLoginActivity.class);
                        intent.putExtra("yanzheng", "splash");//登录验证为splash
                        startActivity(intent);
                        finish();
                    } else {//尚未设置密码锁，就直接去主页
                        if(SharedPreferenceUtil.getNotidic(SplashActivity.this)==1){//有推送消息--直接去消息页面
                            Intent intent = new Intent(SplashActivity.this, HosMessageActivity.class);
                            startActivity(intent);
                            finish();
                        }else {//不是从通知栏进入的，直接去主页
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {//否则就是没登录 直接去主页
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public Bitmap bitmap;

    public void initAnination() {
        bitmap = readBitMap(SplashActivity.this, R.drawable.splash_load);
        img_splash.setImageBitmap(bitmap);
        animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(4000);
        img_splash.setAnimation(animation);
        animation.start();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2; // 原图的五分之一，设置为2则为二分之一
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public boolean login_stuts;//自动登陆状态
    public boolean isCheck=false;//默认值
    public boolean login = false;//自动登录--则直接跳转到验证页面

    public void isLogin() {
        login_stuts = Tol_util.getCheck(SplashActivity.this, Tol_util.OPEN_LOGIN, isCheck);
        if (login_stuts) {//如果选中--默认登录--发送请求到服务器，获取数据
            login = true;
            //将上一次用户的输入账号和密码保存在sharedPreferences---取出
            String num = SharedPreferenceUtil.getLoginNum(this);
            String tel = SharedPreferenceUtil.getTel(this);
            JSONObject object = new JSONObject();
            try {
                object.put("TradeCode", "Y100");
                object.put("RegNo", num);
                object.put("TelNo", tel);
                RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onNext(Object o) {
                        if(o.equals(RxJavaOkPotting.NET_ERR)){
                            handler.sendEmptyMessage(0);
                        }else{
                            Gson gson = new Gson();
                            infor = new PersonInfor();
                            infor = gson.fromJson(o.toString(), PersonInfor.class);
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
            } catch (Exception e) {
                handler.sendEmptyMessage(0);
            }
        }else{//不是自动登录的情况下，推送消息将自动登录
            SharedPreferenceUtil.saveLoginState(SplashActivity.this,2);//如果不是自动登录，先设置登录状态为未登录
            if(SharedPreferenceUtil.getNotidic(SplashActivity.this)==1) {//从通知栏进入
                    login = true;
                    //将上一次用户的输入账号和密码保存在sharedPreferences---取出
                    String num = SharedPreferenceUtil.getLoginNum(this);
                    String tel = SharedPreferenceUtil.getTel(this);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("TradeCode", "Y100");
                        object.put("RegNo", num);
                        object.put("TelNo", tel);
                        RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onNext(Object o) {
                                if(o.equals(RxJavaOkPotting.NET_ERR)){
                                    handler.sendEmptyMessage(0);
                                }else{
                                    Gson gson = new Gson();
                                    infor = new PersonInfor();
                                    infor = gson.fromJson(o.toString(), PersonInfor.class);
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        });
                    } catch (Exception e) {
                        handler.sendEmptyMessage(0);
                    }
                }
        }
    }

    public PersonInfor infor;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {        //返回数据
                if (infor.getResultCode().equals("0")) {
                    SharedPreferenceUtil.putBean(SplashActivity.this, infor);
                    SharedPreferenceUtil.saveLoginState(SplashActivity.this, 1);//设置登录状态   1
                   MyToast(getString(R.string.auto_login_succ));
                } else if (infor.getResultCode().equals("-1")) {  //入院编号格式不合法
                    MyToast(getString(R.string.userName_rule));
                    SharedPreferenceUtil.saveLoginState(SplashActivity.this, 0);//设置未登录状态  0
                    SharedPreferenceUtil.clearUser(SplashActivity.this);    //说明之前保存的自动登陆信息已经不存在，清空保存的信息，这样进入主页之后，访问也是需要再次登录
                } else if (infor.getResultCode().equals("-2")) {     //系统未查询到
                    MyToast( getString(R.string.systom_no_search));
                    SharedPreferenceUtil.saveLoginState(SplashActivity.this, 0);//设置未登录状态  0
                    clear();
                } else if (infor.getResultCode().equals("-3")) {        //当前患者不在院
                    SharedPreferenceUtil.saveLoginState(SplashActivity.this, 0);//设置未登录状态  0
                    MyToast(getString(R.string.patient_no_hos));
                    clear();
                } else if (infor.getResultCode().equals("-4")) {     //登记号与手机号码不匹配
                    SharedPreferenceUtil.saveLoginState(SplashActivity.this, 0);//设置未登录状态  0
                    MyToast(getString(R.string.tel_djh_no));
                    SharedPreferenceUtil.clearUser(SplashActivity.this);
                }
            } else if (msg.what == 0) {      //请求失败
                ToastUtil.MyToast(SplashActivity.this, getString(R.string.login_auto_fail));
                SharedPreferenceUtil.clearUser(SplashActivity.this);
                SharedPreferenceUtil.saveLoginState(SplashActivity.this, 0);//设置未登录状态  0
            }
        }
    };

    public void clear() {
        Tol_util.setCheck(SplashActivity.this, Tol_util.OPEN_LOGIN, false);
        SharedPreferenceUtil.clearUser(SplashActivity.this);//清除用户信息实体类
        SharedPreferenceUtil.clearLoginNum(SplashActivity.this);//清除用户登记号
        SharedPreferenceUtil.clearTel(SplashActivity.this);//清除用户手机号码
        SharedPreferenceUtil.clearMima(SplashActivity.this);//清除用户手势密码
        SharedPreferenceUtil.clearLoginState(SplashActivity.this);//清除登录状态
        SharedPreferenceUtil.clearPersonHeader(SplashActivity.this);//清除用户头像数据

    }
}
