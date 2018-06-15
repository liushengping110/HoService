package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.SurgeryDeparent;

/**
 * Created by a on 2017/8/28.
 * 手术
 * 科室列表适配器
 */

public class SurgerytAdapter extends ConcreteAdapter<SurgeryDeparent>{

    public SurgerytAdapter(Context context, List<SurgeryDeparent> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SurgeryDeparent item, int position) {
        viewHolder.setText(item.getSurgeryDeparentName(), R.id.text_depare_name);
    }
}
