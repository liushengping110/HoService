package wizrole.hoservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.CustMess_adapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.CustMess;
import wizrole.hoservice.beam.CustMessage;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 * 自定义消息列表
 */

public class CustomMessActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.list_custmess)ListView list_custmess;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int totalNum;
    public int page=1;
    public CustMess custMess;
    public List<CustMessage> custMessages=new ArrayList<>();
    public List<CustMessage> final_custMessages=new ArrayList<>();
    public CustMess_adapter adapter;
    @Override
    protected int getLayout() {
        return R.layout.activity_custmess;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.noti_mess));
        setXRfreshView();
//        getData(page);
        setView();
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }
    public void setView(){
        for (int i=0;i<2;i++){
            CustMessage custMessage=new CustMessage();
            if(0==0){
                custMessage.setCustMess_imgurl("");
                custMessage.setCustMess_title("我院召开党建与党风廉政建设工作会议");
                custMessage.setCustMess_content(MyApplication.getContextObject().getString(R.string.cust_msg_one));
                custMessages.add(custMessage);
            }else {
                custMessage.setCustMess_imgurl("");
                custMessage.setCustMess_title("安全生产工作会议召开");
                custMessage.setCustMess_content(MyApplication.getContextObject().getString(R.string.cust_msg_two));
                custMessages.add(custMessage);
            }
            custMessage=null;
        }
        custMess=new CustMess();
        custMess.setCustMessage(custMessages);
        custMess.setResultCode("0");
        custMess.setResultContent("");
        custMess.setTotalNum(1);
        handler.sendEmptyMessage(2);
    }
    public void getData(int page){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y135");
            object.put("PageNo", page);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler.sendEmptyMessage(1);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(1);
                    }else{
                        Gson gson=new Gson();
                        custMess=new CustMess();
                        custMess=gson.fromJson(o.toString(),CustMess.class);
                        if(custMess.getResultCode().equals("0")){
                            handler.sendEmptyMessage(2);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(1);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://无数据
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_custmess.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(),R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                    text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
                    text_no_data.setBackgroundResource(R.color.white);
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 1://失败
                    if(page==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_custmess.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                        text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                        text_no_data.setBackgroundResource(R.drawable.login_sel);
                    }else{
                        refreshView.finishLoadmore(false);
                    }
                    MyToast(MyApplication.getContextObject().getString(R.string.wifi_err));
                    break;
                case 2://成功
                    custMessages=custMess.getCustMessage();
                    totalNum=custMess.getTotalNum();
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list_custmess.setVisibility(View.VISIBLE);
                    if(page==1){
                        final_custMessages=custMessages;
                        adapter=new CustMess_adapter(CustomMessActivity.this,custMessages,R.layout.recy_item_healscien);
                        list_custmess.setAdapter(adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_custMessages.addAll(custMessages);
                        adapter.notifyDataSetChanged();refreshView.finishLoadmore(true);
                    }
                    refreshView.setEnableRefresh(true);
                    refreshView.setEnableLoadmore(true);
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        //点击重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    pro_main.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    page=1;
                    getData(page);
                }
            }
        });

        list_custmess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustMessage name=final_custMessages.get(i);
                Intent intent=new Intent(CustomMessActivity.this,DynamicDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("custMessage",name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                final_custMessages=null;
//                page=1;
//                getData(page);
                final_custMessages.clear();
                setView();
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData(page);
                }else{
                    refreshView.finishLoadmore(true);
                    MyToast(MyApplication.getContextObject().getString(R.string.bottom_no_data));
                    refreshView.finishLoadmoreWithNoMoreData();

                }
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
