package wizrole.hoservice.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.fragment.Fragment_me;
import wizrole.hoservice.util.DelImage;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.Tol_util;

/**
 * 设置中心
 * Created by a on 2017/4/18.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.lin_mima)LinearLayout lin_mima;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_mima)TextView text_mima;
    @BindView(R.id.btn_zhuxiao)TextView btn_zhuxiao;

    @Override
    protected int getLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.set));
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.lin_back, R.id.lin_mima,R.id.btn_zhuxiao})
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.lin_back: //返回
                finish();
                break;
            case R.id.lin_mima: //安全设置
                if(SharedPreferenceUtil.getLoginState(SettingActivity.this)==1){
                    intent=new Intent(SettingActivity.this,SafeActivity.class);
                    startActivity(intent);
                }else{
                    MyToast(getString(R.string.now_go_login));
                }
                break;
            case R.id.btn_zhuxiao://注销
                initDialog();
                break;
        }
    }

    public static Handler handler;
    public AlertDialog dialog;
    public void initDialog(){
        dialog=new AlertDialog.Builder(this).create();
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_zhuxiao,null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        text_sure.setOnClickListener(listener);//确定注销监听
        //设置date布局
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(view);
        dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    public View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            //清除用户登录数据
            Tol_util.setCheck(SettingActivity.this,Tol_util.OPEN_LOGIN,false);
            SharedPreferenceUtil.clearUser(SettingActivity.this);//清除用户信息实体类
            SharedPreferenceUtil.clearLoginNum(SettingActivity.this);//清除用户登记号
            SharedPreferenceUtil.clearTel(SettingActivity.this);//清除用户手机号码
            SharedPreferenceUtil.clearMima(SettingActivity.this);//清除用户手势密码
            SharedPreferenceUtil.clearLoginState(SettingActivity.this);//清除登录状态
            SharedPreferenceUtil.clearPersonHeader(SettingActivity.this);//清除用户头像数据
            DelImage delImage=new DelImage();//删除用户相册数据
            delImage.startDel(SettingActivity.this);
            handler= Fragment_me.handler;
            handler.sendEmptyMessage(9999);
            finish();
        }
    };
}
