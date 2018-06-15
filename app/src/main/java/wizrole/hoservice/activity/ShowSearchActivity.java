package wizrole.hoservice.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.SerachDeparentAdapter;
import wizrole.hoservice.adapter.SerachDoctAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.DoctorInfos;
import wizrole.hoservice.beam.SearchBack;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by a on 2017/9/11.
 * 科室 和医生搜索展示页面
 */

public class ShowSearchActivity extends BaseActivity {
    @BindView(R.id.list_deparent)ListView list_deparent;
    @BindView(R.id.list_doc)ListView list_doc;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.scrollView)ScrollView scrollView;
    @BindView(R.id.lin_all_content)LinearLayout lin_all_content;
    //科室无结果
    @BindView(R.id.lin_wifi_err_depar)LinearLayout lin_wifi_err_depar;
    @BindView(R.id.img_net_err_depar)ImageView img_net_err_depar;
    @BindView(R.id.text_no_data_depar)TextView text_no_data_depar;
    //医生无结果
    @BindView(R.id.lin_wifi_err_doc)LinearLayout lin_wifi_err_doc;
    @BindView(R.id.img_net_err_doc)ImageView img_net_err_doc;
    @BindView(R.id.text_no_data_doc)TextView text_no_data_doc;
    //网络请求失败
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    public String editSearch;
    public List<DoctorInfos> infosList;
    public  List<DeparentIntrduce> intrduces;
    public Dialog dialog;
    @Override
    protected int getLayout() {
        return R.layout.activity_showsearch;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        editSearch=getIntent().getStringExtra("search");
        text_title.setText(getString(R.string.search_result));
        getData(editSearch);
    }

    public SearchBack searchBack;
    public void getData(String msg){
        dialog= LoadingDailog.createLoadingDialog(ShowSearchActivity.this,"加载中");
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y106");
            object.put("keyword",msg);
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
                        searchBack=new SearchBack();
                        Gson gson=new Gson();
                        searchBack=gson.fromJson(o.toString(),SearchBack.class);
                        //搜索结果--科室结果
                        if(searchBack.getDeparentIntrduce().size()==0){
                            handler.sendEmptyMessage(1);
                        }else{//科室搜索有结果
                            handler.sendEmptyMessage(3);
                        }
                        //搜索结果--医生列表
                        if(searchBack.getDoctorInfos().size()==0){
                            handler.sendEmptyMessage(2);
                        }else{
                            handler.sendEmptyMessage(4);
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
            if(msg.what==0){//网络连接失败
                LoadingDailog.closeDialog(dialog);
                lin_all_content.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(ShowSearchActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//科室搜索无结果
                lin_wifi_err.setVisibility(View.INVISIBLE);
                LoadingDailog.closeDialog(dialog);
                lin_all_content.setVisibility(View.VISIBLE);
                list_deparent.setVisibility(View.INVISIBLE);
                lin_wifi_err_depar.setVisibility(View.VISIBLE);
                ImageLoading.common(ShowSearchActivity.this,R.drawable.null_data,img_net_err_depar,R.drawable.null_data);
            }else if(msg.what==2){//医生搜索无结果
                LoadingDailog.closeDialog(dialog);
                lin_all_content.setVisibility(View.VISIBLE);
                list_doc.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                lin_wifi_err_doc.setVisibility(View.VISIBLE);
                ImageLoading.common(ShowSearchActivity.this,R.drawable.null_data,img_net_err_doc,R.drawable.null_data);
            }else if(msg.what==3){//科室搜索有结果
                lin_wifi_err.setVisibility(View.INVISIBLE);
                LoadingDailog.closeDialog(dialog);
                lin_all_content.setVisibility(View.VISIBLE);
                intrduces=searchBack.getDeparentIntrduce();
                list_deparent.setVisibility(View.VISIBLE);
                SerachDeparentAdapter depar_adapter=new SerachDeparentAdapter(ShowSearchActivity.this,intrduces,R.layout.list_item_search);
                list_deparent.setAdapter(depar_adapter);
            }else if(msg.what==4){//医生搜素有结果
                lin_wifi_err.setVisibility(View.INVISIBLE);
                LoadingDailog.closeDialog(dialog);
                lin_all_content.setVisibility(View.VISIBLE);
                infosList=searchBack.getDoctorInfos();
                list_doc.setVisibility(View.VISIBLE);
                SerachDoctAdapter doc_adapter=new SerachDoctAdapter(ShowSearchActivity.this,infosList,R.layout.list_item_search);
                list_doc.setAdapter(doc_adapter);
            }
        }
    };
    @Override
    protected void setListener() {
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_wifi_err.setVisibility(View.INVISIBLE);
                getData(editSearch);
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //科室监听
        list_deparent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeparentIntrduce intrduce=intrduces.get(position);
                Intent intent=new Intent(ShowSearchActivity.this,DeparentIntrduActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("deparentment",intrduce);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        list_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorInfos doctorInfos=infosList.get(position);
                Intent intent=new Intent(ShowSearchActivity.this,DocSingleActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("intrduce",doctorInfos);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
