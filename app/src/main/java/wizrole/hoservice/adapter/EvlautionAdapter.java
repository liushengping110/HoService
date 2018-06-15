package wizrole.hoservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.activity.ImageLookActivity;
import wizrole.hoservice.life.model.evaluation.CommentList;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class EvlautionAdapter extends RecyclerView.Adapter<EvlautionAdapter.EvlauationViewHolder>
implements EvlauationImageAdapter.GridItemListener{

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private View mHeaderView;
    private View mFooterView;
    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


    public Context context;
    public List<CommentList> commentLists;
    public EvlautionAdapter(Context context, List<CommentList> commentLists){
        this.commentLists=commentLists;
        this.context=context;
    }

    @Override
    public EvlauationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new EvlauationViewHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new EvlauationViewHolder(mFooterView);
        }
        return new EvlauationViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_eclaution_item,null));
    }

    @Override
    public void onBindViewHolder(EvlauationViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof EvlauationViewHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
//                ((EvlauationViewHolder) holder).text_evlau_name.setText(commentLists.get(position-1).getCommentHostName());
                ImageLoading.commonRound(context, Constant.ip_temp+commentLists.get(position-1).getCommentHostHeadPic(),holder.img_evlau_header);
                holder.text_evlau_name.setText(commentLists.get(position-1).getCommentHostName());
                holder.img_evlau_content.setText(commentLists.get(position-1).getCommentContent());
                holder.img_evlau_time.setText(commentLists.get(position-1).getCommentTime());
                if(commentLists.get(position-1).getCommentPicList().size()>0){
                    holder.grid_evlau_view.setVisibility(View.VISIBLE);
                    EvlauationImageAdapter adapter=new EvlauationImageAdapter(context,commentLists.get(position-1).getCommentPicList());
                    holder.grid_evlau_view.setAdapter(adapter);
                    adapter.setGridItemListener(position-1,this);
                }else{
                    holder.grid_evlau_view.setVisibility(View.INVISIBLE);
                }
                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }




    /**
     * //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
      * @return
     */
    @Override
    public int getItemCount() {
//        return commentLists.size();

        if(mHeaderView == null && mFooterView == null){
            return commentLists.size();
        }else if(mHeaderView == null && mFooterView != null){
            return commentLists.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return commentLists.size() + 1;
        }else {
            return commentLists.size() + 2;
        }
    }

    @Override
    public void ItemClick(int position_cate,int position) {
        Intent intent=new Intent(context, ImageLookActivity.class);
        intent.putExtra("url", Constant.ip_temp+commentLists.get(position_cate).getCommentPicList().get(position));
        context.startActivity(intent);
    }

    public class EvlauationViewHolder extends RecyclerView.ViewHolder {
        ImageView img_evlau_header;
        TextView text_evlau_name;
        TextView img_evlau_time;
        TextView img_evlau_content;
        GridView grid_evlau_view;
        public EvlauationViewHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }
            img_evlau_header = (ImageView) itemView.findViewById(R.id.img_evlau_header);
            text_evlau_name = (TextView) itemView.findViewById(R.id.text_evlau_name);
            img_evlau_time = (TextView) itemView.findViewById(R.id.img_evlau_time);
            img_evlau_content = (TextView) itemView.findViewById(R.id.img_evlau_content);
            grid_evlau_view = (GridView) itemView.findViewById(R.id.grid_evlau_view);
            }
        }
}
