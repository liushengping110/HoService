package wizrole.hoservice.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.DepaementDiseaseAdapter;
import wizrole.hoservice.adapter.DepaementDiseaseDetailAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.Disease;
import wizrole.hoservice.beam.DiseaseDeparDetailName;
import wizrole.hoservice.beam.DiseaseDeparent;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by ${liushengping} on 2017/10/11.
 * 何人执笔？
 * 疾病库
 * -----科室列表  内科--消化内科 ---复用
 */

public class DiseaseActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.rel_search)RelativeLayout rel_search;//搜索
    @BindView(R.id.list_depaerment)ListView list_depaerment;//大科室
    @BindView(R.id.list_depaerment_detail)ListView list_depaerment_detail;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;//详细科室
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    public boolean detail_sel=false;//后退标记
    public Disease disease;
    public List<DiseaseDeparent> deparents;
    public  List<DiseaseDeparDetailName> detailNames;
    @Override
    protected int getLayout() {
        return R.layout.activity_deparen_disease;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.deparent_list));
        getData();
    }

    public Dialog dialog;
    public void getData(){
            dialog= LoadingDailog.createLoadingDialog(DiseaseActivity.this,"加载中");
            JSONObject object=new JSONObject();
            try {
                object.put("TradeCode","Y125");
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
                        if (o.equals(RxJavaOkPotting.NET_ERR)){
                            handler.sendEmptyMessage(0);
                        }else{
                            disease=new Disease();
                            Gson  gson=new Gson();
                            disease=gson.fromJson(o.toString(),Disease.class);
                            if(disease.getResultCode().equals("0")){
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
                    LoadingDailog.closeDialog(dialog);
                    list_depaerment.setVisibility(View.INVISIBLE);
                    list_depaerment_detail.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_err_agagin_center.setText(getString(R.string.wifi_err_try_again));
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.white));
                    text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                    ImageLoading.common(DiseaseActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    break;
                case 1://无数据
                    LoadingDailog.closeDialog(dialog);
                    list_depaerment.setVisibility(View.INVISIBLE);
                    list_depaerment_detail.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_err_agagin_center.setText(getString(R.string.no_data));
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.text_fz));
                    text_err_agagin_center.setBackgroundResource(R.color.white);
                    ImageLoading.common(DiseaseActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    break;
                case 2://成功
                    LoadingDailog.closeDialog(dialog);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    deparents=disease.getDiseaseDeparent();
                    list_depaerment_detail.setVisibility(View.INVISIBLE);
                    list_depaerment.setVisibility(View.VISIBLE);
                    DepaementDiseaseAdapter adapter=new DepaementDiseaseAdapter(DiseaseActivity.this,deparents,R.layout.list_depar_item);
                    list_depaerment.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        list_depaerment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                detail_sel=true;
                DiseaseDeparent deparent=deparents.get(i);
                detailNames=deparent.getDiseaseDeparDetailName();
                list_depaerment.setVisibility(View.INVISIBLE);
                list_depaerment_detail.setVisibility(View.VISIBLE);
                DepaementDiseaseDetailAdapter adapter=new DepaementDiseaseDetailAdapter(DiseaseActivity.this,detailNames,R.layout.list_depar_item);
                list_depaerment_detail.setAdapter(adapter);
            }
        });

        list_depaerment_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiseaseDeparDetailName name=detailNames.get(i);
                Intent intent=new Intent(DiseaseActivity.this,DiseaseDetailActivity.class);
                intent.putExtra("name",name.getDiseaseDeparDetailName());
                startActivity(intent);
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detail_sel){
                    list_depaerment_detail.setVisibility(View.INVISIBLE);
                    list_depaerment.setVisibility(View.VISIBLE);
                    detail_sel=false;
                }else{
                    finish();
                }
            }
        });
        //重试
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_err_agagin_center.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    getData();
                }
            }
        });
        //搜索
        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DiseaseActivity.this,DiseaseSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(detail_sel){
                list_depaerment_detail.setVisibility(View.INVISIBLE);
                list_depaerment.setVisibility(View.VISIBLE);
                detail_sel=false;
            }else{
                finish();
            }
        }
        return false;
    }
}
