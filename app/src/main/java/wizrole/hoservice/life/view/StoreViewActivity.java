package wizrole.hoservice.life.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.interface_base.ChangePage;
import wizrole.hoservice.life.model.getallstore.bean.StoreList;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.view.fragment.Fragment_Goods;
import wizrole.hoservice.life.view.fragment.Fragment_StoreInfor;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by liushengping on 2017/12/15/015.
 * 何人执笔？
 * 店铺商品列表页面
 */
public class StoreViewActivity extends BaseActivity implements View.OnClickListener,Fragment_Goods.AllGoods,Fragment_Goods.Coll{

    @BindView(R.id.text_s_activity) TextView text_s_activity;
    @BindView(R.id.text_s_intrduce) TextView text_s_intrduce;
    @BindView(R.id.text_s_name) TextView text_s_name;
    @BindView(R.id.img_s_logo) ImageView img_s_logo;
    @BindView(R.id.img_logo_bg) ImageView img_logo_bg;
    @BindView(R.id.img_left_back) ImageView img_left_back;
    @BindView(R.id.img_right_more)ImageView img_right_more;
    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.viewpager)ViewPager viewpager;
    @BindView(R.id.lin_all)RelativeLayout lin_all;
    @BindView(R.id.text_s_search)TextView text_s_search;
    @BindView(R.id.appBarLayout)AppBarLayout appBarLayou;
    @BindView(R.id.collapsing_toolbar_layout)CollapsingToolbarLayout mCollapsingToolbarLayout;
    public StoreList StoreInfor;
    @Override
    protected int getLayout() {
        return R.layout.activity_storeview;
    }
    @Override
    protected void initData() {
        ButterKnife.bind(this);
        StoreInfor=(StoreList) getIntent().getSerializableExtra("storeInfor");
        setInfor();
        reflex();
        setView();
    }

    public void setInfor(){
        ImageLoading.commonBlurTeans(StoreViewActivity.this,Constant.ip_temp+StoreInfor.getStoreLogoPic(),img_logo_bg);
        ImageLoading.common(StoreViewActivity.this,Constant.ip_temp+StoreInfor.getStoreLogoPic(),img_s_logo,R.drawable.img_loadfail);
        text_s_name.setText(StoreInfor.getStoreName());
        text_s_intrduce.setText("新店开张，欢迎选购");
        text_s_activity.setText("本店满购100元赠送一份饮料。");
    }
    public boolean s;
    @Override
    protected void setListener() {
        text_s_search.setOnClickListener(this);
        img_right_more.setOnClickListener(this);
        img_left_back.setOnClickListener(this);
        appBarLayou.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset==0){
                    //搜索
                    text_s_search.setAlpha(0.0f);
                    text_s_search.setEnabled(false);
                }else{
                    //搜索
                    text_s_search.setAlpha((float) ((-verticalOffset*1.0)/400));
                    if(text_s_search.getAlpha()>0.9){
                        text_s_search.setEnabled(true);
                    }else{
                        text_s_search.setEnabled(false);
                    }
                }
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(getChangePage()!=null){
                    changePage.changeNum(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_right_more:
                ToastShow("收藏店铺");
                break;
            case R.id.img_left_back:
                finish();
                break;
            case R.id.text_s_search://搜索
                if(commodityList.size()>0){
                    Intent intent = new Intent(StoreViewActivity.this,GoodsSearchActivity.class);
                    int location[] = new int[2];
                    text_s_search.getLocationOnScreen(location);
                    intent.putExtra("x",location[0]);
                    intent.putExtra("y",location[1]);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("list",(Serializable) commodityList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }else{
                    ToastShow("当前商铺暂无商品，不支持搜索");
                }
                break;
        }
    }

    public Bundle bundle;
    public void setView(){
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Fragment_Goods fragment_goods = new Fragment_Goods();
                        fragment_goods.setAllGoods(StoreViewActivity.this);//获取所有的商品，--传递到GoodsActivity中进行search
                        fragment_goods.setColl(StoreViewActivity.this);
                        bundle = new Bundle();
                        bundle.putString("id",StoreInfor.getStoreNo());
                        fragment_goods.setArguments(bundle);
                        return fragment_goods;
                    case 1:
                        Fragment_StoreInfor fragment_storeInfor = new Fragment_StoreInfor();
                        bundle = new Bundle();
                        bundle.putSerializable("StoreInfor",StoreInfor);
                        fragment_storeInfor.setArguments(bundle);
                        return fragment_storeInfor;
                    default:
                        return new Fragment_Goods();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "商品列表";
                    case 1:
                        return "商家信息";
                    default:
                        return "未知";
                }
            }
        });
        viewpager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewpager);
    }

    //设置taLayout的下划线宽度
    public void reflex(){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout,60,60);
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


    public List<CommodityList> commodityList=new ArrayList<>();

    @Override
    public void getAllGoods(List<CommodityList> commodityLists) {
        commodityList=commodityLists;
    }

    /*******************************************/
    public ChangePage changePage;

    public ChangePage getChangePage() {
        return changePage;
    }

    public void setChangePage(ChangePage changePage) {
        this.changePage = changePage;
    }

    @Override
    public void collChange() {
        appBarLayou.setExpanded(false);
    }
}
