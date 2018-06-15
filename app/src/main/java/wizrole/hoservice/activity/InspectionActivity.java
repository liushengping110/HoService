package wizrole.hoservice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.InpectionAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.AdmResultReports;
import wizrole.hoservice.beam.Inspection;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.dialog.LoadingDailog;

/**
 * Created by ${liushengping} on 2017/9/26.
 * 何人执笔？
 * 检验结果
 */

public class InspectionActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.text_start)TextView text_start;
    @BindView(R.id.text_jy_sure)TextView text_jy_sure;
    @BindView(R.id.list_jy)ListView list_jy;
    @BindView(R.id.edit_num)EditText edit_num;
    @BindView(R.id.text_end)TextView text_end;

    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;
    public SimpleDateFormat format_Y;
    public SimpleDateFormat format_M;
    public SimpleDateFormat format_D;
    public SimpleDateFormat format;
    public String year;
    public String month;
    public Calendar calendar;
    public Date date;
    public CalendarView calendarView;
    public SimpleDateFormat sdf;
    public boolean ok_sel=false;
    public Inspection inspection;
    public List<AdmResultReports> reportses;
    @Override
    protected int getLayout() {
        return R.layout.activity_inspection;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.jianyanbg));
        date=new Date();
        format_Y = new SimpleDateFormat("yyyy");
        format_M= new SimpleDateFormat("MM");
        format_D= new SimpleDateFormat("dd");
        format=new SimpleDateFormat("yyyy-MM-dd");
        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        //初始化日期控件年月所需
        year=format_Y.format(date);
        month=format_M.format(date);
    }

    public Dialog dialog;
    /**
     * 获取检验报告
     */
    public void getInpection(String start,String end,String number){
        dialog= LoadingDailog.createLoadingDialog(InspectionActivity.this,"加载中");
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y108");
            object.put("CardNo",number);
            object.put("StartDate",start);
            object.put("EndDate",end);
            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
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
                        inspection=new Inspection();
                        inspection=gson.fromJson(o.toString(),Inspection.class);
                        if(inspection.getResultCode().equals("0")){
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
                    list_jy.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(InspectionActivity.this, R.drawable.null_data, img_net_err, R.drawable.null_data);
                    text_err_agagin_center.setText(getString(R.string.data_numm));
                    text_err_agagin_center.setBackgroundResource(R.color.white);
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.text_bule));
                    break;
                case 1://断网
                    LoadingDailog.closeDialog(dialog);
                    list_jy.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(InspectionActivity.this, R.drawable.net_err, img_net_err, R.drawable.net_err);
                    text_err_agagin_center.setText(getString(R.string.wifi_err_try_again));
                    text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.white));
                    MyToast(getString(R.string.wifi_err));
                    break;
                case 2://成功
                    LoadingDailog.closeDialog(dialog);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    list_jy.setVisibility(View.VISIBLE);
                    reportses=inspection.getAdmResultReports();
                    InpectionAdapter adapter=new InpectionAdapter(reportses,InspectionActivity.this);
                    list_jy.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(true);
            }
        });

        text_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(false);
            }
        });
        //重试
        text_err_agagin_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_err_agagin_center.getText().toString().equals(getString(R.string.wifi_err_try_again))){
                    if(text_start.getText().toString().equals(getString(R.string.start))){
                        MyToast(getString(R.string.start));
                    }else if(text_end.getText().toString().equals(getString(R.string.end))){
                        MyToast(getString(R.string.end));
                    }else if(edit_num.getText().toString().length()==0){
                        MyToast("请输入十二位就诊卡号");
                    }else{
                        lin_wifi_err.setVisibility(View.INVISIBLE);
                        ok_sel=false;
                        getInpection(text_start.getText().toString(),text_end.getText().toString(),edit_num.getText().toString());
                    }
                }
            }
        });
        //确定监听
        text_jy_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_start.getText().toString().equals(getString(R.string.start))){
                    MyToast(getString(R.string.start));
                }else if(text_end.getText().toString().equals(getString(R.string.end))){
                    MyToast(getString(R.string.end));
                }else if(edit_num.getText().toString().length()==0){
                    MyToast("请输入十二位就诊卡号");
                }else{
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    ok_sel=false;
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_num.getWindowToken(), 0);
                    getInpection(text_start.getText().toString(),text_end.getText().toString(),edit_num.getText().toString());
                }
            }
        });
    }
    public int year_sel=0;
    public int month_sel=0;
    public int day_sel=0;
    public AlertDialog dialog_time;
    public void showAlertDialog(final boolean status){
        dialog_time=new AlertDialog.Builder(this).create();
        dialog_time.setCancelable(false);
        dialog_time.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(this).inflate(R.layout.date_select,null);
        dialog_time.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        calendar = Calendar.getInstance();
        calendarView=(CalendarView)view.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                year_sel=year;
                month_sel=month+1;
                day_sel=dayOfMonth;
            }
        });
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);//取消
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);//确定
        if(ok_sel){
            if(status){//开始
                if(!text_end.getText().toString().equals(getString(R.string.end))){
                    //结束日期已选定后，设置开始时间往前一周
                    String[] e=text_end.getText().toString().split("-");
                    String max_e=e[0]+"年"+e[1]+"月"+e[2]+"日";
                    Date date_s=formatStringToDate(text_end.getText().toString());
                    Calendar calendar = Calendar.getInstance();  //得到日历
                    calendar.setTime(date_s);
                    calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为前七天
                    Date one = calendar.getTime();
                    String msg=sdf.format(one);
                    calendarView.setMinDate(getStringToDate(msg));//设置最小可选择七天前
                    calendarView.setMaxDate(getStringToDate(max_e));//设置最大可选择结束日期
                }
            }else{//结束
                if(!text_start.getText().toString().equals(getString(R.string.start))){
                    //开始日期已选定后，设置开始时间往后一周
                    String[] s=text_start.getText().toString().split("-");
                    String max_e=s[0]+"年"+s[1]+"月"+s[2]+"日";
                    Date date_s=formatStringToDate(text_start.getText().toString());
                    Calendar calendar = Calendar.getInstance();  //得到日历
                    calendar.setTime(date_s);
                    calendar.add(Calendar.DAY_OF_MONTH, +7);  //设置为后七天
                    Date one = calendar.getTime();
                    String msg=sdf.format(one);
                    calendarView.setMinDate(getStringToDate(max_e));//设置最小可选择七天后
                    calendarView.setMaxDate(getStringToDate(msg));//设置最大可选择开始日期
                }
            }
        }
        //确定
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year_sel==0||month_sel==0){
                    String sel_date=format.format(date);
                    if(status){//开始
                        text_start.setText(sel_date);
                    }else{//结束
                        text_end.setText(sel_date);
                    }
                }else{
                    //获取到设置的时间
                    String date=year_sel+"-"+month_sel+"-"+day_sel;
                    if(status){//开始
                        text_start.setText(date);
                    }else{//结束
                        text_end.setText(date);
                    }
                }
                ok_sel=true;
                dialog_time.dismiss();
            }
        });
        //取消
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok_sel=false;
                dialog_time.dismiss();
            }
        });
        //设置date布局
        dialog_time.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_time.setView(view);
        dialog_time.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = dialog_time.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog_time.getWindow().setAttributes(layoutParams);
    }

    /*将字符串转为时间戳*/
    public  long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try{
            date = sdf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将传入的日期String转换为日期对象
     */
    public  Date formatStringToDate(String dateString ) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
