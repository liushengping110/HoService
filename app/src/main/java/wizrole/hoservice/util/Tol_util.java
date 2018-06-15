package wizrole.hoservice.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 存储开关的开---关 状态
 * Created by a on 2016/12/14.
 */

public class Tol_util {

    /**开关打开*/
    public static final String OPEN_MESS= "mess_open";//手势密码保存标记
    public static final String OPEN_LOGIN= "open_login";//登录保存标记
    /**
     * 获取配置
     * @param context
     * @param name
     * @param isCheck
     * @return
     */
    public static boolean get(Context context, String name, boolean isCheck) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = prefs.getBoolean(name, isCheck);
        return value;
    }

    /**
     * 保存用户配置
     * @param context
     * @param name
     * @param value
     * @return
     */
    public static boolean set(Context context, String name, boolean value) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        return editor.commit(); //提交
    }
    /**
     * 获取用户自动登录设置
     * @param context
     * @param isCheck
     */
    public static boolean getCheck(Context context, String name, boolean isCheck) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = prefs.getBoolean(name, isCheck);
        return value;
    }
    /**
     * 保存用户自动登录设置
     * @param context
     * @param value
     */
    public static boolean setCheck(Context context, String name, boolean value) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        return editor.commit(); //提交
    }
}
