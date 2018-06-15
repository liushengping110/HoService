package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getallstore.bean.StoreList;


/**
 * Created by Administrator on 2018/1/22.
 */

public class StoreSearchListAdapter extends ConcreteAdapter<StoreList> {
    //图片添加ip
    public  final String ip="http://39.107.75.214/";

    public StoreSearchListAdapter(Context context, List<StoreList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, StoreList item, int position) {
        viewHolder.setImageView(ip+item.getStoreLogoPic(), R.id.img_merchants_logo, R.drawable.img_loadfail)
                .setText(item.getStoreName(), R.id.text_merchants_name)
                .setText(item.getStorePlace(), R.id.text_merchants_address)
                .setText(item.getStorePhone(), R.id.text_merchants_tel)
                .setText("满50减5，满100减15", R.id.text_merchants_sub)
                .setText("20元起送", R.id.text_merchants_qs)
                .setText("配送费￥5", R.id.text_merchants_psf);
    }
}
