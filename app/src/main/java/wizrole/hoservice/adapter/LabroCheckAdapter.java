package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.LaboratCheckCate;

/**
 * Created by a on 2017/8/28.
 * 化验检查适配器
 */

public class LabroCheckAdapter extends ConcreteAdapter<LaboratCheckCate>{

    public LabroCheckAdapter(Context context, List<LaboratCheckCate> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, LaboratCheckCate item, int position) {
        viewHolder.setText(item.getLaboratCheckCateName(), R.id.text_depare_name);
    }
}
