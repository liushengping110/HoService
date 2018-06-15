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
import wizrole.hoservice.adapter.TestSelf_adapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.TestSelfCateDetail;
import wizrole.hoservice.beam.TestSelfDetail;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;

/**
 * Created by liushengping on 2017/11/8/008.
 * 何人执笔？
 * 智能导诊---疾病列表
 */

public class TestSelfListActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.list_testself)ListView list_testself;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    public int page=1;
    public List<TestSelfCateDetail> selfCateDetails;
    public List<TestSelfCateDetail> final_list;
    public TestSelfDetail detail;
    public int totalNum;
    public TestSelf_adapter adapter;
    public String msg;
    @Override
    protected int getLayout() {
        return R.layout.activity_testselflist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.xmlb));
        msg=getIntent().getStringExtra("TestSelType");
        setXRfreshView();
        final_list=new ArrayList<TestSelfCateDetail>();
        getData(page,msg);
    }

    public void  setXRfreshView(){
        refreshView.setRefreshHeader(new OrdinaryHeaderView(this));
        refreshView.setRefreshFooter(new OrdinaryFooterView(this));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
    }


    public void getData(int page,String message){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y136");
            object.put("SymptomType",message);
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
                        Gson gson=new Gson();
                        detail=new TestSelfDetail();
                        detail=gson.fromJson(o.toString(),TestSelfDetail.class);
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
            switch (msg.what){
                case 0:
                    if(page==1){
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        list_testself.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(TestSelfListActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                        text_no_data.setVisibility(View.VISIBLE);
                        text_no_data.setText(getString(R.string.wifi_err_try_again));
                    }else{
                        refreshView.finishLoadmore(false);
                    }
                    MyToast(getString(R.string.wifi_err));
                    break;
                case 1:
                    refreshView.finishRefresh(false);
                    pro_main.setVisibility(View.INVISIBLE);
                    list_testself.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(TestSelfListActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    refreshView.setEnableRefresh(false);
                    refreshView.setEnableLoadmore(false);
                    MyToast(getString(R.string.data_numm));
                    break;
                case 2:
                    selfCateDetails=detail.getTestSelfCateDetail();
                    totalNum=detail.getTotalNum();
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list_testself.setVisibility(View.VISIBLE);
                    if(page==1){
                        final_list=selfCateDetails;
                        adapter=new TestSelf_adapter(TestSelfListActivity.this,selfCateDetails,R.layout.recy_item_healscien);
                        list_testself.setAdapter(adapter);
                        refreshView.finishRefresh(true);
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    }else{
                        final_list.addAll(selfCateDetails);
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
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    pro_main.setVisibility(View.VISIBLE);
                    page=1;
                    getData(page,msg);

                }
            }
        });

        list_testself.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TestSelfCateDetail name=final_list.get(i);
                Intent intent=new Intent(TestSelfListActivity.this,TestSelfDetailInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("TestSelfDetail",name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                page=1;
                getData(page,msg);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(page<totalNum){
                    page++;
                    getData(page,msg);
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
    }
}
