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
import wizrole.hoservice.adapter.HealEduAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.HealEdu;
import wizrole.hoservice.beam.HealEduDatail;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by ${liushengping} on 2017/9/27.
 * 何人执笔？
 * 健康教育
 */

public class HealEduActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_healedu)ListView list_edu;

    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.xrefresh_view)RefreshLayout refreshView;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    public boolean status=false;//第一次进入  请求失败
    public int pageNum=1;
    public int totalNum;//总页数
    public List<HealEduDatail> final_list;
    @Override
    protected int getLayout() {
        return R.layout.activity_healedu;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_edu));
        final_list=new ArrayList<HealEduDatail>();
        pro_main.setVisibility(View.VISIBLE);
        setXRfreshView();
        getData(pageNum);
    }
    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }
    public HealEdu healEdu;
    public List<HealEduDatail> datails;
    public void getData(int pageNum){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y122");
            object.put("PageNo",pageNum);
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
                        healEdu=new HealEdu();
                        Gson  gson=new Gson();
                        healEdu=gson.fromJson(o.toString(),HealEdu.class);
                        if(healEdu.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(2);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(0);
        }
    }

    public HealEduAdapter adapter;
    public Handler  handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){//请求失败
                if(pageNum==1){
                    status=true;
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_edu.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(HealEduActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.wifi_err_try_again));
                }else{
                    status=false;
                    refreshView.finishLoadmore(false);
                }
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//成功
                status=false;
                datails=healEdu.getHealEduDatail();
                totalNum=healEdu.getTotalNun();
                pro_main.setVisibility(View.INVISIBLE);
                list_edu.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                if(pageNum==1){//加载第一页
                    final_list=datails;
                    adapter=new HealEduAdapter(HealEduActivity.this,datails,R.layout.list_dynamic_item);
                    list_edu.setAdapter(adapter);
                    refreshView.finishRefresh(true);
                    refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                }else{
                    final_list.addAll(datails);
                    adapter.notifyDataSetChanged();
                    refreshView.finishLoadmore(true);
                }
                refreshView.setEnableRefresh(true);
                refreshView.setEnableLoadmore(true);
            }else if(msg.what==2){//无数据
                refreshView.finishRefresh(false);
                pro_main.setVisibility(View.INVISIBLE);
                list_edu.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(HealEduActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setVisibility(View.VISIBLE);
                text_no_data.setText(getString(R.string.data_numm));
                refreshView.setEnableRefresh(false);
                refreshView.setEnableLoadmore(false);
                MyToast(getString(R.string.data_numm));
            }
        }
    };
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list_edu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HealEduDatail datail=final_list.get(i);
                Intent intent=new Intent(HealEduActivity.this,HealEduDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("healEduDetail",datail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                pageNum=1;
                getData(pageNum);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            if(pageNum<totalNum){
                pageNum++;
                getData(pageNum);
            }else{
                if(status){
                    refreshView.finishLoadmore(false);
                }else{
                    ToastShow("已经没有更多数据了");
                    refreshView.finishLoadmoreWithNoMoreData();
                }
            }
            }
        });
        //点击重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro_main.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                pageNum=1;
                getData(pageNum);
            }
        });
    }
}
