package wizrole.hoservice.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import wizrole.hoservice.R;


/**
 * Created by liushengping on 2018/3/5.
 * 何人执笔？
 */

public class WhiteBgFooterView extends LinearLayout implements RefreshFooter{
    public WhiteBgFooterView(Context context) {
        super(context);
        initView(context);
    }

    public WhiteBgFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public WhiteBgFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private TextView mHeaderText;//标题文本
    private ImageView mProgressView;//刷新动画视图
    private ProgressDrawable mProgressDrawable;//刷新动画

    public void initView(Context context){
        setGravity(Gravity.CENTER);
        mHeaderText = new TextView(context);//提示文本
        mHeaderText.setTextColor(getResources().getColor(R.color.text_bule));
        mHeaderText.setTextAppearance(context,R.style.TabLayoutTextStyle);
        mProgressDrawable = new ProgressDrawable();//旋转动画
        mProgressView = new ImageView(context);//动画视图
        mProgressView.setColorFilter(getResources().getColor(R.color.text_bule));
        mProgressView.setImageDrawable(mProgressDrawable);
        addView(mProgressView, DensityUtil.dp2px(15), DensityUtil.dp2px(15));
        addView(new View(context), DensityUtil.dp2px(15), DensityUtil.dp2px(15));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));

    }
    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {

    }

    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        mProgressDrawable.start();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mProgressDrawable.stop();//停止动画
        mProgressView.setVisibility(View.VISIBLE);
        if (success){
            mHeaderText.setText("加载完成");
        } else {
            mHeaderText.setText("加载失败,请检查网络");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                break;
            case PullToUpLoad:
                mHeaderText.setText("上拉开始加载");
                mProgressView.setVisibility(GONE);//隐藏动画
                break;
            case Loading:
                mHeaderText.setText("正在加载");
                mProgressView.setVisibility(VISIBLE);//显示加载动画
                break;
            case ReleaseToLoad:
                mHeaderText.setText("释放立即加载");
                break;
        }
    }
}
