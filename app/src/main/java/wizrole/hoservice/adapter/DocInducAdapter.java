package wizrole.hoservice.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.DoctorInfos;

/**
 * Created by a on 2017/6/7.
 */

public class DocInducAdapter extends ConcreteAdapter<DoctorInfos>{

    public DocInducAdapter(Context context, List<DoctorInfos> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, DoctorInfos item, int position) {
//        外网   http://61.177.223.85:8080//hospitalbgsite//image//doctor//all//王临光.jpg
//        内网   http://172.16.1.159:8080//hospitalbgsite//image//doctor//all//王临光.jpg
        String waiWangIP="61.177.223.85";
        String neiWangIP="172.16.1.159";
        String url=item.getDocHeadImage().replaceAll(neiWangIP,waiWangIP);
    viewHolder.setImageView(url, R.id.doc_header,R.drawable.per_infor)//头像
            .setText(item.getDocName(), R.id.doc_name)//名字
            .setText(item.getRecordSchool(), R.id.doc_RecordSchool)//学历
            .setText(item.getDepartment(), R.id.doc_department)//科室
            .setText(item.getPosition(), R.id.doc_docPosition)//职位
            .setText(item.getIntrduce(),R.id.doc_intrduce)//详细介绍
            .setText(item.getTitle(), R.id.doc_title);//职称
        //如果内容不存在，则隐藏该控件，不占位
        if(item.getRecordSchool()==null){
            viewHolder.setVil(R.id.doc_RecordSchool);
        }else if(item.getPosition()==null){
            viewHolder.setVil(R.id.doc_docPosition);
        }else if(item.getTitle()==null){
            viewHolder.setVil(R.id.doc_title);
        }
    }
}
