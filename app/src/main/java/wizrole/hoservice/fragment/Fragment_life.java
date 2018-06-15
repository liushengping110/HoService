package wizrole.hoservice.fragment;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import wizrole.hoservice.R;
import wizrole.hoservice.life.view.OrderStatusActivity;
import wizrole.hoservice.life.view.SuperMaketActivity;
import wizrole.hoservice.adapter.LifeNewsViewpAdapter;
import wizrole.hoservice.adapter.NewsViewpNetErrAdapter;
import wizrole.hoservice.beam.HNews;
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.ui.Point;
import wizrole.hoservice.util.RxJavaOkPotting;


/**
 * Created by a on 2017/8/5.
 * 生活服务  超市 餐饮
 */

public class Fragment_life extends Fragment implements View.OnClickListener{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    private View view;
    public LinearLayout lin_all_order,lin_dfk,lin_dfh,lin_dsh,lin_dpj,lin_ywc;
    public RelativeLayout lin_life_yl;
    public RelativeLayout lin_life_xh;
    public RelativeLayout lin_life_cx;
    public RelativeLayout lin_life_sg;
    public RelativeLayout lin_life_cs, lin_life_dc, lin_life_hg;
    public Point point;
    private boolean cunhuan=true;//自动播放ViewPager判断调条件
    public boolean  control_start=true;
    public ViewPager viewpager;//ViewPager控件
    public  LinearLayout group;//图片轮播点
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_life , null);
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
            //加载数据操作
            initViewPager();
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
        lin_life_cs=(RelativeLayout)view.findViewById(R.id.lin_life_cs);
        lin_life_dc=(RelativeLayout)view.findViewById(R.id.lin_life_dc);
        lin_life_hg=(RelativeLayout)view.findViewById(R.id.lin_life_hg);
        lin_life_yl=(RelativeLayout)view.findViewById(R.id.lin_life_yl);
        lin_life_xh=(RelativeLayout)view.findViewById(R.id.lin_life_xh);
        lin_life_cx=(RelativeLayout)view.findViewById(R.id.lin_life_cx);
        lin_life_sg=(RelativeLayout)view.findViewById(R.id.lin_life_sg);
        lin_all_order=(LinearLayout)view.findViewById(R.id.lin_all_order);
        lin_dfh=(LinearLayout)view.findViewById(R.id.lin_dfh);
        lin_dfk=(LinearLayout)view.findViewById(R.id.lin_dfk);
        lin_dsh=(LinearLayout)view.findViewById(R.id.lin_dsh);
        lin_dpj=(LinearLayout)view.findViewById(R.id.lin_dpj);
        lin_ywc=(LinearLayout)view.findViewById(R.id.lin_ywc);

        viewpager = (ViewPager)view .findViewById(R.id.view_pager);
        group=(LinearLayout) view.findViewById(R.id.viewGroup);
    }

    public List<HosNews> newses;
    public List<HosNews> newses_sel;
    public void initViewPager(){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y134");
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

    public List<Integer> integerList;
    public Handler handler_viewpager=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){//院级新闻请求失败
                integerList=new ArrayList<Integer>();
                integerList.add(R.drawable.life_one);
                integerList.add(R.drawable.life_two);
                setNetErrViewPager(integerList);
            }else if(msg.what==2){//请求成功，有数据
                setViewPager(newses_sel);
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
        LifeNewsViewpAdapter hosNewsViewpAdapter =new LifeNewsViewpAdapter(getActivity(),list);
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



    public void setListener(){
        lin_life_cs.setOnClickListener(this);
        lin_life_dc.setOnClickListener(this);
        lin_life_yl.setOnClickListener(this);
        lin_life_xh.setOnClickListener(this);
        lin_life_cx.setOnClickListener(this);
        lin_life_sg.setOnClickListener(this);
        lin_life_hg.setOnClickListener(this);
        lin_all_order.setOnClickListener(this);
        lin_dfh.setOnClickListener(this);
        lin_dfk.setOnClickListener(this);
        lin_dsh.setOnClickListener(this);
        lin_dpj.setOnClickListener(this);
        lin_ywc.setOnClickListener(this);

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
    }

    public Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_all_order:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","0");
                startActivity(intent);
                break;
            case R.id.lin_dfk:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.lin_dfh:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.lin_dsh:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
                break;
            case R.id.lin_dpj:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","4");
                startActivity(intent);
                break;
            case R.id.lin_ywc:
                intent=new Intent(getActivity(),OrderStatusActivity.class);
                intent.putExtra("type","5");
                startActivity(intent);
                break;
            case R.id.lin_life_cs://超市
                intent=new Intent(getActivity(), SuperMaketActivity.class);
                intent.putExtra("storeType","1");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_dc://餐饮
                intent=new Intent(getActivity(), SuperMaketActivity.class);
                intent.putExtra("storeType","2");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_yl://甜品饮料
                intent=new Intent(getActivity(),SuperMaketActivity.class);
                intent.putExtra("storeType","3");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_sg://水果生鲜
                intent=new Intent(getActivity(),SuperMaketActivity.class);
                intent.putExtra("storeType","4");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_xh://鲜花花卉
                intent=new Intent(getActivity(),SuperMaketActivity.class);
                intent.putExtra("storeType","5");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_hg://护工
                intent=new Intent(getActivity(), SuperMaketActivity.class);
                intent.putExtra("storeType","6");
                getActivity().startActivity(intent);
                break;
            case R.id.lin_life_cx://出院出行
                intent=new Intent(getActivity(),SuperMaketActivity.class);
                intent.putExtra("storeType","7");
                getActivity().startActivity(intent);
                break;
        }
    }
}
