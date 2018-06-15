package wizrole.hoservice.fragment;

import android.app.Activity;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.MedDetailAdapter;
import wizrole.hoservice.adapter.Med_NoDetailAdapter;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.Med;
import wizrole.hoservice.beam.MedItem;
import wizrole.hoservice.beam.Med_No;
import wizrole.hoservice.beam.TarItem;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.ToastUtil;

/**
 * Created by a on 2017/9/4.
 * 药品查询
 *
 * 两者复用一个fragment
 */

public class Fragment_med extends LazyFragment {

    private int tabIndex;
    public static final String INTENT_INT_INDEX="index";
    public ListView list_med_view;
    public TextView text_searche;
    public JSONObject  object;
    public  List<MedItem> medItems;
    public  List<TarItem> tarItems;
    public MedDetailAdapter med_adapter;
    public Med_NoDetailAdapter med_no_adapter;
    public EditText edit_search;
    public LinearLayout lin_med_no,lin_med;
    public View view_top,view_bottom;
    public LinearLayout lin_wifi_err;
    public ImageView img_net_err;
    public TextView text_err_agagin_center;
    public ProgressBar pro_main;
    public static Fragment_med newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        Fragment_med fragment = new Fragment_med();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_med);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        initUI();
        getData(tabIndex,"a");
        setListener();
    }
    public void initUI(){
        list_med_view=(ListView)findViewById(R.id.list_med);
        text_searche=(TextView) findViewById(R.id.text_searche);
        edit_search=(EditText) findViewById(R.id.edit_search);
        lin_med_no=(LinearLayout) findViewById(R.id.lin_med_no);
        lin_med=(LinearLayout) findViewById(R.id.lin_med);
        view_top=(View) findViewById(R.id.view_top);
        view_bottom=(View) findViewById(R.id.view_bottom);
        lin_wifi_err=(LinearLayout) findViewById(R.id.lin_wifi_err);
        img_net_err=(ImageView) findViewById(R.id.img_net_err);
        text_err_agagin_center=(TextView) findViewById(R.id.text_err_agagin_center);
        pro_main=(ProgressBar) findViewById(R.id.pro_main);
    }

    public void getData(int type,String med){
        switch (type){
            case 1://药品查询
                object=new JSONObject();
                try {
                    object.put("TradeCode","Y120");
                    object.put("Alias",med);
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
                                med_o=new Med();
                                Gson gson=new Gson();
                                med_o=gson.fromJson(o.toString(),Med.class);
                                if(med_o.getResultCode().equals("0")){//有数据
                                    medItems=med_o.getMedItemS().getMedItem();
                                    handler.sendEmptyMessage(2);
                                }else{//无数据
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }
                    });
                }catch (JSONException e){
                    handler.sendEmptyMessage(0);
            }
                break;
            case 2://非药品查询
                object=new JSONObject();
                try {
                    object.put("TradeCode","Y121");
                    object.put("Alias",med);
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
                                med_no=new Med_No();
                                Gson gson=new Gson();
                                med_no=gson.fromJson(o.toString(),Med_No.class);
                                if(med_no.getResultCode().equals("0")){//有数据
                                    tarItems=med_no.getTarItemS().getTarItem();
                                    handler.sendEmptyMessage(3);
                                }else{//无数据
                                    handler.sendEmptyMessage(1);
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
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_err_agagin_center.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    if(edit_search.getText().toString().length()==0){
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        pro_main.setVisibility(View.VISIBLE);
                        getData(tabIndex,"a");
                    }else{
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        pro_main.setVisibility(View.VISIBLE);
                        getData(tabIndex,edit_search.getText().toString());
                    }
                }
            }
        });
        text_searche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_search.getText().length()>0){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                    list_med_view.setVisibility(View.INVISIBLE);
                    getData(tabIndex,edit_search.getText().toString());
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.med_search));
                }
            }
        });

        list_med_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(tabIndex==1){//药品查询
                    ToastUtil.MyToast(getActivity(),medItems.get(i).getMedCate());
                }else{//非药品查询
                    ToastUtil.MyToast(getActivity(),tarItems.get(i).getRegExpDate());
                }
            }
        });
    }
    public Med_No med_no;
    public Med med_o;
    public boolean status;
     public Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             if(msg.what==0){//网络连接失败
                 pro_main.setVisibility(View.INVISIBLE);
                 status=false;
                 if(tabIndex==1){
                     lin_med.setVisibility(View.INVISIBLE);
                 }else{
                     lin_med_no.setVisibility(View.INVISIBLE);
                 }
                 list_med_view.setVisibility(View.INVISIBLE);
                 lin_wifi_err.setVisibility(View.VISIBLE);
                 text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                 view_bottom.setVisibility(View.INVISIBLE);
                 view_top.setVisibility(View.INVISIBLE);
                 text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.wifi_err_try_again));
                 ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                 text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                 ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.wifi_err));
//
             }else if(msg.what==1){//数据为空
                 pro_main.setVisibility(View.INVISIBLE);
                 status=true;
                 view_bottom.setVisibility(View.INVISIBLE);
                 view_top.setVisibility(View.INVISIBLE);
                 if(tabIndex==1){
                     lin_med.setVisibility(View.INVISIBLE);
                 }else{
                     lin_med_no.setVisibility(View.INVISIBLE);
                 }
                 list_med_view.setVisibility(View.INVISIBLE);
                 lin_wifi_err.setVisibility(View.VISIBLE);
                 text_err_agagin_center.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                 text_err_agagin_center.setBackgroundResource(R.color.white);
                 text_err_agagin_center.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.text_fz));
                 ImageLoading.common(MyApplication.getContextObject(),R.drawable.null_data,img_net_err,R.drawable.null_data);
                 ToastUtil.MyToast(MyApplication.getContextObject(),MyApplication.getContextObject().getString(R.string.search_no_result));
             }else if(msg.what==2){// 药品查询   请求成功 展示数据
                 pro_main.setVisibility(View.INVISIBLE);
                 lin_wifi_err.setVisibility(View.INVISIBLE);
                 status=true;
                 if(tabIndex==1){
                     lin_med.setVisibility(View.VISIBLE);
                     view_bottom.setVisibility(View.VISIBLE);
                     view_top.setVisibility(View.VISIBLE);
                 }
                 list_med_view.setVisibility(View.VISIBLE);
                 med_adapter=new MedDetailAdapter(getApplicationContext(),medItems,R.layout.list_med_item);
                 list_med_view.setAdapter(med_adapter);
             }else if(msg.what==3){//非药品查询 ---请求成功
                 pro_main.setVisibility(View.INVISIBLE);
                 lin_wifi_err.setVisibility(View.INVISIBLE);
                 status=true;
                 if(tabIndex==2){
                     lin_med_no.setVisibility(View.VISIBLE);
                     view_bottom.setVisibility(View.VISIBLE);
                     view_top.setVisibility(View.VISIBLE);
                 }
                 list_med_view.setVisibility(View.VISIBLE);
                 med_no_adapter=new Med_NoDetailAdapter(getApplicationContext(),tarItems,R.layout.list_med_no_item);
                 list_med_view.setAdapter(med_no_adapter);
             }
         }
     };
}
