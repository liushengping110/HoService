package wizrole.hoservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class EvlauationImageAdapter extends BaseAdapter {


    public Context context;
    public List<String> strings;
    public LayoutInflater inflater;
    public EvlauationImageAdapter(Context context, List<String> strings){
        this.context=context;
        this.strings=strings;
        inflater=LayoutInflater.from(context);
    }

//    @Override
//    protected void convert(ViewHolder viewHolder, String item, final int position) {
//        viewHolder.setImageView(Constant.ip+item, R.id.grid_img_evalua,R.drawable.img_loadfail).setOnClickListener(new ItemClick(position),position);
//    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder{
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.grid_evaluation_img,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.grid_img_evalua);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        String str=strings.get(position);
        ImageLoading.common(context,ip+str,holder.imageView,R.drawable.img_loadfail);
        holder.imageView.setOnClickListener(new ItemClick(position_cate,position));
        return convertView;
    }

    //图片添加ip
    public  final String ip="http://39.107.75.214/";

    public class ItemClick implements View.OnClickListener {
        public int position;
        public int position_cate;
        public ItemClick(int position_cate,int position){
            this.position=position;
            this.position_cate=position_cate;
        }
        @Override
        public void onClick(View v) {
            if(gridItemListener!=null){
                gridItemListener.ItemClick(position_cate,position);
            }
        }
    }
    public GridItemListener gridItemListener;
    public int position_cate;
    public interface GridItemListener{
        void ItemClick(int position_cate, int position);
    }
    public void setGridItemListener(int position_cate, GridItemListener gridItemListener) {
        this.gridItemListener = gridItemListener;
        this.position_cate=position_cate;
    }
}
