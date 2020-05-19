package com.tutao.common.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tutao.common.model.AppInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author guopeng
 * @ClassName: Util
 * @Description: TODO(常用方法类)
 * @date 2014 2014年9月18日 下午8:04:03
 */
@SuppressLint("NewApi")
public class BaseUtil {


    public static int getFirstSimState(Context ctx) {
        return getSimState("gsm.sim.state", ctx);
    }

    public static int getSecondSimState(Context ctx) {
        return getSimState("gsm.sim.state_2", ctx);
    }

    private static int getSimState(String paramString, Context ctx) {
        int result = 0;
        try {
            Method localMethod = Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("get", new Class[]{String.class});
            localMethod.setAccessible(true);
            String str = (String) localMethod.invoke(null,
                    new Object[]{paramString});
            if (str != null)
                str = str.split(",")[0];
            if ("ABSENT".equals(str))
                result = 1;
            else if ("PIN_REQUIRED".equals(str))
                result = 2;
            else if ("PUK_REQUIRED".equals(str))
                result = 3;
            else if ("NETWORK_LOCKED".equals(str))
                result = 4;
            else if ("READY".equals(str))
                result = 5;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == 0) {
            TelephonyManager tm = (TelephonyManager) ctx
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getSimState();
        }
        return result;
    }

