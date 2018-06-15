package wizrole.hoservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.TarItemDetail;

public class Pay_adapter extends RecyclerView.Adapter<Pay_adapter.MyViewHolder>{

        public List<TarItemDetail> tarItemDetails;
        public Context context;
        public Pay_adapter(Context context, List<TarItemDetail> tarItemDetails){
            this.context=context;
            this.tarItemDetails=tarItemDetails;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_detail_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.text_index.setText(tarItemDetails.get(position).getIndex());
            holder.text_category.setText(tarItemDetails.get(position).getCategory());
            holder.text_itemName.setText(tarItemDetails.get(position).getItemName());
            holder.text_qty.setText(tarItemDetails.get(position).getQty());
            holder.text_uom.setText(tarItemDetails.get(position).getUOM());
            holder.text_price.setText(tarItemDetails.get(position).getPrice());
            holder.text_totalAmt.setText(tarItemDetails.get(position).getTotalAmt());
        }

        @Override
        public int getItemCount() {
            return tarItemDetails.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text_index;    //编号
            TextView text_category; //种类
            TextView text_itemName; //项目名称
            TextView text_qty;  //数量
            TextView text_uom;  //单位
            TextView text_price;    //单价
            TextView text_totalAmt; //总计
            public MyViewHolder(View view){
                super(view);
                text_index=(TextView)view.findViewById(R.id.text_index);
                text_category=(TextView)view.findViewById(R.id.text_category);
                text_itemName=(TextView)view.findViewById(R.id.text_itemName);
                text_qty=(TextView)view.findViewById(R.id.text_qty);
                text_uom=(TextView)view.findViewById(R.id.text_uom);
                text_price=(TextView)view.findViewById(R.id.text_price);
                text_totalAmt=(TextView)view.findViewById(R.id.text_totalAmt);
            }
        }
    }
