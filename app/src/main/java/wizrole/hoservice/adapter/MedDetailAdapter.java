package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.MedItem;

/**
 * Created by a on 2017/9/19.
 * 药品费用查询适配器
 */

public class MedDetailAdapter extends ConcreteAdapter<MedItem> {

    public MedDetailAdapter(Context context, List<MedItem> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MedItem item, int position) {
        viewHolder.setText(item.getMedCate(), R.id.med_cate)
                .setText(item.getItemDesc(),R.id.med_desc)
                .setText(item.getUom(),R.id.med_uom)
                .setText(item.getDrugForm(),R.id.med_form)
                .setText(item.getPrice()+"",R.id.med_price)
                .setText(item.getFactory(),R.id.med_factory);
    }
}
