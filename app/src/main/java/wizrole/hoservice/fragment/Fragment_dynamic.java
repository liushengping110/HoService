package wizrole.hoservice.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.activity.DynamicDetailActivity;
import wizrole.hoservice.activity.HealEduActivity;
import wizrole.hoservice.adapter.HealEduAdapter;
import wizrole.hoservice.adapter.HosAnnounceAdapter;
import wizrole.hoservice.adapter.HosDynamicAdapter;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.Announce;
import wizrole.hoservice.beam.AnnounceMsg;
import wizrole.hoservice.beam.Dynamic;
import wizrole.hoservice.beam.DynamicMsg;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;
import wizrole.hoservice.view.WhiteBgFooterView;
import wizrole.hoservice.view.WhiteBgHeaderView;

/**
 * Created by a on 2017/9/4.
 * 医院动态
 * 最新公告
 *
 * 两者复用一个fragment
 */

public class Fragment_dynamic extends LazyFragment {

    private int tabIndex;
    public static final String INTENT_INT_INDEX="index";
    public ListView list_dynamic_view;
    public List<DynamicMsg> dynamicMsgs;//医院动态
    public List<DynamicMsg> finalDyn_list;//医院动态
    public List<AnnounceMsg> announceMsgs;//最新公告
    public List<AnnounceMsg> finalAnn_list;//最新公告
    public LinearLayout lin_wifi_err;
    public ImageView img_net_err;
    public TextView text_err_agagin_center;
    public ProgressBar pro_main;
    public int totalNum;//最大页数
    public JSONObject  object;
    public HosDynamicAdapter dyna_adapter;//医院动态适配器
    public HosAnnounceAdapter annou_adapter;//最新公告适配器
    public RefreshLayout xrefresh_view;
    public int pageNo=1;
    public static long lastRefreshTime;//刷新时间
    public boolean first=false;//第一次进入

