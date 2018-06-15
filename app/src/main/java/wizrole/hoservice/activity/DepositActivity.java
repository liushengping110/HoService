package wizrole.hoservice.activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.Deposit;
import wizrole.hoservice.beam.PayInfor;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by a on 2017/8/30.
 */

public class DepositActivity extends BaseActivity {

    /**返回按钮*/
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;//缴费记录
    @BindView(R.id.text_deposit)TextView text_deposit;//
    @BindView(R.id.text_patfee)TextView text_patfee;//
    @BindView(R.id.lin_deposit_top)LinearLayout lin_deposit_top;//显示隐藏二维码全部界面
    @BindView(R.id.btn_sure_depo)Button btn_sure_depo;//确定
    @BindView(R.id.edit_depao)EditText edit_depao;//确定
    public boolean status=true;
    public String patfree;
    public String deposit;
    @Override
    protected int getLayout() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.free_jn));
        text_right.setText(getString(R.string.free_jl));
        edit_depao.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        getData();
    }

    @Override
    protected void setListener() {
        /**
         * 设置只能输入保留两位小数
         */
        edit_depao.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2){
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });
    }
    @OnClick({R.id.lin_back,R.id.text_right,R.id.btn_sure_depo})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.btn_sure_depo://确定缴费
                getImg();
                break;
            case R.id.text_right://缴费记录
                break;
        }
    }

    /**
     * 获取费用余额和花费
     */
    public PayInfor infor;
    public void getData(){
        dialog=LoadingDailog.createLoadingDialog(DepositActivity.this,"加载中");
        Calendar calendar=Calendar.getInstance();
        Date date =new Date();
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为昨天
        Date three = calendar.getTime();   //得到大前天的时间
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String now_frist=format.format(three);
        JSONObject object=new JSONObject();
        try{
            object.put("TradeCode","Y101");
            object.put("CardNo", SharedPreferenceUtil.getLoginNum(DepositActivity.this));
            object.put("StartDate", now_frist);
            object.put("EndDate",now_frist);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
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
                        infor=new PayInfor();
                        infor=gson.fromJson(o.toString(),PayInfor.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (Exception e){
            handler.sendEmptyMessage(0);
        }
    }

    public Deposit  de;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LoadingDailog.closeDialog(dialog);
                    MyToast(MyApplication.getContextObject().getString(R.string.wifi_err));
                    text_deposit.setText(MyApplication.getContextObject().getString(R.string.no_know));
                    text_patfee.setText(MyApplication.getContextObject().getString(R.string.no_know));
                    break;
                case 1:
                    LoadingDailog.closeDialog(dialog);
                    patfree=infor.getPatFee();
                    deposit=infor.getDeposit();
                    text_deposit.setText(deposit);
                    text_patfee.setText(patfree);
                    break;
                case 11:
                    LoadingDailog.closeDialog(dialog);
                    String payUrl=de.getPayUrl();
                    String tradeNum=de.getTradeNum();
                    if(!payUrl.equals("")){
                        Intent intent=new Intent(DepositActivity.this,ImageActivity.class);
//                        intent.putExtra("free",payUrl);
                        intent.putExtra("free","https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=796304741,232370400&fm=27&gp=0.jpg");
                        intent.putExtra("tradeMoney",edit_depao.getText().toString());
                        intent.putExtra("tradeNum",tradeNum);
                        startActivityForResult(intent,10);
                    }else{
                        MyToast("很抱歉，二维码获取失败");
                    }
                    break;
                case 10:
                    LoadingDailog.closeDialog(dialog);
                    MyToast("很抱歉，二维码获取失败");
                    break;
            }
        }
    };

    //充值成功回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10){
            if(resultCode==11){
                getData();//重新刷新一次费用信息
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取二维码地址
     */
    public void getImg(){
        PersonInfor infor=(PersonInfor) SharedPreferenceUtil.getBean(DepositActivity.this);
        if(infor.getAdmNo()==null||infor.getAdmNo().equals("")){
            MyToast("就诊号获取失败");
        }else if(SharedPreferenceUtil.getLoginNum(DepositActivity.this).equals("")){
            MyToast("住院登记号获取失败");
        }else if(edit_depao.getText().length()==0){
            MyToast("请输入缴纳金额");
        }else{
            colseInput();
            getImgNext();
        }
    }

    public void colseInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_depao.getWindowToken(), 0);
    }

    public Dialog dialog;
    /**
     * 获取二维码地址
     */
    public void getImgNext(){
        dialog= LoadingDailog.createLoadingDialog(DepositActivity.this,"加载中");
        PersonInfor infor=(PersonInfor) SharedPreferenceUtil.getBean(DepositActivity.this);
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y115");
            object.put("PatientId", SharedPreferenceUtil.getLoginNum(DepositActivity.this));
            object.put("PatAmt", edit_depao.getText().toString());
            object.put("AdmId", infor.getAdmNo());
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler.sendEmptyMessage(10);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(10);
                    }else {
                        Gson gson=new Gson();
                        de=new Deposit();
                        de=gson.fromJson(o.toString(),Deposit.class);
                        if(de.getResultCode().equals("0")){
                            handler.sendEmptyMessage(11);
                        }else{
                            handler.sendEmptyMessage(10);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(10);
        }
    }
}
