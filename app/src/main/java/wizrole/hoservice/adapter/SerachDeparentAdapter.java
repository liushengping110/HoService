package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;

/**
 * Created by a on 2017/9/12.
 * 关键字搜索返回科室信息
 */

public class SerachDeparentAdapter extends ConcreteAdapter<DeparentIntrduce>{

    public SerachDeparentAdapter(Context context, List<DeparentIntrduce> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DeparentIntrduce item, int position) {
        viewHolder.setText(item.getDeparentName(), R.id.text_pop_item_deparent);
    }
}