    public static Fragment_dynamic newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        Fragment_dynamic fragment = new Fragment_dynamic();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_dynamic);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        initUI();
        setXRfreshView();
        getData(tabIndex,pageNo);
        setListener();
    }
    public void initUI(){
        list_dynamic_view=(ListView)findViewById(R.id.list_dynamic_view);
        xrefresh_view=(RefreshLayout) findViewById(R.id.xrefresh_view);
        lin_wifi_err=(LinearLayout) findViewById(R.id.lin_wifi_err);
        img_net_err=(ImageView) findViewById(R.id.img_net_err);
        text_err_agagin_center=(TextView) findViewById(R.id.text_err_agagin_center);
        pro_main=(ProgressBar) findViewById(R.id.pro_main);
        finalAnn_list=new ArrayList<AnnounceMsg>();
        finalDyn_list=new ArrayList<DynamicMsg>();
    }

    public void  setXRfreshView(){
        xrefresh_view.setRefreshHeader(new WhiteBgHeaderView(getActivity()));
        xrefresh_view.setRefreshFooter(new WhiteBgFooterView(getActivity()));
        xrefresh_view.setHeaderHeightPx(300);
        xrefresh_view.setFooterHeightPx(100);
    }


    public void getData(int type,int pageNum){
        switch (type){
            case 1://最新公告
                object=new JSONObject();
                try {
                    object.put("TradeCode","Y109");
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
                                Announce announce=new Announce();
                                Gson gson=new Gson();
                                announce=gson.fromJson(o.toString(),Announce.class);
                                if(announce.getResultCode().equals("0")){
                                    totalNum=announce.getTotalNun();
                                    if(announce.getAnnounceMsg().size()>0){
                                        announceMsgs=announce.getAnnounceMsg();
                                        handler.sendEmptyMessage(2);
                                    }else{
                                        handler.sendEmptyMessage(0);
                                    }
                                }else{
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }
                    });
                }catch (JSONException e){
                    handler.sendEmptyMessage(0);
            }
                break;
            case 2://医院动态
                object=new JSONObject();
                try {
                    object.put("TradeCode","Y110");
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
                                Dynamic dynamic=new Dynamic();
                                Gson gson=new Gson();
                                dynamic=gson.fromJson(o.toString(),Dynamic.class);
                                if(dynamic.getResultCode().equals("0")){
                                        dynamicMsgs=dynamic.getDynamicMsg();
                                        handler.sendEmptyMessage(2);
                                }else{
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }
                    });
                }catch (JSONException e){
                    handler.sendEmptyMessage(0);
                }
                break;
        }
    }

    public  void setListener(){
        list_dynamic_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(tabIndex==1){//最新公告
                    AnnounceMsg announceMsg=finalAnn_list.get(i);
                    Intent intent=new Intent(getActivity(),DynamicDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("announMsg",announceMsg);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{//医院动态
                    DynamicMsg dynamicMsg=finalDyn_list.get(i);
                    Intent intent=new Intent(getActivity(),DynamicDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("dynamicMsg",dynamicMsg);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        xrefresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                if(tabIndex==1){
                    finalAnn_list=null;
                }else{
                    finalDyn_list=null;
                }
                pageNo=1;
                getData(tabIndex,pageNo);
            }
        });
        xrefresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageNo<totalNum){
                    pageNo++;
                    getData(tabIndex,pageNo);
                }else{
                    xrefresh_view.finishLoadmore(true);
                    xrefresh_view.finishLoadmoreWithNoMoreData();
                    ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.bottom_no_data));
                }
            }
        });
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_main.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                pageNo=1;
                getData(tabIndex,pageNo);
            }
        });
    }

     public Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             if(msg.what==0){//网络连接失败
                 pro_main.setVisibility(View.INVISIBLE);
                 if(pageNo==1){
                     xrefresh_view.finishRefresh(false);
                     list_dynamic_view.setVisibility(View.INVISIBLE);
                     lin_wifi_err.setVisibility(View.VISIBLE);
                     ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                     setNetErr();//禁止加载刷新
                 }else{
                     xrefresh_view.finishLoadmore(false);
                 }
                 ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.wifi_err));
             }else if(msg.what==2){//请求成功 展示数据
                 list_dynamic_view.setVisibility(View.VISIBLE);
                 lin_wifi_err.setVisibility(View.INVISIBLE);
                 pro_main.setVisibility(View.INVISIBLE);
                 if(tabIndex==1){//最新公告
                     if(pageNo==1){//加载第一页
                         xrefresh_view.resetNoMoreData();//恢复没有更多数据状态状态
                         finalAnn_list=announceMsgs;
                         annou_adapter=new HosAnnounceAdapter(getActivity(),announceMsgs,R.layout.list_dynamic_item);
                         list_dynamic_view.setAdapter(annou_adapter);
                         xrefresh_view.finishRefresh(true);//完成
                         xrefresh_view.resetNoMoreData();//恢复没有更多数据状态状态
                     }else{
                         finalAnn_list.addAll(announceMsgs);
                         annou_adapter.notifyDataSetChanged();
                         xrefresh_view.finishLoadmore(true);//完成加载
                     }
                 }else{//医院动态
                     if(pageNo==1){//加载第一页
                         xrefresh_view.resetNoMoreData();//恢复没有更多数据状态状态
                         finalDyn_list=dynamicMsgs;
                         dyna_adapter=new HosDynamicAdapter(getActivity(),dynamicMsgs,R.layout.list_dynamic_item);
                         list_dynamic_view.setAdapter(dyna_adapter);
                         xrefresh_view.finishRefresh(true);//完成
                         xrefresh_view.resetNoMoreData();//恢复没有更多数据状态状态
                     }else{
                         finalDyn_list.addAll(dynamicMsgs);
                         dyna_adapter.notifyDataSetChanged();
                         xrefresh_view.finishLoadmore(true);//完成加载
                     }
                 }
                setNetNormal();//可以加载刷新
             }
         }
     };

    /*****************网络请求失败时的禁止加载和刷新***********************/
    //禁止加载和刷新
    public void setNetErr(){
        xrefresh_view.setEnableLoadmore(false);
        xrefresh_view.setEnableRefresh(false);
    }
    //支持加载和刷新
    public void setNetNormal(){
        xrefresh_view.setEnableLoadmore(true);
        xrefresh_view.setEnableRefresh(true);
    }
}
