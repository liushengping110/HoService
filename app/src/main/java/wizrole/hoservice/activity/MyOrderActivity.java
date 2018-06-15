package wizrole.hoservice.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wizrole.hoservice.R;
import wizrole.hoservice.adapter.MyOrderAdapter;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.beam.MyOrder;

/**
 * Created by a on 2017/5/2.
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.lin_back)LinearLayout lin_back;
    @BindView(R.id.text_title)TextView text_title;
    @BindView(R.id.list_myorder)ListView list_myorder;
    public List<MyOrder> myOrders;
    @Override
    protected int getLayout() {
        return R.layout.activity_myorder;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        text_title.setText("我的订单");
        myOrders=new ArrayList<MyOrder>();
        for (int i=0;i<15;i++){
            MyOrder myOrder=new MyOrder();
            myOrder.setStoreName("膳当家黄焖鸡米饭"+i+"（望京店）");
            myOrder.setOrderStatus("订单已完成");
            myOrder.setOrder_example("黄焖鸡大份等2件商品");
            myOrder.setTime("2017-05-02");
            myOrder.setAllPrice(188.0);
            myOrders.add(myOrder);
        }
        MyOrderAdapter adapter=new MyOrderAdapter(this,myOrders);
        list_myorder.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        //返回
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //item监听
        list_myorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
