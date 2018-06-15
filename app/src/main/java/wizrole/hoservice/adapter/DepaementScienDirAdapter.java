package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.ObstetricsGynecology;

/**
 * Created by a on 2017/8/28.
 * 妇产科学目录适配器
 */

public class DepaementScienDirAdapter extends ConcreteAdapter<ObstetricsGynecology>{

    public DepaementScienDirAdapter(Context context, List<ObstetricsGynecology> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ObstetricsGynecology item, int position) {
        viewHolder.setText(item.getChapterName(), R.id.text_depare_name);
    }
}
