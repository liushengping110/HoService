package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.DynamicMsg;

/**
 * Created by a on 2017/9/4.
 * 复用同一个layout
 */

public class HosDynamicAdapter extends ConcreteAdapter<DynamicMsg>{

    public HosDynamicAdapter(Context context, List<DynamicMsg> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DynamicMsg item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path,R.id.img_dynamic,R.drawable.img_loadfail)
                .setText(Html.fromHtml(item.getDynamic_title()).toString(),R.id.text_dynamic_title)
                .setText(Html.fromHtml(item.getDynamic_msg()).toString(), R.id.text_dynamic_msg);
    }
}
