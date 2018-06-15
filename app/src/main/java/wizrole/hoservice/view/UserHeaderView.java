package wizrole.hoservice.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.internal.pathview.PathsView;

import wizrole.hoservice.R;


/**
 * Created by liushengping on 2018/3/5.
 * 何人执笔？
 * 购物车刷新
 */

public class UserHeaderView extends LinearLayout implements RefreshHeader {

    public UserHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public UserHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public UserHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    public ElmRefreshView refreshView;
    public View view;
    public void initView(Context content){
        view= LayoutInflater.from(content).inflate(R.layout.header_view, this);
        refreshView=(ElmRefreshView)view.findViewById(R.id.view_fresh);
    }


    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        final int height = refreshView.getHeight() / 2;
        float offset_temp = (offset - height) *1.0f;
        if(offset_temp < 0){
            offset_temp = 0;
        }
        refreshView.setPullPositionChanged(offset_temp / (view.getHeight() - height));
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {

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

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (success){

        } else {

        }
        refreshView.setStatus(ElmRefreshView.STATUS_STOP);
        refreshView.setStatus(ElmRefreshView.STATUS_MOVING);
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
            case PullDownToRefresh://下拉开始刷新
                break;
            case Refreshing://正在刷新
                refreshView.setStatus(ElmRefreshView.STATUS_RUNNING);
                break;
            case ReleaseToRefresh://释放立即刷新
                break;
        }
    }
}
