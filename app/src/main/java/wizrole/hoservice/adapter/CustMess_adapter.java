package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.CustMessage;
import wizrole.hoservice.beam.DiseaseName;

/**
 * 健康科普适配器
 */

public class CustMess_adapter extends ConcreteAdapter<CustMessage>{


    public CustMess_adapter(Context context, List<CustMessage> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CustMessage item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path, R.id.img_healscien_img,R.drawable.img_loadfail).
                setText(item.getCustMess_title(),R.id.text_healscien_title).
                setText(Html.fromHtml(item.getCustMess_content()).toString(),R.id.text_healscien_content);
     }
    }
