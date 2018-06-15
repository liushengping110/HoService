package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DiseaseName;
import wizrole.hoservice.beam.LabCheckName;

/**
 * Created by a on 2017/9/12.
 * 关键字搜索返回科室信息
 */

public class SerachLabCheckAdapter extends ConcreteAdapter<LabCheckName>{

    public SerachLabCheckAdapter(Context context, List<LabCheckName> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, LabCheckName item, int position) {
        viewHolder.setText(item.getLabCheckName(), R.id.text_pop_item_deparent);
    }
}
