package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.DiseaseName;

/**
 * 健康科普适配器
 */

public class HealScien_adapter extends ConcreteAdapter<DiseaseName>{


    public HealScien_adapter(Context context, List<DiseaseName> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DiseaseName item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path, R.id.img_healscien_img,R.drawable.img_loadfail).
                setText(item.getDiseaseDetailName(),R.id.text_healscien_title).
                setText(Html.fromHtml(item.getDisOverview()).toString(),R.id.text_healscien_content);
     }
    }
