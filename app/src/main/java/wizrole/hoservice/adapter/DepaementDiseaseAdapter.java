package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.DiseaseDeparent;

/**
 * Created by a on 2017/8/28.
 * 疾病库--科室列表适配器
 */

public class DepaementDiseaseAdapter extends ConcreteAdapter<DiseaseDeparent>{

    public DepaementDiseaseAdapter(Context context, List<DiseaseDeparent> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DiseaseDeparent item, int position) {
        viewHolder.setText(item.getDiseaseDeparName(), R.id.text_depare_name);
    }
}
