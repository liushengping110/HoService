package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.SurgeryDetailInfor;

/**
 * Created by a on 2017/8/28.
 * 手术名称列表适配器
 */

public class SurgeryNameAdapter extends ConcreteAdapter<SurgeryDetailInfor>{

    public SurgeryNameAdapter(Context context, List<SurgeryDetailInfor> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SurgeryDetailInfor item, int position) {
        viewHolder.setText(item.getSurgeryName(), R.id.text_depare_name);
    }
}
