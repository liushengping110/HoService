package wizrole.hoservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ScrollView;

import wizrole.hoservice.interface_base.Pullable;

public class PullableScrollView extends ScrollView implements Pullable {

	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp(boolean isPullUp)
	{
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight())&&isPullUp)
			return true;
		else
			return false;
	}


	private OnScrollListener listener;

	// 滑动距离监听器
	public interface OnScrollListener{
		/**
		 * 在滑动的时候调用，scrollY为已滑动的距离
		 */
		void onScroll(int scrollY);
	}


	public void setOnScrollListener(OnScrollListener listener) {
		this.listener = listener;
	}




	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(listener != null){
			listener.onScroll(t);
		}
}
}
