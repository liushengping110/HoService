package wizrole.hoservice.activity;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.HosKnowAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.HosKnow;
import wizrole.hoservice.beam.HospNotesList;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by liushengping on 2017/12/5/005.
 * 何人执笔？
 */

public class HosKnowActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_hosknow)ListView list_hosknow;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @Override
    protected int getLayout() {
        return R.layout.activity_hosknow;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("住院须知");
        String mac=getMacAddress();
        if(mac==null){
            handler.sendEmptyMessage(1);
        }else{
            getHosKnow(mac);
        }
    }

    public Dialog dialog;
    public HosKnow  hosKnow;
    public List<HospNotesList> lists;
    public void getHosKnow(String mac){
        dialog= LoadingDailog.createLoadingDialog(HosKnowActivity.this,"加载中");
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y117");
            object.put("Mac",mac);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler.sendEmptyMessage(1);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(1);
                    }else{
                        Gson gson=new Gson();
                        hosKnow=new HosKnow();
                        hosKnow=gson.fromJson(o.toString(),HosKnow.class);
                        if(hosKnow.getResultCode().equals("0")){
                            handler.sendEmptyMessage(2);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(1);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://无数据
                    LoadingDailog.closeDialog(dialog);
                    list_hosknow.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(HosKnowActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_no_data.setText(getString(R.string.no_data));
                    text_no_data.setTextColor(getResources().getColor(R.color.huise));
                    text_no_data.setBackgroundResource(R.color.white);
                    break;
                case 1://断网
                    LoadingDailog.closeDialog(dialog);
                    list_hosknow.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(HosKnowActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    text_no_data.setText(getString(R.string.lock_try_again));
                    text_no_data.setTextColor(getResources().getColor(R.color.white));
                    text_no_data.setBackgroundResource(R.drawable.login_sel);
                    break;
                case 2://成功
                    LoadingDailog.closeDialog(dialog);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list_hosknow.setVisibility(View.VISIBLE);
                    lists=hosKnow.getHospNotesList();
                    HosKnowAdapter adapter=new HosKnowAdapter(HosKnowActivity.this,lists,R.layout.list_hosknow_item);
                    list_hosknow.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void setListener() {
        list_hosknow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HospNotesList hospNotesList=lists.get(position);
                Intent intent=new Intent(HosKnowActivity.this,HosKnowDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("hosknow",hospNotesList);
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
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_no_data.getText().toString().equals(getString(R.string.no_look_err))){
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    String mac=getMacAddress();
                    if(mac==null){
                        handler.sendEmptyMessage(1);
                    }else{
                        getHosKnow(mac);
                    }
                }
            }
        });
    }

    /**
     * 获取移动设备本地IP
     * @return
     */
    protected static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress)en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 根据IP地址获取MAC地址
     * @return
     */
    @SuppressLint({ "NewApi", "DefaultLocale" })
    public  String getMacAddress(){
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) { buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            MyToast("Mac地址获取失败，请检查无线网络是否打开！");
        }
        return strMacAddr;
    }
}
