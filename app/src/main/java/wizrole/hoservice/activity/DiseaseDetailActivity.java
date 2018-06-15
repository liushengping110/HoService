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
import wizrole.hoservice.adapter.DiseaseNameAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.DiseasInfor;
import wizrole.hoservice.beam.DiseaseName;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 * 疾病--病种
 */

public class DiseaseDetailActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.disease_detail_infor)ListView list_name;
    @BindView(R.id.rel_search)RelativeLayout rel_search;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.xrefresh_view)RefreshLayout refreshView;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    public int pageNum=1;
    public List<DiseaseName> final_list;
    public String deparent_name;
    @Override
    protected int getLayout() {
        return R.layout.activity_diseasedetail;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.dis_list));
        deparent_name=getIntent().getStringExtra("name");
        final_list=new ArrayList<DiseaseName>();
        setXRfreshView();
        getData(pageNum,deparent_name);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    public DiseasInfor diseasInfor;
    public List<DiseaseName> diseaseName;
    public int totalNum;
    public DiseaseNameAdapter adapter;
    public void getData(int page,String name){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y126");
            object.put("PageNo",page);
            object.put("DeparentName",name);
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
                        Gson gson=new Gson();
                        diseasInfor=new DiseasInfor();
                        diseasInfor=gson.fromJson(o.toString(),DiseasInfor.class);
                        if(diseasInfor.getResultCode().equals("0")){
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
            if(msg.what==0){//失败
                if(pageNum==1){
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_name.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                }else{
                    refreshView.finishLoadmore(false);
                }
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//无数据
                refreshView.finishRefresh(false);
                pro_main.setVisibility(View.INVISIBLE);
                list_name.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(DiseaseDetailActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setVisibility(View.VISIBLE);
                text_no_data.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                refreshView.setEnableRefresh(false);
                refreshView.setEnableLoadmore(false);
                MyToast(getString(R.string.data_numm));
                refreshView.setEnableRefresh(false);
                refreshView.setEnableLoadmore(false);
            }else if(msg.what==2){
                pro_main.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                diseaseName=diseasInfor.getDiseaseName();
                totalNum=diseasInfor.getTotalNum();
                list_name.setVisibility(View.VISIBLE);
                if(pageNum==1){//加载第一页
                    final_list=diseaseName;
                    adapter=new DiseaseNameAdapter(DiseaseDetailActivity.this,diseaseName,R.layout.list_depar_item);
                    list_name.setAdapter(adapter);
                    refreshView.finishRefresh(true);
                    refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                }else{
                    final_list.addAll(diseaseName);
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
                    pageNum=1;
                    getData(pageNum,deparent_name);
                }
            }
        });

        list_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiseaseName name=final_list.get(i);
                Intent intent=new Intent(DiseaseDetailActivity.this,DiseaseDetailInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("infor",name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                pageNum=1;
                getData(pageNum,deparent_name);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageNum<totalNum){
                    pageNum++;
                    getData(pageNum,deparent_name);
                }else{
                    refreshView.finishLoadmore(true);
                    MyToast("已经没有更多数据了");
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

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DiseaseDetailActivity.this,DiseaseSearchActivity.class);
                startActivity(intent);
            }
        });
    }

}
