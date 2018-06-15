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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.HosNewsAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.HNews;
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by liushengping on 2017/11/3/003.
 * 何人执笔？
 * 院级  科级  生活级  新闻列表
 */

public class NewsListActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.list_news_list)ListView list_news_list;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int toalPageNum;
    public int page=1;
    public  String newsType;
    public HNews hNews;
    public List<HosNews> hosNews;
    public List<HosNews> final_hosNews;

    public HosNewsAdapter hos_adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_newslist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.news_list));
        newsType=getIntent().getStringExtra("newsType");
        setXRfreshView();
        SingleGetData();
    }
    public void SingleGetData(){
        switch (newsType){
            case "hos"://院级新闻
                getData("Y111",page);
                break;
            case "deparent"://科级新闻
                getData("Y112",page);
                break;
            case "life"://生活级新闻
                getData("Y134",page);
                break;
            case "textScroll"://科室滚动文本
                getData("Y133",page);
                break;
        }
    }

    public void getData(String type,int page){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", type);
            object.put("PageNo", page);
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
                        hNews=new HNews();
                        hNews=gson.fromJson(o.toString(),HNews.class);
                        if(hNews.getResultCode().equals("0")){
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
                    if(page==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_news_list.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(NewsListActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
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
                    list_news_list.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(NewsListActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 2:
                    pro_main.setVisibility(View.INVISIBLE);
                    list_news_list.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    hosNews=hNews.getHosNews();
                    toalPageNum=hNews.getTotalNum();
                    if(page==1){//加载第一页
                        final_hosNews=hosNews;
                        hos_adapter=new HosNewsAdapter(NewsListActivity.this,hosNews,R.layout.recy_item_healscien);
                        list_news_list.setAdapter(hos_adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_hosNews.addAll(hosNews);
                        hos_adapter.notifyDataSetChanged();
                        refreshView.finishLoadmore(true);
                    }
                    refreshView.setEnableRefresh(true);
                    refreshView.setEnableLoadmore(true);
            }
        }
    };
    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }

    @Override
    protected void setListener() {
        list_news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HosNews hosNews=final_hosNews.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("NewsDetail",hosNews);
                Intent intent=new Intent(NewsListActivity.this,NewsDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    pro_main.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    page=1;
                    SingleGetData();
                }
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page=1;
                switch (newsType){
                    case "hos"://院级新闻
                        final_hosNews=null;
                        getData("Y111",page);
                        break;
                    case "deparent"://科级新闻
                        final_hosNews=null;
                        getData("Y112",page);
                        break;
                    case "life"://生活级新闻
                        final_hosNews=null;
                        getData("Y134",page);
                        break;
                }
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<toalPageNum){
                    page++;
                    SingleGetData();
                }else{
                    refreshView.finishLoadmore(true);
                    MyToast("已经没有更多数据了");
                    refreshView.finishLoadmoreWithNoMoreData();

                }
            }
        });
    }
}
