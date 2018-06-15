package wizrole.hoservice.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.fragment.Fragment_me;
import wizrole.hoservice.ui.AlertDialogFragment;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.Tol_util;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_name)EditText edit_name;
    @BindView(R.id.edit_password)EditText edit_password;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.check_auto)CheckBox check_auto;
    public Bundle bundle;
    public String num;  //用户输入的登记号
    public String tel;  //用户的手机号码
    public boolean is=false;
    public String new_Login;
    public String forgetLogin;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        new_Login=getIntent().getStringExtra("newLogin");//手势密码忘记入口
        forgetLogin=getIntent().getStringExtra("forgetLogin");//设置中心--忘记密码入口
        if(new_Login!=null){
            loginAgain();
        }
        if(forgetLogin!=null){
            loginAgain();
        }
        String auto=getIntent().getStringExtra("auto");
        if (auto!=null){    //自动登录失败情况下--设置登记号和手机号码在输入框内
            if(auto.equals("auto")){
                String num= SharedPreferenceUtil.getLoginNum(this);
                String tel=SharedPreferenceUtil.getTel(this);
                edit_name.setText(num);
                edit_password.setText(tel);
            }
        }
        text_title.setText(getString(R.string.login));
        check_auto.setChecked(Tol_util.getCheck(this,Tol_util.OPEN_LOGIN,is));
    }

    @Override
    protected void setListener() {
        check_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){//保存自动登录状态
                    tol_status=true;
                }else{
                    tol_status=false;
                }
            }
        });
    }

    public Dialog dialog;
    @OnClick({R.id.btn_login, R.id.lin_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:            //登录监听
                num=edit_name.getText().toString();
                tel=edit_password.getText().toString();
                //正则表达式--手机号码
                String phone="^1[34578]\\d{9}$";
                Pattern pattern = Pattern.compile(phone);
                Matcher matcher = pattern.matcher(tel);
                //先验证编号和手机是否为空
                if (edit_name.getText().toString().length() == 0) {
                    MyToast(getString(R.string.djh_null));
                } else if (edit_password.getText().toString().length() == 0) {
                    MyToast(getString(R.string.tel_null));
                    //其次来判断用户名和密码的格式合法性
                } else if (num.length() < 6 || num.length() > 12) {     //入院编号格式不正确
                    showUsernameError();
                } else if (!matcher.matches()) {        //身份证格式不正确
                    showPasswordError();
                } else {        //上传服务器
                    dialog= LoadingDailog.createLoadingDialog(LoginActivity.this,"登录中");
                    JSONObject object=new JSONObject();
                    try{
                        object.put("TradeCode","Y100");
                        object.put("RegNo",num);
                        object.put("TelNo",tel);
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
                                    Gson gson=new Gson();
                                    infor=new PersonInfor();
                                    infor=gson.fromJson(o.toString(),PersonInfor.class);
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        });
                    }catch (Exception e){
                        handler.sendEmptyMessage(0);
                    }
                }
                break;
            case R.id.lin_back:     //返回
                if(SharedPreferenceUtil.getBean(LoginActivity.this)==null){
                    //尚未登录 点击登陆-按返回--直接finish
                    //注销之后点击登陆，按返回--直接finish
                    finish();
                }else if(forgetLogin!=null){ //假如是点击设置的忘记密码入口--返回 也只需finish当前页面
                    finish();
                }else if(new_Login!=null){//手势密码进入的登录 也finish
                   finish();
                }else{
                    finish();
                }
                break;
        }
    }
    /**自动登录监听*/
    public boolean  tol_status=false;
    public PersonInfor infor;
    public static Handler handler_me;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){        //返回数据
                String resultCode=infor.getResultCode();    //处理结果编码
                LoadingDailog.closeDialog(dialog);
                 if(resultCode.equals("0")){//成功登录
                     //登录成功后---如果是验证手势密码
                     if(new_Login!=null){//标记是从验证手势密码进入的登录页面
                         //清除SharedpreferenceUtil中存储的手势密码
                         SharedPreferenceUtil.clearMima(LoginActivity.this);
                     }
                     MyToast(getString(R.string.login_succ));
                     //checkBox监听保存了自动登录的状态
                     SharedPreferenceUtil.saveLoginState(LoginActivity.this,1);//设置登录状态  1
                     //保存登记号和手机号码
                     SharedPreferenceUtil.saveLoginNum(LoginActivity.this, num);
                     SharedPreferenceUtil.saveTel(LoginActivity.this, tel);
                     SharedPreferenceUtil.putBean(LoginActivity.this,infor);//保存个人信息
                     Tol_util.setCheck(LoginActivity.this,Tol_util.OPEN_LOGIN,tol_status);//保存自动登录状态
                        //如果第一次登录，没有设置手势密码
                     if (SharedPreferenceUtil.getMama(LoginActivity.this).equals("")){
                         Intent intent=new Intent(LoginActivity.this,CreateGestureActivity.class);
                         startActivity(intent);
                         finish();
                     }else{ //设置了手势密码 --直接跳过
                         handler_me = Fragment_me.handler;
                         handler_me.sendEmptyMessage(9999);
                         Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                         startActivity(intent);
                         finish();
                     }
                }else if(resultCode.equals("-1")){  //入院编号格式不合法
                     showUsernameError();

                     SharedPreferenceUtil.saveLoginState(LoginActivity.this,0);//设置未登录状态  0
                }else if (resultCode.equals("-2")){     //系统未查询到
                     no_LookFor();

                     SharedPreferenceUtil.saveLoginState(LoginActivity.this,0);//设置未登录状态  0
                }else if (resultCode.equals("-3")){        //当前患者不在院
                     no_Hos();

                     SharedPreferenceUtil.saveLoginState(LoginActivity.this,0);//设置未登录状态  0
                }else if (resultCode.equals("-4")){     //登记号与手机号码不匹配
                     all_Error();
                     SharedPreferenceUtil.saveLoginState(LoginActivity.this,0);//设置未登录状态  0
                }
            }else if(msg.what==0){      //请求失败
                MyToast(getString(R.string.login_fail));
               LoadingDailog.closeDialog(dialog);
                SharedPreferenceUtil.saveLoginState(LoginActivity.this,0);//设置未登录状态  0
            }
        }
    };
    /**用户名或密码错误*/
    //此方法须在请求服务器之后获取登录状态进行用户提醒
    public void all_Error(){
        String msg = getResources().getString(R.string.name_password_err);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.name_password, msg);
        fragment.show(getSupportFragmentManager(), "all_Error");
    }
    /**系统中未查询到**/
    public void no_LookFor(){
        String msg = getResources().getString(R.string.no_hos);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.no_look_err, msg);
        fragment.show(getSupportFragmentManager(), "all_Error");
    }
    /**系统中未查询到**/
    public void no_Hos(){
        String msg = getResources().getString(R.string.no_hos);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.no_hos_err, msg);
        fragment.show(getSupportFragmentManager(), "all_Error");
    }
    // 用户名格式不正确Dialog
    //此方法无需请求服务器，通过正则表达式判断格式是否正确
    private void showUsernameError(){
        String msg = getResources().getString(R.string.userName_rule);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.userName_err, msg);
        fragment.show(getSupportFragmentManager(), "showUsernameError");
    }

    // 密码格式不正确Dialog
    //此方法无需请求服务器，通过正则表达式判断格式是否正确
    private void showPasswordError(){
        String msg = getString(R.string.id_rule);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.id_err, msg);
        fragment.show(getSupportFragmentManager(), "showPasswordError");
    }
    public void loginAgain(){
        String msg = getResources().getString(R.string.login_again_clear);
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.string.login_again, msg);
        fragment.show(getSupportFragmentManager(), "login_again");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (SharedPreferenceUtil.getBean(LoginActivity.this)==null){
            finish();
        }else if(forgetLogin!=null){ //假如是点击设置的忘记密码入口--返回 也只需finish当前页面
            finish();
        }else if(new_Login!=null){
            finish();
        }else{
            finish();
        }
    }
}
