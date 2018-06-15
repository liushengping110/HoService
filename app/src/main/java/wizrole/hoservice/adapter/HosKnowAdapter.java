package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DeparentIntrduce;
import wizrole.hoservice.beam.HosKnow;
import wizrole.hoservice.beam.HospNotesList;

/**
 * Created by a on 2017/8/28.
 * 住院须知列表适配器
 */

public class HosKnowAdapter extends ConcreteAdapter<HospNotesList>{

    public HosKnowAdapter(Context context, List<HospNotesList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HospNotesList item, int position) {
        viewHolder.setText(item.getHospNotesTitle(), R.id.text_depare_name);
    }
}
