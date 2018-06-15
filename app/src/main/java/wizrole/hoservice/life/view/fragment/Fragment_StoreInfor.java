package wizrole.hoservice.life.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getallstore.bean.StoreList;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Fragment_StoreInfor extends Fragment {

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public View view;
    public StoreList StoreInfor;
    public ImageView frag_s_logo;
    public TextView frag_s_intrduce,frag_s_address,frag_s_type,frag_s_tel,frag_s_name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_storeinfor,null);
            Bundle bundle = getArguments();
            StoreInfor = (StoreList)bundle.getSerializable("StoreInfor");
            initUI();
            setView();
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
        frag_s_logo=(ImageView)view.findViewById(R.id.frag_s_logo);
        frag_s_intrduce=(TextView)view.findViewById(R.id.frag_s_intrduce);
        frag_s_type=(TextView)view.findViewById(R.id.frag_s_type);
        frag_s_address=(TextView)view.findViewById(R.id.frag_s_address);
        frag_s_tel=(TextView)view.findViewById(R.id.frag_s_tel);
        frag_s_name=(TextView)view.findViewById(R.id.frag_s_name);
    }
    public void setView(){
        ImageLoading.common(getActivity(), Constant.ip+StoreInfor.getStoreLogoPic(),frag_s_logo,R.drawable.img_loadfail);
        frag_s_name.setText(StoreInfor.getStoreName());
        frag_s_address.setText(StoreInfor.getStorePlace());
        frag_s_intrduce.setText("本店成立于1992年，经过25年的艰苦卓越的发展，已经成为以经营正宗北京烤鸭、" +
                "精品京菜为主体，融合各大菜系各种美食为一体的大型中餐连锁餐饮集团。2014年，本店被国家工商总局认定中国驰名商标。");
        frag_s_tel.setText(StoreInfor.getStorePhone());
        frag_s_type.setText(StoreInfor.getStoreType());
    }
    public void setListener(){

    }

}
