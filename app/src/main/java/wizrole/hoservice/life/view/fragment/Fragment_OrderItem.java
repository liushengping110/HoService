package wizrole.hoservice.life.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.adapter.OrderListAdapter;
import wizrole.hoservice.life.model.orderlist.OrderContent;
import wizrole.hoservice.life.model.orderlist.OrderList;
import wizrole.hoservice.view.LoadingView;
import wizrole.hoservice.view.UserFooterView;
import wizrole.hoservice.view.UserHeaderView;

/**
 * Created by 何人执笔？ on 2018/4/27.
 * liushengping
 */

public class Fragment_OrderItem extends LazyFragment {

    public static final String INTENT_INT_INDEX="index";
    private int tabIndex;
    public LoadingView loadingView;
    public ListView list_order;
    public LinearLayout lin_wifi_err;
    public RefreshLayout refreshView;
    public ImageView img_net_err;
    public String id;
    public TextView text_err_agagin_center;
    List<OrderContent> orderContent=new ArrayList<>();

    public static Fragment_OrderItem newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        Fragment_OrderItem fragment = new Fragment_OrderItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_order_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        Bundle bundle = getArguments();
        id= bundle.getString("type_id");
        initUI();
        switch (id){
            case "0"://待付款
                for(int z=0;z<5;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("黄焖鸡米饭"+z);
                    content.setOrderGoodPrice(18.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://imgupload2.youboy.com/imagestore20160110be415fef-0821-4c59-8a78-1853b14b545f.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
            case "1"://待配送
                for(int z=0;z<1;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("干煸回锅肉"+z);
                    content.setOrderGoodPrice(38.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://p1.so.qhmsg.com/bdr/_240_/t0170c8eb477044a126.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
            case "2"://待送达
                for(int z=0;z<2;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("松花蛋"+z);
                    content.setOrderGoodPrice(16.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://p0.so.qhimgs1.com/bdr/_240_/t0127896228d0efa9c4.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
            case "3"://待评级
                for(int z=0;z<4;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("土豆片香肠"+z);
                    content.setOrderGoodPrice(26.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://p4.so.qhmsg.com/bdr/_240_/t0198a98180defa40f9.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
            case "4"://已完成
                for(int z=0;z<4;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("土豆片香肠"+z);
                    content.setOrderGoodPrice(26.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://p4.so.qhmsg.com/bdr/_240_/t0198a98180defa40f9.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
            case "5":
                for(int z=0;z<4;z++){
                    OrderContent content=new OrderContent();
                    content.setOrderGoodsName("烤鸭"+z);
                    content.setOrderGoodPrice(128.00);
                    content.setOrderGoodsNumber(1);
                    content.setOrderGoodsLogo("http://image.cn.made-in-china.com/prod/000-LaWQKpSIuUcT.jpg");
                    orderContent.add(content);
                    content=null;
                }
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(orderContent);
            }
        }, 1500);
    }

    public void initUI(){
        loadingView=(LoadingView)findViewById(R.id.loadView_cener);
        loadingView.addBitmap(R.drawable.icon_chicken);
        loadingView.addBitmap(R.drawable.icon_flower);
        loadingView.addBitmap(R.drawable.icon_orange);
        loadingView.addBitmap(R.drawable.icon_ufo);
        loadingView.addBitmap(R.drawable.icon_pear);
        loadingView.setShadowColor(Color.LTGRAY);
        loadingView.setDuration(700);
        loadingView.start();
        list_order=(ListView)findViewById(R.id.list_order);
        text_err_agagin_center=(TextView)findViewById(R.id.text_err_agagin_center);
        img_net_err=(ImageView) findViewById(R.id.img_net_err);
        lin_wifi_err=(LinearLayout) findViewById(R.id.lin_wifi_err);
        //加载刷新
        refreshView=(RefreshLayout) findViewById(R.id.xrefreshview);
        refreshView.setRefreshHeader(new UserHeaderView(getActivity()));
        refreshView.setRefreshFooter(new UserFooterView(getActivity()));
        refreshView.setHeaderHeightPx(300);
        refreshView.setFooterHeightPx(80);
        setNetErr();
    }

    public List<OrderList> orderLists=new ArrayList<>();
    public void getData(List<OrderContent> orderContent){
        for(int j=0;j<12;j++){
            OrderList orderList=new OrderList();
            if(j<2){
                orderList.setOrderStatus("0");
                orderList.setOrderContent(orderContent);
            }else if(1<j&&j<4){
                orderList.setOrderStatus("1");
                orderList.setOrderContent(orderContent);
            }else if(3<j&&j<6){
                orderList.setOrderStatus("2");
                orderList.setOrderContent(orderContent);
            }else if(5<j&&j<8){
                orderList.setOrderStatus("3");
                orderList.setOrderContent(orderContent);
            }else if(7<j&&j<10){
                orderList.setOrderStatus("4");
                orderList.setOrderContent(orderContent);
            }else{
                orderList.setOrderStatus("5");
                orderList.setOrderContent(orderContent);
            }
            orderList.setOrderAddress("江苏省徐州市泉山区泰山街道88号");
            orderList.setOrderDistrTime("2018-01-01-13:01");
            orderList.setOrderName("如玉烧菜馆");
            orderList.setOrderDistrTime("尽快送达");
            orderList.setOrderTel("18720999936");
            orderList.setOrderHeader("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523337255288&di=11946af2148c28e6cf80702d0eea5386&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fbainuo%2Fwh%3D720%2C436%2Fsign%3Debbf41a8b5fb43161a4a727d12946a17%2F72f082025aafa40f9d29efbeac64034f79f019e1.jpg");
            orderLists.add(orderList);
            orderList=null;
        }
        setAdapter();
    }

    public OrderListAdapter orderListAdapter;
    public void setAdapter(){
        if(orderListAdapter==null){
            orderListAdapter=new OrderListAdapter(getActivity(),orderLists);
            list_order.setAdapter(orderListAdapter);
        }else{
            orderListAdapter.notifyDataSetChanged();
        }
        loadingView.setVisibility(View.INVISIBLE);
        setNetNormal();
    }
    public void setListener(){

    }
    /*****************网络请求失败时的禁止加载和刷新***********************/
    //禁止加载和刷新
    public void setNetErr(){
        refreshView.setEnableLoadmore(false);
        refreshView.setEnableRefresh(false);
    }
    //支持加载和刷新
    public void setNetNormal(){
        refreshView.setEnableLoadmore(true);
        refreshView.setEnableRefresh(true);
    }
}