    /**
     * 判断网络是否连通
     *
     * @param ctx
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
            Log.i("net", info.toString());
        return (info != null && info.isAvailable());
    }

    /**
     * 设置是否启用移动数据
     *
     * @param ctx
     * @param enabled
     */
    public static boolean setMobileDataEnabled(Context ctx, boolean enabled) {
        boolean result = false;
        if (Build.VERSION.SDK_INT < 9)// android2.3及以上适用
            return result;
        ConnectivityManager connectivitymanager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivitymanager.getActiveNetworkInfo();
        // 倘若手机目前不在网络的服务范围，则connec.getActiveNetworkInfo()会返回null
        if (info != null) {
            return result;
        }

        try {
            @SuppressWarnings("rawtypes")
            Class connectClass = connectivitymanager.getClass();
            Method[] methods = connectClass.getMethods();
            for (Method method : methods) {
                System.out.println(method.getName());
            }
            @SuppressWarnings("unchecked")
            Method method = connectClass.getMethod("setMobileDataEnabled",
                    Boolean.TYPE);
            method.invoke(connectivitymanager, enabled);
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPackageName(Context ctx) {
        String name = "";
        try {
            name = ctx.getPackageName();
        } catch (Exception e) {

        }
        return name;
    }

    public static String getAppName(Context ctx) {
        String name = "";
        try {
            PackageManager packageManager = null;
            ApplicationInfo applicationInfo = null;
            try {
                packageManager = ctx.getPackageManager();
                applicationInfo = packageManager.getApplicationInfo(
                        ctx.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                applicationInfo = null;
            }
            name = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Exception e) {

        }
        return name;
    }

    public static boolean isRoot() {
        String[] PATHS = {"/system/bin/", "/system/xbin/", "/system/sbin/",
                "/sbin/", "/vendor/bin/"};
        for (String v : PATHS) {
            String name = v + "su";

            File file = new File(name);

            if (file.exists()) {
                return true;
            }
        }
        return false;
    }
    public static String getIMEI(Context context){
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                imei = tm.getDeviceId();
            }else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(imei==null){
            return "";
        }
        return imei;
    }

    public static String getIMSI(int i, Context ctx) {
        String s = Build.MODEL;
        String s1;
        Method method;
        Object obj = null;
        Object obj2 = null;
        if (i == 0) {
            if ("Philips T939".equals(s))
                s1 = "iphonesubinfo0";
            else
                s1 = "iphonesubinfo";
        } else if (i == 1) {
            if ("Philips T939".equals(s))
                s1 = "iphonesubinfo1";
            else
                s1 = "iphonesubinfo2";
        } else {
            return null;
//			throw new IllegalArgumentException("cardIndex can only be 0 or 1");
        }
        Method method1;
        Object obj1;
        String s2 = "";
        try {
            method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
            method.setAccessible(true);
            obj = method.invoke(null, new Object[]{s1});
            if (obj == null && i == 1) {
                obj2 = method.invoke(null, new Object[]{"iphonesubinfo1"});
                obj = obj2;
            }
            if (obj != null) {
                method1 = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub")
                        .getDeclaredMethod("asInterface",
                                new Class[]{IBinder.class});
                method1.setAccessible(true);
                obj1 = method1.invoke(null, new Object[]{obj});
                s2 = (String) obj1.getClass().getMethod("getSubscriberId", new Class[0]).invoke(obj1, new Object[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (TextUtils.isEmpty(s2)) {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                s2 = tm.getSubscriberId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s2;
    }

    /*
     * -1:未知;0:WIFI;1:2G;2:3G;3:4G
     */
    public static String getAPNType(Context ctx) {
        String apnType = "";
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnectedOrConnecting()) {
                int type = info.getType();
                int subtype = info.getSubtype();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    apnType = "wifi";
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    switch (subtype) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: {
                            apnType = "2g";
                            break;
                        }
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case 12://TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case 14://TelephonyManager.NETWORK_TYPE_EHRPD:
                        case 15://TelephonyManager.NETWORK_TYPE_HSPAP:
                        {
                            apnType = "3g";
                            break;
                        }
                        case 13://TelephonyManager.NETWORK_TYPE_LTE
                            apnType = "4g";
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apnType;
    }

    /**
     * 获取运营商1:联通； 2:移动;3：电信；4：铁通；-1：其它
     */
    public static String getProviderType(String IMSI) {
        String type = "-1";
        if (null == IMSI) {
            return type;
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                || IMSI.startsWith("46007") || IMSI.startsWith("898600")) {
            type = "2";
        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {// 联通
            type = "1";
        } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {// 电信
            type = "3";
        } else if (IMSI.startsWith("46020")) {// 电信
            type = "3";
        }
        return type;
    }

    /**
     * 获取application节点下 metadata
     */
    public static String getMetadata(Context ctx, String name) {
        String result = "";
        try {
            ApplicationInfo ap = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ap.metaData;
            Object obj = bundle.get(name);
            if (obj != null) {
                result = obj.toString();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            result = "";
        }
        if (TextUtils.isEmpty(result)) {
            result = "";
        }
        return result;
    }


    // cpu最大频率
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (Exception ex) {
            // ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();

    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 内存使用
    @SuppressWarnings("resource")
    public static String getMemoryInfo() {
        String localObject = "/proc/meminfo";
        try {
            FileReader reader = new FileReader(localObject);
            if ((localObject = new BufferedReader(reader, 8192).readLine()) != null) {
                localObject = ((String) localObject).substring(
                        ((String) localObject).indexOf(":") + 2,
                        ((String) localObject).indexOf("k") - 1).trim();
                Log.i("MobileUtils", (String) localObject);
                return (String) localObject;
            }
        } catch (IOException localIOException) {
        }
        return "";
    }

    // 屏幕高
    public static int getScreenHeight(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    // 屏幕宽
    public static int getScreenWidth(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    // 手机像素密度(Dp)
    public static int getDPI(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.densityDpi;
    }

    /**
     * .获取手机MAC地址 只有手机开启wifi才能获取到mac地址
     */
    public static String getMacAddress(Context ctx) {
        String result = "";
        WifiManager wifiManager = (WifiManager) ctx
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }

    // 过滤
    public static String filter(String paramString) {
        if (paramString == null)
            return "";
        if (paramString.length() > 30)
            paramString = paramString.substring(0, 29);
        return paramString.replace("\\", "").replace("|", "");
    }

    public static String getChannel(Context ctx) {
        String result = getFromAssets(ctx, "channel.txt").trim();
        return result;
    }

    public static String getUrl(Context ctx) {
        String result = getFromAssets(ctx, "url.txt").trim();
        return result;
    }

    // 从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
            // result = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    /**
     * GPS是否打开
     */
    public static boolean isGpsEnable(Context ctx) {
        LocationManager locationManager = ((LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 是否为标准的URL
     */
    public static boolean isURL(String url) {
        boolean result = false;
        String regex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        if (isMatch) {
            result = true;
        }
        return result;
    }

    /**
     * 获取APK版本号
     */
    public static String getVersionName(Context ctx) {
        // 获取packagemanager的实例
        String version = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
        }
        return version;
    }

    /**
     * 获取代码版本号
     */
    public static int getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        int version = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (Exception e) {
        }
        return version;
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static String getCustomTimeString(int timeSecond) {
        String str = "";
        Date time = new Date(timeSecond * 1000L);
        Date now = new Date(System.currentTimeMillis());

        long timeDay = time.getTime() / 86400000;
        long nowDay = now.getTime() / 86400000;

        int diffDay = (int) (nowDay - timeDay);

        switch (diffDay) {
            case 0:
                str = getTimeRegion(time) + " " + getStrTime(time, "HH:mm");
                break;
            case 1:
                str = "昨天" + getTimeRegion(time) + " " + getStrTime(time, "HH:mm");
                break;
            case 2:
                str = "前天" + getTimeRegion(time) + " " + getStrTime(time, "HH:mm");
                break;
            default:
                str = getStrTime(time, "yyyy-MM-dd HH:mm");
        }
        return str;
    }

    public static String getTimeRegion(Date time) {
        String strRegion = "";
        int hour = time.getHours();
        if (hour > -1 && hour < 5) {
            strRegion = "凌晨";
        } else if (hour > 4 && hour < 8) {
            strRegion = "早晨";
        } else if (hour > 7 && hour < 12) {
            strRegion = "上午";
        } else if (hour > 11 && hour < 14) {
            strRegion = "中午";
        } else if (hour > 13 && hour < 18) {
            strRegion = "下午";
        } else if (hour > 17 && hour < 21) {
            strRegion = "晚上";
        } else if (hour > 20 && hour < 24) {
            strRegion = "深夜";
        }
        return strRegion;
    }

    public static String getStrTime(Date time, String format) {
        String re_StrTime = null;
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 例如：cc_time=1291778220
        re_StrTime = sdf.format(time);
        return re_StrTime;
    }

    /**
     * @param @param  context
     * @param @param  dpValue
     * @param @return 设定文件
     * @return float 返回类型
     * @throws
     * @Title: dip2px
     * @Description: TODO(dpi转px)
     */
    public static float dip2px(Context context, float dpValue) {
        if (context == null) {
            return dpValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        float result = dpValue * scale + 0.5f;
        return result;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //当前程序是否在后台
    public static boolean isApplicationBroughtToBackground(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
//			if (MLog.debug) {
//				Log.d("XX", topActivity.getPackageName()+","+ctx.getPackageName());
//			}
            if (!topActivity.getPackageName().equals(ctx.getPackageName())) {
                return true;
            }
        }
        tasks = null;
        return false;
    }

    //isBackground
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
//			if (MLog.debug) {
//				Log.d("XX", "进程名:"+appProcess.processName);
//			}
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    if (MLog.debug) {
                        Log.d("后台", appProcess.processName);
                    }

                    return true;
                }
//				else{
//					Log.d("前台", appProcess.processName);
//					return false;
//				}
            }
        }
        appProcesses = null;
        return false;
    }

    //获取资源图片为bitmap
    public static Bitmap getBitmapFromResource(Context ctx, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = ctx.getResources().openRawResource(resId);
        Bitmap bmp = BitmapFactory.decodeStream(is, null, opt);
        return bmp;
    }

    //打开APK
    public static void openAPK(Context ctx, String path) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
//        ctx.startActivity(intent);
        File apkFile = new File(path);
        Intent intent = new Intent();
        String packageName = Util.getAppPackageName(ctx);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0启动姿势<pre name="code" class="html">    //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同；apkFile为问题1中的外部存储apk文件</pre>
            uri = FileProvider.getUriForFile(ctx, packageName + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，次过程没有用户交互
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            //7.0以下启动姿势
            uri = Uri.fromFile(apkFile);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }
    //剩余时间
    public static String formatTime(int second, String strSecond, String strMinute) {
        String str = "";
        int minute = second / 60;
        second = second % 60;
        if (second > 0) {
            if (minute > 0) {
                str = String.format(strMinute, minute, second);
            } else {
                str = String.format(strSecond, second);
            }
        }
        return str;
    }

    //下载速度
    public static String formatSpeed(long speed) {
        StringBuffer sb = new StringBuffer();
        if (speed > 0) {

            long KB = speed / 1024;
            double MB = 0;
            if (KB > 1000) {
                MB = KB / 1024.00;
            }
            if (MB > 0.8) {
                DecimalFormat df = new DecimalFormat("#.##");
                sb.append(df.format(MB));
                sb.append("M/s");
            } else if (KB > 0) {
                sb.append(KB);
                sb.append("K/s");
            } else {
                sb.append(speed);
                sb.append("B/s");
            }
        }
        return sb.toString();
    }

    /**
     * <p>功能:取两个数据之间的随机数，包括这两个数</p>
     *
     * @param min
     * @param max
     * @return
     * @throws
     * @since 1.0.0
     * <P>guopeng 创建于 2014年12月14日 下午1:17:24</p>
     * <P>修改：修改人-时间-修改内容</p>
     */
    public static int getRandomInt(int min, int max) {
        int result = 0;
        if (min == max) {
            result = min;
        } else {
            result = min + (int) (Math.random() * (max - min + 1));
        }
        return result;
    }

    /**
     * <p> 此方法用于对TextView中的指定字符串进行加亮操作
     *
     * @param txt
     * @param target 指定加亮的字符串
     *               <p> 当target ="[0-9\\.]+"时，代表将TextView中的所有数字加亮
     */
    public void foreLightext(TextView txt, String target) {
        String str = txt.getText().toString();
        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(str);
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        CharacterStyle span = null;
        while (m.find()) {
            //			String find =m.group();//匹配到的结果
            //			int x = str.indexOf(find, 0);//返回第一次匹配到的结果
            span = new ForegroundColorSpan(Color.RED);
            builder.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        txt.setText(builder);
    }

    public boolean isToday(long time) {
        boolean result = false;
        time = time / 86400000;
        long nowDay = System.currentTimeMillis() / 86400000;
        if (time == nowDay) {
            result = true;
        }
        return result;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getFileName(String filename) {

        int start = filename.lastIndexOf("/");
        int end = filename.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return filename.substring(start + 1, end);
        } else {
            return null;
        }
    }

    public static String md5(String str) {
        String md5Str = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            md5Str = buf.toString();
            buf = null;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return md5Str;
    }

    public static boolean copyAssetsFileToCache(Context ctx, String fileName, String path, boolean overWrite) {
        boolean result = false;
        try {
            InputStream is = ctx.getAssets().open(fileName);
            result = writeTo(is, path, overWrite);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static boolean writeTo(InputStream is, String newPath, boolean overWrite) {
        boolean result = false;
        try {
            int byteread = 0;
            File file = new File(newPath);
            if (file.exists()) {
                if (overWrite)
                    file.delete();
                else
                    result = true;
            }
            if (!result) {
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void saveImage(File file, Bitmap bitmap) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserAgent(AppConfig config) {
        StringBuffer sb = new StringBuffer();
        sb.append("android/").append(Build.VERSION.RELEASE).append(" ");

        Context ctx = RuntimeData.getInstance().getContext();
        if (ctx != null) {
            Locale locale = ctx.getResources().getConfiguration().locale;
            sb.append("net/").append(getAPNType(ctx)).append(" ");
            String language = locale.getLanguage();//获取语言
            String country = locale.getCountry().toLowerCase();//获取国家
            sb.append("language/").append(language).append("-").append(country).append(" ");
        }

        sb.append("timezone/").append(getGMTTimeZone()).append(" ");

        String model = Build.MODEL.toLowerCase().trim().replace(" ", "_").replace("/", "_");
        sb.append("model/").append(model).append(" ");

        sb.append(RuntimeData.getInstance().getAppConfig().xCode + "/").append(getVersionName(ctx)).append(" ");

        sb.append("api/").append(config.api_version).append(" ");

        sb.append("build/").append(getApkCreatedTime(ctx)).append(" ");

        sb.append("env/");
        if (MLog.debug) {
            sb.append("debug");
        } else {
            sb.append("production");
        }
        return sb.toString();

    }

    public static String getApkCreatedTime(Context context) {

        String signTime = "_";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        ZipFile zf = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0);
            zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            return format.format(new Date(ze.getTime()));
        } catch (Exception e) {
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return signTime;
    }

    public static String getGMTTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String str = tz.getDisplayName(false, TimeZone.SHORT);
        //str = GMT+08:00
        String result = "";
        Pattern p = Pattern
                .compile("[-+]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            result = m.group();
        }
        p = Pattern.compile("\\d{2}");
        m = p.matcher(str);
        if (m.find()) {
            result += Integer.valueOf(m.group());
        }
        return result;
    }

    public static boolean isUrl(String url) {
        Pattern p = Pattern.compile("^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * <p> 获取手机应用
     *
     * @param ctx
     * @return
     */
    public static List<AppInfo> getUserApplicationList(Context ctx) {
        List<AppInfo> list = new ArrayList<AppInfo>();
        List<PackageInfo> allPackageInfos = ctx.getPackageManager()
                .getInstalledPackages(0);
        //遍历应用程序的集合
        for (int i = 0; i < allPackageInfos.size(); i++) {
            PackageInfo temp = allPackageInfos.get(i);
            ApplicationInfo appInfos = temp.applicationInfo;
            //如果是系统程序不执行任何操作，否则将程序添加到用户集合中去
            if ((appInfos.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 ||
                    (appInfos.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            } else {
                AppInfo info = new AppInfo();
                info.setAppName(appInfos.name);
                info.setPkg(appInfos.packageName);
                info.setVersionCode(Integer.toString(temp.versionCode));
                list.add(info);
            }
        }
        return list;
    }

    /**
     * 查看手机是否安装支付宝
     *
     * @param ctx
     * @return
     */
    public static boolean isInstallAliPAy(Context ctx) {

        List<AppInfo> list = getUserApplicationList(ctx);
        boolean result = false;
        for (AppInfo appInfo : list) {
            if (appInfo.getPkg().equals("com.eg.android.AlipayGphone")) {
                result = true;
            }

        }
        return result;
    }

    /**
     * 根据图片名字，获取图片的id
     *
     * @return
     */
    public static int getIdByName(String name) {
        int res_id = 0;
        Class mipmap = R.mipmap.class;
        Field field = null;
        try {
            field = mipmap.getField(name);
            res_id = field.getInt(field.getName());
            MLog.e("ansen", "获得图片id " + res_id);
        } catch (Exception e) {
            MLog.e("ansen", "getIdByName error : " + e.toString());
        }

        return res_id;
    }


    public static String getRouterMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo().getBSSID();
    }

    public static String getLocalMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    //	public static ThemeConfig getAnalysisZipJson() {
//
//		ThemeConfig themeConfig = null;
//		try {
//			Gson gson = new Gson();
//			Reader reader = new FileReader(WeexUtil.getWeexJsPath() + "/dist/app.json");
//			themeConfig = gson.fromJson(reader, ThemeConfig.class);
//			RuntimeData.getInstance().setThemeConfig(themeConfig);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	
//		return themeConfig;
//	}
    public static ThemeConfig getAnalysisZipJson() {
        ThemeConfig themeConfig = null;
        Reader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            themeConfig = new ThemeConfig();
            BufferedReader br = new BufferedReader(new FileReader(new File(WeexUtil.getWeexJsPath() + "/dist/app.json")));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            themeConfig = JSON.parseObject(sb.toString(), ThemeConfig.class);
            RuntimeData.getInstance().setThemeConfig(themeConfig);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return themeConfig;
    }

    /**
     * 跳转手机设置界面
     */
    public static void goToSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            RuntimeData.getInstance().getCurrentActivity().startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            RuntimeData.getInstance().getCurrentActivity().startActivity(intent);
            return;
        }
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    /**
     * 排除不合法的 head
     *
     * @param headInfo
     * @return
     */
    public static String encodeHeadInfo(String headInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }


    /*
   * 将秒数转为时分秒
   * */
    public static String formatTimeToHMS(int second) {

        String result;
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }

        if (h == 0) {
            StringBuffer sb = new StringBuffer();
            if (d < 10) {
                sb.append("0" + d + ":");
            } else {
                sb.append(d + ":");
            }
            if (s < 10) {
                sb.append("0" + s);
            } else {
                sb.append(s);
            }
            return sb.toString();
        }

        return h + ":" + d + ":" + s + "";
    }


    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return (runningTaskInfos.get(0).topActivity).toString();
        else
            return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getAppPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Android Id
     * 系统第一次启动时产生的64bit数，设备被wipe还原后，该ID将被重置
     */
    public static String getAndroidId(Context context) {
        String androidId=readDeviceID();
        if(!TextUtils.isEmpty(androidId)){
            return  androidId;
        }
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(TextUtils.isEmpty(androidId)){
            androidId = String.valueOf(UUID.randomUUID());//防止androidId获取失败就去获取UUID存储本地
        }
        saveDeviceID(androidId);
        return androidId;
    }

    public static void saveDeviceID(String str) {
        try {
            FileOutputStream fos = new FileOutputStream(FileUtil.getGifPath()+"/log");
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readDeviceID() {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(FileUtil.getGifPath()+"/log");
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


}
