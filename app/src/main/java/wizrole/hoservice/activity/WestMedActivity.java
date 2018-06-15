package wizrole.hoservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import wizrole.hoservice.adapter.WestMedAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.WestMed;
import wizrole.hoservice.beam.WestMedInfor;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.view.WhiteBgFooterView;
import wizrole.hoservice.view.WhiteBgHeaderView;

/**
 * Created by ${liushengping} on 2017/10/12.
 * 何人执笔？
 * 西药
 */

public class WestMedActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.list_westmed)ListView list_westmed;
    @BindView(R.id.text_searche)TextView text_searche;
    @BindView(R.id.edit_search)EditText edit_search;
    @BindView(R.id.text_title)TextView text_title;

    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_no_data;
    @BindView(R.id.pro_main)ProgressBar pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int page=1;
    public int totalNum;
    public WestMed westMed;
    public List<WestMedInfor> westMedInfors;
    public List<WestMedInfor> final_list;
    public WestMedAdapter adapter;
    public boolean status=false;
    @Override
    protected int getLayout() {
        return R.layout.activity_westmed;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.west_med));
        final_list=new ArrayList<WestMedInfor>();
        edit_search.setHint(getString(R.string.search_west_med));
        setXRfreshView();
        getData(getString(R.string.a),page);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new WhiteBgHeaderView(WestMedActivity.this));
        refreshView.setRefreshFooter(new WhiteBgFooterView(WestMedActivity.this));
        refreshView.setHeaderHeightPx(300);
        refreshView.setFooterHeightPx(100);
    }


    public void getData(String name,int page){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y128");
            object.put("PageNo", page);
            object.put("WestMedName", name);
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
                        westMed=new WestMed();
                        Gson gson=new Gson();
                        westMed=gson.fromJson(o.toString(),WestMed.class);
                        if(westMed.getResultCode().equals("0")){
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
                case 0://断网
                    status=true;
                    if(page==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_westmed.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(WestMedActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText(getString(R.string.wifi_err_try_again));
                        text_no_data.setTextColor(getResources().getColor(R.color.white));
                        text_no_data.setBackgroundResource(R.drawable.login_sel);
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
                    ImageLoading.common(WestMedActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    text_no_data.setBackgroundResource(R.color.white);
                    text_no_data.setTextColor(getResources().getColor(R.color.shenhui));
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 2://有数据
                    pro_main.setVisibility(View.INVISIBLE);
                    list_westmed.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    westMedInfors=westMed.getWestMedInfor();
                    totalNum=westMed.getTotalNum();
                    if(page==1){
                        final_list=westMedInfors;
                        adapter=new WestMedAdapter(WestMedActivity.this,westMedInfors,R.layout.recy_item_healscien);
                        list_westmed.setAdapter(adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_list.addAll(westMedInfors);
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
        text_searche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_search.getText().toString().length()>0){
                    pro_main.setVisibility(View.VISIBLE);
                    list_westmed.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                    page=1;
                    getData(edit_search.getText().toString(),page);
                }else{
                    ToastUtil.MyToast(WestMedActivity.this,getString(R.string.west_med_name));
                }
            }
        });
        list_westmed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WestMedInfor infor=final_list.get(i);
                Intent intent=new Intent(WestMedActivity.this,WestMedInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("westMed",infor);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status){
                    if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                        page=1;
                        pro_main.setVisibility(View.VISIBLE);
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        if(edit_search.getText().toString().length()>0){
                            getData(edit_search.getText().toString(),page);
                        }else{
                            getData(getString(R.string.a),page);
                        }
                    }
                }
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                page=1;
                if(edit_search.getText().toString().length()>0){
                    getData(edit_search.getText().toString(),page);
                }else{
                    getData(getString(R.string.a),page);
                }
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    if(edit_search.getText().toString().length()>0){
                        getData(edit_search.getText().toString(),page);
                    }else{
                        getData(getString(R.string.a),page);
                    }
                }else{
                    refreshView.finishLoadmore(true);
                    refreshView.finishLoadmoreWithNoMoreData();
                    ToastUtil.MyToast(WestMedActivity.this,getString(R.string.bottom_no_data));
                }
            }
        });

    }
}
