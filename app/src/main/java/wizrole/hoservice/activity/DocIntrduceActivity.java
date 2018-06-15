package wizrole.hoservice.activity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.DeparentPopAdapter;
import wizrole.hoservice.adapter.DeparentTitleAdapter;
import wizrole.hoservice.adapter.DocInducAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.Deparentment;
import wizrole.hoservice.beam.Doc;
import wizrole.hoservice.beam.DoctorInfos;
import wizrole.hoservice.beam.SearchBack;
import wizrole.hoservice.ui.PopupWindowPotting;
import wizrole.hoservice.util.DensityUtil;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;
import wizrole.hoservice.view.PopupMaxHeightListView;

/**
 * Created by a on 2017/5/12.
 * 医生列表
 */

public class DocIntrduceActivity extends BaseActivity {
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_all_deparent)TextView text_all_deparent;
    @BindView(R.id.text_de_title)TextView text_de_title;
    @BindView(R.id.lin_all_deparent)LinearLayout lin_all_deparent;
    @BindView(R.id.lin_all_title)LinearLayout lin_all_title;
    @BindView(R.id.rel_search)RelativeLayout rel_search;
    @BindView(R.id.list_doc)ListView list_doc;
    @BindView(R.id.img_deparent)ImageView img_deparent;
    @BindView(R.id.img_title)ImageView img_title;
    @BindView(R.id.view_over)View view_over;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    public Dialog dialog;
    public PopupWindowPotting DeparentwindowPotting;
    public PopupWindowPotting TitlewindowPotting;
    public PopupMaxHeightListView list_pop_departitle;//弹窗列表控件
    public String name;
    public boolean status=false;//断网状态
    public boolean img_status;//职称和科室动画状态
    public List<String> list;
    public String s;
    public  List<DeparentIntrduce> intrduces;//科室集合
    public  List<DeparentIntrduce> intrduces_final;//科室集合
    public int open_close_status=0;//弹窗的显示和关闭的状态  0  都关闭  1显示1  2显示2
    @Override
    protected int getLayout() {
        return R.layout.activity_docintrduce;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        intrduces_final=new ArrayList<DeparentIntrduce>();
        text_title.setText(getString(R.string.doc_intrduces));
        name=getIntent().getStringExtra("name");
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width/2,DensityUtil.dip2px(this,45));
        lin_all_deparent.setLayoutParams(params);
        lin_all_title.setLayoutParams(params);
        list=new ArrayList<String>();
        for (int i=0;i<3;i++){
            if(i==0){
                 s=getString(R.string.no_xianzhi);
            }else if(i==1){
                 s=getString(R.string.zhuren_doc);
            }else if (i==2){
                 s=getString(R.string.fzhuren_doc);
            }
            list.add(s);
        }
        DeparentIntrduce intrduce=new DeparentIntrduce();
        intrduce.setDeparentName(getString(R.string.all_deparent));
        intrduces_final.add(intrduce);
        getDataDeparent();//获取科室列表  104
        if(name==null||name.equals("")){//院级直接进入
            text_all_deparent.setText(getString(R.string.all_deparent));
            getDoctorInfos("","");//获取医生列表
        }else if(name.equals("hos")){//科级进入
            //114接口
            if(getMacAddress()==null){
                handler.sendEmptyMessage(4);
            }else{
                getData(getMacAddress());
            }
        }else{//院级-科室进入
            text_all_deparent.setText(name);
            getDoctorInfos(name,"");//获取医生列表
        }
    }

    /***
     * 科室弹窗
     * @param list_deparentName
     */
    public String title;
    public String deparentName;
    public void initDeparent( final  List<DeparentIntrduce> list_deparentName ){
        if(DeparentwindowPotting==null){
            DeparentwindowPotting=new PopupWindowPotting(this,2) {
                @Override
                protected int getLayout() {
                    return R.layout.pop_list_deapren_title;
                }

                @Override
                protected void initUI() {
                    list_pop_departitle=$(R.id.list_pop_departitle);
                    list_pop_departitle.setListViewHeight((int)(700));
                }

                @Override
                protected void setListener() {
                    list_pop_departitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DeparentwindowPotting.Hide();
                            rotateEnd(img_deparent);
                            open_close_status=3;
                            view_over.setVisibility(View.INVISIBLE);
                            list_doc.setVisibility(View.INVISIBLE);
                            lin_wifi_err.setVisibility(View.INVISIBLE);
                            DeparentIntrduce intrdece=list_deparentName.get(position);
                            text_all_deparent.setText(intrdece.getDeparentName());
                            if(text_de_title.getText().toString().equals(getString(R.string.zhicheng))||text_de_title.getText().toString().equals(getString(R.string.no_xianzhi))){
                                title="";
                            }else{
                                title=text_de_title.getText().toString();
                            }
                            if(list_deparentName.get(position).getDeparentName().equals(getString(R.string.all_deparent))){
                                getDoctorInfos("",title);
                            }else{
                                getDoctorInfos(intrdece.getDeparentName(),title);
                            }
                        }
                    });
                }
            };
        }
    }
    public void initTitlePop( final List<String> list_title){
        if(TitlewindowPotting==null){
            TitlewindowPotting=new PopupWindowPotting(this,2) {
                @Override
                protected int getLayout() {
                    return R.layout.pop_list_deapren_title;
                }

                @Override
                protected void initUI() {
                    list_pop_departitle=$(R.id.list_pop_departitle);
                    list_pop_departitle.setListViewHeight((int)(700));
                }

                @Override
                protected void setListener() {
                    list_pop_departitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TitlewindowPotting.Hide();
                            rotateEnd(img_title);
                            open_close_status=3;
                            view_over.setVisibility(View.INVISIBLE);
                            list_doc.setVisibility(View.INVISIBLE);
                            lin_wifi_err.setVisibility(View.INVISIBLE);
                            String s=list_title.get(position);
                            text_de_title.setText(s);
                            if(text_all_deparent.getText().toString().equals(getString(R.string.all_deparent))){
                                deparentName="";
                            }else{
                                deparentName=text_all_deparent.getText().toString();
                            }
                            if(list_title.get(position).equals(getString(R.string.zhicheng))||list_title.get(position).equals(getString(R.string.no_xianzhi))){
                                getDoctorInfos(deparentName,"");
                            }else{
                                getDoctorInfos(deparentName,list_title.get(position));
                            }
                        }
                    });
                }
            };
        }
    }


    /***
     * 获取科室列表
     */
    public void getDataDeparent(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y104");
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(Constant.base_Url, object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    status=false;
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        status=false;
                        handler.sendEmptyMessage(0);
                    }else{
                        Deparentment deparentment=new Deparentment();
                        Gson gson=new Gson();
                        deparentment=gson.fromJson(o.toString(),Deparentment.class);
                        if(deparentment.getResultCode().equals("0")){
                            intrduces=deparentment.getDeparentIntrduce();
                            intrduces_final.addAll(intrduces);//添加，默认支持全部科室
                            if(intrduces.size()>1){
                                status=true;
                            }else{
                                status=false;
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            handler.sendEmptyMessage(0);
        }
    }

    public Doc doc;
    public List<DoctorInfos> infoses;
    public DocInducAdapter adapter;
    /**
     * 获取医生列表
     * @param deparenName
     * @param title
     */
    public void getDoctorInfos(String deparenName,String title){
        dialog=LoadingDailog.createLoadingDialog(DocIntrduceActivity.this,"加载中");
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y105");
            object.put("DeparentName",deparenName);
            object.put("DocTitle",title);
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
                        Gson gson=new Gson();
                        doc=new Doc();
                        doc=gson.fromJson(o.toString(),Doc.class);
                        if(doc.getResultCode().equals("0")){
                            handler.sendEmptyMessage(4);
                        }else{
                            handler.sendEmptyMessage(3);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(2);
        }
    }

    public SearchBack infor;

    /**
     * 此方法传入mac，获取设备对应的科室，返回科室对应的医生列表
     * @param mac
     */
    public void getData(String mac){
        dialog=LoadingDailog.createLoadingDialog(DocIntrduceActivity.this,"加载中");
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
                    handler.sendEmptyMessage(6);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(6);
                    }else{
                        Gson gson=new Gson();
                        infor=new SearchBack();
                        infor=gson.fromJson(o.toString(),SearchBack.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(5);
                        }else{
                            handler.sendEmptyMessage(7);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(6);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){//科室获取失败
                text_all_deparent.setText(getString(R.string.lock_try_again));
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==1){//科室  --获取无科室
                text_all_deparent.setText(getString(R.string.no_deparent));
            }else if(msg.what==2){//医生列表获取失败
                LoadingDailog.closeDialog(dialog);
                list_doc.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                text_no_data.setText("重新加载");
                text_no_data.setBackgroundResource(R.drawable.login_sel);
                text_no_data.setTextColor(getResources().getColor(R.color.white));
                ImageLoading.common(DocIntrduceActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==3){//医生列表  无数据
                LoadingDailog.closeDialog(dialog);
                list_doc.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                text_no_data.setText("当前科室无医生列表");
                text_no_data.setBackgroundResource(R.color.white);
                text_no_data.setTextColor(getResources().getColor(R.color.huise));
                ImageLoading.common(DocIntrduceActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                MyToast(getString(R.string.no_doc));
            }else if(msg.what==4){//医生列表获取成功
                LoadingDailog.closeDialog(dialog);
                infoses=doc.getDoctorInfos();
                list_doc.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                adapter=new DocInducAdapter(DocIntrduceActivity.this,infoses,R.layout.list_doc_item);
                list_doc.setAdapter(adapter);
            }else if(msg.what==5){//科级进入--获取医生数据成功
                infoses=infor.getDoctorInfos();
                List<DeparentIntrduce> de=infor.getDeparentIntrduce();
                if(de.size()>0){
                    text_all_deparent.setText(de.get(0).getDeparentName());
                }else{
                    text_all_deparent.setText(getString(R.string.all_deparent));
                }
                LoadingDailog.closeDialog(dialog);
                list_doc.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                adapter=new DocInducAdapter(DocIntrduceActivity.this,infoses,R.layout.list_doc_item);
                list_doc.setAdapter(adapter);
            }else if(msg.what==6){//科级进入-获取医生列表请求失败
                LoadingDailog.closeDialog(dialog);
                list_doc.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                text_no_data.setText("重新加载");
                text_no_data.setBackgroundResource(R.drawable.login_sel);
                text_no_data.setTextColor(getResources().getColor(R.color.white));
                ImageLoading.common(DocIntrduceActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                MyToast(getString(R.string.wifi_err));
            }else if(msg.what==7){//科级进入--获取医生列表无数据
                LoadingDailog.closeDialog(dialog);
                text_all_deparent.setText(getString(R.string.all_deparent));
                list_doc.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                text_no_data.setText("当前科室无医生列表");
                text_no_data.setBackgroundResource(R.color.white);
                text_no_data.setTextColor(getResources().getColor(R.color.huise));
                ImageLoading.common(DocIntrduceActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setTextAppearance(DocIntrduceActivity.this,R.style.TabLayoutTextStyle);
                text_no_data.setBackgroundResource(R.color.white);
                MyToast(getString(R.string.no_doc));
            }
        }
    };

    @Override
    protected void setListener() {
        //重试
        text_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_wifi_err.setVisibility(View.INVISIBLE);
                if(text_de_title.getText().toString().equals(getString(R.string.zhicheng))||text_de_title.getText().toString().equals(getString(R.string.no_xianzhi))){
                    title="";
                }else{
                    title=text_de_title.getText().toString();
                }
                if(text_all_deparent.getText().toString().equals(getString(R.string.all_deparent))){
                    getDoctorInfos("",title);
                }else{
                    getDoctorInfos(text_all_deparent.getText().toString(),title);
                }
            }
        });
        list_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDiss(position);
            }
        });
        view_over.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(open_close_status==1||open_close_status==2){
                    onDiss(-1);
                    return true;
                }else{
                    return false;
                }
            }
        });
        //title
        text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(open_close_status==1){
                    onDiss(-1);
                }else if(open_close_status==2){
                    onDiss(-1);
                }
            }
        });
    }

    @OnClick({R.id.lin_back, R.id.rel_search,R.id.lin_all_deparent,R.id.lin_all_title})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back: //返回
                if(open_close_status==1){
                    onDiss(-1);
                }else if(open_close_status==2){
                    onDiss(-1);
                }else{
                    finish();
                }
                break;
            case R.id.rel_search:
                if(open_close_status==1){
                    onDiss(-1);
                }else if(open_close_status==2){
                    onDiss(-1);
                }else{
                    Intent intent=new Intent(this,HosSearchActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.lin_all_deparent://科室
                if(open_close_status==1){
                    onDiss(-1);
                    return;
                }else if(open_close_status==2){
                    TitlewindowPotting.Hide();
                    rotateEnd(img_title);
                }
                view_over.setVisibility(View.VISIBLE);
                img_status=true;
                if(status){
                    if(DeparentwindowPotting==null){
                        initDeparent( intrduces_final);
                        DeparentPopAdapter adapter=new DeparentPopAdapter(DocIntrduceActivity.this,intrduces_final,R.layout.list_pop_item_departitle);
                        list_pop_departitle.setAdapter(adapter);
                    }
                    DeparentwindowPotting.Show(lin_all_deparent);
                    rotateStart(img_deparent);
                }else{//断网 数据为空的时候
                    getDataDeparent();
                }
                open_close_status=1;
                break;
            case R.id.lin_all_title://职称
                if(open_close_status==2){
                    onDiss(-1);
                    return;
                }else if(open_close_status==1){
                    rotateEnd(img_deparent);
                    DeparentwindowPotting.Hide();
                }
                img_status=false;
                view_over.setVisibility(View.VISIBLE);
                if(TitlewindowPotting==null){
                    initTitlePop(list);
                    DeparentTitleAdapter title_adapter=new DeparentTitleAdapter(DocIntrduceActivity.this,list,R.layout.list_pop_item_departitle);
                    list_pop_departitle.setAdapter(title_adapter);
                }
                TitlewindowPotting.Show(lin_all_deparent);
                rotateStart(img_title);
                open_close_status=2;
                break;
        }
    }


    /**
     * 弹窗上的三角形翻转
     */
    public void rotateStart(ImageView imageView) {
        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }

    /**
     * 弹窗上的三角形翻转
     */
    public void rotateEnd(ImageView imageView) {
        RotateAnimation animation = new RotateAnimation(180, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }

    public void onDiss(int psi) {
        if(open_close_status==1){
            rotateEnd(img_deparent);
            view_over.setVisibility(View.INVISIBLE);
        }else if(open_close_status==2){
            rotateEnd(img_title);
            view_over.setVisibility(View.INVISIBLE);
        }else {
            if(psi>=0){//item点击监听
                DoctorInfos intrduce=infoses.get(psi);
                Intent intent=new Intent(DocIntrduceActivity.this,DocSingleActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("intrduce",intrduce);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        open_close_status=3;//清空，此时都未打开
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

    @Override
    public void onBackPressed() {
        if(open_close_status==1){
            DeparentwindowPotting.Hide();
            rotateEnd(img_deparent);
            view_over.setVisibility(View.INVISIBLE);
        }else if(open_close_status==2){
            TitlewindowPotting.Hide();
            rotateEnd(img_title);
            view_over.setVisibility(View.INVISIBLE);
        }else{
            finish();
        }
        open_close_status=3;
    }
}
