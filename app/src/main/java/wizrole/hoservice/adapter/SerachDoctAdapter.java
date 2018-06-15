package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.DoctorInfos;

/**
 * Created by a on 2017/9/12.
 * 关键字搜索返回科室信息
 */

public class SerachDoctAdapter extends ConcreteAdapter<DoctorInfos>{

    public SerachDoctAdapter(Context context, List<DoctorInfos> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DoctorInfos item, int position) {
        viewHolder.setText(item.getDocName(), R.id.text_pop_item_deparent);
    }
}
