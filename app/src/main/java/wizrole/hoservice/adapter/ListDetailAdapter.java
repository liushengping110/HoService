package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.TarItemDetail;

/**
 * Created by a on 2017/3/9.
 * 费用清单（详细）适配器
 */

public class ListDetailAdapter extends BaseAdapter {

    public List<TarItemDetail> infors;
    public LayoutInflater inflater;
    public ListDetailAdapter(List<TarItemDetail> infors, Context context){
        this.infors=infors;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infors.size();
    }

    @Override
    public Object getItem(int i) {
        return infors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Holder{
        TextView text_index;    //编号
        TextView text_category; //种类
        TextView text_itemName; //项目名称
        TextView text_qty;  //数量
        TextView text_uom;  //单位
        TextView text_price;    //单价
        TextView text_totalAmt; //总计
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if (view==null){
            holder=new Holder();
            view=inflater.inflate(R.layout.list_detail_item,null);
            holder.text_index=(TextView)view.findViewById(R.id.text_index);
            holder.text_category=(TextView)view.findViewById(R.id.text_category);
            holder.text_itemName=(TextView)view.findViewById(R.id.text_itemName);
            holder.text_qty=(TextView)view.findViewById(R.id.text_qty);
            holder.text_uom=(TextView)view.findViewById(R.id.text_uom);
            holder.text_price=(TextView)view.findViewById(R.id.text_price);
            holder.text_totalAmt=(TextView)view.findViewById(R.id.text_totalAmt);
            view.setTag(holder);
        }else{
            holder=(Holder)view.getTag();
        }
        if (i%2==0){
            view.setBackgroundResource(R.color.white);
        }else{
            view.setBackgroundResource(R.color.danhui);
        }
            TarItemDetail tarItemDetail=infors.get(i);
            holder.text_index.setText(tarItemDetail.getIndex());
            holder.text_category.setText(tarItemDetail.getCategory());
            holder.text_itemName.setText(tarItemDetail.getItemName());
            holder.text_qty.setText(tarItemDetail.getQty());
            holder.text_uom.setText(tarItemDetail.getUOM());
            holder.text_price.setText(tarItemDetail.getPrice());
            holder.text_totalAmt.setText(tarItemDetail.getTotalAmt());
        return view;
    }
}
