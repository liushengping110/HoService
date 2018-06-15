package wizrole.hoservice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

/**
 * 网络连接工具类
 */
public class NetUtils {

  /**
   * 判断网络连接状况
   */
  public static boolean isNetworkConnected(Context context) {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  /**
   * 判断Wifi连接状况
   */
  public static boolean isWifiConnected(Context context) {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      if (mWiFiNetworkInfo != null) {
        return mWiFiNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  /**
   * 判断移动网络连接状况
   */
  public static boolean isMobileConnected(Context context) {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      if (mMobileNetworkInfo != null) {
        return mMobileNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  /**
   * 是否是飞行模式
   */
  public static boolean isAirplaneModeOn(Context context) {
    try {
      int airplaneModeSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
      return airplaneModeSetting == 1 ? true : false;
    } catch (SettingNotFoundException e) {
      return false;
    }
  }

  /**
   * 获取连接类型
   */
  public static int getConnectedType(Context context) {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
        return mNetworkInfo.getType();
      }
    }
    return -1;
  }
}
