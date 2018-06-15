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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.DepaementAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.Deparentment;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by a on 2017/8/25.
 * 科室列表
 */

public class DeparentActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.rel_search)RelativeLayout rel_search;
    @BindView(R.id.list_depaerment)ListView list_depaerment;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    public Dialog dialog;
    @Override
    protected int getLayout() {
        return R.layout.activity_deaparlist;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.deparent_list));
        getData();
    }

    public void getData(){
        dialog=LoadingDailog.createLoadingDialog(DeparentActivity.this,"加载中");
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y104");
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
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
                        infor=new Deparentment();
                        infor=gson.fromJson(o.toString(),Deparentment.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(0);
        }
    }
    public Deparentment infor;
    public  List<DeparentIntrduce> list;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                LoadingDailog.closeDialog(dialog);
                list_depaerment.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(DeparentActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
            }else{//成功
                LoadingDailog.closeDialog(dialog);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                list_depaerment.setVisibility(View.VISIBLE);
                list=infor.getDeparentIntrduce();
                DepaementAdapter adapter=new DepaementAdapter(DeparentActivity.this,list,R.layout.list_depar_item);
                list_depaerment.setAdapter(adapter);
            }
        }
    };


    @Override
    protected void setListener() {
        list_depaerment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeparentIntrduce intrdece=list.get(position);
                Intent intent=new Intent(DeparentActivity.this,DeparentIntrduActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("deparentment",intrdece);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_wifi_err.setVisibility(View.INVISIBLE);
                getData();
            }
        });
    }

    public Intent intent;
    @OnClick({R.id.lin_back,R.id.rel_search})
    public void onClick(View  view){
        switch (view.getId()){
            case R.id.lin_back://返回
                finish();
                break;
            case R.id.rel_search://搜索
                intent=new Intent(this,HosSearchActivity.class);
                startActivity(intent);
        }
    }
}
