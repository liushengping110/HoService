package wizrole.hoservice.life.view;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by 何人执笔？ on 2018/4/12.
 * liushengping
 * 订单支付
 */

public class OrderPayActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_pay_ff)TextView text_pay_ff;
    @BindView(R.id.img_erweima)ImageView img_erweima;
    public int type;
    @Override
    protected int getLayout() {
        return R.layout.activity_orderpay;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("订单支付");
        type=getIntent().getIntExtra("type",1);
        switch (type){
            case 1:
                text_pay_ff.setText("请使用支付宝扫描该二维码进行订单支付");
                break;
            case 2:
                text_pay_ff.setText("请使用微信扫描该二维码进行订单支付");
                break;
            case 3:
                text_pay_ff.setText("请使用银联扫描该二维码进行订单支付");
                break;
        }
        ImageLoading.common(OrderPayActivity.this,"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=796304741,232370400&fm=27&gp=0.jpg",img_erweima,R.drawable.img_loadfail);
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog("是否放弃订单支付","如您已扫码，请等待支付结果",false);
            }
        });
    }

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
            text_cancle.setText("放弃支付");
            text_sure.setText("继续支付或等待支付结果");
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
                    MyToast("请你扫描二维码进行订单支付，如您已扫描，请等待支付结果");
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
    public void onBackPressed() {
        initDialog("是否放弃订单支付","如您已扫码，请等待支付结果",false);
    }
}
