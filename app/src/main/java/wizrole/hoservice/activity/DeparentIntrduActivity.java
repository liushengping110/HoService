package wizrole.hoservice.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.DoctorInfos;
import wizrole.hoservice.beam.SearchBack;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;

/**
 * Created by a on 2017/8/28.
 * 科室详细介绍页面
 */

public class DeparentIntrduActivity extends BaseActivity {

    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.text_depar_intrduce)TextView text_depar_intrduce;
    @BindView(R.id.pro_main)ProgressBar pro_main;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;

    public  String name;
    public boolean status=true;
    public String content;
    public DeparentIntrduce intrduce;//院级进入
    @Override
    protected int getLayout() {
        return R.layout.activity_depareintrdu;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.deparent_intrduces));
        text_right.setText(getString(R.string.deparent_doc));
        //科室列表进入
        intrduce=(DeparentIntrduce) getIntent().getSerializableExtra("deparentment");
        if(intrduce!=null){//医院概括进入
            content=intrduce.getDeparentIntrduce();
            name=intrduce.getDeparentName();//科室名称
            lin_wifi_err.setVisibility(View.INVISIBLE);
            pro_main.setVisibility(View.INVISIBLE);
            text_depar_intrduce.setText(content);
        }else{//住院级别进入
            if(getMacAddress()==null){
                handler.sendEmptyMessage(0);
            }else{
                String mac=getMacAddress();
                getData(mac);
            }
        }
    }

    public SearchBack infor;
    public void getData(String mac){
        pro_main.setVisibility(View.VISIBLE);
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y114");
            object.put("mac",mac);
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
                        infor=new SearchBack();
                        infor=gson.fromJson(o.toString(),SearchBack.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(2);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(0);
        }
    }


    public  List<DeparentIntrduce> list_deparent;
    public  List<DoctorInfos> list_doc;
    public String de_name;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                status=false;
                text_depar_intrduce.setVisibility(View.INVISIBLE);
                pro_main.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(DeparentIntrduActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                text_err_agagin_center.setText(getString(R.string.wifi_err_try_again));
                text_err_agagin_center.setTextColor(getResources().getColor(R.color.white));
                text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
            }else if(msg.what==1){
                status=true;
                //只获取科室
                list_deparent=infor.getDeparentIntrduce();
                if(list_deparent.size()>0){
                    text_depar_intrduce.setVisibility(View.VISIBLE);
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    String de_intrduce=list_deparent.get(0).getDeparentIntrduce();
                    de_name=list_deparent.get(0).getDeparentName();
                    text_depar_intrduce.setText(de_intrduce);
                }else{
                    pro_main.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(DeparentIntrduActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_err_agagin_center.setText(getString(R.string.data_numm));
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.huise));
                    text_err_agagin_center.setBackgroundResource(R.color.white);
                }
            }else{//无数据
                pro_main.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(DeparentIntrduActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_err_agagin_center.setText(getString(R.string.data_numm));
                text_err_agagin_center.setTextColor(getResources().getColor(R.color.huise));
                text_err_agagin_center.setBackgroundResource(R.color.white);
            }
        }
    };

    @Override
    protected void setListener() {
        //返回
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //科室医生
        text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intrduce!=null){//院级 --科室详情进入--
                    Intent intent=new Intent(DeparentIntrduActivity.this,DocIntrduceActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }else{//科级
                    Intent intent=new Intent(DeparentIntrduActivity.this,DocIntrduceActivity.class);
                    intent.putExtra("name", de_name);
                    startActivity(intent);
                }
            }
        });
        //重试
        text_depar_intrduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status){
                    if(getMacAddress()==null){
                        handler.sendEmptyMessage(0);
                    }else{
                        if(text_err_agagin_center.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                            lin_wifi_err.setVisibility(View.INVISIBLE);
                            getData(getMacAddress());
                        }
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
