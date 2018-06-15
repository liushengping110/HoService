package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.AnnounceMsg;
import wizrole.hoservice.beam.DynamicMsg;

/**
 * Created by a on 2017/9/4.
 * 复用同一个layout
 */

public class HosAnnounceAdapter extends ConcreteAdapter<AnnounceMsg>{

    public HosAnnounceAdapter(Context context, List<AnnounceMsg> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, AnnounceMsg item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path,R.id.img_dynamic,R.drawable.img_loadfail)
                .setText(Html.fromHtml(item.getAnnounce_title()).toString(),R.id.text_dynamic_title)
                .setText(Html.fromHtml(item.getAnnounce_msg()).toString(), R.id.text_dynamic_msg);
    }
}
