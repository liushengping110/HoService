package wizrole.hoservice.activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PaySataus;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by liushengping on 2017/11/23/023.
 * 何人执笔？
 */

public class ImageActivity extends BaseActivity {
    @BindView(R.id.img_erweima)ImageView img_erweima;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    public String free;
    public String tradeNum;
    public String tradeMoney;
    public Timer timer;
    public TimerTask timerTask;
    @Override
    protected int getLayout() {
        return R.layout.activity_image;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("押金充值");
        free=getIntent().getStringExtra("free");
        tradeNum=getIntent().getStringExtra("tradeNum");
        tradeMoney=getIntent().getStringExtra("tradeMoney");
        config();
        creatErWeiMa(free);
        //定时任务查询订单状态
        timerTask=new TimerTask() {
            @Override
            public void run() {
//                getStatus();//查询订单状态
            }
        };
        //设置时间长短
        timer=new Timer();
        timer.schedule(timerTask,4000, 3000);//4秒后开始查询，间隔时间为3秒
    }



    public void creatErWeiMa(String payUrl){
//        int width = DensityUtil.dip2px(this, 400);
//        Bitmap bitmap = EncodingUtils.createQRCode(payUrl, width, width, BitmapFactory.decodeResource(getResources(),
//                R.drawable.hos_logo));
//        img_erweima.setImageBitmap(bitmap);
        ImageLoading.common(ImageActivity.this,payUrl,img_erweima,R.drawable.img_loadfail);
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog("是否放弃此次押金缴纳","如您已扫码，请等待支付结果",false);
            }
        });
    }

    public void getStatus(){
        PersonInfor infor=(PersonInfor)SharedPreferenceUtil.getBean(ImageActivity.this);
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y116");
            object.put("TradeNo",tradeNum);
            object.put("PatAmt",tradeMoney);
            object.put("AdmId",infor.getAdmNo());
            object.put("PatientId",SharedPreferenceUtil.getLoginNum(ImageActivity.this));
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
                    Gson gson=new Gson();
                    PaySataus paySataus=new PaySataus();
                    paySataus=gson.fromJson(o.toString(),PaySataus.class);
                    String code=paySataus.getResultCode();
                    if(code.equals("0")){//充值成功
                        handler.sendEmptyMessage(0);
                    }else if(code.equals("1")){//充值未到医院账户
                        handler.sendEmptyMessage(1);
                    }else{//充值到医院账户，但是未到卡里
                        handler.sendEmptyMessage(2);
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
            switch (msg.what){
                case 0://充值成功
                    timer.cancel();
                    initDialog("充值成功","恭喜您，充值成功",true);
                    break;
                case 1://充值未到医院账户
                    //继续请求
                    break;
                case 2://充值到医院账户，但是未到卡里
                    //提示病人
                    timer.cancel();
                    initDialog("充值失败","充值遇到问题，请到门诊楼四楼微机中心",true);
                    break;
            }
        }
    };

    /**
     * 充值弹窗
     * msg--充值成功 ---充值失败
     */
    public AlertDialog dialog;
    public void initDialog(String msg, String message,final boolean status){
        dialog=new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_paysucc,null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);//取消
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);//确定
        TextView text_dialog_mess=(TextView)view.findViewById(R.id.text_dialog_mess);//详细内容
        View view_line=(View)view.findViewById(R.id.text_view);//线条
        TextView text_dialog_pay=(TextView)view.findViewById(R.id.text_dialog_pay);//提示内容标题
        if(status){//充值成功-失败
            text_dialog_pay.setText(msg);
            text_sure.setVisibility(View.VISIBLE);
            text_cancle.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
            text_dialog_mess.setText(message);
            text_sure.setText("确定");
        }else{//退出提示
            text_cancle.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            text_sure.setVisibility(View.VISIBLE);
            text_dialog_mess.setVisibility(View.VISIBLE);
            text_dialog_pay.setText(msg);
            text_dialog_mess.setText(message);
            text_cancle.setText("放弃充值");
            text_sure.setText("继续充值或等待支付结果");
        }
        //确定
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(status){//充值成功--失败
                    setResult(11);
                    finish();
                }else{//返回页面---继续充值
                    MyToast("请你扫描二维码进行充值，如您已扫描，请等待充值结果");
                }
            }
        });
        //取消
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
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

    /**
     * 提高屏幕亮度
     */
    private void config() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
    }
    //降低屏幕亮度
    private void config_go() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 0.5f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
    @Override
    public void onBackPressed() {
        initDialog("是否放弃此次押金缴纳","如您已扫码，请等待支付结果",false);
    }
}
