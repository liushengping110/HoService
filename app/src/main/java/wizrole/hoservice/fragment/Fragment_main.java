package wizrole.hoservice.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.activity.DepositActivity;
import wizrole.hoservice.activity.DiseaseDetailInforActivity;
import wizrole.hoservice.activity.GuideActivity;
import wizrole.hoservice.activity.HealToolActivity;
import wizrole.hoservice.activity.HealWikipediaActivity;
import wizrole.hoservice.activity.HosIntrducActivity;
import wizrole.hoservice.activity.HosMessageActivity;
import wizrole.hoservice.activity.HosdynamicActivity;
import wizrole.hoservice.activity.HosnavigaActivity;
import wizrole.hoservice.activity.MainActivity;
import wizrole.hoservice.activity.MedsicSerachActivity;
import wizrole.hoservice.adapter.HealScien_adapter;
import wizrole.hoservice.adapter.HosNewsViewpAdapter;
import wizrole.hoservice.adapter.NewsViewpNetErrAdapter;
import wizrole.hoservice.beam.DiseasInfor;
import wizrole.hoservice.beam.DiseaseName;
import wizrole.hoservice.beam.HNews;
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.beam.PersonInfor;
import wizrole.hoservice.ui.Point;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.RxJavaOkPotting;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.view.MyScroview;
import wizrole.hoservice.view.OrdinaryFooterView;
import wizrole.hoservice.view.OrdinaryHeaderView;
import wizrole.hoservice.view.WhiteBgFooterView;


/**
 * Created by a on 2017/8/5.
 * 首页
 */

