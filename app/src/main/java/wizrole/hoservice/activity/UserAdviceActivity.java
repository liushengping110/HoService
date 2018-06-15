package wizrole.hoservice.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.beam.UserAdvice;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/8/13.
 * 用户意见反馈页面
 */

public class UserAdviceActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_get)TextView text_get;
    @BindView(R.id.text_uatnum)TextView text_uatnum;
    @BindView(R.id.edit_useradvice)EditText edit_useradvice;

    @Override
    protected int getLayout() {
        return R.layout.activity_useradvice;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_uatnum.setText(0+"/"+400);
        text_title.setText(getString(R.string.user_advice));
    }

    @Override
    protected void setListener() {
        edit_useradvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = edit_useradvice.getText().toString();
                text_uatnum.setText(content.length() + "/"
                        + 400);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(edit_useradvice.getText().toString().length()>=400){
                    ToastShow(getString(R.string.order_bznum));
                }
            }
        });

        text_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_useradvice.getText().length()==0){
                    MyToast(getString(R.string.advice_no));
                }else if(edit_useradvice.getText().length()>=400){
                    MyToast(getString(R.string.order_bznum));
                }else{
                    LoginMsg(edit_useradvice.getText().toString());
                }
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void LoginMsg(String msg){
        showProgress();
        PersonInfor infor= (PersonInfor) SharedPreferenceUtil.getBean(UserAdviceActivity.this);
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y113");
            object.put("Name",infor.getName());
            object.put("zyno",infor.getZyno());
            object.put("TelNo",infor.getTelNo());
            object.put("UserAdvices",msg);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(0);
                    }else{
                        UserAdvice advice=new UserAdvice();
                        Gson gson=new Gson();
                        advice=gson.fromJson(o.toString(),UserAdvice.class);
                        if(advice.getResultStatus().equals("成功")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(0);
        }
    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){//失败
                hideProgress();
                MyToast("很抱歉，提交失败，请检查网络!");
            }else{//成功
                hideProgress();
                MyToast("提交成功，非常感谢您的宝贵意见和建议！");
                finish();
            }
        }
    };

    /**展示登录进度视图*/
    private ProgressDialog progressDialog;
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "正在提交,请稍候...");
    }
    /**隐藏登录进度视图*/
    public void hideProgress() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


    /**
     　　* 保存文件
     　　* @param toSaveString
     　　* @param filePath
     　　*/
    public  void saveFile(String toSaveString){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String rel = formatter.format(curDate);
            String filePath="/sdcard/wizrole/useradvice/";
            String filePathName=filePath+rel;
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(filePathName);
            outStream.write(toSaveString.getBytes());
            outStream.close();
            } catch (FileNotFoundException e){
            e.printStackTrace();
            MyToast("很抱歉，意见反馈提交失败，请重试！");
            } catch (IOException e){
            e.printStackTrace();
            MyToast("很抱歉，意见反馈提交失败，请重试！");
            }
    }
}
