package wizrole.hoservice.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.HosMessAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.beam.HosMsg;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * Created by a on 2017/9/4.
 * 住院消息
 */

public class HosMessageActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_right)TextView text_right;
    @BindView(R.id.list_hos_message)ListView list_hos_message;

    @BindView(R.id.lin_wifi_err)RelativeLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_no_data)TextView text_no_data;
    @BindView(R.id.pro_main)RelativeLayout pro_main;

    public List<HosMsg> hosMsgList;
    public String[] string;
    public HosMessAdapter adapter;
    @Override
    protected int getLayout() {
        return R.layout.activity_hosmessage;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.hos_message));
        text_right.setText(getString(R.string.other_message));
        hosMsgList=new ArrayList<HosMsg>();
//        String pushmessage=readFileSdcard("/sdcard/wizrole/pushMess/pushMess.txt");
        String pushmessage="1-1-2-3-2-1-";
        if(!pushmessage.equals("")){
            string=pushmessage.split("-");
            for (int i=0;i<string.length;i++){
                HosMsg hosMsg=new HosMsg();
                hosMsg.setType(string[i]);
                hosMsgList.add(hosMsg);
            }
        }
        setView();
    }
    //读在/mnt/sdcard/目录下面的文件
    public String readFileSdcard(String fileName){
        String isoString="";
        try {
            File urlFile = new File(fileName);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
                isoString = str+mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isoString;
    }

    public void setView(){
        if(hosMsgList!=null){
            if(hosMsgList.size()>0){
                pro_main.setVisibility(View.INVISIBLE);
                list_hos_message.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                adapter=new HosMessAdapter(HosMessageActivity.this,hosMsgList);
                list_hos_message.setAdapter(adapter);
            }else{
                list_hos_message.setVisibility(View.INVISIBLE);
                pro_main.setVisibility(View.INVISIBLE);
                lin_wifi_err.setVisibility(View.VISIBLE);
                ImageLoading.common(HosMessageActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                text_no_data.setVisibility(View.VISIBLE);
                text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
                text_no_data.setBackgroundResource(R.color.white);
                text_no_data.setText(getString(R.string.data_numm));
                MyToast(getString(R.string.data_numm));
            }
        }else{
            list_hos_message.setVisibility(View.INVISIBLE);
            pro_main.setVisibility(View.INVISIBLE);
            lin_wifi_err.setVisibility(View.VISIBLE);
            ImageLoading.common(HosMessageActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
            text_no_data.setVisibility(View.VISIBLE);
            text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
            text_no_data.setBackgroundResource(R.color.white);
            text_no_data.setText(getString(R.string.data_numm));
            MyToast(getString(R.string.data_numm));
        }
    }


    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除推送消息数据源---已查阅
                File file = new File("/sdcard/wizrole/pushMess/pushMess.txt");
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
                if(SharedPreferenceUtil.getNotidic(HosMessageActivity.this)==1){
                    //删除通知栏进入标志
                    SharedPreferenceUtil.clearNotidic(HosMessageActivity.this);
                    Intent intent=new Intent(HosMessageActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    setResult(2);
                    finish();
                }
            }
        });
        list_hos_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HosMsg hosMsg=hosMsgList.get(position);
                if(hosMsg.getType().equals("1")){//检查医嘱
                    hosMsgList.remove(hosMsgList.get(position));
                    adapter.notifyDataSetChanged();
                    Intent intent=new Intent(HosMessageActivity.this, AdviceActivity.class);
                    startActivity(intent);
                    check();
                }else if(hosMsg.getType().equals("2")){//检验结果
                    hosMsgList.remove(hosMsgList.get(position));
                    adapter.notifyDataSetChanged();
                    Intent intent=new Intent(HosMessageActivity.this, InspectionActivity.class);
                    startActivity(intent);
                    check();
                }else if(hosMsg.getType().equals("3")){//自定义推送消息
                    hosMsgList.remove(hosMsgList.get(position));
                    adapter.notifyDataSetChanged();
                    Intent intent=new Intent(HosMessageActivity.this, CustomMessActivity.class);
                    startActivity(intent);
                    check();
                }
            }
        });
        //其他消息（自定义消息）
        text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HosMessageActivity.this,CustomMessActivity.class);
                startActivity(intent);
            }
        });
    }

    public void check(){
          if(hosMsgList.size()==0){
              lin_wifi_err.setVisibility(View.VISIBLE);
              list_hos_message.setVisibility(View.INVISIBLE);
              ImageLoading.common(HosMessageActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
              text_no_data.setText(MyApplication.getContextObject().getString(R.string.data_numm));
              text_no_data.setBackgroundResource(R.color.white);
              text_no_data.setTextColor(MyApplication.getContextObject().getResources().getColor(R.color.huise));
          }
    }
    @Override
    public void onBackPressed() {
        if(SharedPreferenceUtil.getNotidic(HosMessageActivity.this)==1){
            //删除推送消息数据源---已查阅
            File file = new File("/sdcard/wizrole/pushMess/pushMess.txt");
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            //删除通知栏进入标志
            SharedPreferenceUtil.clearNotidic(HosMessageActivity.this);
            Intent intent=new Intent(HosMessageActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            setResult(2);
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除推送消息数据源---已查阅
        File file = new File("/sdcard/wizrole/pushMess/pushMess.txt");
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        //删除通知栏进入标志
        if(SharedPreferenceUtil.getNotidic(HosMessageActivity.this)==1) {
            SharedPreferenceUtil.clearNotidic(HosMessageActivity.this);
        }
    }
}
