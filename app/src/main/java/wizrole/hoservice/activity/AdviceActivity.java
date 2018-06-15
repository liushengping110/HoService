package wizrole.hoservice.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.ListAdAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.Advice_infor;
import wizrole.hoservice.beam.OrdList;
import wizrole.hoservice.beam.OrderList;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/8/29.
 */

public class AdviceActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_ad)ListView list_ad;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;
    @BindView(R.id.pro_main)ProgressBar pro_main;

    public String admNo;
    public String now;
    @Override
    protected int getLayout() {
        return R.layout.activity_advice;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.doc_advice));
        PersonInfor infor= (PersonInfor) SharedPreferenceUtil.getBean(this);
        admNo=infor.getAdmNo();
        //获取当前时间，进入费用清单，textview默认显示当前时间  以及对应的当天费用清单列表
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        now=format.format(date);    //当前时间
        list_temp=new ArrayList<OrderList>();
        list_temp_tomorrow=new ArrayList<OrderList>();
        //获取数据
        getData(now);
    }


    @Override
    protected void setListener() {
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_err_agagin_center.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    getData(now);
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

    public void getData(String date){
        JSONObject object=new JSONObject();
        try{
            object.put("TradeCode","Y102");
            object.put("AdmNo", admNo);    //登录接口返回的就诊号
            object.put("StDate", date);
            object.put("EndDate",date);
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
                        infor=new Advice_infor();
                        infor=gson.fromJson(o.toString(),Advice_infor.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(2);
                        }
                    }
                }
            });
        }catch (Exception e){
            handler.sendEmptyMessage(0);
        }
    }


    public Advice_infor infor;
    public OrdList ordList;
    public List<OrderList> list;
    Handler handler = new  Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://失败
                    pro_main.setVisibility(View.INVISIBLE);
                    list_ad.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.net_err, img_net_err, R.drawable.net_err);
                    text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                    text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                    text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                    break;
                case 1://成功
                    ordList = infor.getOrdList();
                    list = ordList.getOrderList();
                    new SelctList().start();
                    break;
                case 2://无数据
                    pro_main.setVisibility(View.INVISIBLE);
                    list_ad.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.null_data, img_net_err, R.drawable.null_data);
                    text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                    text_err_agagin_center.setBackgroundResource(R.color.white);
                    text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.text_bule));
                    break;
                case 3://筛选后--设置适配器
                    pro_main.setVisibility(View.INVISIBLE);
                    list_ad.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    ListAdAdapter adapter = new ListAdAdapter(list, AdviceActivity.this, size_type);
                    list_ad.setAdapter(adapter);
                    break;
            }
        }
    };

    public List<OrderList> list_temp;
    public List<OrderList> list_temp_tomorrow;
    public boolean status_today=true;//显示今天
    public boolean status_tomorrow=true;//显示次日
    public int  size_type;
    class  SelctList extends Thread{
        @Override
        public void run() {
            for(int i=0;i<list.size();i++){
                if (list.get(i).getOrdDate().equals(now)){//今天的
                    if (status_today){//标记每天的第一位显示
                        list.get(i).setStatus_today(true);
                        status_today=false;
                    }
                    list_temp.add(list.get(i));
                    size_type=i;
                }else{//次日的
                    if (status_tomorrow){//标记每天的第一位显示
                        list.get(i).setStatus_tomorrow(true);
                        status_tomorrow=false;
                    }
                    list_temp_tomorrow.add(list.get(i));
                }
            }
            list.clear();
            list.addAll(list_temp);
            list.addAll(list_temp_tomorrow);
            handler.sendEmptyMessage(3);
            super.run();
        }
    }
}
