package wizrole.hoservice.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.android.exoplayer.C;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.List;


/**
 * Created by a on 2016/12/6.
 */

public class SharedPreferenceUtil {
    public static final String  name ="name";//手势密码标签


    /**保存用户登记号*/
    public static void saveLoginNum(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("num", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_num", name);
        editor.commit();
    }
    /**获取用户登记号*/
    public static String getLoginNum(Context context){
        SharedPreferences sp=context.getSharedPreferences("num", Context.MODE_PRIVATE);
        return sp.getString("user_num", "");
    }
    /**
     * 清除用户登记号
     * @param context
     */
    public static void clearLoginNum(Context context){
        SharedPreferences sp = context.getSharedPreferences("num", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
    /**保存用户手机号码*/
    public static void saveTel(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("tel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_tel", name);
        editor.commit();
    }
    /**获取用户手机号码*/
    public static String getTel(Context context){
        SharedPreferences sp=context.getSharedPreferences("tel", Context.MODE_PRIVATE);
        return sp.getString("user_tel", "");
    }
    /**
     * 清除用户手机号码
     * @param context
     */
    public static void clearTel(Context context){
        SharedPreferences sp = context.getSharedPreferences("tel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 存储病人详细信息
     * 存放实体类以及任意类型
     * @param context 上下文对象
     * @param obj
     */
    public static void putBean(Context context,Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences sp=context.getSharedPreferences("person", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sp.edit();
                editor.putString("pf", string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    /**
     * 获取病人详细信息
     * @param context
     * @return
     */
    public static Object getBean(Context context) {
        Object obj = null;
        try {
            SharedPreferences sp=context.getSharedPreferences("person", Context.MODE_PRIVATE);
            String base64 = sp.getString("pf", "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * 清除用户信息实体类
     * @param context
     */
    public static void clearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences("person", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**保存用户登陆状态*/
    public static void saveLoginState(Context context,int status){
        SharedPreferences sp=context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("success", status);
        editor.commit();
    }
    /**获取用户登陆状态*/
    public static int getLoginState(Context context){
        SharedPreferences sp=context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        return sp.getInt("success", 2);
    }
    /**
     * 清除用户登录状态
     * @param context
     */
    public static void clearLoginState(Context context){
        SharedPreferences sp = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取手势密码
     * @param context
     * @return
     */
    public static String getMama(Context context) {
        SharedPreferences sharedPreferences =context. getSharedPreferences("mima",Context.MODE_PRIVATE);
        String string = sharedPreferences.getString("user_mima", "");
        return string;
    }

    /**
     * 设置手势密码
     * @param context
     * @param name
     */
    public static void setMima(Context context,String name) {
        SharedPreferences sharedPreferences =context. getSharedPreferences("mima", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_mima", name);
        editor.commit();
    }
    /**
     * 清除用户手势密码
     * @param context
     */
    public static void clearMima(Context context){
        SharedPreferences sp = context.getSharedPreferences("mima", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**保存用户头像*/
    public static void savePersonHeader(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("header", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_header", name);
        editor.commit();
    }
    /**获取用户头像*/
    public static String getPersonHeader(Context context){
        SharedPreferences sp=context.getSharedPreferences("header", Context.MODE_PRIVATE);
        return sp.getString("user_header", "");
    }
    /**
     * 清除用户头像
     * @param context
     */
    public static void clearPersonHeader(Context context){
        SharedPreferences sp = context.getSharedPreferences("header", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 存放实体类以及任意类型
     *  订餐人地址
     * @param context 上下文对象
     * @param obj
     */
    public static void putAdd(Context context, Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences sp=context.getSharedPreferences("selAddress", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sp.edit();
                editor.putString("sd", string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    /**
     *  订餐人地址
     * @param context
     * @return
     */
    public static Object getAdd(Context context) {
        Object obj = null;
        try {
            SharedPreferences sp=context.getSharedPreferences("selAddress", Context.MODE_PRIVATE);
            String base64 = sp.getString("sd", "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 订餐人地址
     * @param context
     */
    public static void clearAdd(Context context){
        SharedPreferences sp = context.getSharedPreferences("selAddress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    /**保存用户上次查阅的号码*/
    public static void savePdfPagerNum(Context context, int name){
        SharedPreferences sp=context.getSharedPreferences("page_num", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("user_pager_num", name);
        editor.commit();
    }
    /**获取用户上次查阅的号码*/
    public static int getPdfPagerNum(Context context){
        SharedPreferences sp=context.getSharedPreferences("page_num", Context.MODE_PRIVATE);
        return sp.getInt("user_pager_num", 0);
    }
    /**
     * 清除用户上次查阅的号码
     * @param context
     */
    public static void clearPdfPagerNum(Context context){
        SharedPreferences sp = context.getSharedPreferences("page_num", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**保存是否从通知栏进入标志位*/
    public static void saveNotidic(Context context, int name){
        SharedPreferences sp=context.getSharedPreferences("notific_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("user_notific", name);
        editor.commit();
    }
    /**获取从通知栏进入标志位*/
    public static int getNotidic(Context context){
        SharedPreferences sp=context.getSharedPreferences("notific_status", Context.MODE_PRIVATE);
        return sp.getInt("user_notific", 0);
    }
    /**
     * 清除从通知栏进入标志位
     * @param context
     */
    public static void clearNotidic(Context context){
        SharedPreferences sp = context.getSharedPreferences("notific_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
