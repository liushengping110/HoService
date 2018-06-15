package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.AdmResultReports;
import wizrole.hoservice.beam.ReportMsgDetail;

/**
 * Created by liushengping on 2017/11/23/023.
 * 何人执笔？
 */

public class InpectionAdapter extends BaseAdapter{
    public Context context;
    public LayoutInflater inflater;
    public List<AdmResultReports> reportses;
    public List<ReportMsgDetail> list1;

    public InpectionAdapter(List<AdmResultReports> reportses,Context context){
        this.reportses=reportses;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return reportses.size();
    }

    @Override
    public Object getItem(int position) {
        return reportses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder{
        TextView text_OrdName;
        TextView text_cate;
        TextView text_OrdDate;
        ListView in_child_list;//子listView--显示检验结果
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(holder==null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.list_inspic_item,null);
            holder.text_OrdName=(TextView)convertView.findViewById(R.id.text_OrdName);
            holder.text_cate=(TextView)convertView.findViewById(R.id.text_cate);
            holder.text_OrdDate=(TextView)convertView.findViewById(R.id.text_OrdDate);
            holder.in_child_list=(ListView)convertView.findViewById(R.id.in_child_list);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        AdmResultReports reports=reportses.get(position);
        holder.text_cate.setText(reports.getSpecName());
        holder.text_OrdName.setText(reports.getArcDesc());
        holder.text_OrdDate.setText(reports.getRecDateTime());

        final  TextView in_child_look=(TextView)convertView.findViewById(R.id.in_child_look);
        final  View view=(View)convertView.findViewById(R.id.view);
        final InpectionChilAdapter chilAdapter=new InpectionChilAdapter(context);
        int z=reportses.get(position).getReportMsg().size();
        if(z<=2){
            in_child_look.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            chilAdapter.addAll(reportses.get(position).getReportMsg());
            holder.in_child_list.setAdapter(chilAdapter);
        }else{
            in_child_look.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            list1=new ArrayList<>();
            for(int a=0;a<2;a++){
                list1.add(reportses.get(position).getReportMsg().get(a));
            }
            chilAdapter.addAll(list1);
            holder.in_child_list.setAdapter(chilAdapter);
            in_child_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    in_child_look.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    chilAdapter.addAll(reportses.get(position).getReportMsg());
                }
            });
        }
        return convertView;
    }
}
