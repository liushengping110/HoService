package wizrole.hoservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.activity.AdviceActivity;
import wizrole.hoservice.activity.DeparentIntrduActivity;
import wizrole.hoservice.activity.DeparentScienDirActivity;
import wizrole.hoservice.activity.DepositActivity;
import wizrole.hoservice.activity.DocIntrduceActivity;
import wizrole.hoservice.activity.HosCommituListActivity;
import wizrole.hoservice.activity.HosKnowActivity;
import wizrole.hoservice.activity.InspectionActivity;
import wizrole.hoservice.activity.NewsDetailActivity;
import wizrole.hoservice.activity.PayActivity;
import wizrole.hoservice.activity.PerInforActivity;
import wizrole.hoservice.adapter.DeparentNewsViewpAdapter;
import wizrole.hoservice.adapter.NewsViewpNetErrAdapter;
import wizrole.hoservice.beam.HNews;
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.ui.Point;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;


/** 住院界面 */
public class Fragment_hos extends Fragment implements View.OnClickListener{

    private View view;
    private View layout;
    public ViewFlipper upview;
    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public Point point;
    private boolean cunhuan=true;//自动播放ViewPager判断调条件
    public ViewPager viewpager;//ViewPager控件
    public  LinearLayout group;//图片轮播点
    public  RelativeLayout  lin_hos_yj, lin_hos_jh, lin_hos_message, lin_hos_bg, lin_hos_js, lin_hos_ys,lin_hos_jl;
    public RelativeLayout lin_hos_xx, lin_hos_qd, lin_hos_yz;
    public boolean  control_start=true;
    public boolean text_scroll=false;//滚动公告网络请求失败标记为
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_hos , null);
            //加载数据操作
            initUI();
            setListener();
            isCreateView = true;
        }
        return view;
    }

    //此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if(!isLoadData){
            initViewPager();
            textScroll();
            isLoadData=true;
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }

    public void initUI(){
        upview = (ViewFlipper)view .findViewById(R.id.view_flipper);
        viewpager = (ViewPager)view .findViewById(R.id.view_pager);
        group=(LinearLayout) view.findViewById(R.id.viewGroup);
        lin_hos_xx=(RelativeLayout) view.findViewById(R.id.lin_hos_xx);
        lin_hos_qd=(RelativeLayout) view.findViewById(R.id.lin_hos_qd);
        lin_hos_yz=(RelativeLayout) view.findViewById(R.id.lin_hos_yz);
        lin_hos_yj=(RelativeLayout) view.findViewById(R.id.lin_hos_yj);
        lin_hos_jh=(RelativeLayout) view.findViewById(R.id.lin_hos_jh);
        lin_hos_message=(RelativeLayout) view.findViewById(R.id.lin_hos_message);
        lin_hos_bg=(RelativeLayout) view.findViewById(R.id.lin_hos_bg);
        lin_hos_js=(RelativeLayout) view.findViewById(R.id.lin_hos_js);
        lin_hos_ys=(RelativeLayout) view.findViewById(R.id.lin_hos_ys);
        lin_hos_jl=(RelativeLayout) view.findViewById(R.id.lin_hos_jl);
    }

    public List<HosNews> newses;
    public List<HosNews> newses_sel;
    public void initViewPager(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y112");
            object.put("PageNo",1);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler_viewpager.sendEmptyMessage(0);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler_viewpager.sendEmptyMessage(0);
                    }else{
                        HNews dNews=new HNews();
                        Gson gson=new Gson();
                        dNews=gson.fromJson(o.toString(),HNews.class);
                        if(dNews.getResultCode().equals("0")){
                            if(dNews.getHosNews().size()>0){
                                newses=dNews.getHosNews();
                                newses_sel=new ArrayList<HosNews>();
                                if(newses.size()>4){
                                    for(int i=0;i<4;i++){//默认加载四条
                                        newses_sel.add(newses.get(i));
                                    }
                                }else{
                                    newses_sel.addAll(newses);
                                }
                                handler_viewpager.sendEmptyMessage(2);
                            }else{
                                handler_viewpager.sendEmptyMessage(0);
                            }
                        }else{
                            handler_viewpager.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler_viewpager.sendEmptyMessage(0);
        }
    }

    public TextView text_one;
    public TextView text_two;
    public HNews hNews;
    public List<HosNews> hosNews;
    public void textScroll() {
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode", "Y133");
            object.put("PageNo", 1);
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler_viewpager.sendEmptyMessage(3);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler_viewpager.sendEmptyMessage(3);
                    }else{
                        Gson gson=new Gson();
                        hNews=new HNews();
                        hNews=gson.fromJson(o.toString(),HNews.class);
                        if(hNews.getResultCode().equals("0")){
                            handler_viewpager.sendEmptyMessage(4);
                        }else{
                            handler_viewpager.sendEmptyMessage(3);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler_viewpager.sendEmptyMessage(3);
        }
    }

    public void setListener(){
        lin_hos_xx.setOnClickListener(this);
        lin_hos_qd.setOnClickListener(this);
        lin_hos_yz.setOnClickListener(this);
        lin_hos_yj.setOnClickListener(this);
        lin_hos_jh.setOnClickListener(this);
        lin_hos_message.setOnClickListener(this);
        lin_hos_bg.setOnClickListener(this);
        lin_hos_js.setOnClickListener(this);
        lin_hos_ys.setOnClickListener(this);
        lin_hos_jl.setOnClickListener(this);
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        control_start=false;
                        break;
                    case MotionEvent.ACTION_UP:
                        control_start=true;
                        break;
                }
                return false;
            }
        });

        upview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    upview.getCurrentView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if(text_scroll){//请求成功后，方可点击
                                int num=upview.getDisplayedChild();
                                HosNews news=hosNews.get(num);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("NewsDetail",news);
                                Intent intent=new Intent(getActivity(),NewsDetailActivity.class);
                                intent.putExtra("type","textScroll");
                                intent.putExtras(bundle);
                                startActivity(intent );
                            }
                        }
                    });
            }
        });
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_hos_xx://基本信息
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), PerInforActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_qd://费用清单
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), PayActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_yz://检查医嘱
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), AdviceActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_yj://押金缴纳
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), DepositActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_jh://检验查询
                if (SharedPreferenceUtil.getLoginState(getActivity())==1) {
                    intent = new Intent(getActivity(), InspectionActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_message://住院须知
                intent=new Intent(getActivity(),HosKnowActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_hos_bg://妇产科学
                intent=new Intent(getActivity(),DeparentScienDirActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_hos_js://科室介绍--传递科室名称进入
                if (SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), DeparentIntrduActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_ys://科室医生---传递科室进行筛选进入
                if (SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(), DocIntrduceActivity.class);
                    intent.putExtra("name","hos");
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_hos_jl://医患交流
                intent=new Intent(getActivity(),HosCommituListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public List<Integer> integerList;
    public Handler handler_viewpager=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){//院级新闻----请求失败
                integerList=new ArrayList<Integer>();
                integerList.add(R.drawable.deparent_one);
                integerList.add(R.drawable.deparent_two);
                setNetErrViewPager(integerList);
            }else if(msg.what==2){//院级新闻---请求成功，有数据
                setViewPager(newses_sel);
            }else if(msg.what==3){//科级滚动文本-----请求失败
                text_scroll=false;
                setTextScrollView(textScrollNerERR());
            }else if(msg.what==4){//科级滚动文本-----请求成功
                text_scroll=true;
                hosNews=hNews.getHosNews();
               setTextScrollView(hosNews);
            }
        }
    };

    /**
     * 网络连接成功， 广告轮播
     * @param list
     */
    public void setViewPager(List<HosNews> list){
        //初始化点 控件
        point=new Point(getActivity(), list.size());
        group.addView(point);
        // 设置Adapter
        DeparentNewsViewpAdapter hosNewsViewpAdapter =new DeparentNewsViewpAdapter(getActivity(),list);
        viewpager.setAdapter(hosNewsViewpAdapter);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                point.changePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 自动切换页面功能
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cunhuan) {
                    SystemClock.sleep(4000);
                    VPhandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }


    private Handler VPhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(control_start){
                if (viewpager.getCurrentItem() == newses.size() - 1) {
                    viewpager.setCurrentItem(0);
                }else{
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                }
            }
        }
    };

    /**
     *网络连接失败，广告轮播
     * @param list
     */
    public void setNetErrViewPager(List<Integer> list){
        //初始化点 控件
        point=new Point(getActivity(), list.size());
        group.addView(point);
        // 设置Adapter
        NewsViewpNetErrAdapter errAdapter =new NewsViewpNetErrAdapter(getActivity(),list);
        viewpager.setAdapter(errAdapter);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                point.changePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 自动切换页面功能
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cunhuan) {
                    SystemClock.sleep(4000);
                    err_handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private Handler err_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(control_start){
                if (viewpager.getCurrentItem() == integerList.size() - 1) {
                    viewpager.setCurrentItem(0);
                }else{
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                }
            }
        }
    };

    /**
     * 滚动公告网络错误
     */
    public List<HosNews> textScrollNerERR() {
        hosNews = new ArrayList<HosNews>();
        for (int i = 0; i < 2; i++) {
            HosNews hos = new HosNews();
            hos.setNews_msg(getString(R.string.text_scroll_content));
            hos.setNews_title(getString(R.string.text_scroll_title));
            hosNews.add(hos);
            hos = null;
        }
        return hosNews;
    }

    /**
     * 设置滚动公告
     * @param News
     */
    public void setTextScrollView( List<HosNews> News) {
        for(int i=0;i<5;i++){
            if(i<hosNews.size()){
                LayoutInflater inflater=LayoutInflater.from(getActivity());
                layout=(LinearLayout) inflater.inflate(R.layout.flipper_item,null);
                text_one=(TextView)layout.findViewById(R.id.text_one);
                text_two=(TextView)layout.findViewById(R.id.text_two);
                text_one.setText(Html.fromHtml(News.get(i).getNews_title()));
                text_two.setText(Html.fromHtml(News.get(i).getNews_msg()));
                upview.addView(layout);
            }
        }
        upview.setInAnimation(getActivity(),R.anim.push_up_in);
        upview.setOutAnimation(getActivity(),R.anim.push_up_out);
        upview.setAutoStart(true);
        upview.startFlipping();
    }
}
