package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.AnnounceMsg;
import wizrole.hoservice.beam.HealEduDatail;

/**
 * Created by a on 2017/9/4.
 */

public class HealEduAdapter extends ConcreteAdapter<HealEduDatail>{

    public HealEduAdapter(Context context, List<HealEduDatail> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HealEduDatail item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path,R.id.img_dynamic,R.drawable.img_loadfail)
                .setText(Html.fromHtml(item.getHeal_title()).toString(),R.id.text_dynamic_title)
                .setText(Html.fromHtml(item.getHeal_content()).toString(), R.id.text_dynamic_msg);
    }
}
