package wizrole.hoservice.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.FragmentAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.fragment.Fragment_life;
import wizrole.hoservice.fragment.Fragment_me;
import wizrole.hoservice.fragment.Fragment_hos;
import wizrole.hoservice.fragment.Fragment_main;
import wizrole.hoservice.util.DensityUtil;
import wizrole.hoservice.util.ScreenListener;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.Tol_util;
import wizrole.hoservice.util.UpdateManager;

public class MainActivity extends BaseActivity implements Fragment_main.GetView{

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.rel_hos) RadioButton rel_hos;
    @BindView(R.id.rel_msg) RadioButton rel_msg;
    @BindView(R.id.rel_life) RadioButton rel_life;
    @BindView(R.id.rel_me) RadioButton rel_me;
    private List<Fragment> fragments;
    private int currentPosition = 0;
    public FragmentAdapter adapter;
    public boolean auto_again;
    public ScreenListener ls;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }
    public static Handler handler_main;
    @Override
    protected void initData() {
        ButterKnife.bind(this);
        initRadioButton();
        initViewPager();
        setLock();//手势锁
        requestPermissions();//版本更新
        handler_main=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 8888:
                        setLock();
                        break;
                }
            }
        };
    }

    public void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment_main());
        fragments.add(new Fragment_hos());
        fragments.add(new Fragment_life());
        fragments.add(new Fragment_me());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(4);
    }

    /**
     * 设置RadioButton的宽高
     */
    public void initRadioButton(){
        initDrawable(rel_hos);
        initDrawable(rel_msg);
        initDrawable(rel_life);
        initDrawable(rel_me);
    }

    /**
     * 设置RadioButton的宽高
     * @param v
     */
    public void  initDrawable(RadioButton v){
        Drawable drawable = v.getCompoundDrawables()[1];
        drawable.setBounds(0,0, DensityUtil.dip2px(this,25), DensityUtil.dip2px(this,25));
        v.setCompoundDrawables(null,drawable,null,null);
    }

    @OnClick({R.id.rel_hos, R.id.rel_msg, R.id.rel_life, R.id.rel_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_hos:
                view_pager.setCurrentItem(0);
                break;
            case R.id.rel_msg:
                view_pager.setCurrentItem(1);
                break;
            case R.id.rel_life:
                view_pager.setCurrentItem(2);
                break;
            case R.id.rel_me:
                view_pager.setCurrentItem(3);
                break;
        }
    }

    @Override
    protected void setListener() {
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragments.get(currentPosition).onPause();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    fragment.onResume();
                }
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setLock() {
        ls = new ScreenListener(this);
        ls.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }
            @Override
            public void onScreenOff() {

            }
            @Override
            public void onUserPresent() {
                if (SharedPreferenceUtil.getLoginState(MainActivity.this) == 1) {//如果登录了
                    if (!SharedPreferenceUtil.getMama(MainActivity.this).toString().equals("")) {//如果设置密码了
                        if (Tol_util.getCheck(MainActivity.this, Tol_util.OPEN_MESS, auto_again)) {//如果手势密码开
                            ActivityManager mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
                            List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
                            // 应用程序位于堆栈的顶层
                            if ("wizrole.hoservice".equals(tasksInfo.get(0).topActivity
                                    .getPackageName())) {
                                Intent intent = new Intent(MainActivity.this, GestureLoginActivity.class);
                                intent.putExtra("yanzheng", "yanzheng");//锁屏后验证  为 yanzheng
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 当有多个权限需要申请的时候
     * 这里以打电话和SD卡读写权限为例
     */
    private void requestPermissions(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){  //申请的集合不为空时，表示有需要申请的权限
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else { //所有的权限都已经授权过了
            UpdateManager updateManager = new UpdateManager(MainActivity.this,false);
            updateManager.downloadTxt();
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
                if (grantResults.length > 0){ //安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){ //这个是权限拒绝
                            String s = permissions[i];
                            Toast.makeText(this,s+"SD卡权限已被您禁止，若影响使用，可前往【权限管理】进行权限授权",Toast.LENGTH_SHORT).show();
                        }else{ //授权成功了
//                            UpdateManager updateManager = new UpdateManager(MainActivity.this,false);
//                            updateManager.downloadTxt();
                        }
                    }
                }
                break;
        }
    }
    // 定义是否退出程序的标记
    private boolean isExit = false;
    // 定义接受用户发送信息的handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                // 标记用户不退出状态
                isExit = false;
            }
        }
    };

    // 监听手机的物理按键点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断用户是否点击的是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果isExit标记为false，提示用户再次按键
            if (!isExit) {
                isExit = true;
                MyToast( getString(R.string.lock_again_shutdown));
                // 如果用户没有在2秒内再次按返回键的话，就发送消息标记用户为不退出状态
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }
            // 如果isExit标记为true，退出程序
            else {
                // 退出程序
                finish();
                System.exit(0);
            }
        }
        return false;
    }


    public LinearLayout main_tab2;
    public  LinearLayout main_tab1;
    public int topHeight;
    //一定是在此方法中获取布局的实际高度
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topHeight = main_tab2.getBottom() - main_tab1.getHeight();
        }
    }

    public int getTopHeight() {
        return topHeight;
    }

    @Override
    public void getView(LinearLayout linearLayout_one, LinearLayout linearLayout_two) {
        main_tab1=linearLayout_one;
        main_tab2=linearLayout_two;
    }
}