public class Fragment_main extends Fragment implements View.OnClickListener{
    private View view;
    public Point point;
    private boolean cunhuan=true;//自动播放ViewPager判断调条件
    public ViewPager viewpager;//ViewPager控件
    public  LinearLayout group;//图片轮播点
    public RelativeLayout lin_main_bg,lin_main_bk,lin_main_dt,lin_main_gj,lin_main_dh,lin_main_yj;
    public RelativeLayout lin_main_hos,lin_main_guide,lin_main_message;
    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public ImageView floating_button;
    public TextView text_msg_num;
    public String[] pushmess;
    public ListView list_view;
    public LinearLayout pro_main;
    public TextView text_no_data;
    public LinearLayout lin_wifi_err;
    public ImageView img_net_err;
    public List<DiseaseName> final_list;
    public boolean item_status=false;
    //加载刷新
    public RefreshLayout refreshView;
    //标题置顶
    public LinearLayout main_tab2;
    public LinearLayout main_tab1;
    public LinearLayout tab_mian;
    public TextView text_main_tab_content;
    public boolean scrollTotop=false;//滑动到顶端标记位
    public int scroll_null=0;
    public GetView getView;
    public interface GetView {
        void getView(LinearLayout linearLayout_one, LinearLayout linearLayout_two);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getView=(GetView)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_main,null);
            initUI();
            initViewPager();
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
            //加载数据操作
            getData(pno);
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
        group=(LinearLayout) view.findViewById(R.id.viewGroup);
        viewpager=(ViewPager) view.findViewById(R.id.view_pager);
        floating_button=(ImageView) view.findViewById(R.id.floating_button);
        lin_main_hos=(RelativeLayout) view.findViewById(R.id.lin_main_hos);
        lin_main_guide=(RelativeLayout) view.findViewById(R.id.lin_main_guide);
        lin_main_message=(RelativeLayout) view.findViewById(R.id.lin_main_message);
        lin_main_yj=(RelativeLayout) view.findViewById(R.id.lin_main_yj);
        lin_main_bg=(RelativeLayout) view.findViewById(R.id.lin_main_bg);
        lin_main_bk=(RelativeLayout) view.findViewById(R.id.lin_main_bk);
        lin_main_dt=(RelativeLayout) view.findViewById(R.id.lin_main_dt);
        lin_main_gj=(RelativeLayout) view.findViewById(R.id.lin_main_gj);
        lin_main_dh=(RelativeLayout) view.findViewById(R.id.lin_main_dh);
        text_msg_num=(TextView) view.findViewById(R.id.text_msg_num);
        text_msg_num.setText("6");
        text_msg_num.setVisibility(View.VISIBLE);
        //加载刷新
        list_view=(ListView) view.findViewById(R.id.list_view);
        scrollView=(MyScroview)view.findViewById(R.id.scrollView);
        pro_main=(LinearLayout)view.findViewById(R.id.pro_main);
        text_no_data=(TextView)view.findViewById(R.id.text_no_data);
        img_net_err=(ImageView) view.findViewById(R.id.img_net_err);
        lin_wifi_err=(LinearLayout) view.findViewById(R.id.lin_wifi_err);
        //推送消息
        String filePath="/sdcard/wizrole/pushMess/";
        String fileName="pushMess.txt";
        String mess=readFileSdcard(filePath+fileName);
        if(!mess.equals("")){//有推送消息
            pushmess=mess.split("-");
            text_msg_num.setVisibility(View.VISIBLE);
            text_msg_num.setText(pushmess.length+"");
        }
        final_list=new ArrayList<DiseaseName>();//初始化列表集合
        //加载刷新
        refreshView=(RefreshLayout)view.findViewById(R.id.xrefresh);
        refreshView.setRefreshHeader(new OrdinaryHeaderView(getActivity()));
        refreshView.setRefreshFooter(new WhiteBgFooterView(getActivity()));
        refreshView.setHeaderHeightPx(200);
        refreshView.setFooterHeightPx(100);
        /**标题制定**/
        main_tab2=(LinearLayout)view.findViewById(R.id.main_tab2);// 在MyScrollView里面的购买布局
        main_tab1=(LinearLayout)view.findViewById(R.id.main_tab1);
        tab_mian=(LinearLayout)view.findViewById(R.id.tab_mian);
        text_main_tab_content=(TextView)view.findViewById(R.id.text_main_tab_content);
        scrollView.setOnScrollListener(new MyScroview.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY >= ((MainActivity)getActivity()).getTopHeight()) {
                    if (tab_mian.getParent() != main_tab1) {
                        main_tab2.removeView(tab_mian);
                        main_tab1.addView(tab_mian);
                        text_main_tab_content.setBackgroundResource(R.color.text_bule);
                        text_main_tab_content.setTextColor(getResources().getColor(R.color.white));
                        //滑动到顶端
                        scrollTotop=true;
                    }
                } else {
                    scrollTotop=false;
                    if (tab_mian.getParent() != main_tab2) {
                        main_tab1.removeView(tab_mian);
                        main_tab2.addView(tab_mian);
                        text_main_tab_content.setBackgroundResource(R.color.white);
                        text_main_tab_content.setTextColor(getResources().getColor(R.color.text_fz));
                    }
                }
                if (scrollY<3000){
                    floating_button.setVisibility(View.INVISIBLE);
                }else{
                    if(scrollY>scroll_null){
                        floating_button.setVisibility(View.INVISIBLE);
                    }else {
                        floating_button.setVisibility(View.VISIBLE);
                    }
                }
                scroll_null=scrollY;
            }
        });
        getView.getView(main_tab1,main_tab2);
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

    public List<HosNews> newses;
    public List<HosNews> newses_sel;
    public void initViewPager(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y111");
            object.put("PageNo",1);
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
                        HNews hNews=new HNews();
                        Gson gson=new Gson();
                        hNews=gson.fromJson(o.toString(),HNews.class);
                        if(hNews.getResultCode().equals("0")){
                            if(hNews.getHosNews().size()>0){
                                newses=hNews.getHosNews();
                                newses_sel=new ArrayList<HosNews>();
                                if(newses.size()>4){
                                    for(int i=0;i<4;i++){//默认加载四条
                                        newses_sel.add(newses.get(i));
                                    }
                                }else{
                                    newses_sel.addAll(newses);
                                }
                                handler.sendEmptyMessage(2);
                            }else{
                                handler.sendEmptyMessage(0);
                            }
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(0);
        }
    }

    public DiseasInfor diseasInfor;
    public List<DiseaseName> diseaseName;
    public int totalNum;
    public int pno=1;
    public MyScroview scrollView;

    public void getData(int pno){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y107");
            object.put("PageNo",pno);
            object.put("DiagnosisName",getPatinentDis());//传入病人诊断getPatinentDis()
            RxJavaOkPotting.getInstance(RxJavaOkPotting.getBase_url()).Ask(RxJavaOkPotting.getBase_url(), object.toString(), new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    handler.sendEmptyMessage(10);
                }

                @Override
                public void onNext(Object o) {
                    if(o.equals(RxJavaOkPotting.NET_ERR)){
                        handler.sendEmptyMessage(10);
                    }else{
                        Gson gson=new Gson();
                        diseasInfor=new DiseasInfor();
                        diseasInfor=gson.fromJson(o.toString(),DiseasInfor.class);
                        if(diseasInfor.getResultCode().equals("0")){
                            handler.sendEmptyMessage(11);
                        }else{
                            handler.sendEmptyMessage(12);
                        }
                    }
                }
            });
        }catch (JSONException e){
            handler.sendEmptyMessage(10);
        }
    }

    public boolean s=false;
    public void setListener(){
        //菜单按钮监听
        lin_main_hos.setOnClickListener(this);
        lin_main_guide.setOnClickListener(this);
        lin_main_message.setOnClickListener(this);
        lin_main_yj.setOnClickListener(this);
        lin_main_bg.setOnClickListener(this);
        lin_main_bk.setOnClickListener(this);
        lin_main_dt.setOnClickListener(this);
        lin_main_gj.setOnClickListener(this);
        lin_main_dh.setOnClickListener(this);
        text_no_data.setOnClickListener(this);
        floating_button.setOnClickListener(this);

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
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item_status=true;
                DiseaseName name=final_list.get(position);
                Intent intent=new Intent(getActivity(), DiseaseDetailInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("infor",name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                final_list=null;
                pno=1;
                getData(pno);
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pno<totalNum){
                    pno++;
                    getData(pno);
                }else{
                    if(status){
                        refreshView.finishLoadmore(false);
                    }else{
                        ToastUtil.MyToast(getActivity(),"已经没有更多数据了");
                        refreshView.finishLoadmoreWithNoMoreData();
                    }
                }
            }
        });
    }
    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_main_hos://医院概况
                intent=new Intent(getActivity(),HosIntrducActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_guide://就医指南
                intent=new Intent(getActivity(),GuideActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_message://住院消息
                if(SharedPreferenceUtil.getLoginState(getActivity())==1){
                    intent=new Intent(getActivity(),HosMessageActivity.class);
                    startActivityForResult(intent, 1000);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_main_yj://押金缴纳
                if(SharedPreferenceUtil.getLoginState(getActivity())==1) {
                    intent = new Intent(getActivity(), DepositActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.MyToast(getActivity(),getString(R.string.no_login));
                }
                break;
            case R.id.lin_main_bg://药品查询
                intent=new Intent(getActivity(),MedsicSerachActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_bk://健康百科
                intent=new Intent(getActivity(), HealWikipediaActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_dt://医院动态
                intent=new Intent(getActivity(), HosdynamicActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_gj://智能导诊
                intent=new Intent(getActivity(),HealToolActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_main_dh://院内导航
                intent=new Intent(getActivity(),HosnavigaActivity.class);
                startActivity(intent);
                break;
            case R.id.text_no_data://列表重试
                lin_wifi_err.setVisibility(View.INVISIBLE);
                pro_main.setVisibility(View.VISIBLE);
                pno=1;
                getData(pno);
                break;
            case R.id.floating_button://回顶部
                scrollView.smoothScrollTo(0,0);
                break;

        }
    }

    public boolean status=false;//第一次进入  请求失败
    public List<Integer> integerList;
    public HealScien_adapter adapter;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://院级新闻请求失败
                    integerList=new ArrayList<Integer>();
                    integerList.add(R.drawable.hos_one);
                    integerList.add(R.drawable.hos_two);
                    setNetErrViewPager(integerList);
                    break;
                case 2://请求成功，有数据
                    setViewPager(newses_sel);
                    break;
                case 1000://消息推送数据条目数量
                    String message=(String)msg.obj;
                    String[] s=message.split("-");
                    text_msg_num.setVisibility(View.VISIBLE);
                    text_msg_num.setText(s.length+"");
                    break;
                case 10://诊断新闻列表，请求失败
                    if(pno==1){
                        status=true;
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        text_no_data.setText("重新加载");
                        text_no_data.setBackgroundResource(R.drawable.login_sel);
                        ImageLoading.common(getActivity(),R.drawable.net_err,img_net_err,R.drawable.net_err);
                        list_view.setVisibility(View.GONE);
                        refreshView.setEnableLoadmore(false);//禁止加载
                        refreshView.setEnableRefresh(true);
                    }else {
                        status=false;
                        refreshView.finishLoadmore(false);
                        refreshView.setEnableLoadmore(true);
                        refreshView.setEnableRefresh(true);
                    }
                    ToastUtil.MyToast(getActivity(),getString(R.string.wifi_err));
                    break;
                case 11://诊断新闻列表--请求成功
                    diseaseName=diseasInfor.getDiseaseName();
                    totalNum=diseasInfor.getTotalNum();
                    status=false;
                    if(diseaseName.size()>0) {
                        if (pno == 1) {
                            refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                            final_list = diseaseName;
                            adapter = new HealScien_adapter(getActivity(), final_list, R.layout.recy_item_healscien);
                            list_view.setAdapter(adapter);
                            refreshView.finishRefresh(true);
                            pro_main.setVisibility(View.INVISIBLE);
                            lin_wifi_err.setVisibility(View.INVISIBLE);
                            list_view.setVisibility(View.VISIBLE);
                        } else {
                            final_list.addAll(diseaseName);
                            adapter.notifyDataSetChanged();
                            refreshView.finishLoadmore(true);
                        }
                        refreshView.setEnableLoadmore(true);
                        refreshView.setEnableRefresh(true);
//                        scrollView.smoothScrollTo(0, 0);
                    }
                    break;
                case 12://诊断新闻列表--无数据
                    if(pno==1){
                        status=true;
                        refreshView.finishRefresh(false);
                        pro_main.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        ImageLoading.common(getActivity(),R.drawable.null_data,img_net_err,R.drawable.null_data);
                        text_no_data.setText("当前暂无数据");
                        text_no_data.setBackgroundResource(R.color.white);
                        list_view.setVisibility(View.GONE);
                    }else {
                        status=true;
                        refreshView.finishLoadmore(false);
                    }
                    refreshView.setEnableLoadmore(false);
                    refreshView.setEnableRefresh(false);
                    ToastUtil.MyToast(getActivity(),"当前暂无数据！");
                    break;
            }
        }
    };

    /******************************以下是viewPager************************************/
    /**
     * 网络连接正常，广告轮播
     * @param list
     */
    public void setViewPager(List<HosNews> list){
        //初始化点 控件
        point=new Point(getActivity(), list.size());
        group.addView(point);
        // 设置Adapter
        HosNewsViewpAdapter hosNewsViewpAdapter =new HosNewsViewpAdapter(getActivity(),list);
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


    public boolean  control_start=true;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1000){
            if(resultCode==2){
                text_msg_num.setVisibility(View.INVISIBLE);
                status=true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public PollingThread thread;
    @Override
    public void onResume() {
        super.onResume();
            String filePath="/sdcard/wizrole/pushMess/";
            String fileName="pushMess.txt";
            String mess=readFileSdcard(filePath+fileName);
            if(!mess.equals("")){//有推送消息
                pushmess=mess.split("-");
                text_msg_num.setVisibility(View.VISIBLE);
                text_msg_num.setText(pushmess.length+"");
            }
        if(status){
            text_msg_num.setVisibility(View.INVISIBLE);
        }

        if(thread==null){
            if(isFirst){
                isFirst = false;
                thread = new PollingThread();
                thread.start();
            }
        }else{
            thread.onResume();
        }
        //如果是不是从item返回后，
//        if(!item_status){
//            //如果不是从item返回，需要滑动到上面去
//            scrollView.smoothScrollTo(0, 0);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(thread!=null){
            thread.onPause();
        }
    }
    public  Object lock;
    public boolean isPause;
    public boolean isStop;
    public boolean isFirst = true;

    /**
     * 推送消息的实时更新ui
     */
    @SuppressLint("HandlerLeak")
    class PollingThread extends Thread {
        public PollingThread() {
            lock = new Object();
            isPause = false;
            isStop = false;
        }

        //暂停
        public void onPause() {
            if (!isPause)
                isPause = true;
        }

        public void onWait() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onResume() {
            synchronized (lock) {
                isPause = false;
                lock.notifyAll();
            }
        }

        public void onStop() {
            //如果线程处于wait状态，那么会唤醒它，但该中断也就被消耗了，无法捕捉到,退出操作会在下一个循环时实现
            //但如果线程处于running状态，那么该中断便可以被捕捉到
            isStop = true;
            this.interrupt();
        }


        @Override
        public void run() {
            super.run();
            while (!isStop) {
                //捕获中断
                if (Thread.interrupted()) {
                    //结束中断
                    if (isStop) {
                        return;
                        //如果中断不是由我们手动发出，那么就不予处理，直接交由更上层处理
                        //不应该生吞活剥
                    } else {
                        this.interrupt();
                        continue;
                    }
                }
                if (isPause) {
                    onWait();
                }
                try {
                Thread.sleep(20000);
                String filePath = "/sdcard/wizrole/pushMess/";
                String fileName = "pushMess.txt";
                String mess = readFileSdcard(filePath + fileName);
                if (!mess.equals("")) {//有推送消息
                    handler.obtainMessage(1000,mess).sendToTarget();
                }
                }catch (InterruptedException e){

                }
            }
        }
    }

    /**
     * 获取病人的诊断
     * @return
     */
    public String getPatinentDis(){
        String dis=null;
        if(SharedPreferenceUtil.getLoginState(getActivity())==1){//已登录
            PersonInfor infor=(PersonInfor)SharedPreferenceUtil.getBean(getActivity());
            dis=infor.getDiagnose();
        }else{//未登录-
            dis="a";
        }
        return dis;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(thread!=null){
            thread.onStop();
        }
    }
}
