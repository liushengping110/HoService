package wizrole.hoservice.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wizrole.hoservice.R;

public class ButtonMark extends LinearLayout {
    private int count;
    private Context context;
    private ViewPager viewPager;
    private LinearLayout ll_text;
    private LinearLayout ll_line;
    public String tabone_Msg;
    public String tabtwo_msg;
    public int width;
    public ButtonMark(Context context,int count,ViewPager viewPager,
                      LinearLayout ll_text,LinearLayout ll_line,String tabone_Msg,String tabtwo_msg ) {
        super(context);
        this.count=count;
        this.context=context;
        this.viewPager=viewPager;
        this.ll_text=ll_text;
        this.ll_line=ll_line;
        this.tabone_Msg=tabone_Msg;
        this.tabtwo_msg=tabtwo_msg;
    }

    public View initLinear(){
        View lineView=null;
        width=((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        LayoutParams btn_lp=new LayoutParams(width/count, 100);
        LayoutParams line_lp=new LayoutParams(width/count-200, 5);
        btn_lp.setMargins(2, 2, 2, 2);
        for (int i = 0; i < count; i++) {
            TextView textView=new TextView(context);
            textView.setLayoutParams(btn_lp);
            if(i==0){
                textView.setText(tabone_Msg);
            }else if(i==1){
                textView.setText(tabtwo_msg);
            }
            textView.setTextAppearance(context,R.style.TabLayoutTextStyle);
            textView.setGravity(Gravity.CENTER);
            ll_text.addView(textView);

            if (i==0) {
                textView.setTextColor(getResources().getColor(R.color.text_bule));
            }
            textView.setOnClickListener(new BtnClickListener(i));
            View view=new View(context);
            view.setLayoutParams(line_lp);
            if (i==0) {
                view.setBackgroundColor(getResources().getColor(R.color.text_bule));
                lineView=view;
            }
            ll_line.addView(view);
        }
        return lineView;
    }


    class BtnClickListener implements OnClickListener{
        private int position;
        public BtnClickListener(int position) {
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            currentBtn(position);
            viewPager.setCurrentItem(position);
        }
    }

    public void currentBtn(int position){
        for (int i = 0; i < ll_text.getChildCount(); i++) {
            TextView textView=(TextView) ll_text.getChildAt(i);
            if (position==i) {
                textView.setTextColor(getResources().getColor(R.color.text_bule));
            }else{
                textView.setTextColor(Color.BLACK);
            }
        }
    }
}
