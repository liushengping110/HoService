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
import wizrole.hoservice.beam.OrderTimeSel;


/**
 * Created by a on 2017/4/7.
 * 时间选择适配器
 */

public class ListTimeSelAdapter extends BaseAdapter {

    public List<OrderTimeSel> list;
    public Context context;
    public LayoutInflater inflater;
    public ListTimeSelAdapter(Context context, List<OrderTimeSel> list){
        this.list=list;
        this.context=context;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    class Holdler{
        TextView text_time;
        ImageView img_sel_time;//选中
        ImageView img_no_sel_time;//未选中

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holdler holdler=null;
        if(view==null){
            holdler=new Holdler();
            view=inflater.inflate(R.layout.list_item_pop_order_time,null);
            holdler.text_time=(TextView)view.findViewById(R.id.text_time);
            holdler.img_sel_time=(ImageView) view.findViewById(R.id.img_sel_time);
            holdler.img_no_sel_time=(ImageView)view.findViewById(R.id.img_no_sel_time);
            view.setTag(holdler);
        }else{
            holdler=(Holdler)view.getTag();
        }
        OrderTimeSel orderTimeSel=list.get(i);
        holdler.text_time.setText(orderTimeSel.getTime());

        if(orderTimeSel.isSelect()==1){    //如果选中 设置绿色背景图片
            holdler.img_sel_time.setVisibility(View.VISIBLE);
            holdler.img_no_sel_time.setVisibility(View.INVISIBLE);
        }else{
            holdler.img_sel_time.setVisibility(View.INVISIBLE);
            holdler.img_no_sel_time.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
