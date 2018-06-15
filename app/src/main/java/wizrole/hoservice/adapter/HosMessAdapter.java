package wizrole.hoservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.HosMsg;

/**
 * Created by ${liushengping} on 2017/9/26.
 * 何人执笔？
 */

public class HosMessAdapter extends BaseAdapter {
    public Context context;
    public List<HosMsg> hosMsgs;
    public LayoutInflater inflater;
    public HosMessAdapter(Context context,List<HosMsg> hosMsgs){
        this.context=context;
        this.hosMsgs=hosMsgs;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return hosMsgs.size();
    }

    @Override
    public Object getItem(int i) {
        return hosMsgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Holder{
        ImageView img_logo;
        TextView text_name;
        TextView text_cate;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if(view==null){
            holder=new Holder();
            view=inflater.inflate(R.layout.list_hosmess_item,null);
            holder.img_logo=(ImageView)view.findViewById(R.id.img_mess_logo);
            holder.text_name=(TextView) view.findViewById(R.id.text_mess_name);
            holder.text_cate=(TextView) view.findViewById(R.id.text_mess_cate);
            view.setTag(holder);
        }else{
            holder=(Holder) view.getTag();
        }
        final HosMsg hosMsg=hosMsgs.get(i);
        if(hosMsg.getType().equals("1")){//检查医嘱
            holder.img_logo.setImageResource(R.drawable.hos_yz);
            holder.text_name.setText("检查医嘱");
            holder.text_cate.setText("您有一份检查医嘱尚未查看!");
        }else if(hosMsg.getType().equals("2")){//检验结果
            holder.img_logo.setImageResource(R.drawable.main_bg);
            holder.text_name.setText("检验结果");
            holder.text_cate.setText("您有一份检验结果尚未查看!");
        }else if(hosMsg.getType().equals("3")){
            holder.img_logo.setImageResource(R.drawable.main_xx);
            holder.text_name.setText("通知消息");
            holder.text_cate.setText("您有一份通知消息尚未查看!");
        }
//        holder.text_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(hosMsg.getType().equals("1")){//检查医嘱
//                    Intent intent=new Intent(context, AdviceActivity.class);
//                    context.startActivity(intent);
//                }else if(hosMsg.getType().equals("2")){//检验结果
//                    Intent intent=new Intent(context, InspectionActivity.class);
//                    context.startActivity(intent);
//                }else if(hosMsg.getType().equals("3")){//自定义推送消息
//                    Intent intent=new Intent(context, CustomMessActivity.class);
//                    context.startActivity(intent);
//                }
//            }
//        });
        return view;
    }
}
