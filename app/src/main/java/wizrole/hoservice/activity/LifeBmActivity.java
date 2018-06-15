package wizrole.hoservice.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.NewsViewpNetErrAdapter;
import wizrole.hoservice.adapter.StoreListAdapter;
import wizrole.hoservice.adapter.StoreListVPAdapter;
import wizrole.hoservice.adapter.SuperMarketTypeAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.interface_base.Pop_DissListener;
import wizrole.hoservice.life.model.getallstore.bean.MyStoreInforBack;
import wizrole.hoservice.life.model.getallstore.bean.StoreList;
import wizrole.hoservice.life.model.getsecondtypename.GetSecondTypeNameBack;
import wizrole.hoservice.life.model.getsecondtypename.TwoTypeList;
import wizrole.hoservice.life.preserent.getallstore.GetAllStoreInterface;
import wizrole.hoservice.life.preserent.getallstore.GetAllStorePreserent;
import wizrole.hoservice.life.preserent.getsecondstore.GetSecondStoreInterface;
import wizrole.hoservice.life.preserent.getsecondstore.GetSecondStorePreserent;
import wizrole.hoservice.life.preserent.getsecondtypename.GetSecondTypeNameInterface;
import wizrole.hoservice.life.preserent.getsecondtypename.GetSecondTypeNamePreserent;
import wizrole.hoservice.life.view.StoreSearchActivity;
import wizrole.hoservice.life.view.StoreViewActivity;
import wizrole.hoservice.ui.Point;
import wizrole.hoservice.ui.PopupWindowPotting;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.view.LoadingView;
import wizrole.hoservice.view.PopupMaxHeightListView;
import wizrole.hoservice.view.UserFooterView;
import wizrole.hoservice.view.UserHeaderView;

/**
 * Created by liushengping on 2018/1/9/009.
 * 何人执笔？
 */

