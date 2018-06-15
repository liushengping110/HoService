package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.DiseaseName;

/**
 * Created by a on 2017/8/28.
 * 疾病科 --病种列表
 */

public class DiseaseNameAdapter extends ConcreteAdapter<DiseaseName>{

    public DiseaseNameAdapter(Context context, List<DiseaseName> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DiseaseName item, int position) {
        viewHolder.setText(item.getDiseaseDetailName(), R.id.text_depare_name);
    }
}
