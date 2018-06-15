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
import wizrole.hoservice.adapter.SerachSurgeryAdapter;
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
 * 手术搜索结果展示
 */

public class ShowSurgerySearchActivity extends BaseActivity {

    @BindView(R.id.list_dis)ListView list_dis;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;

    public String editSearch;
    public boolean status=false;
    public static long lastRefreshTime;//刷新时间
    public int page=1;
    public int totalNum;
    public List<SurgeryDetailInfor> final_list;

    @Override
    protected int getLayout() {
        return R.layout.activity_showdiseasesearch;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        editSearch=getIntent().getStringExtra("search");
        text_title.setText(getString(R.string.search_result));
        final_list=new ArrayList<SurgeryDetailInfor>();
        setXRfreshView();
        getData(editSearch,page);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    public SurgeryDetail searchDis;
    public List<SurgeryDetailInfor> diseaseNames;
    public SerachSurgeryAdapter adapter;
    public void getData(String msg,int page){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y132");
            object.put("keyword",msg);
            object.put("SurgeryName", "");
            object.put("PageNo",page);
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
                        searchDis=new SurgeryDetail();
                        Gson gson=new Gson();
                        searchDis=gson.fromJson(o.toString(),SurgeryDetail.class);
                        //搜索结果
                        if(searchDis.getResultCode().equals("0")){
                            handler.sendEmptyMessage(2);
                        }else {
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
                status=true;
                if(page==1){
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_dis.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(ShowSurgerySearchActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.wifi_err_try_again));
                    text_no_data.setTextColor(getResources().getColor(R.color.white));
                    text_no_data.setBackgroundResource(R.drawable.login_sel);
                }else{
                    refreshView.finishLoadmore(false);
                }
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//无数据
                refreshView.finishRefresh(false);
                pro_main.setVisibility(View.INVISIBLE);
                list_dis.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(ShowSurgerySearchActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setVisibility(View.VISIBLE);
                text_no_data.setBackgroundResource(R.color.white);
                text_no_data.setTextColor(getResources().getColor(R.color.shenhui));
                text_no_data.setText(getString(R.string.data_numm));
                refreshView.setEnableRefresh(false);
                refreshView.setEnableLoadmore(false);
                MyToast(getString(R.string.data_numm));
            }else if(msg.what==2){//有数据
                pro_main.setVisibility(View.INVISIBLE);
                list_dis.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                diseaseNames=searchDis.getSurgeryDetailInfor();
                totalNum=searchDis.getTotalNum();
                list_dis.setVisibility(View.VISIBLE);
                if(page==1){//加载第一页
                    final_list=diseaseNames;
                    adapter=new SerachSurgeryAdapter(ShowSurgerySearchActivity.this,diseaseNames,R.layout.list_item_search);
                    list_dis.setAdapter(adapter);
                    refreshView.finishRefresh(true);
                    refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                }else{
                    final_list.addAll(diseaseNames);
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
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //科室监听
        list_dis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SurgeryDetailInfor intrduce=final_list.get(position);
                Intent intent=new Intent(ShowSurgerySearchActivity.this,SurgeryDetailInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("surgeryDetail",intrduce);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status){
                    if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        pro_main.setVisibility(View.VISIBLE);
                        page=1;
                        getData(editSearch,page);
                    }
                }
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                page=1;
                getData(editSearch,page);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData(editSearch,page);
                }else{
                    refreshView.finishLoadmore(true);
                    MyToast("已经没有更多数据了");
                    refreshView.finishLoadmoreWithNoMoreData();

                }
            }
        });
    }
}
