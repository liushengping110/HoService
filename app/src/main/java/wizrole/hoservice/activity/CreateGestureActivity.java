package wizrole.hoservice.activity;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wizrole.hoservice.R;
import wizrole.hoservice.base.BaseActivity;
import wizrole.hoservice.fragment.Fragment_me;
import wizrole.hoservice.util.SharedPreferenceUtil;
import wizrole.hoservice.util.Tol_util;
import wizrole.hoservice.util.cache.ACache;

/**
 * create gesture activity
 * Created by Sym on 2015/12/23.
 */
public class CreateGestureActivity extends BaseActivity {

	@BindView(R.id.lockPatterIndicator)LockPatternIndicator lockPatternIndicator;
	@BindView(R.id.lockPatternView) LockPatternView lockPatternView;
	@BindView(R.id.resetBtn) TextView resetBtn;
	@BindView(R.id.messageTv) TextView messageTv;
	@BindView(R.id.text_title) TextView text_title;
	@BindView(R.id.lin_back) LinearLayout lin_back;
	private List<LockPatternView.Cell> mChosenPattern = null;
	private ACache aCache;
	private static final long DELAYTIME = 600L;
	private static final String TAG = "CreateGestureActivity";
	public String change;

	@Override
	protected int getLayout() {
		return R.layout.activity_creategesture;
	}

	@Override
	protected void initData() {
		ButterKnife.bind(this);
		change = getIntent().getStringExtra("change");//如果为null，就是从 登录进入，如果不为null，就为设置中心进入
		text_title.setText(getString(R.string.lock_mima));
		init();
	}
	public static Handler handler;
	@Override
	protected void setListener() {
		lin_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (change != null) {   //说明是从【设置中心】--或者【设置中心验证后】进入的
					finish();
				} else {  //否则就是从登录页面进入的
					handler = Fragment_me.handler;
					handler.sendEmptyMessage(9999);
					finish();
				}
			}
		});
	}

	private void init() {
		aCache = ACache.get(CreateGestureActivity.this);
		lockPatternView.setOnPatternListener(patternListener);
	}

	/**
	 * 手势监听
	 */
	private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			lockPatternView.removePostClearPatternRunnable();
			//updateStatus(Status.DEFAULT, null);
			lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
		}

		@Override
		public void onPatternComplete(List<LockPatternView.Cell> pattern) {
			//Log.e(TAG, "--onPatternDetected--");
			if(mChosenPattern == null && pattern.size() >= 4) {
				mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
				updateStatus(Status.CORRECT, pattern);
			} else if (mChosenPattern == null && pattern.size() < 4) {
				updateStatus(Status.LESSERROR, pattern);
			} else if (mChosenPattern != null) {
				if (mChosenPattern.equals(pattern)) {
					updateStatus(Status.CONFIRMCORRECT, pattern);
				} else {
					updateStatus(Status.CONFIRMERROR, pattern);
				}
			}
		}
	};

	/**
	 * 更新状态
	 * @param status
	 * @param pattern
     */
	private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
		messageTv.setTextColor(getResources().getColor(status.colorId));
		messageTv.setText(status.strId);
		switch (status) {
			case DEFAULT:
				resetBtn.setTextColor(getResources().getColor(R.color.danhui));
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CORRECT:
				resetBtn.setTextColor(getResources().getColor(R.color.text_fz));
				updateLockPatternIndicator();
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case LESSERROR:
				resetBtn.setTextColor(getResources().getColor(R.color.text_fz));
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CONFIRMERROR:
				resetBtn.setTextColor(getResources().getColor(R.color.text_fz));
				lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
				lockPatternView.postClearPatternRunnable(DELAYTIME);
				break;
			case CONFIRMCORRECT:
				saveChosenPattern(pattern);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				setLockPatternSuccess();
				break;
		}
	}

	/**
	 * 更新 Indicator
	 */
	private void updateLockPatternIndicator() {
		if (mChosenPattern == null)
			return;
		lockPatternIndicator.setIndicator(mChosenPattern);
	}

	/**
	 * 重新设置手势
	 */
	@OnClick(R.id.resetBtn)
	public void onClick(View v){
		switch (v.getId()){
			case R.id.resetBtn:
				mChosenPattern = null;
				lockPatternIndicator.setDefaultIndicator();
				updateStatus(Status.DEFAULT, null);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
		}
	}


	/**
	 * 成功设置了手势密码(跳到首页)
     */
	private void setLockPatternSuccess() {
		//就默认设置打开
		Tol_util.setCheck(CreateGestureActivity.this, Tol_util.OPEN_MESS, true);
		if (change != null) {  //从设置中心进入
			finish();
		} else {  //从登录页面进入
			handler = Fragment_me.handler;
			handler.sendEmptyMessage(9999);
			finish();
		}
		MyToast(getString(R.string.set_mima_ok));
	}

	/**
	 * 保存手势密码
	 */
	private void saveChosenPattern(List<LockPatternView.Cell> cells) {
		byte[] bytes = LockPatternUtil.patternToHash(cells);
//		aCache.put(Constant.GESTURE_PASSWORD, bytes);
		try{
			String str = new String(bytes,"UTF-8");
			SharedPreferenceUtil.setMima(CreateGestureActivity.this,str);//存入
		}catch (UnsupportedEncodingException e){

		}
	}

	private enum Status {
		//默认的状态，刚开始的时候（初始化状态）
		DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
		//第一次记录成功
		CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
		//连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
		LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
		//二次确认错误
		CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
		//二次确认正确
		CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

		private Status(int strId, int colorId) {
			this.strId = strId;
			this.colorId = colorId;
		}
		private int strId;
		private int colorId;
	}

	@Override
	public void onBackPressed() {
		if (change == null) {    //登录进入，
			handler = Fragment_me.handler;
			handler.sendEmptyMessage(9999);
			finish();
		} else {//说明是从设置中心进入的
			finish();
		}
		super.onBackPressed();
	}
}
