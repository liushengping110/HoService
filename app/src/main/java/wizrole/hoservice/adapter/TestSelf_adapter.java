package wizrole.hoservice.adapter;

import android.content.Context;
import android.text.Html;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.CustMessage;
import wizrole.hoservice.beam.TestSelfCateDetail;

/**
 * 智能导诊
 */

public class TestSelf_adapter extends ConcreteAdapter<TestSelfCateDetail>{


    public TestSelf_adapter(Context context, List<TestSelfCateDetail> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, TestSelfCateDetail item, int position) {
        viewHolder.setImageView(Constant.ip+Constant.samllImg_path, R.id.img_healscien_img,R.drawable.img_loadfail).
                setText(item.getTestCateDetailName(),R.id.text_healscien_title).
                setText(Html.fromHtml(item.getTestCateDetailIntrduce()).toString(),R.id.text_healscien_content);
     }
    }
