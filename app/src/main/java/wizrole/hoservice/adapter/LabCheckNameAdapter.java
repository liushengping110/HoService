package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DiseaseName;
import wizrole.hoservice.beam.LabCheckName;

/**
 * Created by a on 2017/8/28.
 * 疾病科 --病种列表
 */

public class LabCheckNameAdapter extends ConcreteAdapter<LabCheckName>{

    public LabCheckNameAdapter(Context context, List<LabCheckName> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, LabCheckName item, int position) {
        viewHolder.setText(item.getLabCheckName(), R.id.text_depare_name);
    }
}
