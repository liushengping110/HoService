package wizrole.hoservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import wizrole.hoservice.adapter.HealVedioAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.Vedio;
import wizrole.hoservice.beam.VedioList;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by ${liushengping} on 2017/9/29.
 * 何人执笔？
 * 健康视频
 */

public class HealVedioActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_vedio)GridView list_vedio;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int pageNum=1;
    public int totalNum;//总页数
    public HealVedioAdapter adapter;
    public List<VedioList> final_list;

    @Override
    protected int getLayout() {
        return R.layout.activity_healvedio;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.heal_vedio));
        final_list=new ArrayList<VedioList>();
        setXRfreshView();
        getData(pageNum);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    public Vedio vedio;
    public void getData(int pageNum){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y124");
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
                        vedio=new Vedio();
                        Gson gson=new Gson();
                        vedio=gson.fromJson(o.toString(),Vedio.class);
                        if(vedio.getResultCode().equals("0")){
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

    public List<VedioList> datails;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://断网
                    if(pageNum==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_vedio.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(HealVedioActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText(getString(R.string.wifi_err_try_again));
                        list_vedio.setVisibility(View.INVISIBLE);
                    }else{
                        refreshView.finishLoadmore(false);
                    }
                    MyToast(getString(R.string.wifi_err));
                    break;
                case 1://无数据
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_vedio.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(HealVedioActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 2://有数据
                    pro_main.setVisibility(View.INVISIBLE);
                    list_vedio.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    datails=vedio.getVedioList();
                    totalNum=vedio.getTotalNum();
                    if(pageNum==1){//加载第一页
                        final_list=datails;
                        adapter = new HealVedioAdapter(HealVedioActivity.this,datails);
                        list_vedio.setAdapter(adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_list.addAll(datails);
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
        list_vedio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VedioList vedioList=final_list.get(i);
                Intent intent=new Intent(HealVedioActivity.this,VedioPlayerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("vedioUrl",vedioList);
                intent.putExtras(bundle);
                intent.putExtra(VedioPlayerActivity.TRANSITION, true);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Pair pair = new Pair<>(view, VedioPlayerActivity.IMG_TRANSITION);
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            HealVedioActivity.this, pair);
                    ActivityCompat.startActivity(HealVedioActivity.this, intent, activityOptions.toBundle());
                } else {
                    HealVedioActivity.this.startActivity(intent);
                    HealVedioActivity.this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    refreshView.finishLoadmore(true);
                    MyToast("已经没有更多数据了");
                    refreshView.finishLoadmoreWithNoMoreData();

                }
            }
        });
        //点击重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    pro_main.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pageNum=1;
                    getData(pageNum);
                }
            }
        });
    }
}
