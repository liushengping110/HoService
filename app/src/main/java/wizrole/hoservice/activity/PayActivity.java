package wizrole.hoservice.activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.PayInfor;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.fragment.Fragment_pay_three;
import wizrole.hoservice.util.EventUtil;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;

/**
 * 费用清单
 * 根据日期查询费用
 * Created by a on 2016/11/22.
 */

public class PayActivity extends BaseActivity {
    /**返回按钮*/
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    /**选择日期按钮*/
    @BindView(R.id.text_deposit)TextView text_deposit;//预存
    @BindView(R.id.text_patfee)TextView text_patfee;//合计花费
    @BindView(R.id.pay_view_pager)ViewPager viewPager;//列表控件
    @BindView(R.id.tabLayout)TabLayout tabLayout;//列表控件
    public int year_sel=0;
    public int month_sel=0;
    public int day_sel=0;
    /**当前日期-年 月  日*/
    public SimpleDateFormat format_Y;
    public SimpleDateFormat format_M;
    public SimpleDateFormat format_D;
    public SimpleDateFormat format;
    public String admDate;
    public String adm;
    public String now_max;
    public Date date;
    public Date three,one ,two,four;
    public String msg;
    public PersonInfor personInfor;
    public Calendar calendar;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        personInfor=(PersonInfor)SharedPreferenceUtil.getBean(this);
        admDate=personInfor.getAdmDate();
        String[] strs = admDate.split("[-]");  //字符串截取--设置DatePciker控件时间范围
        adm=strs[0]+getString(R.string.year)+strs[1]+getString(R.string.month)+strs[2]+getString(R.string.day);
        text_title.setText(getString(R.string.pay_list));
        setTabLayoutTitle();
        reflex();
        setView();
        getData(format.format(date));//获取余额和花费
    }

    public void setTabLayoutTitle(){
        //获取当前时间，进入费用清单，textview默认显示当前时间  以及对应的当天费用清单列表
        date =new Date();
        format_Y = new SimpleDateFormat("yyyy");
        format_M= new SimpleDateFormat("MM");
        format_D=new SimpleDateFormat("dd");
        format=new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();  //得到日历
        for(int i=1;i<5;i++){
            if(i==1){
                calendar.setTime(date);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH, -3);  //设置为大前天
                one = calendar.getTime();   //得到大前天的时间
                //获取第一次进入，请求数据的日期为前三天
            }else if(i==2){
                calendar.setTime(date);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH, -2);  //设置为前天
                two = calendar.getTime();   //得到前天的时间
            }else if(i==3){
                calendar.setTime(date);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为昨天
                three = calendar.getTime();   //得到昨天的时间
            }else if(i==4){//获取日期控件最大日期为当天
                calendar.setTime(date);//把当前时间赋给日历
                calendar.add(Calendar.DAY_OF_MONTH, 0);
                four = calendar.getTime();
                now_max=format_Y.format(four)+getString(R.string.year)+format_M.format(four)+getString(R.string.month)+format_D.format(four)+getString(R.string.day);
            }
        }
    }
    /**网络请求**/
    public PayInfor infor;
    public void getData(String date){
        JSONObject object=new JSONObject();
        try{
            object.put("TradeCode","Y101");
            object.put("CardNo", SharedPreferenceUtil.getLoginNum(PayActivity.this));
            object.put("StartDate", date);
            object.put("EndDate",date);
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
                        infor=new PayInfor();
                        infor=gson.fromJson(o.toString(),PayInfor.class);
                        if(infor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (Exception e){
            handler.sendEmptyMessage(0);
        }
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    text_deposit.setText(getString(R.string.no_know));
                    text_patfee.setText(getString(R.string.no_know));
                    break;
                case 1:
                    text_deposit.setText(infor.getDeposit());
                    text_patfee.setText(infor.getPatFee());
                    break;
            }
        }
    };

    public boolean four_status=false;//第四个页面有无初始化状态
    @Override
    protected void setListener() {
        tabLayout.getTabAt(3).getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()!=3){
                    text_date.setTextColor(getResources().getColor(R.color.text_fz));
                    text_more.setTextColor(getResources().getColor(R.color.text_fz));
                }else{
                    text_date.setTextColor(getResources().getColor(R.color.text_bule));
                    text_more.setTextColor(getResources().getColor(R.color.text_bule));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            //viewPager的监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    if(!four_status){
                        showAlertDialog();
                    }
                }
                position_now=position;//记录当前页面下表
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public Bundle bundle;
    public void setView(){
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return Fragment_pay_three.newInstance(1,false);
                    case 1:
                        return Fragment_pay_three.newInstance(2,true);
                    case 2:
                        return Fragment_pay_three.newInstance(3,true);
                    case 3:
                        return Fragment_pay_three.newInstance(4,true);
                    default:
                        return Fragment_pay_three.newInstance(1,true);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return format_M.format(one)+"-"+format_D.format(one);
                    case 1:
                        return format_M.format(two)+"-"+format_D.format(two);
                    case 2:
                        return format_M.format(three)+"-"+format_D.format(three);
                    case 3:
                        return "查看更多";
                    default:
                        return "";
                }
            }
        });
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        View view= LayoutInflater.from(PayActivity.this).inflate(R.layout.pay_tablayout_title,null);
        text_date=(TextView)view.findViewById(R.id.text_date);
        text_more=(TextView)view.findViewById(R.id.text_more);
        tabLayout.getTabAt(3).setCustomView(view);
    }
    public TextView text_date;
    public TextView text_more;

    //设置taLayout的下划线宽度
    public void reflex(){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout,20,20);
            }
        });
    }
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    public AlertDialog dialog;
    public CalendarView calendarView;
    public void showAlertDialog(){
        dialog=new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        final View view= LayoutInflater.from(this).inflate(R.layout.date_select,null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        calendar = Calendar.getInstance();
        calendarView=(CalendarView)view.findViewById(R.id.calendar);
        long min=getStringToDate(adm);  //设置住院开始时间-----起始时间设置
        long max=getStringToDate(now_max);  //设置最大选择时间
        calendarView.setMaxDate(max);
        calendarView.setMinDate(min);
        TextView text_cancle=(TextView)view.findViewById(R.id.text_cancle);//取消
        TextView text_sure=(TextView)view.findViewById(R.id.text_sure);//确定
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                year_sel=year;
                month_sel=month+1;
                day_sel=dayOfMonth;
            }
        });
        //确定
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year_sel==0||month_sel==0){
                    MyToast("今天的费用清单待明日查看");
                }else{
                    String now=format.format(date);
                    String[] strs = now.split("[-]");  //字符串截取--设置DatePciker控件时间范围
                    String temp=strs[0]+strs[1]+strs[2];
                    if(month_sel<=8){
                        m_sel="0"+month_sel;
                    }else{
                        m_sel=month_sel+"";
                    }
                    if(day_sel<=9){
                        d_sel="0"+day_sel;
                    }else{
                        d_sel=day_sel+"";
                    }
                    String a=year_sel+""+m_sel+""+d_sel+"";
                    if(temp.equals(a)){
                        MyToast("今天的费用清单待明日查看");
                    }else{
                        four_status=true;//已调用
//                //获取到设置的时间
                        EventBus.getDefault().post(new EventUtil(year_sel+"-"+month_sel+"-"+day_sel));//发送数据
                        text_date.setVisibility(View.VISIBLE);
                        text_date.setText(m_sel+"-"+d_sel);
                        text_date.setTextColor(getResources().getColor(R.color.text_bule));
                        text_more.setTextColor(getResources().getColor(R.color.text_bule));
                        tabLayout.getTabAt(3).select();
                        isSelect=true;
                    }
                    dialog.dismiss();
                    m_sel=null;
                    d_sel=null;
                    year_sel=0;
                    month_sel=0;
                    day_sel=0;
                }
            }
        });
        //取消
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!four_status){
                    viewPager.setCurrentItem(0);
                }
                dialog.dismiss();
                m_sel=null;
                d_sel=null;
                year_sel=0;
                month_sel=0;
                day_sel=0;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!four_status){
                    viewPager.setCurrentItem(0);
                }
                m_sel=null;
                d_sel=null;
                year_sel=0;
                month_sel=0;
                day_sel=0;
            }
        });
        //设置date布局
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(view);
        dialog.show();
        //设置大小
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }

    public String m_sel;
    public String d_sel;
    public int position_now=0;
    public boolean  isSelect=false;

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
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

    @Override
    public void onBackPressed() {
        if(dialog.isShowing()){
            dialog.dismiss();
        }else{
            finish();
        }
    }
}