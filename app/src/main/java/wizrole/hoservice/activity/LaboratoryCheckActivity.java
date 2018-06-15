package wizrole.hoservice.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import wizrole.hoservice.adapter.LabroCheckAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.LaboratCheck;
import wizrole.hoservice.beam.LaboratCheckCate;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by ${liushengping} on 2017/10/13.
 * 何人执笔？
 * 化验检查
 */

public class LaboratoryCheckActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_labcheck)ListView list_labcheck;
    @BindView(R.id.rel_search)RelativeLayout rel_search;

    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_no_data;
    @BindView(R.id.pro_main)ProgressBar pro_main;

    public LaboratCheck laboratCheck;
    public List<LaboratCheckCate> checkCates;
    public boolean status=false;

    @Override
    protected int getLayout() {
        return R.layout.activity_laboracheck;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hyjc));
        getData();
    }

    public void getData(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y129");
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
                        laboratCheck=new LaboratCheck();
                        laboratCheck=gson.fromJson(o.toString(),LaboratCheck.class);
                        if(laboratCheck.getResultCode().equals("0")){
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
                    pro_main.setVisibility(View.INVISIBLE);
                    list_labcheck.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(LaboratoryCheckActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.wifi_err_try_again));
                    text_no_data.setTextColor(getResources().getColor(R.color.white));
                    text_no_data.setBackgroundResource(R.drawable.login_sel);
                    break;
                case 1://无数据
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    list_labcheck.setVisibility(View.INVISIBLE);
                    ImageLoading.common(LaboratoryCheckActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setVisibility(View.VISIBLE);
                    text_no_data.setText(getString(R.string.data_numm));
                    text_no_data.setBackgroundResource(R.color.white);
                    text_no_data.setTextColor(getResources().getColor(R.color.shenhui));
                    break;
                case 2:
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    checkCates=laboratCheck.getLaboratCheckCate();
                    list_labcheck.setVisibility(View.VISIBLE);
                    LabroCheckAdapter adapter=new LabroCheckAdapter(LaboratoryCheckActivity.this,checkCates,R.layout.list_depar_item);
                    list_labcheck.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status){
                    if(text_no_data.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        pro_main.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });

        list_labcheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LaboratCheckCate cate=checkCates.get(i);
                Intent intent=new Intent(LaboratoryCheckActivity.this,LaboratoryCheckDetailActivity.class);
                intent.putExtra("CateName",cate.getLaboratCheckCateName());
                startActivity(intent);
            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LaboratoryCheckActivity.this,LabCheckSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
