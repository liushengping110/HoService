package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DiseaseName;
import wizrole.hoservice.beam.SurgeryDetailInfor;

/**
 * Created by a on 2017/9/12.
 * 关键字搜索返回手术信息
 */

public class SerachSurgeryAdapter extends ConcreteAdapter<SurgeryDetailInfor>{

    public SerachSurgeryAdapter(Context context, List<SurgeryDetailInfor> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SurgeryDetailInfor item, int position) {
        viewHolder.setText(item.getSurgeryName(), R.id.text_pop_item_deparent);
    }
}
