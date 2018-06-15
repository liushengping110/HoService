package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;

/**
 * Created by a on 2017/8/28.
 * 全部科室选择  适配器
 */

public class DeparentPopAdapter extends ConcreteAdapter<DeparentIntrduce>{

    public DeparentPopAdapter(Context context, List<DeparentIntrduce> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DeparentIntrduce item, int position) {
        viewHolder.setText(item.getDeparentName(), R.id.text_pop_item_deparent);
    }
}
