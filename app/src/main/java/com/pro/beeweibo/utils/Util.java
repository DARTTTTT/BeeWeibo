


package com.pro.beeweibo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {


    private static boolean eye = true;

    public static Map<String, Object> jsonToMap(String content) {
        content = content.trim();
        Map<String, Object> result = new HashMap<>();
        try {
            if (content.charAt(0) == '[') {
                JSONArray jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object value = jsonArray.get(i);
                    if (value instanceof JSONArray || value instanceof JSONObject) {
                        result.put(i + "", jsonToMap(value.toString().trim()));
                    } else {
                        result.put(i + "", jsonArray.getString(i));
                    }
                }
            } else if (content.charAt(0) == '{') {
                JSONObject jsonObject = new JSONObject(content);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof JSONArray || value instanceof JSONObject) {
                        result.put(key, jsonToMap(value.toString().trim()));
                    } else {
                        result.put(key, value.toString().trim());
                    }
                }
            } else {
                Log.e("异常", "json2Map: 字符串格式错误");
            }
        } catch (JSONException e) {
            Log.e("异常", "json2Map: ", e);
            result = null;
        }
        return result;
    }

    public static String Random32() {
        String strRand = "";
        for (int i = 0; i < 32; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }

        return strRand;
    }

    public static String jsonReplace(String content) {
        String s = content.replaceAll("\\?", "").replaceAll("\\(", "").replaceAll("\\)", "");
        String substring = s.substring(0, s.length() - 1);


        return substring;
    }

    public static List<String> quoteResult(String content) {
        List<String> quoteList = new ArrayList<>();
        String[] split = content.split(";");
        if (split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                String a = split[i];
                quoteList.add(a);
            }
            return quoteList;
        } else {
            return null;
        }
    }


    public static String filter(String content) {
        StringBuilder stringBuilder;
        ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
        stringBuilder = new StringBuilder();
        for (int i = 0; i < allSatisfyStr.size(); i++) {
            stringBuilder.append(allSatisfyStr.get(i));
        }
        if (stringBuilder.toString().contains("_")) {
            return stringBuilder.toString().replaceAll("_", "");
        } else {
            return stringBuilder.toString();
        }
    }



    public static String filterNumber(String content) {
        StringBuilder stringBuilder;
        ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[\\u4e00-\\u9fa5_a-zA-Z_]{4,10}");
        stringBuilder = new StringBuilder();
        for (int i = 0; i < allSatisfyStr.size(); i++) {
            stringBuilder.append(allSatisfyStr.get(i));
        }
        return stringBuilder.toString();

    }


    public static String quoteNme(String content) {
        String name;
        StringBuilder stringBuilder;

        if (content == null) {
            return null;
        } else {
            ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
            stringBuilder = new StringBuilder();
            for (int i = 0; i < allSatisfyStr.size(); i++) {
                stringBuilder.append(allSatisfyStr.get(i));
            }
            if (stringBuilder.toString().contains("USDT")) {
                name = content.substring(0, content.length() - 8) + "/" + content.substring(content.length() - 8, content.length() - 4);
            } else {
                name = stringBuilder.toString() + "," + "null";
            }
            return name;
        }
    }

    public static String quoteList(String content) {
        String name;

        StringBuilder stringBuilder;
        if (content == null) {
            return null;
        } else {
            ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
            stringBuilder = new StringBuilder();
            for (int i = 0; i < allSatisfyStr.size(); i++) {
                stringBuilder.append(allSatisfyStr.get(i));
            }


            if (stringBuilder.toString().contains("USDT")) {
                name = stringBuilder.substring(0, content.length() - 8) + "," + stringBuilder.substring(content.length() - 8, content.length() - 4);
            } else {
                name = stringBuilder.toString() + "," + "null";
            }
            return name;
        }
    }

    public static ArrayList<String> getAllSatisfyStr(String str, String regex) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList<String> allSatisfyStr = new ArrayList<>();
        if (regex == null || regex.isEmpty()) {
            allSatisfyStr.add(str);
            return allSatisfyStr;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            allSatisfyStr.add(matcher.group());
        }
        return allSatisfyStr;
    }


    public static String getNumberFormat2(String value) {
        double v = Double.parseDouble(value);
        DecimalFormat mFormat = new DecimalFormat("#0.00");
        return mFormat.format(v);
    }





    /*日期转成时间戳*/
    public static String dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime() / 1000;
        return String.valueOf(ts);
    }

    public static boolean compareDate(String nowDate, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(nowDate);
            Date compare = df.parse(compareDate);
            if (now.before(compare)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int str2Calendar(String str, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (type.equals("year")) {
                return calendar.get(Calendar.YEAR);
            } else if (type.equals("month")) {
                return calendar.get(Calendar.MONTH);
            } else if (type.equals("day")) {
                return calendar.get(Calendar.DAY_OF_MONTH);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }




    public static String startDisplay(String createTimeGe) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(createTimeGe);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(now);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }




    public static void changeViewVisibilityGone(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    public static Long dateToStampLong(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }

    /*时间戳转成日期*/
    public static String stampToDate(long milSecond) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(milSecond);
    }


    public static String getHours(long second) {//计算秒有多少小时
        long h = 00;
        if (second > 3600) {
            h = second / 3600;
        }
        return h + "";
    }

    public static String getMins(long second) {//计算秒有多少分
        long d = 00;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                }
            }
        } else {
            d = second / 60;
        }

        if (d < 10) {
            return "0" + d;
        } else {
            return d + "";
        }
    }

    public static String getSeconds(long second) {//计算秒有多少秒
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (s < 10) {
            return "0" + s;
        } else {
            return s + "";
        }
    }

    /*日期*/
    public static Date parseServerTime(String serverTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = sdf.parse(serverTime);
        } catch (Exception e) {

        }
        return date;
    }


    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getSign(Map<String, String> params, String security) {
        Map<String, String> sortedMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedMap.putAll(params);

        // 不参与签名
        sortedMap.remove("sign");
        // 特殊：登录情况下会有access_token参数
        sortedMap.remove("access_token");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // 空值不参与签名
            if (TextUtils.isEmpty(val)) {
                continue;
            }
            sb.append(key).append("=").append(val).append("&");
        }
        // 拼接key
        sb.append("key").append("=").append(security);

        // MD5加密转小写
        String sign = md5(sb.toString());
        return sign.toLowerCase();
    }

    //复制
    public static void copy(Context context, String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

    //粘贴
    public static void paste(Context context, TextView textView) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // 获取剪贴板的剪贴数据集
        ClipData clipData = clipboard.getPrimaryClip();

        if (clipData != null && clipData.getItemCount() > 0) {
            // 从数据集中获取（粘贴）第一条文本数据
            CharSequence text = clipData.getItemAt(0).getText();

            textView.setText(text);
        }

    }



}
