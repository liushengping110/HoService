package wizrole.hoservice.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import wizrole.hoservice.R;

/**
 * Created by Administrator on 2017/3/10.
 */

public class ImageLoading {

    /**
     * url
     * @param activity
     * @param url
     * @param imageView
     * @param bitmap
     */
    public static void common(Context activity, String url, ImageView imageView, int bitmap) {
        if(activity!=null){
            Glide.with(activity)
                    .load(url)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(bitmap)
                    .into(imageView);
        }
    }

    /**
     * path
     * @param activity
     * @param path
     * @param imageView
     */
    public static void common(Context activity,String path,ImageView imageView){
        if(activity!=null){
            Glide.with(activity)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.drawable.img_loadfail)           //设置错误图片
                    .placeholder(R.drawable.img_loadfail)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }
    }

    /**
     * rid
     * @param activity
     * @param rid
     * @param imageView
     * @param bitmap
     */
    public static void common(Context activity, int  rid, ImageView imageView, int bitmap) {
        if(activity!=null){
            Glide.with(activity)
                    .load(rid)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(bitmap)
                    .into(imageView);
        }
    }

    /**
     * url圆形
     * @param context
     * @param url
     * @param imageView
     */
    public static void commonRound(Context context,String url,ImageView imageView){
        if(context!=null){
            Glide.with(context).
                    load(url)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .crossFade(100)
                    .into(imageView);
        }
    }

    /**
     * drawable圆形
     * @param context
     * @param rid
     * @param imageView
     */
    public static void commonRound(Context context,int rid,ImageView imageView){
        if(context!=null){
            Glide.with(context).
                    load(rid)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .crossFade(100)
                    .into(imageView);
        }
    }


    /**
     * 高斯模糊
     * @param context
     * @param url
     * @param imageView
     */
    public static void commonBlurTeans(Context context,String url,ImageView imageView){
        if(context!=null){
            Glide.with(context)
                    .load(url)
                    .bitmapTransform(new BlurTransformation(context, 25))
                    .crossFade(100)
                    .into(imageView);
        }

    }
    /**
     * drawable高斯模糊
     * @param context
     * @param rid
     * @param imageView
     */
    public static void commonBlurTeans(Context context,int rid,ImageView imageView){
        if(context!=null){
            Glide.with(context)
                    .load(rid)
                    .bitmapTransform(new BlurTransformation(context, 25))
                    .crossFade(100)
                    .into(imageView);
        }
    }

    /**
     * view的背景高斯模糊
     * @param context
     * @param url
     * @param view
     */
    public static void commonViewBlurTeans(Context context, String url, final View view){
        if(context!=null){
            Glide.with(context)
                    .load(url)
//                .bitmapTransform(new BlurTransformation(context, 25))
//                .crossFade(100)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(180,180) {//设置宽高
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                view.setBackground(drawable);//设置背景
                            }
                        }
                    });
        }
    }


    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }

    public static void setUri(Activity activity, String url, Bitmap bitmap, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .placeholder(new BitmapDrawable(bitmap))
                .into(imageView);
    }
    public static Bitmap getBitMap(Activity activity, String url) throws ExecutionException, InterruptedException {
        return Glide.with(activity)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(100, 100)
                .get();
    }

    public static  Bitmap getBit(Context context,String url) throws InterruptedException,ExecutionException{
        Bitmap myBitmap = Glide.with(context)
                .load(url)
                .asBitmap() //必须
                .centerCrop()
                .into(400, 400)
                .get();
        return myBitmap;
    }


    private static String getPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public String getRealFilePath(final Context context, final Uri uri, String path) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        path = data;
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }
}