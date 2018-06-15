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
import wizrole.hoservice.life.model.orderlist.OrderContent;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created by liushengping on 2018/1/17/017.
 * 何人执笔？
 */

public class OrderDetailAdapter extends BaseAdapter {
    public Context context;
    public List<OrderContent> orderContents;
    public LayoutInflater inflater;
    public OrderDetailAdapter(Context context, List<OrderContent> orderContents){
        this.context=context;
        this.orderContents=orderContents;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return orderContents.size();
    }

    @Override
    public Object getItem(int position) {
        return orderContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder{
        ImageView img_goods_logo;
        TextView text_goods_name;
        TextView text_goods_prices;
        TextView text_goods_number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.list_order_detail_item,null);
            holder.img_goods_logo=(ImageView)convertView.findViewById(R.id.img_goods_logo);
            holder.text_goods_name=(TextView) convertView.findViewById(R.id.text_goods_name);
            holder.text_goods_prices=(TextView) convertView.findViewById(R.id.text_goods_prices);
            holder.text_goods_number=(TextView) convertView.findViewById(R.id.text_goods_number);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        OrderContent content=orderContents.get(position);
        ImageLoading.common(context,content.getOrderGoodsLogo(),holder.img_goods_logo,R.drawable.img_loadfail);
        holder.text_goods_name.setText(content.getOrderGoodsName());
        holder.text_goods_number.setText(content.getOrderGoodsNumber()+"");
        holder.text_goods_prices.setText(content.getOrderGoodPrice()+"");
        return convertView;
    }
}
