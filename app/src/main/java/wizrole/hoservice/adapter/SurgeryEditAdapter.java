package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DisHostry;
import wizrole.hoservice.beam.SurgeryHostry;

/**
 * Created by a on 2017/8/25.
 * 手术搜索历史纪录适配器
 */

public class SurgeryEditAdapter extends ConcreteAdapter<SurgeryHostry>{

    public SurgeryEditAdapter(Context context, List<SurgeryHostry> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SurgeryHostry item, int position) {
        viewHolder.setText(item.getContent(), R.id.edit_item_text);
    }
}
