package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;


/**
 * Created by liushengping on 2018/1/25.
 * 何人执笔？
 */

public class GoodsSearchShowAdapter extends ConcreteAdapter<CommodityList> {

    public GoodsSearchShowAdapter(Context context, List<CommodityList> list, int itemLayout) {
        super(context, list, itemLayout);
    }


    @Override
    protected void convert(ViewHolder viewHolder, CommodityList item, final int position) {
        viewHolder.setText(item.getCommodityName(), R.id.textview_detail)
                .setText(item.getCommodityAmt(), R.id.tvGoodsPrice)
                .setText(item.getCommodityContent(),R.id.tvGoodsDescription)
                .setText("88",R.id.text_monSaleNum)
                .setImageView(Constant.ip_temp+item.getCommodityPic(),R.id.imageview_superlogo,R.drawable.img_loadfail);
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addOnClickListener!=null){
                    addOnClickListener.addOnClick(position);
                }
            }
        },R.id.img_add );
        viewHolder.getView(R.id.img_add).setVisibility(View.INVISIBLE);
    }

    public AddOnClickListener addOnClickListener;
    public interface AddOnClickListener{
        void addOnClick(int position);
    }

    public void setAddOnClickListener(AddOnClickListener addOnClickListener) {
        this.addOnClickListener = addOnClickListener;
    }
}
