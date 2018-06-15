package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;

/**
 * Created by a on 2017/8/29.
 */

public class DeparentTitleAdapter extends ConcreteAdapter<String>{

    public DeparentTitleAdapter(Context context, List<String> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        viewHolder.setText(item.toString(), R.id.text_pop_item_deparent);
    }
}
