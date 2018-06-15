package wizrole.hoservice.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by Administrator on 2017/1/11.
 */

public interface Holder {

    <T extends View> T getView(int rid);

    Holder setText(String result, int rid);

    Holder setResources(int did, int rid);

    Holder setBitmap(Bitmap bitmap, int rid);

    Holder setOnClickListener(View.OnClickListener onClickListener, int rid);

    Holder setImageView(String url, int rid,int drawable);

    View getHoldView();
    /**隐藏控件*/
    Holder setVil(int rid);

}