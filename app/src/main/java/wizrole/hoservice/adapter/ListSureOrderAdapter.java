package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.model.getgoodtype.FoodDetail;


/**
 * Created by a on 2017/3/31.
 * 订单确认适配器
 */

public class ListSureOrderAdapter extends BaseAdapter {

    public List<CommodityList> list;
    public Context context;
    public LayoutInflater inflater;
    public ListSureOrderAdapter(Context context, List<CommodityList> list){
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
        TextView text_food_name;
        TextView text_food_num;
        TextView text_food_price;
        View view_line;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holdler holdler=null;
        if(view==null){
            holdler=new Holdler();
            view=inflater.inflate(R.layout.list_item_sureorder,null);
            holdler.view_line=(View)view.findViewById(R.id.view_line);
            holdler.text_food_name=(TextView)view.findViewById(R.id.text_food_name);
            holdler.text_food_num=(TextView)view.findViewById(R.id.text_food_num);
            holdler.text_food_price=(TextView)view.findViewById(R.id.text_food_allprice);
            view.setTag(holdler);
        }else{
            holdler=(Holdler)view.getTag();
        }
        CommodityList foodDetail=list.get(i);
        holdler.text_food_name.setText(foodDetail.getCommodityName());
        holdler.text_food_num.setText(foodDetail.getYiOrder()+"");
        holdler.text_food_price.setText((foodDetail.getYiOrder())*Double.parseDouble(foodDetail.getCommodityAmt())+"");
        return view;
    }
}
