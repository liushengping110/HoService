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
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.DepaementScienDirAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.DeparentScien;
import wizrole.hoservice.beam.ObstetricsGynecology;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by ${liushengping} on 2017/10/9.
 * 何人执笔？
 * 妇产科学目录
 */

public class DeparentScienDirActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_deparent_dir)ListView list_dir;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.pro_main)ProgressBar pro_main;
    @Override
    protected int getLayout() {
        return R.layout.activity_deparentsciendir;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.fckx));
        getData();
    }

    public DeparentScien scien;
    public void getData(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y123");
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(2);
                    }else{
                        scien=new DeparentScien();
                        Gson gson=new Gson();
                        scien=gson.fromJson(o.toString(),DeparentScien.class);
                        if(scien.getResultCode().equals("0")){
                            handler.sendEmptyMessage(3);
                        }else{
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(2);
        }
    }

    public List<ObstetricsGynecology> list;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://无数据
                    pro_main.setVisibility(View.INVISIBLE);
                    list_dir.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(),R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.no_data));
                    text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
                    text_no_data.setBackgroundResource(R.color.white);
                    break;
                case 2://断网
                    pro_main.setVisibility(View.INVISIBLE);
                    list_dir.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(MyApplication.getContextObject(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setText(MyApplication.getContextObject().getString(R.string.lock_try_again));
                    text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.white));
                    text_no_data.setBackgroundResource(R.drawable.login_sel);
                    break;
                case 3://成功
                    pro_main.setVisibility(View.INVISIBLE);
                    list_dir.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list=scien.getObstetricsGynecology();
                    DepaementScienDirAdapter adapter=new DepaementScienDirAdapter(MyApplication.getContextObject(),list,R.layout.list_depar_item);
                    list_dir.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_no_data.getText().toString().equals(getString(R.string.lock_try_again))){
                    pro_main.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    getData();
                }
            }
        });
        list_dir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ObstetricsGynecology gynecology=list.get(i);
                Intent intent=new Intent(DeparentScienDirActivity.this,DeparentScienActivity.class);
                intent.putExtra("gynecology",gynecology);
                startActivity(intent);
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
