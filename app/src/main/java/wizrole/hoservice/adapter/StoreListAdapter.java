package wizrole.hoservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getallstore.bean.StoreList;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created by Administrator on 2018/1/22.
 */

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.StoreListViewHolder> {


    public Context context;
    public  List<StoreList> list;
    public String httpType;//1-7 月嫂护工  出院出行（区别）

    public StoreListAdapter(Context context, List<StoreList> list,String httpType) {
        this.context=context;
        this.list=list;
        this.httpType=httpType;
    }

    @Override
    public StoreListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_storelist_item,null));
    }
    @Override
    public void onBindViewHolder(StoreListViewHolder holder, final int position) {
        ImageLoading.common(context,Constant.ip_temp+list.get(position).getStoreLogoPic(),holder.img_merchants_logo,R.drawable.img_loadfail);
        holder.text_merchants_name.setText(list.get(position).getStoreName());
        holder.text_merchants_address.setText(list.get(position).getStorePlace());
        holder.text_merchants_tel.setText(list.get(position).getStorePhone());
        holder.text_merchants_tel.setText(list.get(position).getStorePhone());
        if(httpType.equals("月嫂护工")){
            holder.text_merchants_sub.setText("超过1周，可返50元");
            holder.text_merchants_qs.setText("最少3小时");
            holder.text_merchants_psf.setVisibility(View.INVISIBLE);
        }else if(httpType.equals("出院出行")){
            holder.text_merchants_sub.setText("满50公里，可返15元");
            holder.text_merchants_qs.setText("起步价50元");
            holder.text_merchants_psf.setVisibility(View.INVISIBLE);
        }else{
            holder.text_merchants_sub.setText("满50减5，满100减15");
            holder.text_merchants_qs.setText("20元起送");
            holder.text_merchants_psf.setText("配送费￥5");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class StoreListViewHolder extends RecyclerView.ViewHolder{
        ImageView img_merchants_logo;
        TextView text_merchants_name;
        TextView text_merchants_address;
        TextView text_merchants_tel;
        TextView text_merchants_sub;
        TextView text_merchants_qs;
        TextView text_merchants_psf;
        public StoreListViewHolder(View itemView) {
            super(itemView);
            img_merchants_logo = (ImageView) itemView.findViewById(R.id.img_merchants_logo);
            text_merchants_name = (TextView) itemView.findViewById(R.id.text_merchants_name);
            text_merchants_address = (TextView) itemView.findViewById(R.id.text_merchants_address);
            text_merchants_tel = (TextView) itemView.findViewById(R.id.text_merchants_tel);
            text_merchants_sub = (TextView) itemView.findViewById(R.id.text_merchants_sub);
            text_merchants_qs = (TextView) itemView.findViewById(R.id.text_merchants_qs);
            text_merchants_psf = (TextView) itemView.findViewById(R.id.text_merchants_psf);
        }
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
