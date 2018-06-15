package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.life.model.orderlist.OrderList;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.view.CustListView;


/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class OrderListAdapter extends BaseAdapter{
    public Context context;
    public List<OrderList> orderLists;
    public LayoutInflater inflater;
    public double price=0;

    public OrderListAdapter(Context context, List<OrderList> orderLists){
        this.context=context;
        this.orderLists=orderLists;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return orderLists.size();
    }

    @Override
    public Object getItem(int position) {
        return orderLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder{
        ImageView img_custmo_header;//订单人头像
        TextView text_custmo_name;//订单人姓名
        TextView text_custmo_status;//订单状态
        CustListView list_goods;//订单商品名
        TextView text_goods_allnum;//订单商品数量
        TextView text_goods_allprices;//订单商品价格
        TextView text_action_one;//订单下一步操作按钮
        TextView text_action_two;//订单下一步操作按钮
        TextView text_action_three;//订单下一步操作按钮
        TextView text_action_four;//订单下一步操作按钮

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.list_order_item,null);
            holder.img_custmo_header=(ImageView)convertView.findViewById(R.id.img_custmo_header);
            holder.text_custmo_name=(TextView) convertView.findViewById(R.id.text_custmo_name);
            holder.text_custmo_status=(TextView) convertView.findViewById(R.id.text_custmo_status);
            holder.text_goods_allnum=(TextView) convertView.findViewById(R.id.text_goods_allnum);
            holder.text_goods_allprices=(TextView) convertView.findViewById(R.id.text_goods_allprices);
            holder.list_goods=(CustListView) convertView.findViewById(R.id.list_goods);
            holder.text_action_one=(TextView) convertView.findViewById(R.id.text_action_one);
            holder.text_action_two=(TextView) convertView.findViewById(R.id.text_action_two);
            holder.text_action_three=(TextView) convertView.findViewById(R.id.text_action_three);
            holder.text_action_four=(TextView) convertView.findViewById(R.id.text_action_four);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        OrderList orderList=orderLists.get(position);
        ImageLoading.common(context,orderList.getOrderHeader(),holder.img_custmo_header,R.drawable.img_loadfail);
        switch (orderList.getOrderStatus()){
            case "0"://全部
                holder.text_custmo_status.setText("订单已完成");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.GONE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_four.setText("再次评价");
                break;
            case "1"://待付款
                holder.text_custmo_status.setText("订单待付款");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.VISIBLE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_three.setText("取消订单");
                holder.text_action_four.setText("付款");
                break;
            case "2"://待发货
                holder.text_custmo_status.setText("订单待发货");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.INVISIBLE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_four.setText("取消订单");
                break;
            case "3"://待收货
                holder.text_custmo_status.setText("订单待收货");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.VISIBLE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_three.setText("查看物流");
                holder.text_action_four.setText("确认送达");
                break;
            case "4"://待评价
                holder.text_custmo_status.setText("订单待评价");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.GONE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_four.setText("评价");
                break;
            case "5"://已完成
                holder.text_custmo_status.setText("订单已完成");
                holder.text_action_one.setVisibility(View.GONE);
                holder.text_action_two.setVisibility(View.GONE);
                holder.text_action_three.setVisibility(View.VISIBLE);
                holder.text_action_four.setVisibility(View.VISIBLE);
                holder.text_action_three.setText("再次评价");
                holder.text_action_four.setText("再来一单");
                break;
        }
        holder.text_custmo_name.setText(orderList.getOrderName());
        holder.text_goods_allnum.setText("共计"+orderList.getOrderContent().size()+"件商品");
        for(int a=0;a<orderList.getOrderContent().size();a++){
            price+=orderList.getOrderContent().get(a).getOrderGoodPrice()*orderList.getOrderContent().get(a).getOrderGoodsNumber();
        }
        holder.text_goods_allprices.setText("共计"+price+"元");
        price=0;
        OrderDetailAdapter adapter=new OrderDetailAdapter(context,orderList.getOrderContent());
        holder.list_goods.setAdapter(adapter);
        return convertView;
    }
}
