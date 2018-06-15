package wizrole.hoservice.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.ListDetailAdapter;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.PayInfor;
import wizrole.hoservice.beam.TarItemDetail;
import wizrole.hoservice.util.EventUtil;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;

/**
 * Created by a on 2016/11/29.
 * 用药费用
 */


public class Fragment_pay_four extends Fragment {
    public ListView list_four;
    public LinearLayout lin_frag_four;
    private View view;
    public String msg;
    public List<TarItemDetail> list;
    public View incl_header;

    public ImageView img_net_err;
    public RelativeLayout lin_wifi_err;
    public TextView text_no_data;
    public RelativeLayout pro_main;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view=inflater.inflate(R.layout.fragment_four,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_four=(ListView)view.findViewById(R.id.list_four);
        lin_frag_four=(LinearLayout)view.findViewById(R.id.lin_frag_four);
        incl_header=(View)view.findViewById(R.id.incl_header);

        pro_main=(RelativeLayout)view. findViewById(R.id.pro_main);
        lin_wifi_err=(RelativeLayout) view.findViewById(R.id.lin_wifi_err);
        img_net_err=(ImageView) view.findViewById(R.id.img_net_err);
        text_no_data=(TextView)view. findViewById(R.id.text_no_data);

        list_four.setVisibility(View.INVISIBLE);
        lin_frag_four.setVisibility(View.INVISIBLE);
        incl_header.setVisibility(View.INVISIBLE);
        setListener();
    }

    @Subscribe
    public void onEventAsync(EventUtil eventUtil){
        msg=eventUtil.getMsg();
        if (!msg.equals("")&&msg!=null){
            getData(msg);
        }else{
            list_four.setVisibility(View.INVISIBLE);
            incl_header.setVisibility(View.INVISIBLE);
            ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.wifi_err));
        }
}
    public PayInfor infor;
    /**网络请求**/
    public void getData(String date){
        JSONObject object=new JSONObject();
        try{
            object.put("TradeCode","Y101");
            object.put("CardNo", SharedPreferenceUtil.getLoginNum(MyApplication.getContextObject()));
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
                    }else{
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

    Handler handler=new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://duanwang
                    pro_main.setVisibility(View.INVISIBLE);
                    list_four.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.net_err, img_net_err, R.drawable.net_err);
                    ToastUtil.MyToast(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.wifi_err));

                    break;
                case 1://成功
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    //设置适配器
                    lin_frag_four.setVisibility(View.VISIBLE);
                    list_four.setVisibility(View.VISIBLE);
                    incl_header.setVisibility(View.VISIBLE);
                    setHeader(list);
                    ListDetailAdapter adapter=new ListDetailAdapter(list,MyApplication.getContextObject());
                    list_four.setAdapter(adapter);
                    break;
                case 2://无数据
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.null_pay_list));
                    ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject(). getString(R.string.data_numm));
                    ImageLoading.common(MyApplication.getContextObject(), R.drawable.null_data, img_net_err, R.drawable.null_data);
                    list_four.setVisibility(View.INVISIBLE);
                    lin_frag_four.setVisibility(View.INVISIBLE);
                    incl_header.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };
    public double allPrice;
    public boolean isFrist=true;
    public void  setHeader(List<TarItemDetail> list){
        incl_header.setVisibility(View.VISIBLE);
        TextView text_money=(TextView)view.findViewById(R.id.text_header_money);
        TextView text_date=(TextView)view.findViewById(R.id.text_header_date);
        for(int i=0;i<list.size();i++){
            double b = Double.parseDouble(list.get(i).getTotalAmt());   //字符型转Double类型
            allPrice+=b;
        }
        DecimalFormat format=new DecimalFormat("#0.00#");   //保留两位小数点
        text_money.setText(format.format(allPrice)+"");
        allPrice=0;
        String[]  strs=msg.split("-");
        text_date.setText(strs[1]+"-"+strs[2]);
        if(isFrist){
            isFrist=false;
        }
    }

    public void setListener(){
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    getData(msg);
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}