public class LifeBmActivity extends BaseActivity implements View.OnClickListener,GetAllStoreInterface
        ,StoreListAdapter.OnItemClickListener,GetSecondTypeNameInterface,Pop_DissListener,GetSecondStoreInterface {

    @BindView(R.id.loadView_cener)LoadingView loadView_cener;
    @BindView(R.id.tab1_one)TextView tab1_one;
    @BindView(R.id.list_store)RecyclerView list_store;
    @BindView(R.id.view_pager)ViewPager view_pager;
    @BindView(R.id.viewGroup)LinearLayout group;
    @BindView(R.id.lin_content)LinearLayout lin_content;
    @BindView(R.id.lin_wifi_err)LinearLayout lin_wifi_err;
    @BindView(R.id.img_net_err)ImageView img_net_err;
    @BindView(R.id.img_one_bottom)ImageView img_one_bottom;
    @BindView(R.id.text_err_agagin_center)TextView text_err_agagin_center;
    @BindView(R.id.text_search)TextView text_search;
    @BindView(R.id.xrefresh)RefreshLayout refreshView;
    @BindView(R.id.view_over)View view_over;
    @BindView(R.id.rel_tabl_one)RelativeLayout rel_tabl_one;
    @BindView(R.id.rel_search)RelativeLayout rel_search;
    @BindView(R.id.appBarLayout)AppBarLayout appBarLayout;
    @BindView(R.id.img_back)ImageView img_back;
    public List<String> strings;
    public List<Integer> integers;
    public int pno=1;
    public StoreListAdapter adapter;
    public SuperMarketTypeAdapter type_adapter;//二级分类pop适配器
    public GetAllStorePreserent getAllStorePreserent=new GetAllStorePreserent(this);//获取对应的商家列表
    public GetSecondTypeNamePreserent getSecondTypeNamePreserent= new GetSecondTypeNamePreserent(this);//获取对应商家二级分类店铺
    public GetSecondStorePreserent getSecondStorePreserent=new GetSecondStorePreserent(this);
    public boolean open_close_status=false;//是否弹窗显示 状态
    public boolean coll_status=false;//折叠、展开状态  false--展开
    @Override
    protected int getLayout() {
        return R.layout.activity_lifebm;
    }

    @Override
    protected void initData() {
        setView();
        initStoreType();//二级pop
        setType();
    }

    public void setView(){
        loadView_cener.addBitmap(R.drawable.icon_chicken);
        loadView_cener.addBitmap(R.drawable.icon_flower);
        loadView_cener.addBitmap(R.drawable.icon_orange);
        loadView_cener.addBitmap(R.drawable.icon_ufo);
        loadView_cener.addBitmap(R.drawable.icon_pear);
        loadView_cener.setShadowColor(Color.LTGRAY);
        loadView_cener.setDuration(700);
        loadView_cener.start();
        refreshView.setRefreshHeader(new UserHeaderView(this));
        refreshView.setRefreshFooter(new UserFooterView(this));
//        refreshView.setHeaderHeight(70);
        refreshView.setFooterHeight(40);
        list_store.setLayoutManager(new LinearLayoutManager(this));
        integers=new ArrayList<>();
        integers.add(R.drawable.hos_one);
        integers.add(R.drawable.hos_two);
        setViewPager(strings,integers);
        getData();
    }

    /******************************以下是选择商家类型****************************************************/
    public PopupWindowPotting store_popupWindowPotting;
    public PopupMaxHeightListView pop_list;
    public void initStoreType(){
        if(store_popupWindowPotting==null) {
            store_popupWindowPotting = new PopupWindowPotting(this, 3) {
                @Override
                protected int getLayout() {
                    return R.layout.pop_list_store_type;
                }

                @Override
                protected void initUI() {
                    pop_list =$(R.id.list_pop_departitle);
                    pop_list.setListViewHeight((int)(700));//必须这个高度，否则ListView不会显示
                }

                @Override
                protected void setListener() {
                    pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tab1_one.setText(tabList.get(position).getTwoTypeName());
                            rotateEnd(img_one_bottom);
                            store_popupWindowPotting.Hide();
                            view_over.setVisibility(View.INVISIBLE);
                            open_close_status=false;
                            lin_content.setVisibility(View.INVISIBLE);
                            loadView_cener.setVisibility(View.INVISIBLE);
                            getTypeStore(tabList.get(position).getTwoTypeId(),1);
                            type_id=tabList.get(position).getTwoTypeId();//储存，点击重试需要
                        }
                    });
                }
            };
        }
    }

    /*****************************默认所有的一级店铺列表***********************************/
    public void getData(){
        getAllStorePreserent.getAllStore("月嫂护工","",pno);
    }
    /**************************获取二级分类名称*************************************/
    public void setType(){
        getSecondTypeNamePreserent.GetSecondTypeName("月嫂护工");
    }
    /***************************获取二级分类对应的商家列表*****************************************************/
    public void getTypeStore(String id_type,int page){
        getSecondStorePreserent.getSecondStore(id_type,page);
    }

    /***
     * 获取所有商家列表
     * @param getAllStoreBack
     */
    public  List<StoreList> storeLists;
    public  List<StoreList> all_storeLists=new ArrayList<>();
    public int totalNum;//最大页码
    @Override
    public void getAllStoreSucc(MyStoreInforBack getAllStoreBack) {
        storeLists=getAllStoreBack.getStoreList();
        totalNum=getAllStoreBack.getTotalNum();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getAllStoreFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(1);
        }else{
            handler.sendEmptyMessage(2);
        }
    }
    /*********************以下是获取商家列表的二级差选**********************************/
    public List<TwoTypeList> tabList;
    public String type_id="";//二级分类id
    @Override
    public void getSecondTypeNameSucc(GetSecondTypeNameBack getSecondTypeNameBack) {
        tabList=getSecondTypeNameBack.getTwoTypeList();
        handler.sendEmptyMessage(3);
    }

    @Override
    public void getSecondTypeNameFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(4);
        }else{
            handler.sendEmptyMessage(5);
        }
    }

    /******************************二级分类下对应的商家列表返回************************************************/
    /***
     * 获取所有商家列表
     * @param myStoreInforBack
     */
    @Override
    public void GetSecondStoreSucc(MyStoreInforBack myStoreInforBack) {
        storeLists=myStoreInforBack.getStoreList();
        totalNum=myStoreInforBack.getTotalNum();
        handler.sendEmptyMessage(6);
    }

    @Override
    public void GetSecondStoreFail(String msg) {
        if(msg.equals("")){
            handler.sendEmptyMessage(7);
        }else{
            handler.sendEmptyMessage(8);
        }
    }
    /****************************以下是网络请求数据返回************************************/
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.VISIBLE);
                    lin_wifi_err.setVisibility(View.INVISIBLE);
                    if(pno==1){
                        refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                        all_storeLists.clear();
                        all_storeLists.addAll(storeLists);
                        refreshView.finishRefresh();
                        adapter=new StoreListAdapter(LifeBmActivity.this,all_storeLists,"月嫂护工");
                        list_store.setAdapter(adapter);
                    }else{
                        all_storeLists.addAll(storeLists);
                        adapter.notifyDataSetChanged();
                        refreshView.finishLoadmore();
                    }
                    adapter.setOnItemClickListener(LifeBmActivity.this);
                    break;
                case 1:
                    loadView_cener.setVisibility(View.INVISIBLE);
                    if(pno==1){
                        refreshView.finishRefresh();
                        lin_content.setVisibility(View.INVISIBLE);
                        lin_wifi_err.setVisibility(View.VISIBLE);
                        text_err_agagin_center.setText("重新加载");
                        text_err_agagin_center.setTextColor(getResources().getColor(R.color.white));
                        text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                        ImageLoading.common(LifeBmActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    }else{
                        refreshView.finishLoadmore();
                    }
                    ToastUtil.MyToast(LifeBmActivity.this,"数据获取失败，请检查网络");
                    break;
                case 2://无数据
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(LifeBmActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_err_agagin_center.setText("当前暂无数据");
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.huise));
                    ToastUtil.MyToast(LifeBmActivity.this,"当前暂无数据");
                    break;
                case 3://二级筛选获取成功
                    rel_tabl_one.setEnabled(true);
                    img_one_bottom.setVisibility(View.VISIBLE);
                    tab1_one.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 4://二级分类请求网络无连接
                    tab1_one.setText("当前网络连接失败");
                    tab1_one.setTextColor(getResources().getColor(R.color.red));
                    rel_tabl_one.setEnabled(false);
                    img_one_bottom.setVisibility(View.INVISIBLE);
                    break;
                case 5://二级分类请求无数据
                    tab1_one.setText("所有商家");
                    tab1_one.setTextColor(getResources().getColor(R.color.black));
                    img_one_bottom.setVisibility(View.INVISIBLE);
                    rel_tabl_one.setEnabled(false);
                    break;
                case 6://二级分类名下的商家列表
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.VISIBLE);
                    refreshView.resetNoMoreData();//恢复没有更多数据状态状态
                    all_storeLists.clear();
                    all_storeLists.addAll(storeLists);
                    adapter.notifyDataSetChanged();
                    break;
                case 7://二级分类下请求失败
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    text_err_agagin_center.setText("重新加载");
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.white));
                    text_err_agagin_center.setBackgroundResource(R.drawable.login_sel);
                    ImageLoading.common(LifeBmActivity.this,R.drawable.net_err,img_net_err,R.drawable.net_err);
                    ToastUtil.MyToast(LifeBmActivity.this,"网络请求失败，请检查网络");
                    break;
                case 8://二级分类下无店铺
                    loadView_cener.setVisibility(View.INVISIBLE);
                    lin_content.setVisibility(View.INVISIBLE);
                    lin_wifi_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(LifeBmActivity.this,R.drawable.null_data,img_net_err,R.drawable.null_data);
                    text_err_agagin_center.setText("当前暂无数据");
                    text_err_agagin_center.setTextColor(getResources().getColor(R.color.huise));
                    ToastUtil.MyToast(LifeBmActivity.this,"当前暂无数据");
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        text_err_agagin_center.setOnClickListener(this);
        rel_tabl_one.setOnClickListener(this);
        img_back.setOnClickListener(this);
        rel_search.setOnClickListener(this);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                pno=1;
                getData();
            }
        });
        refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pno<totalNum){
                    pno++;
                    getData();
                }else{
                    ToastShow("已经没有更多数据了");
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayo, int verticalOffset) {
                if(-verticalOffset==appBarLayout.getTotalScrollRange()){//完全折叠
                    coll_status=true;
                }else{
                    coll_status=false;
                }
            }
        });
        view_over.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(open_close_status){//pop显示了
                    view_over.setVisibility(View.INVISIBLE);
                    store_popupWindowPotting.Hide();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back://返回
                finish();
                break;
            case R.id.text_err_agagin_center:
                loadView_cener.setVisibility(View.VISIBLE);
                lin_wifi_err.setVisibility(View.INVISIBLE);
                if(tab1_one.getText().toString().equals("商家类型")){
                    pno=1;
                    getData();
                }else{
                    pno=1;
                    getTypeStore(type_id,pno);
                }
                break;
            case R.id.rel_tabl_one://商家类型筛选
                initStoreType();
                if(type_adapter==null){
                    type_adapter=new SuperMarketTypeAdapter(LifeBmActivity.this,tabList,R.layout.pop_supermarket_type_item);
                    pop_list.setAdapter(type_adapter);
                }
                if (!coll_status){//展开的状态下
                    appBarLayout.setExpanded(false);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            store_popupWindowPotting.Show(rel_tabl_one);
                            view_over.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }else{//折叠状态下
                    store_popupWindowPotting.Show(rel_tabl_one);
                    view_over.setVisibility(View.VISIBLE);
                }
                rotateStart(img_one_bottom);
                open_close_status=true;
                break;
            case R.id.rel_search://搜索
                Intent intent = new Intent(LifeBmActivity.this,StoreSearchActivity.class);
                int location[] = new int[2];
                text_search.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent=new Intent(LifeBmActivity.this, StoreViewActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("storeInfor",all_storeLists.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /****************************以下是viewpager**************************************/
    public Point point;
    /**
     * 网络连接成功  --失败  广告轮播图
     * @param strings---有网络连接
     * @param integers---无网络连接
     */
    public void setViewPager(List<String> strings,List<Integer> integers){
        if(strings!=null){
            //初始化点 控件
            point=new Point(LifeBmActivity.this,strings.size());
            group.addView(point);
            StoreListVPAdapter hosNewsViewpAdapter =new StoreListVPAdapter(LifeBmActivity.this,strings);
            view_pager.setAdapter(hosNewsViewpAdapter);
        }else{
            //初始化点 控件
            point=new Point(LifeBmActivity.this,integers.size());
            group.addView(point);
            NewsViewpNetErrAdapter netErrAdapter=new NewsViewpNetErrAdapter(LifeBmActivity.this,integers);
            view_pager.setAdapter(netErrAdapter);
        }
        view_pager.setOnPageChangeListener(viewPagerChangeListener);
        // 自动切换页面功能
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(4000);
                    handlerVp.sendEmptyMessage(0);
                }
            }
        }).start();
    }
    public Handler handlerVp=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                if(strings==null){
                    if (view_pager.getCurrentItem() ==integers.size()-1) {
                        view_pager.setCurrentItem(0);
                    }else{
                        view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);
                    }
                }else{
                    if (view_pager.getCurrentItem() ==strings.size()-1 ) {
                        view_pager.setCurrentItem(0);
                    }else{
                        view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);
                    }
                }
            }
        }
    };

    public ViewPager.OnPageChangeListener viewPagerChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //根据监听的页面改变当前页对应的小圆点
            point.changePoint(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /*******************pop弹窗******************************/
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
    @Override
    public void onDiss() {
        rotateEnd(img_one_bottom);
        open_close_status=false;
        view_over.setVisibility(View.INVISIBLE);
    }

}
