package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getsecondtypename.TwoTypeList;

/**
 * Created by 54727 on 2018/3/29.
 */

public class SuperMarketTypeAdapter extends ConcreteAdapter<TwoTypeList> {
    public SuperMarketTypeAdapter(Context context, List<TwoTypeList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, TwoTypeList item, int position) {
        viewHolder.setText(item.getTwoTypeName(), R.id.item_text);
    }
}
