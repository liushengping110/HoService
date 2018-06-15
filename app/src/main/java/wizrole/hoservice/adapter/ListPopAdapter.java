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
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.model.getgoodtype.FoodDetail;


/**
 * Created by a on 2017/3/31.
 * 餐饮点餐细节适配器
 */

public class ListPopAdapter extends BaseAdapter {

    public List<CommodityList> list;
    public Context context;
    public LayoutInflater inflater;
    public ListPopAdapter(Context context, List<CommodityList> list){
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
        ImageView img_sub;
        ImageView img_add;
        TextView text_number;
        TextView text_food_allprice;
        View view_line;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holdler holdler=null;
        if(view==null){
            holdler=new Holdler();
            view=inflater.inflate(R.layout.list_item_pop,null);
            holdler.view_line=(View)view.findViewById(R.id.view_line);
            holdler.text_food_name=(TextView)view.findViewById(R.id.text_food_name);
            holdler.text_number=(TextView)view.findViewById(R.id.text_number);
            holdler.text_food_allprice=(TextView)view.findViewById(R.id.text_food_allprice);
            holdler.img_add=(ImageView)view.findViewById(R.id.img_add);
            holdler.img_sub=(ImageView)view.findViewById(R.id.img_sub);
            view.setTag(holdler);
        }else{
            holdler=(Holdler)view.getTag();
        }
        if(i==0){
            holdler.view_line.setVisibility(View.INVISIBLE);
        }else{
            holdler.view_line.setVisibility(View.VISIBLE);
        }
        CommodityList foodDetail=list.get(i);
        holdler.text_food_name.setText(foodDetail.getCommodityName());
        holdler.text_number.setText(foodDetail.getYiOrder()+"");
        holdler.text_food_allprice.setText((foodDetail.getYiOrder())*Double.parseDouble(foodDetail.getCommodityAmt())+"");
        //添加
        holdler.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popAddOnClick!=null) {
                    popAddOnClick.popAddOnClick(i);
                }
            }
        });
        //减少
        holdler.img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popSubOnClick!=null){
                    popSubOnClick.popSubOnClick(i);
                }
            }
        });
        return view;
    }

    public PopAddOnClick popAddOnClick;
    public void setPopAddOnClick(PopAddOnClick popAddOnClick) {
        this.popAddOnClick = popAddOnClick;
    }
    public interface PopAddOnClick{
        void popAddOnClick(int position);
    }

    public PopSubOnClick   popSubOnClick;
    public void setPopSubOnClick(PopSubOnClick popSubOnClick) {
        this.popSubOnClick = popSubOnClick;
    }
    public interface PopSubOnClick{
        void popSubOnClick(int position);
    }
}
