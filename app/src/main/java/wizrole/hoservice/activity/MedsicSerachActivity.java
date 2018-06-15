package wizrole.hoservice.activity;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.fragment.Fragment_med;

/**
 * Created by a on 2017/9/18.
 * 药品查询
 */

public class MedsicSerachActivity extends BaseActivity {
    /**返回按钮*/
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.view_med_pager)ViewPager view_med_pager;
    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @Override
    protected int getLayout() {
        return R.layout.activity_medsearch;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText(getString(R.string.med_seach));
        reflex();
        setView();
    }

    @Override
    protected void setListener() {
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setView(){
        view_med_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
//                        Fragment_Goods fragment_goods = new Fragment_Goods();
//                        fragment_goods.setAllGoods(StoreViewActivity.this);//获取所有的商品，--传递到GoodsActivity中进行search
//                        bundle = new Bundle();
//                        bundle.putString("id",StoreInfor.getStoreNo());
//                        fragment_goods.setArguments(bundle);
                        return Fragment_med.newInstance(1,false);
                    case 1:
//                        Fragment_StoreInfor fragment_storeInfor = new Fragment_StoreInfor();
//                        bundle = new Bundle();
//                        bundle.putSerializable("StoreInfor",StoreInfor);
//                        fragment_storeInfor.setArguments(bundle);
                        return Fragment_med.newInstance(2,true);
                    default:
                        return Fragment_med.newInstance(1,false);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "药品查询";
                    case 1:
                        return "非药品查询";
                    default:
                        return "未知";
                }
            }
        });
        view_med_pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(view_med_pager);
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
}
