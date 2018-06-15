package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.MedItem;
import wizrole.hoservice.beam.TarItem;

/**
 * Created by a on 2017/9/19.
 * 药品费用查询适配器
 */

public class Med_NoDetailAdapter extends ConcreteAdapter<TarItem> {

    public Med_NoDetailAdapter(Context context, List<TarItem> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, TarItem item, int position) {
        viewHolder.setText(item.getItemDesc(), R.id.med_no_name)
                .setText(item.getUom(),R.id.med_no_uom)
                .setText(item.getFactory(),R.id.med_no_factory)
                .setText(item.getPrice(),R.id.med_no_price);
    }
}
