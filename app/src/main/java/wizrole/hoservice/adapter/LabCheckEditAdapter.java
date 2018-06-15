package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DisHostry;
import wizrole.hoservice.beam.LabCheckHostry;

/**
 * Created by a on 2017/8/25.
 * 搜索历史纪录适配器
 */

public class LabCheckEditAdapter extends ConcreteAdapter<LabCheckHostry>{

    public LabCheckEditAdapter(Context context, List<LabCheckHostry> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, LabCheckHostry item, int position) {
        viewHolder.setText(item.getContent(), R.id.edit_item_text);
    }
}
