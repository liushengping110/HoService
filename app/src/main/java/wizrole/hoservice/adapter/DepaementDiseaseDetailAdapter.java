package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DiseaseDeparDetailName;
import wizrole.hoservice.beam.DiseaseDeparent;

/**
 * Created by a on 2017/8/28.
 * 疾病库--详细科室列表适配器
 */

public class DepaementDiseaseDetailAdapter extends ConcreteAdapter<DiseaseDeparDetailName>{

    public DepaementDiseaseDetailAdapter(Context context, List<DiseaseDeparDetailName> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DiseaseDeparDetailName item, int position) {
        viewHolder.setText(item.getDiseaseDeparDetailName(), R.id.text_depare_name);
    }
}
