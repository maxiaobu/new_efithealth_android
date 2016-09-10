package com.efithealth.app.maxiaobu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 马小布 on 2016-04-13.
 */
public class TimesUtil {
    /**
     * 输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String stringToTimestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
//            if (l < 0) {
//                times = stf.substring(0, 11);
//            } else {
            times = stf.substring(0, 10);
//            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日"）
     *
     * @param time
     * @return
     */
    public static String timestampToStringO(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String timestampToStringL(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times;
        if (time.length() == 13) {
            long lcc = Long.valueOf(time);
            times = sdr.format(new Date(lcc));
        } else {
            int i = Integer.parseInt(time);
            times = sdr.format(new Date(i * 1000L));
        }

        return times;

    }

    /**
     * 返回指定格式的时间
     * @param time 时间戳
     * @param sdr 指定格式
     * @return  指定格式的时间
     */
    public static String timestampToStringS(String time, SimpleDateFormat sdr) {
        String times;
        if (time.length() == 10) {
            long lcc = Long.valueOf(time);
            int i = Integer.parseInt(time);
            times = sdr.format(new Date(i * 1000L));
        } else {
            long lcc = Long.valueOf(time);
            times = sdr.format(new Date(lcc));
        }
        return times;
    }

    /**
     * 输入“yyyy-MM-dd”----输出时间戳（10位）
     * @param time
     * @param format
     * @return
     */
    public static String stringsToTimestamp(String time, String format){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat(format).parse(time));
            long timeInMillis = c.getTimeInMillis();
            return String.valueOf(timeInMillis).substring(0,10);

        }catch (ParseException e){
            e.printStackTrace();
        }
        return "-1";
    }

}
