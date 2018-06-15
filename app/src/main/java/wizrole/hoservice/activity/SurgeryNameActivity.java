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
import wizrole.hoservice.adapter.SurgeryNameAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.SurgeryDetail;
import wizrole.hoservice.beam.SurgeryDetailInfor;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by ${liushengping} on 2017/10/18.
 * 何人执笔？
 * 手术名称列表
 */

public class SurgeryNameActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_westmed)ListView list_westmed;
    @BindView(R.id.rel_search)RelativeLayout rel_search;//搜索

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int page=1;
    public int totalNum;
    public String name;
    public List<SurgeryDetailInfor> final_list;
    public List<SurgeryDetailInfor> surgeryDetails;
    public boolean status=false;
    public SurgeryNameAdapter adapter;
    @Override
    protected int getLayout() {
        return R.layout.activity_surgeryname;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.sur_list));
        name=getIntent().getStringExtra("surgeryName");
        final_list=new ArrayList<SurgeryDetailInfor>();
        list_westmed.setVisibility(View.INVISIBLE);
        pro_main.setVisibility(View.VISIBLE);
        setXRfreshView();
        getData(name,page);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    public SurgeryDetail surgeryDetail;
    public void getData(String name,int page){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y132");
            object.put("PageNo", page);
            object.put("SurgeryName", name);
            object.put("keyword","");
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
                    }else {
                        surgeryDetail=new SurgeryDetail();
                        Gson gson=new Gson();
                        surgeryDetail=gson.fromJson(o.toString(),SurgeryDetail.class);
                        if(surgeryDetail.getResultCode().equals("0")){
                            surgeryDetails=surgeryDetail.getSurgeryDetailInfor();
                            handler.sendEmptyMessage(2);
                        }else{
                            handler.sendEmptyMessage(1);
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
            switch (msg.what){
                case 0://失败
                    status=true;
                    if(page==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_westmed.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(SurgeryNameActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText(getString(R.string.wifi_err_try_again));
                    }else{
                        refreshView.finishLoadmore(false);
                    }
                    MyToast(getString(R.string.wifi_err));
                    break;
                case 1://无数据
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_westmed.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(SurgeryNameActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 2://chengg
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list_westmed.setVisibility(View.VISIBLE);
                    totalNum=surgeryDetail.getTotalNum();
                    if(page==1){
                        final_list=surgeryDetails;
                        adapter=new SurgeryNameAdapter(SurgeryNameActivity.this,surgeryDetails,R.layout.list_depar_item);
                        list_westmed.setAdapter(adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_list.addAll(surgeryDetails);
                        adapter.notifyDataSetChanged();
                        refreshView.finishLoadmore(true);
                    }
                    refreshView.setEnableRefresh(true);
                    refreshView.setEnableLoadmore(true);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_westmed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SurgeryDetailInfor infor=final_list.get(i);
                Bundle bundle=new Bundle();
                bundle.putSerializable("surgeryDetail",infor);
                Intent intent=new Intent(SurgeryNameActivity.this,SurgeryDetailInforActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                page=1;
                getData(name,page);

            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData(name,page);
                }else{
                    refreshView.finishLoadmore(true);
                    MyToast("已经没有更多数据了");
                    refreshView.finishLoadmoreWithNoMoreData();

                }
            }
        });
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    page=1;
                    getData(name,page);
                }
            }
        });
        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SurgeryNameActivity.this,SurgerySearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
