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
import wizrole.hoservice.beam.MyOrder;


/**
 * Created by a on 2017/5/2.
 * 我的订单适配器
 */

public class MyOrderAdapter extends BaseAdapter {

    public List<MyOrder> details;
    public Context context;
    public LayoutInflater inflater;
    public double all;
    public MyOrderAdapter(Context context , List<MyOrder> details){
        this.context=context;
        this.details=details;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int i) {
        return details.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    class Holder{
        ImageView img_order_logo;
        TextView text_order_name;
        TextView text_order_status;
        TextView text_order_time;
        TextView text_order_example;
        TextView text_order_price;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if(view==null){
            holder=new Holder();
            view=inflater.inflate(R.layout.list_item_myorder,null);
            holder.img_order_logo=(ImageView)view.findViewById(R.id.img_order_logo);
            holder.text_order_name=(TextView)view.findViewById(R.id.text_order_name);
            holder.text_order_status=(TextView)view.findViewById(R.id.text_order_status);
            holder.text_order_time=(TextView)view.findViewById(R.id.text_order_time);
            holder.text_order_example=(TextView)view.findViewById(R.id.text_order_example);
            holder.text_order_price=(TextView)view.findViewById(R.id.text_order_price);
            view.setTag(holder);
        }else{
            holder=(Holder) view.getTag();
        }
        MyOrder myOrder=details.get(i);
        holder.img_order_logo.setImageResource(R.mipmap.ic_launcher);
        holder.text_order_name.setText(myOrder.getStoreName());
        holder.text_order_status.setText(myOrder.getOrderStatus());
        holder.text_order_time.setText(myOrder.getTime());
        holder.text_order_price.setText(myOrder.getAllPrice()+"");
        holder.text_order_example.setText(myOrder.getOrder_example());
        return view;
    }
}
