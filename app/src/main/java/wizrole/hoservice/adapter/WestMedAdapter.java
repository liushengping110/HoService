package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.WestMedInfor;

/**
 * Created by ${liushengping} on 2017/10/12.
 * 何人执笔？
 * 西药列表适配器
 */

public class WestMedAdapter extends ConcreteAdapter<WestMedInfor> {

    public WestMedAdapter(Context context, List<WestMedInfor> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, WestMedInfor item, int position) {
        viewHolder.setText(item.getMedName(), R.id.text_healscien_title)//药名
                .setText(item.getMedIndication(),R.id.text_healscien_content)//适应症
        .setImageView(Constant.ip+item.getMedImageUrl(),R.id.img_healscien_img,R.drawable.img_loadfail);
    }
}
