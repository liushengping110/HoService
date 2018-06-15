package wizrole.hoservice.life.view;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.OrderFragAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.adapter.OrderListAdapter;
import wizrole.hoservice.life.model.orderlist.OrderContent;
import wizrole.hoservice.life.model.orderlist.OrderList;
import wizrole.hoservice.life.view.fragment.Fragment_OrderItem;
import wizrole.hoservice.view.WhiteBgFooterView;
import wizrole.hoservice.view.WhiteBgHeaderView;

/**
 * Created by 何人执笔？ on 2018/4/9.
 * liushengping
 * 全部订单
 */

public class OrderStatusActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.img_search)ImageView img_search;
    @BindView(R.id.rel_msg)RelativeLayout rel_msg;


    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.view_pager)ViewPager view_pager;
    @BindView(R.id.pro_main)ProgressBar pro_main;
    @BindView(R.id.lin_content)LinearLayout lin_content;


    public String type;
    public OrderListAdapter adapter;
    public List<String> tabList=new ArrayList<>();
    @Override
    protected int getLayout() {
        return R.layout.activity_orderstatus;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("type");
        setView();
    }


    public List<Fragment> fragments;
    public void  setView(){
        tabList.add("全部");
        tabList.add("待付款");
        tabList.add("待发货");
        tabList.add("待收货");
        tabList.add("待评价");
        tabList.add("已完成");
        fragments=new ArrayList<Fragment>();
        for(int i=0;i<6;i++){
            if(i==0){
                fragments.add(Fragment_OrderItem.newInstance(1,false));
            }else{
                fragments.add(Fragment_OrderItem.newInstance(i+1,true));
            }
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        OrderFragAdapter adapter=new OrderFragAdapter(tabList,fragments,supportFragmentManager);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(view_pager);
        pro_main.setVisibility(View.INVISIBLE);
        lin_content.setVisibility(View.VISIBLE);
        switch (type){
            case "0":
                view_pager.setCurrentItem(0);
                break;
            case "1":
                view_pager.setCurrentItem(1);
                break;
            case "2":
                view_pager.setCurrentItem(2);
                break;
            case "3":
                view_pager.setCurrentItem(3);
                break;
            case "4":
                view_pager.setCurrentItem(4);
                break;
            case "5":
                view_pager.setCurrentItem(5);
                break;
        }
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }
}
