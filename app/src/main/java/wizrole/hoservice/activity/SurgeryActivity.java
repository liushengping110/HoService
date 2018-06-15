package wizrole.hoservice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
import wizrole.hoservice.adapter.SurgerytAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.Surgery;
import wizrole.hoservice.beam.SurgeryDeparent;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by ${liushengping} on 2017/10/17.
 * 何人执笔？
 * 手术
 * 科室列表
 */

public class SurgeryActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_depar_nodata)TextView text_surgery_nodata;
    @BindView(R.id.rel_search)RelativeLayout rel_search;
    @BindView(R.id.list_depaerment)ListView list_surgery;

    @Override
    protected int getLayout() {
        return R.layout.activity_surgery;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.deparent_list));
        getData();
    }

    public Surgery surgery;
    public List<SurgeryDeparent> list;
    public void getData(){
        showProgress();
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y131");
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
                        surgery=new Surgery();
                        Gson gson=new Gson();
                        surgery=gson.fromJson(o.toString(),Surgery.class);
                        if(surgery.getResultCode().equals("0")){
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
                    hideProgress();
                    list_surgery.setVisibility(View.INVISIBLE);
                    text_surgery_nodata.setVisibility(View.VISIBLE);
                    text_surgery_nodata.setTextSize(25);
                    text_surgery_nodata.setText(getString(R.string.wifi_err_try_again));
                    text_surgery_nodata.setTextColor(getResources().getColor(R.color.huise));
                    break;
                case 1:
                    hideProgress();
                    list_surgery.setVisibility(View.INVISIBLE);
                    text_surgery_nodata.setVisibility(View.VISIBLE);
                    text_surgery_nodata.setTextSize(25);
                    text_surgery_nodata.setText(getString(R.string.no_deparent));
                    text_surgery_nodata.setTextColor(getResources().getColor(R.color.huise));
                    break;
                case 2:
                    hideProgress();
                    text_surgery_nodata.setVisibility(View.INVISIBLE);
                    list_surgery.setVisibility(View.VISIBLE);
                    list=surgery.getSurgeryDeparent();
                    SurgerytAdapter adapter=new SurgerytAdapter(SurgeryActivity.this,list,R.layout.list_depar_item);
                    list_surgery.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        //重试
        text_surgery_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        list_surgery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SurgeryDeparent deparent=list.get(i);
                Intent intent=new Intent(SurgeryActivity.this,SurgeryNameActivity.class);
                intent.putExtra("surgeryName",deparent.getSurgeryDeparentName());
                startActivity(intent);
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
                Intent intent=new Intent(SurgeryActivity.this,SurgerySearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private ProgressDialog progressDialog;
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.now_getData));
    }
    public void hideProgress() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
