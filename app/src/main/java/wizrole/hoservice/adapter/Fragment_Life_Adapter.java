package wizrole.hoservice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment适配adapter
 */
public class Fragment_Life_Adapter extends FragmentPagerAdapter {
    //fragment 集合
    private List<Fragment> mFragmentList = new ArrayList<>();

    public Fragment_Life_Adapter(FragmentManager fm , List<Fragment> list) {
        super(fm);
        this.mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "健康推荐";
            case 1:
                return "商品超市";
            case 2:
                return "餐饮点餐";
            default:
                return "未知";
        }
    }
}
