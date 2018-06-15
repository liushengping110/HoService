package wizrole.hoservice.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.Pay_adapter;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.PayInfor;
import wizrole.hoservice.beam.TarItemDetail;
import wizrole.hoservice.util.EventUtil;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;

public class Fragment_pay_three extends LazyFragment{
    private LinearLayout lin_type;
    private LinearLayout lin_header;
    public RecyclerView list_detail;
    private int tabIndex;
    public static final String INTENT_INT_INDEX="index";
    public Date date;
    public Date one;
    public String now_frist;
    public SimpleDateFormat format_Y;
    public SimpleDateFormat format_M;
    public SimpleDateFormat format_D;
    //
    public ImageView img_net_err;
    public RelativeLayout lin_wifi_err;
    public TextView text_no_data;
    public RelativeLayout pro_main;


    public static Fragment_pay_three newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        Fragment_pay_three fragment = new Fragment_pay_three();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_pay);
        if (!EventBus.getDefault().isRegistered(this)){//注册
            EventBus.getDefault().register(this);
        }
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        lin_type=(LinearLayout)findViewById(R.id.lin_frag_one);
        lin_header=(LinearLayout)findViewById(R.id.lin_header);
        list_detail=(RecyclerView) findViewById(R.id.list_one);
        list_detail.setLayoutManager(new LinearLayoutManager(getActivity()));
        pro_main=(RelativeLayout) findViewById(R.id.pro_main);
        lin_wifi_err=(RelativeLayout) findViewById(R.id.lin_wifi_err);
        img_net_err=(ImageView) findViewById(R.id.img_net_err);
        text_no_data=(TextView) findViewById(R.id.text_no_data);
        getData(tabIndex);
        setListener();
    }
    public void getData(int tabIndex){
        switch (tabIndex){
            case 1:
                getData(getTime(3));
                break;
            case 2:
                getData(getTime(2));
                break;
            case 3:
                getData(getTime(1));
                break;
            case 4:
                break;
        }
    }
    public String msg_sel;
    @Subscribe
    public void onEventAsync(EventUtil eventUtil){
        msg_sel=eventUtil.getMsg();
        if (!msg_sel.equals("")&&msg_sel!=null){
            getData(msg_sel);
        }else{
            Toast.makeText(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.wifi_err), Toast.LENGTH_SHORT).show();

        }
    }

    /***获取时间**/
    public String getTime(int ind){//参数为三  则查询日期为大前天--布局就为第一个页面
        date =new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        format_Y = new SimpleDateFormat("yyyy");
        format_M= new SimpleDateFormat("MM");
        format_D=new SimpleDateFormat("dd");
        //获取当前时间，进入费用清单，textview默认显示当前时间  以及对应的当天费用清单列表
        date =new Date();
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -ind);  //设置为大前天
        one = calendar.getTime();   //得到大前天的时间
        now_frist=format.format(one);
        return now_frist;
    }

    public PayInfor infor;
    public void getData(String date){
        JSONObject object=new JSONObject();
        try{
            object.put("TradeCode","Y101");
            object.put("CardNo", SharedPreferenceUtil.getLoginNum(getActivity()));
            object.put("StartDate", date);
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
                    }else {
                        Gson gson=new Gson();
                        infor=new PayInfor();
                        infor=gson.fromJson(o.toString(),PayInfor.class);
                        if(infor.getResultCode().equals("0")){
                            list=infor.getTarItemDetail();
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

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeMessages(1);
    }


    public List<TarItemDetail> list;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://断网
                    pro_main.setVisibility(View.INVISIBLE);
                    list_detail.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.net_err, img_net_err, R.drawable.net_err);
                    ToastUtil.MyToast(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.wifi_err));
                    break;
                case 1://成功
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    lin_header.setVisibility(View.VISIBLE);
                    //设置适配器
                    lin_type.setVisibility(View.VISIBLE);
                    list_detail.setVisibility(View.VISIBLE);
                    if(tabIndex==4){
                        String[] str=msg_sel.split("-");
                        setHeader(list, str[1] + "-" + str[2]);
                    }else{
                        setHeader(list, format_M.format(one) + "-" + format_D.format(one));
                    }
                    Pay_adapter adapter = new Pay_adapter(getActivity(),list);
                    list_detail.setAdapter(adapter);
                    break;
                case 2://无数据
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_type.setVisibility(View.INVISIBLE);
                    lin_header.setVisibility(View.INVISIBLE);
                    list_detail.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.null_pay_list));
                    ToastUtil.MyToast(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.data_numm));
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.null_data, img_net_err, R.drawable.null_data);
                    break;
            }
        }
    };

    public void setListener(){
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    getData(tabIndex);
                }
            }
        });
    }

    public double allPrice;
    public void  setHeader(List<TarItemDetail> list, String date ){
        TextView text_money=(TextView)findViewById(R.id.text_header_money);
        TextView text_date=(TextView)findViewById(R.id.text_header_date);
        for(int i=0;i<list.size();i++){
            double b = Double.parseDouble(list.get(i).getTotalAmt());   //字符型转Double类型
            allPrice+=b;
        }
        DecimalFormat format=new DecimalFormat("#0.00#");   //保留两位小数点
        text_date.setText(date);
        text_money.setText(format.format(allPrice)+"");
    }
}