package wizrole.hoservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.activity.EditAddressActivity;
import wizrole.hoservice.beam.PerInforOrder;


/**
 * Created by a on 2017/3/31.
 * 地址列表适配器
 */

public class ListAddAdapter extends BaseAdapter {

    public List<PerInforOrder> list;
    public Context context;
    public LayoutInflater inflater;
    public ListAddAdapter(Context context, List<PerInforOrder> list){
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
        ImageView img_sel_address;  //选中
        RelativeLayout img_edit;  //修改按钮
        TextView text_add_name;     //名字
        TextView text_add_sex;     //性别
        TextView text_add_tel;  //号码
        TextView text_add_detailadd;    //详细地址
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holdler holdler=null;
        if(view==null){
            holdler=new Holdler();
            view=inflater.inflate(R.layout.address_list_item,null);
            holdler.img_sel_address=(ImageView)view.findViewById(R.id.img_sel_address);
            holdler.img_edit=(RelativeLayout)view.findViewById(R.id.img_edit);
            holdler.text_add_name=(TextView)view.findViewById(R.id.text_add_name);
            holdler.text_add_sex=(TextView)view.findViewById(R.id.text_add_sex);
            holdler.text_add_tel=(TextView)view.findViewById(R.id.text_add_tel);
            holdler.text_add_detailadd=(TextView)view.findViewById(R.id.text_add_detailadd);
            view.setTag(holdler);
        }else{
            holdler=(Holdler)view.getTag();
        }
        PerInforOrder inforOrder=list.get(i);
        if(inforOrder.isSelect()==1){  //为true为选中
            holdler.img_sel_address.setVisibility(View.VISIBLE);
        }else{
            holdler.img_sel_address.setVisibility(View.INVISIBLE);
        }
        holdler.text_add_name.setText(inforOrder.getName());
        holdler.text_add_sex.setText(inforOrder.getSex());
        holdler.text_add_tel.setText(inforOrder.getTel());
        holdler.text_add_detailadd.setText(inforOrder.getAddress());
        //修改监听
        holdler.img_edit.setOnClickListener(new OnClick(inforOrder,context,i));
        return view;
    }

    class OnClick implements View.OnClickListener{
        public PerInforOrder inforOrder;
        public Context context;
        public int i;
        public OnClick(PerInforOrder inforOrder, Context context, int i){
            this.inforOrder=inforOrder;
            this.context=context;
            this.i=i;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context, EditAddressActivity.class);
            intent.putExtra("inforOrder",inforOrder);
            context.startActivity(intent);
            Log.e("---------",i+"");
        }
    }
}
