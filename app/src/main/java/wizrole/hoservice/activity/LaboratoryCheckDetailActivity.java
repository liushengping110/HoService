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
import wizrole.hoservice.adapter.LabCheckNameAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.LabCheckName;
import wizrole.hoservice.beam.LaboraCheckDetail;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 化验检查详细列表
 */

public class LaboratoryCheckDetailActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.disease_detail_infor)ListView list_name;
    @BindView(R.id.rel_search)RelativeLayout rel_search;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int page=1;
    public List<LabCheckName> final_list;
    public List<LabCheckName> labCheck_list;
    public String name;
    public LaboraCheckDetail detail;
    public int totalNum;
    public LabCheckNameAdapter adapter;
    @Override
    protected int getLayout() {
        return R.layout.activity_labcheckdetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.xm_list));
        name=getIntent().getStringExtra("CateName");
        final_list=new ArrayList<LabCheckName>();
        setXRfreshView();
        getData(page,name);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    public void getData(int page,String name){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y130");
            object.put("PageNo", page);
            object.put("LaboratCheckCateName", name);
            object.put("LaboratCheckCateType", "");
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
                        detail=new LaboraCheckDetail();
                        Gson gson=new Gson();
                        detail=gson.fromJson(o.toString(),LaboraCheckDetail.class);
                        if(detail.getResultCode().equals("0")){
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
            if(msg.what==0){//断网
                if(page==1){
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    list_name.setVisibility(View.INVISIBLE);
                    ImageLoading.common(LaboratoryCheckDetailActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.wifi_err_try_again));
                }else {
                    refreshView.finishLoadmore(false);
                }
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//无数据
                refreshView.finishRefresh(false);
                pro_main.setVisibility(View.INVISIBLE);
                list_name.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(LaboratoryCheckDetailActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setVisibility(View.VISIBLE);
                text_no_data.setText(getString(R.string.data_numm));
                refreshView.setEnableRefresh(false);
                refreshView.setEnableLoadmore(false);
                MyToast(getString(R.string.data_numm));
            }else if(msg.what==2){
                pro_main.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                labCheck_list=detail.getLabCheckName();
                totalNum=detail.getTotalNum();
                list_name.setVisibility(View.VISIBLE);
                if(page==1){//加载第一页
                    final_list=labCheck_list;
                    adapter=new LabCheckNameAdapter(LaboratoryCheckDetailActivity.this,labCheck_list,R.layout.list_depar_item);
                    list_name.setAdapter(adapter);
                    refreshView.finishRefresh(true);
                    refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                }else{
                    final_list.addAll(labCheck_list);
                    adapter.notifyDataSetChanged();
                    refreshView.finishLoadmore(true);
                }
                refreshView.setEnableRefresh(true);
                refreshView.setEnableLoadmore(true);
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
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    page=1;
                    getData(page,name);

                }
            }
        });

        list_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LabCheckName checkName=final_list.get(i);
                Intent intent=new Intent(LaboratoryCheckDetailActivity.this,LabCheckDetailInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("infor",checkName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                page=1;
                getData(page,name);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<=totalNum){
                    page++;
                    getData(page,name);
                }else{
                    refreshView.finishLoadmore(true);
                    refreshView.finishLoadmoreWithNoMoreData();
                    MyToast(getString(R.string.bottom_no_data));
                }
            }
        });

        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LaboratoryCheckDetailActivity.this,LabCheckSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
