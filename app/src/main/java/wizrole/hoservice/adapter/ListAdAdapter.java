package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.OrderList;

/**
 * Created by a on 2017/3/13.
 * 检查医嘱适配器
 */

/**
 * 同一组数据集合
 * 例如：{  “asd”“df”“fsg”“ddd”“ffgg”“jgdds”}
 * 根据字符串中是否有  “s” 来重新排序，生成列表   有“s”的排列在前面，无“s”排列在后面
 * 适配器中逻辑
 */

public class ListAdAdapter extends BaseAdapter {

    public List<OrderList> infors;
    public Context context;
    public LayoutInflater inflater;
    public String now_time;
    public int sizeType;
    public ListAdAdapter(List<OrderList> infors, Context context,int sizeType){
        this.infors=infors;
        this.context=context;
        this.sizeType=sizeType;
        inflater= LayoutInflater.from(context);
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        now_time=format.format(date);    //当前时间
    /************测试***/
//        Calendar calendar=Calendar.getInstance();
//        calendar.setTime(date);//把当前时间赋给日历
//        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为昨天
//        Date three = calendar.getTime();   //得到大前天的时间
//        now_time=format.format(three);    //当前时间
        /**********测试*******/
    }

    @Override
    public int getCount() {
        return infors.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return infors.get(i);
    }
    class  Holder{
        TextView text_OrdName;
        TextView text_OrdRecDep;
        TextView text_OrdDate;
        TextView text_OrdTime;
        TextView text_OrdDoc;
        TextView text_date_now;
        View      view_date_fg;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if(view==null){
            holder=new Holder();
            view=inflater.inflate(R.layout.list_advice_item,null);
            holder.text_OrdName=(TextView)view.findViewById(R.id.text_OrdName);
            holder.text_OrdRecDep=(TextView)view.findViewById(R.id.text_OrdRecDep);
            holder.text_OrdDate=(TextView)view.findViewById(R.id.text_OrdDate);
            holder.text_OrdTime=(TextView)view.findViewById(R.id.text_OrdTime);
            holder.text_OrdDoc=(TextView)view.findViewById(R.id.text_OrdDoc);
            holder.view_date_fg=(View)view.findViewById(R.id.view_date_fg);
            holder.text_date_now=(TextView)view.findViewById(R.id.text_date_now);
            view.setTag(holder);
        }else{
            holder=(Holder)view.getTag();
        }
        OrderList ol=infors.get(i);
        holder.text_OrdName.setText(ol.getOrdName());
        holder.text_OrdRecDep.setText(ol.getOrdRecDep());
        holder.text_OrdDate.setText(ol.getOrdDate());
        holder.text_OrdTime.setText(ol.getOrdTime());
        holder.text_OrdDoc.setText(ol.getOrdDoc());

        if(ol.getOrdDate().equals(now_time)){//今天的
            if(ol.isStatus_today()){
                holder.text_date_now.setText("今天");
                holder.text_date_now.setVisibility(View.VISIBLE);
                holder.view_date_fg.setVisibility(View.VISIBLE);
            }else{
                holder.text_date_now.setVisibility(View.INVISIBLE);
                holder.view_date_fg.setVisibility(View.INVISIBLE);
            }

//            if(i==sizeType){
//                holder.view_date_fg.setVisibility(View.VISIBLE);
////                holder.view_date_top.setVisibility(View.VISIBLE);
//            }else{
//                holder.view_date_fg.setVisibility(View.INVISIBLE);
////                holder.view_date_top.setVisibility(View.INVISIBLE);
//            }

        }else{//次日的
            if(ol.isStatus_tomorrow()){//只显示一次
                holder.text_date_now.setText("次日");
                holder.text_date_now.setVisibility(View.VISIBLE);
                holder.view_date_fg.setVisibility(View.VISIBLE);
            }else{
                holder.text_date_now.setVisibility(View.INVISIBLE);
                holder.view_date_fg.setVisibility(View.INVISIBLE);
            }
//            if ((i+1)==infors.size()){//显示线条--最后
//                holder.view_date_fg.setVisibility(View.VISIBLE);
//            }else{
//                holder.view_date_fg.setVisibility(View.INVISIBLE);
//            }
        }
        return view;
    }
}
