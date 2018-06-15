package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.ReportMsgDetail;

/**
 * Created by liushengping on 2017/11/24/024.
 * 何人执笔？
 * 检验报告子类
 */

public class InpectionChilAdapter extends BaseAdapter {

    public Context context;
    public List<ReportMsgDetail> details;
    public LayoutInflater inflater;
    public InpectionChilAdapter(Context context){
        this.context=context;
    }

    public void addAll(List<ReportMsgDetail> details){
        this.details=details;
        notifyDataSetChanged();

    }
    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int position) {
        return details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder{
        TextView in_child_num;
        TextView in_child_name;
        TextView in_child_result;
        TextView in_child_dh;
        TextView in_child_zcz;
        TextView in_child_dw;
        TextView in_child_ts;
        View in_child_line;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.list_inpect_child_item,null);
            holder.in_child_dh=(TextView)convertView.findViewById(R.id.in_child_dh);
            holder.in_child_num=(TextView)convertView.findViewById(R.id.in_child_num);
            holder.in_child_name=(TextView)convertView.findViewById(R.id.in_child_name);
            holder.in_child_result=(TextView)convertView.findViewById(R.id.in_child_result);
            holder.in_child_zcz=(TextView)convertView.findViewById(R.id.in_child_zcz);
            holder.in_child_dw=(TextView)convertView.findViewById(R.id.in_child_dw);
            holder.in_child_ts=(TextView)convertView.findViewById(R.id.in_child_ts);
            holder.in_child_line=(View)convertView.findViewById(R.id.in_child_line);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        holder.in_child_dh.setText(details.get(position).getMsgUnit());
        holder.in_child_num.setText(details.get(position).getMsgNo());
        holder.in_child_name.setText(details.get(position).getMsgName());
        holder.in_child_result.setText(details.get(position).getMsgResult());
        holder.in_child_zcz.setText(details.get(position).getMsgNormal());
        holder.in_child_dw.setText(details.get(position).getMsgUnit());
        holder.in_child_ts.setText(details.get(position).getMsgResultPrompt());
        if(details.size()==(1+position)){
            holder.in_child_line.setVisibility(View.INVISIBLE);
        }else{
            holder.in_child_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
