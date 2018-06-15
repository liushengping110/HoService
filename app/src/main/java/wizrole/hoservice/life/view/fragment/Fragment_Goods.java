package wizrole.hoservice.life.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.FoodDetailAdapter;
import wizrole.hoservice.adapter.FoodTypeAdapter;
import wizrole.hoservice.adapter.ListPopAdapter;
import wizrole.hoservice.base.MyApplication;
import wizrole.hoservice.interface_base.ChangePage;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.model.getgoodtype.GoodsTypeBack;
import wizrole.hoservice.life.model.getgoodtype.StoreCommodityType;
import wizrole.hoservice.life.preserent.getgoodtype.GoodsTypeInterface;
import wizrole.hoservice.life.preserent.getgoodtype.GoodsTypePreserent;
import wizrole.hoservice.life.view.SingleGoodsInforActivity;
import wizrole.hoservice.life.view.StoreViewActivity;
import wizrole.hoservice.life.view.SureOrderActivity;
import wizrole.hoservice.util.DensityUtil;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.ToastUtil;
import wizrole.hoservice.util.dialog.LoadingDailog;
import wizrole.hoservice.view.PopupMaxHeightListView;


/**
 * Created by Administrator on 2018/1/24.
 */

public class Fragment_Goods extends Fragment implements GoodsTypeInterface,
        View.OnClickListener,FoodTypeAdapter.OnItemClickListener,
        FoodDetailAdapter.DetailAddOnClick, FoodDetailAdapter.detailSubOnClick,
        FoodDetailAdapter.DetailOnItemClick, ListPopAdapter.PopAddOnClick,
        ListPopAdapter.PopSubOnClick,ChangePage{

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    public String id;
    public Dialog dialog;
    public NestedScrollView nest_scrollview;
    public View view, inc_recyview;
    public ImageView img_net_err;
    public RecyclerView recyclerviewCategory;    //左侧选中的item  view   设置背景
    public RecyclerView recyclerviewDetail;
    public TextView text_err_msg;
    public TextView text_err_agagin;
    public View view_bottom;
    public List<StoreCommodityType> foodTypeList;
    public List<CommodityList> foodDetailList;
    private FoodTypeAdapter foodTypeAdapter;
    /**
     * 种类适配器
     */
    private FoodDetailAdapter foodDetailAdapter;
    /**
     * 细节适配器
     */
    private int oldSelectedPosition = 0;
    private LinearLayoutManager mDetailLayoutManager;
    private LinearLayoutManager mCategoryLayoutManager;
    public List<CommodityList> selectAll = new ArrayList<CommodityList>();  //用来储存选中的菜单
    public GoodsTypePreserent goodsTypePreserent = new GoodsTypePreserent(this);//获取商品列表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_storeview, null);
            Bundle bundle = getArguments();
            id = bundle.getString("id");
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
        if (!isLoadData) {
            //加载数据操作
            getInfor(id);//获取商品
            showBottom();
            isLoadData = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }


    public void initUI() {
        recyclerviewCategory = (RecyclerView) view.findViewById(R.id.recyclerview_category);
        recyclerviewDetail = (RecyclerView) view.findViewById(R.id.recyclerview_detail);
        text_err_msg = (TextView) view.findViewById(R.id.text_err_msg);
        img_net_err = (ImageView) view.findViewById(R.id.img_net_err);
        text_err_agagin = (TextView) view.findViewById(R.id.text_err_agagin);
        inc_recyview = (View) view.findViewById(R.id.inc_recyview);
        view_bottom = (View) view.findViewById(R.id.view_bottom);
        nest_scrollview = (NestedScrollView) view.findViewById(R.id.nest_scrollview);
        //传递接口
        ((StoreViewActivity)getActivity()).setChangePage(this);
    }

    public void getInfor(String No) {
        dialog = LoadingDailog.createLoadingDialog(getActivity(), "加载中");
        goodsTypePreserent.getGoodsType(No);
    }

    public GoodsTypeBack back;

    /**
     * 获取所有商品信息
     * @param goodsTypeBack
     */
    @Override
    public void getGoodsTypeSucc(GoodsTypeBack goodsTypeBack) {
        back = goodsTypeBack;
        if (goodsTypeBack.getResultCode().equals("0")) {
            List<StoreCommodityType> foodTypeList = goodsTypeBack.getStoreCommodityType();
            getFoodDetailList(foodTypeList);    //添加到细节集合
            handler.sendEmptyMessage(0);
        } else if (goodsTypeBack.getResultCode().equals("2")) {
            List<StoreCommodityType> foodTypeList = goodsTypeBack.getStoreCommodityType();
            getFoodDetailList(foodTypeList);    //添加到细节集合
            handler.sendEmptyMessage(2);//有分类-无商品
        } else {//无数据-分类都无
            handler.sendEmptyMessage(1);
        }

    }

    @Override
    public void getGoodsTypeFail(String msg) {
        handler.sendEmptyMessage(3);
    }

    /**
     * 把所有类别的商品都添加到一个集合中
     *
     * @param foodTypeList
     */
    public void getFoodDetailList(List<StoreCommodityType> foodTypeList) {
        foodDetailList = new ArrayList<CommodityList>();
        this.foodTypeList = foodTypeList;
        for (int i = 0; i < foodTypeList.size(); i++) {
            if (foodTypeList != null) {
                foodDetailList.addAll(foodTypeList.get(i).getCommodityList());
            }
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://成功
                    inc_recyview.setVisibility(View.VISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setVisibility(View.INVISIBLE);
                    img_net_err.setVisibility(View.INVISIBLE);
                    nest_scrollview.setVisibility(View.INVISIBLE);
                    initViews();        //设置数据
                    LoadingDailog.closeDialog(dialog);
                    if (allGoods != null) {
                        allGoods.getAllGoods(foodDetailList);
                    }
                    break;
                case 1://失败-都无数据
                    inc_recyview.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                    text_err_msg.setBackgroundResource(R.color.white);
                    text_err_msg.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(), R.drawable.null_data, img_net_err, R.drawable.img_loadfail);
                    img_net_err.setVisibility(View.VISIBLE);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.data_numm), Toast.LENGTH_LONG).show();
                    break;
                case 2://无数据--有分类
                    initViews();        //设置数据
                    recyclerviewDetail.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.INVISIBLE);
                    text_err_msg.setText(MyApplication.getContextObject().getString(R.string.data_numm));
                    text_err_msg.setBackgroundResource(R.color.white);
                    text_err_msg.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(), R.drawable.null_data, img_net_err, R.drawable.img_loadfail);
                    img_net_err.setVisibility(View.VISIBLE);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.data_numm), Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    inc_recyview.setVisibility(View.INVISIBLE);
                    text_err_agagin.setVisibility(View.VISIBLE);
                    text_err_msg.setText(MyApplication.getContextObject().getString(R.string.wifi_err));
                    text_err_msg.setBackgroundResource(R.drawable.login_sel);
                    text_err_msg.setVisibility(View.VISIBLE);
                    img_net_err.setVisibility(View.VISIBLE);
                    ImageLoading.common(getActivity(), R.drawable.net_err, img_net_err, R.drawable.img_loadfail);
                    nest_scrollview.setVisibility(View.VISIBLE);
                    LoadingDailog.closeDialog(dialog);
                    Toast.makeText(MyApplication.getContextObject(), MyApplication.getContextObject().getString(R.string.wifi_err), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    public void setListener() {
        text_err_agagin.setOnClickListener(this);
    }

    public void setColl(Coll coll) {
        this.coll = coll;
    }

    public Coll coll;
    public interface Coll{
        void collChange();
    }

    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.text_err_agagin://重试
                text_err_agagin.setVisibility(View.INVISIBLE);
                text_err_msg.setVisibility(View.INVISIBLE);
                img_net_err.setVisibility(View.INVISIBLE);
                getInfor(id);//获取商品
                break;
        }
    }


    /***************************以下是设置数据列表显示***************************/
    private void initViews() {
        mDetailLayoutManager = new LinearLayoutManager(getActivity());
        mCategoryLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewCategory.setLayoutManager(mCategoryLayoutManager);
        recyclerviewDetail.setLayoutManager(mDetailLayoutManager);
        /**设置种类适配器*/
        foodTypeAdapter = new FoodTypeAdapter(getActivity(), foodTypeList);
        foodTypeAdapter.setOnItemClickListener(this);
        recyclerviewCategory.setAdapter(foodTypeAdapter);
        /**设置细节适配器*/
        foodDetailAdapter = new FoodDetailAdapter(getActivity(), foodTypeList);
        recyclerviewDetail.setAdapter(foodDetailAdapter);
        foodDetailAdapter.setOnItemClickListener(this);
        foodDetailAdapter.setOnItemClickListener(this);
        foodDetailAdapter.setDetailAddOnClick(this);
        foodDetailAdapter.setDetailSubOnClick(this);
        // 添加标题
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(foodDetailAdapter);
        recyclerviewDetail.addItemDecoration(headersDecor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerviewDetail.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //第一个完全显示的item和最后一个item。
                    int firstVisibleItem = mDetailLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastVisibleItem = mDetailLayoutManager.findLastVisibleItemPosition();
                    //此判断，避免左侧点击最后一个item无响应
                    if (lastVisibleItem != mDetailLayoutManager.getItemCount() - 1) {
                        int sort = foodDetailAdapter.getSortType(firstVisibleItem);
                        changeSelected(sort);
                    } else {
                        changeSelected(foodTypeAdapter.getItemCount() - 1);
                    }
                    if (needMove) {
                        needMove = false;
                        //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                        int n = movePosition - mDetailLayoutManager.findFirstVisibleItemPosition();
                        if (0 <= n && n < recyclerviewDetail.getChildCount()) {
                            //获取要置顶的项顶部离RecyclerView顶部的距离
                            int top = recyclerviewDetail.getChildAt(n).getTop() - dip2px(28);
                            //最后的移动
                            recyclerviewDetail.scrollBy(0, top);
                        }
                    }
                }
            });
        } else {
            recyclerviewDetail.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //第一个完全显示的item和最后一个item。
                    int firstVisibleItem = mDetailLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastVisibleItem = mDetailLayoutManager.findLastVisibleItemPosition();
                    //此判断，避免左侧点击最后一个item无响应
                    if (lastVisibleItem != mDetailLayoutManager.getItemCount() - 1) {
                        int sort = foodDetailAdapter.getSortType(firstVisibleItem);
                        changeSelected(sort);
                    } else {
                        changeSelected(foodTypeAdapter.getItemCount() - 1);
                    }
                    if (needMove) {
                        needMove = false;
                        //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                        int n = movePosition - mDetailLayoutManager.findFirstVisibleItemPosition();
                        if (0 <= n && n < recyclerviewDetail.getChildCount()) {
                            //获取要置顶的项顶部离RecyclerView顶部的距离
                            int top = recyclerviewDetail.getChildAt(n).getTop() - dip2px(28);
                            //最后的移动
                            recyclerviewDetail.scrollBy(0, top);
                        }
                    }
                }
            });
        }
    }
    /************************************以下是左右两侧列表点击、增加、减少监听*************************************/
    /*** 接口实现---左侧FoodType的点击*/
    @Override
    public void onItemClick(int position) {
        changeSelected(position);
        moveToThisSortFirstItem(position);
    }

    /*****接口实现---右侧item点击******/
    @Override
    public void detailOnItemClick(View view, int position) {
        foodDetailAdapter.notifyDataSetChanged();
        CommodityList detail = foodDetailList.get(position);
        Intent intent = new Intent(getActivity(), SingleGoodsInforActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("singlegoods",detail);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //左侧列表显示数量
    public int typeNum = 0;
    public int detaAll = 0;
    public int typeAll = 0;
    public int superAll = 0;
    public double allPrice = 0;//选中商品总价格
    //刷新适配器--显示减少按钮。文本显示数量
    @Override
    public void detailAddOnClick(int position) {
        //更新右侧列表ui中的数量
        foodDetailList.get(position).setYiOrder(foodDetailList.get(position).getYiOrder() + 1);
        foodDetailAdapter.notifyDataSetChanged();
        //确定选中菜单
        if (selectAll.size() > 0) {
            boolean isSame = false;
            for (int a = 0; a < selectAll.size(); a++) {
                //判断点击的是同一个item
                if ((foodDetailList.get(position).getCommodityName()).equals(selectAll.get(a).getCommodityName())) {
                    selectAll.get(a).setYiOrder(foodDetailList.get(position).getYiOrder());//直接设置数量
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {    //添加一个对象
                selectAll.add(foodDetailList.get(position));
            }
        } else {  //当选中集合为空时----直接添加
            selectAll.add(foodDetailList.get(position));
        }
        //更新左侧列表的ui中的数量
        for (int i = 0; i < foodTypeList.size(); i++) {//该循环目的为找出点击的右边集合在左边集合中的哪一个分类
            if (foodDetailList.get(position).getListNo() == foodTypeList.get(i).getListNo()) {//判断在同一分类里的数量想加
                //把同一分类下的集合中的点餐数量相加
                List<CommodityList> list = foodTypeList.get(i).getCommodityList(); //取出同一类别下的集合
                for (int c = 0; c < list.size(); c++) {
                    typeNum += list.get(c).getYiOrder();                //把同一集合下的已点数量想加
                }
                foodTypeList.get(i).setSelectNum(typeNum);
                typeNum = 0;
                foodTypeAdapter.notifyDataSetChanged();
            }
        }
        //显示底部购物车数量
        for (int r = 0; r < foodTypeList.size(); r++) {
            superAll += foodTypeList.get(r).getSelectNum();
        }
        //显示购物车底部总价格
        for (int u = 0; u < foodDetailList.size(); u++) {
            if (foodDetailList.get(u).getYiOrder() != 0) {
                allPrice += (foodDetailList.get(u).getYiOrder()) * Double.parseDouble(foodDetailList.get(u).getCommodityAmt());
            }
        }
        img_super_car.setImageResource(R.drawable.super_car);
        text_all_num.setVisibility(View.VISIBLE);
        text_all_num.setText(superAll + "");
        text_all_price.setText(allPrice + "");
        text_goadd.setBackgroundResource(R.color.text_bule);
        superAll = 0;
        allPrice = 0;
    }

    //减少
    @Override
    public void detailSubOnClick(int position) {
        //点击一下  数量减一
        foodDetailList.get(position).setYiOrder(foodDetailList.get(position).getYiOrder() - 1);
        foodDetailAdapter.notifyDataSetChanged();
        //确定选中菜单
        for (int a = 0; a < selectAll.size(); a++) {
            //判断点击的是同一个item
            if ((foodDetailList.get(position).getCommodityName()).equals(selectAll.get(a).getCommodityName())) {
                selectAll.get(a).setYiOrder(foodDetailList.get(position).getYiOrder());//直接设置数量
                break;
            }
        }
        if (foodDetailList.get(position).getYiOrder() == 0) {  //如果减到0时
            selectAll.remove(foodDetailList.get(position));
        }
        //更新左侧列表的ui中的数量
        for (int i = 0; i < foodTypeList.size(); i++) {//该循环目的为找出点击的右边集合在左边集合中的哪一个分类
            if (foodDetailList.get(position).getListNo() == foodTypeList.get(i).getListNo()) {//判断在同一分类里的数量想加
                //把同一分类下的集合中的点餐数量相加
                List<CommodityList> list = foodTypeList.get(i).getCommodityList(); //取出同一类别下的集合
                for (int c = 0; c < list.size(); c++) {
                    typeNum += list.get(c).getYiOrder();                //把同一集合下的已点数量想加
                }
                foodTypeList.get(i).setSelectNum(typeNum);
                typeNum = 0;
                foodTypeAdapter.notifyDataSetChanged();
            }
        }
        //显示隐藏购物车布局
        for (int f = 0; f < foodDetailList.size(); f++) {
            detaAll += foodDetailList.get(f).getYiOrder();
        }
        for (int h = 0; h < foodTypeList.size(); h++) {
            typeAll += foodTypeList.get(h).getSelectNum();
        }
        //显示购物车底部总价格
        for (int u = 0; u < foodDetailList.size(); u++) {
            if (foodDetailList.get(u).getYiOrder() != 0) {
                allPrice += (foodDetailList.get(u).getYiOrder()) * Double.parseDouble(foodDetailList.get(u).getCommodityAmt());
            }
        }
        text_all_num.setText(typeAll + "");
        text_all_price.setText(allPrice + "");    //显示总价
        if (typeAll == 0 && detaAll == 0) {     //当所点餐数量等于0 更改界面布局
            ImageLoading.common(MyApplication.getContextObject(),R.drawable.super_no_sel,img_super_car,R.drawable.super_no_sel);
            text_all_num.setVisibility(View.INVISIBLE);
            text_goadd.setBackgroundResource(R.color.text_fz);
            text_all_price.setText(getString(R.string.no_sel_baby));
        }
        detaAll = 0;  //置空
        typeAll = 0;
        allPrice = 0;
    }

    /**************************************以下是购物车展示************************************/
    /***展示全部订单详情*/
    public PopupMaxHeightListView list_pop_all;//列表控件
    public PopupWindow popupWindow;
    public LinearLayout lin_all_del;//清空
    public ListPopAdapter listPopAdapter;
    public View v_car;
    public TextView text_goadd_c;
    public void showPopupWindow(final List<CommodityList> select) {
        v_car= LayoutInflater.from(getActivity()).inflate(R.layout.pop_listview, null);
        list_pop_all = (PopupMaxHeightListView) v_car.findViewById(R.id.list_pop_all);
        // 设置 PopupWindow 最大高度，如果再高，改成滑动
        list_pop_all.setListViewHeight((int) (1200));
        lin_all_del = (LinearLayout) v_car.findViewById(R.id.lin_all_del);
        text_goadd_c = (TextView) v_car.findViewById(R.id.text_goadd);
        lin_all_del.setOnClickListener(new View.OnClickListener() {//购物车清空
            @Override
            public void onClick(View v) {
                clearCar();
            }
        });
        text_goadd_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAll.size() > 0) {
                    Intent intent = new Intent(getActivity(), SureOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectAll", (Serializable) selectAll);
                    intent.putExtras(bundle);
                    intent.putExtra("text_all_price",text_all_price.getText().toString());
                    startActivity(intent);
                } else {
                    ToastUtil.MyToast(getContext(), getString(R.string.sel_to_buy_baby));
                }
            }
        });
        popupWindow = new PopupWindow(v_car,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
//                popupWindow.setAnimationStyle(R.style.PopupAnimation);
                setWindowAlpha(1.0f);
            }
        });
        listPopAdapter = new ListPopAdapter(getActivity(), select);
        list_pop_all.setAdapter(listPopAdapter);
        listPopAdapter.setPopAddOnClick(this);   //增加 减少按钮
        listPopAdapter.setPopSubOnClick(this);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        setWindowAlpha(0.7f);
        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
    }
    /**************************以下是购物车增减监听**********************************/
    /**
     * 弹窗popupWindow中的增加和减少监听
     * @param position
     */
    @Override
    public void popAddOnClick(int position) {
        selectAll.get(position).setYiOrder(selectAll.get(position).getYiOrder() + 1);
        listPopAdapter.notifyDataSetChanged();
        //通知Activity刷新视图
        for (int q = 0; q < foodDetailList.size(); q++) {
            if ((foodDetailList.get(q).getCommodityName()).equals(selectAll.get(position).getCommodityName())) {  //查找相同的对象
                foodDetailList.get(q).setYiOrder(selectAll.get(position).getYiOrder());
                break;
            }
        }
        foodDetailAdapter.notifyDataSetChanged();   //刷新activity视图
        //更新左侧列表的ui中的数量
        for (int i = 0; i < foodTypeList.size(); i++) {//该循环目的为找出点击的右边集合在左边集合中的哪一个分类
            if (selectAll.get(position).getListNo() == foodTypeList.get(i).getListNo()) {//判断在同一分类里的数量想加
                //把同一分类下的集合中的点餐数量相加
                List<CommodityList> list = foodTypeList.get(i).getCommodityList(); //取出同一类别下的集合
                for (int c = 0; c < list.size(); c++) {
                    typeNum += list.get(c).getYiOrder();                //把同一集合下的已点数量想加
                }
                foodTypeList.get(i).setSelectNum(typeNum);
                typeNum = 0;
                foodTypeAdapter.notifyDataSetChanged();
            }
        }
        //显示底部购物车数量
        for (int r = 0; r < foodTypeList.size(); r++) {
            superAll += foodTypeList.get(r).getSelectNum();
        }
        //显示购物车底部总价格
        for (int u = 0; u < foodDetailList.size(); u++) {
            if (foodDetailList.get(u).getYiOrder() != 0) {
                allPrice += (foodDetailList.get(u).getYiOrder()) * Double.parseDouble(foodDetailList.get(u).getCommodityAmt());
            }
        }
        ImageLoading.common(MyApplication.getContextObject(),R.drawable.super_car,img_super_car,R.drawable.super_car);
        text_all_num.setVisibility(View.VISIBLE);
        text_all_num.setText(superAll + "");
        text_all_price.setText(allPrice + "");
        text_goadd.setBackgroundResource(R.color.text_bule);
        superAll = 0;
        allPrice = 0;
    }

    public String name;
    public int num;
    public int no;

    /**
     * 弹窗减少
     *
     * @param position
     */
    @Override
    public void popSubOnClick(int position) {
        /******每次都执行******/
        if (selectAll.get(position).getYiOrder() > 1) { //当订单数量大于1时
            selectAll.get(position).setYiOrder(selectAll.get(position).getYiOrder() - 1);
            name = selectAll.get(position).getCommodityName();
            num = selectAll.get(position).getYiOrder();
            no = selectAll.get(position).getListNo();
        } else {  //等于1的时候，就需要减一  并且移除这个对象
            selectAll.get(position).setYiOrder(selectAll.get(position).getYiOrder() - 1);
            name = selectAll.get(position).getCommodityName();
            num = selectAll.get(position).getYiOrder();
            no = selectAll.get(position).getListNo();
            selectAll.remove(selectAll.get(position));
            /*****仅当选中集合元素为1--点餐数量属性为1时才执行--需要清除选中集合--隐藏popupwindow********/
            if (selectAll.size() == 0) {
                selectAll.clear();
                popupWindow_bottom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                popupWindow.dismiss();
            }
        }
        listPopAdapter.notifyDataSetChanged();

        /*************联动activity刷新视图**********************/
        for (int i = 0; i < foodDetailList.size(); i++) {   //查找点击对应数据源的数据
            if ((foodDetailList.get(i).getCommodityName()).equals(name)) {
                foodDetailList.get(i).setYiOrder(num);
                break;
            }
        }
        foodDetailAdapter.notifyDataSetChanged();   //刷新activity视图
        //更新左侧列表的ui中的数量
        for (int i = 0; i < foodTypeList.size(); i++) {//该循环目的为找出点击的右边集合在左边集合中的哪一个分类
            if (no == foodTypeList.get(i).getListNo()) {//判断在同一分类里的数量想加
                //把同一分类下的集合中的点餐数量相加
                List<CommodityList> list = foodTypeList.get(i).getCommodityList(); //取出同一类别下的集合
                for (int c = 0; c < list.size(); c++) {
                    typeNum += list.get(c).getYiOrder();                //把同一集合下的已点数量想加
                }
                foodTypeList.get(i).setSelectNum(typeNum);
                typeNum = 0;
                foodTypeAdapter.notifyDataSetChanged();
            }
        }
        //显示隐藏购物车布局
        for (int f = 0; f < foodDetailList.size(); f++) {
            detaAll += foodDetailList.get(f).getYiOrder();
        }
        for (int h = 0; h < foodTypeList.size(); h++) {
            typeAll += foodTypeList.get(h).getSelectNum();
        }
        //显示购物车底部总价格
        for (int u = 0; u < foodDetailList.size(); u++) {
            if (foodDetailList.get(u).getYiOrder() != 0) {
                allPrice += (foodDetailList.get(u).getYiOrder()) * Double.parseDouble(foodDetailList.get(u).getCommodityAmt());
            }
        }
        text_all_num.setText(typeAll + "");
        text_all_price.setText(allPrice + "");    //显示总价
        if (typeAll == 0 && detaAll == 0) {     //当所点餐数量等于0 更改界面布局
            img_super_car.setImageResource(R.drawable.super_no_sel);
            text_all_num.setVisibility(View.INVISIBLE);
            text_goadd.setBackgroundResource(R.color.text_fz);
            text_all_price.setText(getString(R.string.no_sel_baby));
        }
        detaAll = 0;  //置空
        typeAll = 0;
        allPrice = 0;
    }


    /************************************以下是两列listView联动**************************************/
    private boolean needMove = false;
    private int movePosition;

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mDetailLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mDetailLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerviewDetail.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerviewDetail.getChildAt(n - firstItem).getTop();
            recyclerviewDetail.scrollBy(0, top - dip2px(28));
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerviewDetail.scrollToPosition(n);
            movePosition = n;
            needMove = true;
        }
    }

    /***以下方法实现左右两侧联动**/
    private void moveToThisSortFirstItem(int position) {
        movePosition = 0;
        for (int i = 0; i < position; i++) {
            movePosition += foodDetailAdapter.getFoodTypeList().get(i).getCommodityList().size();
        }
        moveToPosition(movePosition);
    }

    private void changeSelected(int position) {
        foodTypeList.get(oldSelectedPosition).setSelected(false);
        foodTypeList.get(position).setSelected(true);
        //增加左侧联动
        recyclerviewCategory.scrollToPosition(position);
        oldSelectedPosition = position;
        foodTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置窗体透明度
     *
     * @param alpha
     * 透明度值
     */
    private WindowManager.LayoutParams lp;

    private void setWindowAlpha(float alpha) {
        lp = getActivity().getWindow().getAttributes();
        // 重新设定窗体透明度
        lp.alpha = alpha;
        // 重新设定窗体布局参数
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 根据手机分辨率从dp转成px
     */
    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public AllGoods allGoods;
    public interface AllGoods {
        void getAllGoods(List<CommodityList> commodityLists);
    }
    public AllGoods getAllGoods() {
        return allGoods;
    }
    public void setAllGoods(AllGoods allGoods) {
        this.allGoods = allGoods;
    }

    /**************************************以下是底部************************************/
    private ImageView img_super_car;
    private TextView text_goadd;
    private TextView text_all_price;
    public TextView text_all_num;
    private TextView text_all_peisong;
    private RelativeLayout rel_super;
    public PopupWindow popupWindow_bottom;
    public View v;
    public void showBottom() {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_bottom, null);
        text_all_num = (TextView) v.findViewById(R.id.text_all_num);
        img_super_car = (ImageView) v.findViewById(R.id.img_super_car);
        text_goadd = (TextView) v.findViewById(R.id.text_goadd);
        text_all_price = (TextView) v.findViewById(R.id.text_all_price);
        text_all_peisong = (TextView) v.findViewById(R.id.text_all_peisong);
        rel_super = (RelativeLayout) v.findViewById(R.id.rel_super);
        rel_super.setOnClickListener(new View.OnClickListener() {//购物车
            @Override
            public void onClick(View v) {
                if (selectAll.size() > 0) {
                    showPopupWindow(selectAll);
                } else {
                    ToastUtil.MyToast(getActivity(), getString(R.string.car_no_sel_baby));
                }
            }
        });
        text_goadd.setOnClickListener(new View.OnClickListener() {//结算
            @Override
            public void onClick(View v) {
                if (selectAll.size() > 0) {
                    Intent intent = new Intent(getActivity(), SureOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectAll", (Serializable) selectAll);
                    intent.putExtras(bundle);
                    intent.putExtra("text_all_price",text_all_price.getText().toString());
                    startActivity(intent);
                } else {
                    ToastUtil.MyToast(getContext(), getString(R.string.sel_to_buy_baby));
                }
            }
        });//结算
        popupWindow_bottom = new PopupWindow(v,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow_bottom.setFocusable(false);
        popupWindow_bottom.setBackgroundDrawable(new BitmapDrawable());
        popupWindow_bottom.setOutsideTouchable(false);
        popupWindow_bottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow_bottom.dismiss();
            }
        });
        popupWindow_bottom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
    /***************得到Activity的页面切换回调信息********************/
    @Override
    public void changeNum(int page) {
        if(page==0){//显示
            popupWindow_bottom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }else{//隐藏
            popupWindow_bottom.dismiss();
        }
    }
    public void clearCar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View vi = inflater.inflate(R.layout.alert, null);
        builder.setTitle(getString(R.string.wxts));
        builder.setView(vi);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectAll.clear();
                popupWindow.dismiss();
                for (int a = 0; a < foodDetailList.size(); a++) {
                    foodDetailList.get(a).setYiOrder(0);
                }
                for (int b = 0; b < foodTypeList.size(); b++) {
                    foodTypeList.get(b).setSelectNum(0);
                }
                foodDetailAdapter.notifyDataSetChanged();
                foodTypeAdapter.notifyDataSetChanged();
                img_super_car.setImageResource(R.drawable.super_no_sel);
                text_all_num.setVisibility(View.INVISIBLE);
                text_goadd.setBackgroundResource(R.color.shenhui);
                text_all_price.setText(getString(R.string.no_sel_baby));
                builder.create().cancel();
            }
        });
        builder.setNeutralButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().cancel();
            }
        });
        builder.create().show();
    }
}
