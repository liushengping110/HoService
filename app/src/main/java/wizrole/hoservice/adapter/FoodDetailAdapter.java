package wizrole.hoservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import wizrole.hoservice.Constant;
import wizrole.hoservice.R;
import wizrole.hoservice.life.model.getgoodtype.CommodityList;
import wizrole.hoservice.life.model.getgoodtype.StoreCommodityType;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created  on 2017/3/1.
 */
public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private List<StoreCommodityType> foodTypeList;
    private List<CommodityList> foodDetailList = new ArrayList<>();
    private Context mContext;
    public FoodDetailAdapter(Context context, List<StoreCommodityType> foodList) {
        mContext = context;
        setFoodList(foodList);
    }

    public void setFoodList(List<StoreCommodityType> foodTypeList) {
        this.foodTypeList = foodTypeList;
        for(int i = 0; i< foodTypeList.size(); i++){
            if(foodTypeList !=null){
                foodDetailList.addAll(foodTypeList.get(i).getCommodityList());
            }
        }
        notifyDataSetChanged();
    }

    public List<StoreCommodityType> getFoodTypeList() {
        return foodTypeList;
    }

    @Override
    public int getItemCount() {
        return foodDetailList.size();
    }

    /**
     * 返回值相同会被默认为同一项
     * @param position
     * @return
     */
    @Override
    public long getHeaderId(int position) {
        return getSortType(position);
    }

    //获取当前饭菜的类型
    public int getSortType(int position) {
        int sort = -1;
        int sum = 0;
        for (int i = 0; i< foodTypeList.size(); i++){
            if(position>=sum){
                sort++;
            }else {
                return sort;
            }
            sum += foodTypeList.get(i).getCommodityList().size();
        }
        return sort;
    }

    /**
     * ===================================================================================================
     * header的ViewHolder
     * ===================================================================================================
     */
    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.header_detail_list, viewGroup, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        textView.setText(foodTypeList.get(getSortType(position)).getTypeName());
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    /**
     * ==================================================================================================
     * 以下为contentViewHolder
     * ==================================================================================================
     */
    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recy_item_detail, parent, false);
        return new ContentViewHolder(view,detailOnItemClick);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ContentViewHolder viewHolder = (ContentViewHolder) holder;

        viewHolder.text_name.setText(foodDetailList.get(position).getCommodityName());
        ImageLoading.common(mContext, Constant.ip_temp+foodDetailList.get(position).getCommodityPic(),viewHolder.image_logo,R.drawable.img_loadfail);
        viewHolder.text_monSaleNum.setText("108");
        viewHolder.tvGoodsDescription.setText(foodDetailList.get(position).getCommodityContent());
        viewHolder.tvGoodsPrice.setText(foodDetailList.get(position).getCommodityAmt()+"");
        viewHolder.text_number.setText(foodDetailList.get(position).getYiOrder()+"");
        if (foodDetailList.get(position).getYiOrder()>0){
            viewHolder.img_sub.setVisibility(View.VISIBLE);
            viewHolder.text_number.setVisibility(View.VISIBLE);
            viewHolder.text_number.setText(foodDetailList.get(position).getYiOrder()+"");
        }else{
            viewHolder.img_sub.setVisibility(View.INVISIBLE);
            viewHolder.text_number.setVisibility(View.INVISIBLE);
        }
        //添加
        viewHolder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailAddOnClick!=null) {
                    detailAddOnClick.detailAddOnClick(position);
                }
            }
        });
        //减少
        viewHolder.img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailSubOnClick!=null){
                    detailSubOnClick.detailSubOnClick(position);
                }
            }
        });
        //item点击
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailOnItemClick!=null){
                    detailOnItemClick.detailOnItemClick(view,position);
                }
            }
        });
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;//菜名
        ImageView image_logo;//菜图片
        ImageView img_add;//添加
        ImageView img_sub;//减少
        TextView text_number;//已点数量
        TextView tvGoodsPrice;//单价
        TextView tvGoodsDescription;//描述
        TextView text_monSaleNum;   //月售
        DetailOnItemClick detailOnItemClick;
        public ContentViewHolder(View itemView, DetailOnItemClick detailOnItemClick) {
            super(itemView);
        text_name = (TextView) itemView.findViewById(R.id.textview_detail);
        image_logo = (ImageView) itemView.findViewById(R.id.imageview_superlogo);
        img_add=(ImageView)itemView.findViewById(R.id.img_add);
        img_sub=(ImageView)itemView.findViewById(R.id.img_sub);
        text_number=(TextView)itemView.findViewById(R.id.text_number);
        tvGoodsPrice=(TextView)itemView.findViewById(R.id.tvGoodsPrice);
        tvGoodsDescription=(TextView)itemView.findViewById(R.id.tvGoodsDescription);
        text_monSaleNum=(TextView)itemView.findViewById(R.id.text_monSaleNum);

        }
    }

    //添加  --减少  item点击
    public DetailOnItemClick detailOnItemClick;
    public void setOnItemClickListener(DetailOnItemClick onItemClickListener) {
        this.detailOnItemClick = onItemClickListener;
    }
    public interface DetailOnItemClick{
        void detailOnItemClick(View view, int position);
    }


    public DetailAddOnClick detailAddOnClick;
    public void setDetailAddOnClick(DetailAddOnClick detailAddOnClick) {
        this.detailAddOnClick = detailAddOnClick;
    }
    public interface DetailAddOnClick{
        void detailAddOnClick(int position);
    }

    public detailSubOnClick   detailSubOnClick;
    public void setDetailSubOnClick(detailSubOnClick detailSubOnClick) {
        this.detailSubOnClick = detailSubOnClick;
    }
    public interface detailSubOnClick{
        void detailSubOnClick(int position);
    }
}
