package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.beam.VedioList;
import wizrole.hoservice.util.ImageLoading;

/**
 * Created by ${liushengping} on 2017/9/29.
 * 何人执笔？
 * 健康视频适配器
 */

public class HealVedioAdapter extends BaseAdapter{

    public List<VedioList> datails;
    public Context context;

    public HealVedioAdapter(Context context,List<VedioList> datails) {
        this.context = context;
        this.datails=datails;
    }

    @Override
    public int getCount() {

        return datails.size();
    }

    @Override
    public Object getItem(int position) {
        return datails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder{
        ImageView video_img;
        TextView text_video_title;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (null == convertView) {
            holder = new Holder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.list_item_videoview, null);
            holder.video_img=(ImageView)convertView.findViewById(R.id.img_video_logo);
            holder.text_video_title=(TextView)convertView.findViewById(R.id.text_video_title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        VedioList vedioList=datails.get(position);
        /*正式访问*/
        ImageLoading.common(context, Constant.ip+vedioList.getVedioImgUrl(),holder.video_img,R.drawable.vedioloadfail);
        holder.text_video_title.setText(vedioList.getVedioTitle());
        return convertView;
    }
}
