package wizrole.hoservice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import wizrole.hoservice.R;
import wizrole.hoservice.util.ImageLoading;


/**
 * Created by Administrator on 2017/1/11.
 */

public class ViewHolder implements Holder {

    private Context context;
    private SparseArray<View> sparseArray;
    private View view;

    public ViewHolder(Context context, int itemLayout, ViewGroup viewGroup) {
        this.context = context;
        sparseArray = new SparseArray<>();
        view = LayoutInflater.from(context).inflate(itemLayout, viewGroup, false);
        view.setTag(this);
    }

    public static ViewHolder get(Context context, View view, ViewGroup viewGroup, int itemLayout) {
        if (view == null) {
            return new ViewHolder(context, itemLayout, viewGroup);
        }
        return (ViewHolder) view.getTag();
    }

    @Override
    public <T extends View> T getView(int rid) {
        View view = sparseArray.get(rid);
        if (view == null) {
            view = this.view.findViewById(rid);
            sparseArray.append(rid, view);
        }
        return (T) view;
    }

    @Override
    public Holder setText(String result, int rid) {
        TextView textView = getView(rid);
        textView.setText(result);
        return this;
    }

    @Override
    public Holder setResources(int did, int rid) {
        ImageView imageView = getView(rid);
        imageView.setImageResource(did);
        return this;
    }

    @Override
    public Holder setBitmap(Bitmap bitmap, int rid) {
        ImageView imageView = getView(rid);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    @Override
    public Holder setOnClickListener(View.OnClickListener onClickListener, int rid) {
        View view = getView(rid);
        view.setOnClickListener(onClickListener);
        return this;
    }

    @Override
    public Holder setImageView(String url, int rid,int drawabele) {
        ImageLoading.common(context,url,(ImageView) getView(rid), drawabele);
        return this;
    }


    @Override
    public View getHoldView() {
        return view;
    }

    /***隐藏控件*/
    @Override
    public Holder setVil(int rid ) {
        TextView textView=(TextView)getHoldView().findViewById(rid);
        textView.setVisibility(View.GONE);
//        getView(rid).setVisibility(View.GONE);
        return this;
    }
}
