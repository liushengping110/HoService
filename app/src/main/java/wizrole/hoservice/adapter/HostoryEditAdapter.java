package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.EditHostry;

/**
 * Created by a on 2017/8/25.
 * 搜索历史纪录适配器
 */

public class HostoryEditAdapter extends ConcreteAdapter<EditHostry>{

    public HostoryEditAdapter(Context context, List<EditHostry> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, EditHostry item, int position) {
        viewHolder.setText(item.getContent(), R.id.edit_item_text);
    }
}
