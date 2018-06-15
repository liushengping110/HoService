package wizrole.hoservice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import wizrole.hoservice.R;
import wizrole.hoservice.beam.HosNews;
import wizrole.hoservice.util.ImageLoading;
import wizrole.hoservice.util.ToastUtil;

/**
 * 院级  科级   生活级 新闻页面网络连接失败适配器（默认加载图片，不执行点击展示详情页面）
 */

public class NewsViewpNetErrAdapter extends PagerAdapter {

    private List<Integer> url;
    private Context mContext;
    public NewsViewpNetErrAdapter(Context context, List<Integer> url) {
        mContext = context;
        this.url = url;
    }

    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 必须要实现的方法
     * 每次滑动的时实例化一个页面,ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将实例化第4个页面
     **/
    public  ImageView imageView;
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        imageView = new ImageView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView .setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ((ViewPager) container).addView(imageView);
        ImageLoading.common(mContext,url.get(position),imageView, R.drawable.viewpager_load_fail);
        imageView.setOnClickListener(new onClickListener(position));
        return imageView;
    }


    /**
     * 必须要实现的方法
     * 滑动切换的时销毁一个页面，ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将销毁第1个页面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub;
        ImageView imageView = (ImageView) object;
        if (imageView == null)
            return;
        Glide.clear(imageView);     //核心，解决OOM
        releaseImageViewResouce(imageView);
        ((ViewPager) container).removeView(imageView);
    }

    class onClickListener implements View.OnClickListener{
        public int position;
        public onClickListener(int position){
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            ToastUtil.MyToast(mContext,"当前网络连接失败，请检查网络");
        }
    }

    /**
     * 释放资源
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        System.gc();
    }
}