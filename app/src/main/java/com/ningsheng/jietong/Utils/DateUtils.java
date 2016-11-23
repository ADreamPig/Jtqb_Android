package com.ningsheng.jietong.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhushunqing on 2015/11/30.
 */
public class DateUtils {

    /*
    开始时间 yyyy-MM-dd hh:mm:ss
    结束时间
     */
    public static boolean DataCompare(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = null;
        try {
            d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d2.getTime() - d1.getTime();
            if (diff > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static String DateInterval(String begintime, String endtime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = null;
        try {
            d1 = df.parse(begintime);

            Date d2 = df.parse(endtime);
            long diff = d2.getTime() - d1.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - days * 24);
            return days+"天"+hour+"时";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }



    public static long HourGap(String begintime, String endtime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        try {
            d1 = df.parse(begintime);

            Date d2 = df.parse(endtime);
            long diff = d2.getTime() - d1.getTime();

//            long days = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000));
            return diff;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 字符串转换成日期 yyyy-MM-dd HH:mm:ss
     *
     * @param strFormat
     *            格式定义 如：yyyy-MM-dd HH:mm:ss
     * @param dateValue
     *            日期对象
     * @return
     */
    public static Date stringToDate(String strFormat, String dateValue) {
        if ( StringUtil.isBlank(dateValue)){

            return null;
        }
        if (strFormat == null)
            strFormat = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date newDate = null;
        try {
            newDate = dateFormat.parse(dateValue);
        } catch (ParseException e) {
            newDate = null;
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 日期转成字符串
     *
     * @param strFormat
     *            格式定义 如：yyyy-MM-dd HH:mm:ss
     * @param date
     *            日期字符串
     * @return
     */
    public static String dateToString(String strFormat, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        return dateFormat.format(date);
    }

    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"")));
    }
}
