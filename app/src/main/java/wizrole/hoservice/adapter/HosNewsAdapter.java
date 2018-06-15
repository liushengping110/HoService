package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.HosNews;

/**
 * Created by liushengping on 2017/11/3/003.
 * 何人执笔？
 * 院级新闻列表
 */

public class HosNewsAdapter extends ConcreteAdapter<HosNews> {
    public HosNewsAdapter(Context context, List<HosNews> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, HosNews item, int position) {
        String url=item.getNews_SamllImg();
        viewHolder.setImageView(Constant.ip+url, R.id.img_healscien_img,R.drawable.img_loadfail).
                setText(Html.fromHtml(item.getNews_title()).toString(),R.id.text_healscien_title).
                setText(Html.fromHtml(item.getNews_msg()).toString(),R.id.text_healscien_content);
    }
}
