package wizrole.hoservice.ui;


import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import wizrole.hoservice.R;

public class Point extends LinearLayout{
	private int pagerCount;
	private Context context;
	public Point(Context context, int pagerCount) {
		super(context);
		this.context=context;
		this.pagerCount=pagerCount;
		setPoint();
	}
	
	/**
	 * 根据页面数量，对比提示点的数量
	 */
	private void setPoint() {
		//设置宽高
		LayoutParams params= new LayoutParams(new LayoutParams(20,20,1));
		params.setMargins(2, 2, 2, 2);
		for (int i = 0; i < pagerCount; i++) {
			ImageView imageView=new ImageView(context);
			//给imageView设置布局属性
			imageView.setLayoutParams(params);
			if(i==0){	//默认第一张图片显示  则透明度显示最大
				imageView.setImageResource(R.drawable.select);
			}else{
				imageView.setImageResource(R.drawable.no_select);
			}
			this.addView(imageView);
		}
	}
	
	/*
	根据传入选中的页面下标，调整点的显示状态
	 */
	public void changePoint(int position){
		for (int i = 0; i < this.getChildCount(); i++) {
			//根据下标，获取子控件
			ImageView imageView=(ImageView)this.getChildAt(i);
			if(i==position){	//如果imageView的下标和页面的下标一致，就显示那个点的透明度
				imageView.setImageResource(R.drawable.select);
			}else{
				imageView.setImageResource(R.drawable.no_select);
			}
		}
	}
}
