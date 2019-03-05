package com.shunlian.app.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighLightKeyWordUtil {

    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 关键字
     * @return
     */
    public static SpannableString getHighLightKeyWord(int color, String text, String keyword) {
        if(TextUtils.isEmpty(keyword)){
            return new SpannableString(text);
        }
        SpannableString s = new SpannableString(text);
        try {
//            Pattern p = Pattern.compile(keyword);
//            Matcher m = p.matcher(s);
//            while (m.find()) {
//                int start = m.start();
//                int end = m.end();
//                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
           int index = text.indexOf(keyword);
            while(index!=-1) {
//                System.out.print("index:"+":"+text.substring(index,index+keyword.length()));
                s.setSpan(new ForegroundColorSpan(color), index, index+keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = text.indexOf(keyword,index+1);
            }
        }catch (Exception e){
            return s;
        }
        return s;
    }

    /**
     * @param color   关键字颜色
     * @param text    文本
     * @param keyword 多个关键字
     * @return
     */
    public static SpannableString getHighLightKeyWord(int color, String text, String[] keyword) {
        SpannableString s = new SpannableString(text);
        try{
            for (int i = 0; i < keyword.length; i++) {
//                Pattern p = Pattern.compile(keyword[i]);
//                Matcher m = p.matcher(s);
//                while (m.find()) {
//                    int start = m.start();
//                    int end = m.end();
//                    s.setSpan(new ForegroundColorSpan(color), start, end,
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }

                if(!TextUtils.isEmpty(keyword[i])) {
                    int index = text.indexOf(keyword[i]);
                    while (index != -1) {
//                System.out.print("index:"+":"+text.substring(index,index+keyword.length()));
                        s.setSpan(new ForegroundColorSpan(color), index, index + keyword[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        index = text.indexOf(keyword[i], index + 1);
                    }
                }
            }
        }catch (Exception e){
            return s;
        }
        return s;
    }

    /**
     * @return
     */
    public static SpannableString getHighBigBoldKeyWord(int size,String bigText, String text) {
        if(TextUtils.isEmpty(bigText)){
            return new SpannableString(text);
        }
        SpannableString s = new SpannableString(text);
        try{
//            Pattern pBold = Pattern.compile(bigText);
//            Matcher m = pBold.matcher(s);
//            while (m.find()) {
//                int start = m.start();
//                int end = m.end();
//                s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.contains("+")&&start>0?start-1:start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                s.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
            int index = text.indexOf(bigText);
            while(index!=-1) {
//                System.out.print("index:"+":"+text.substring(index,index+keyword.length()));
                s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.contains("+")&&index>0?index-1:index, index+bigText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new AbsoluteSizeSpan(size, true), index, index+bigText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                s.setSpan(new ForegroundColorSpan(color), index, index+keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = text.indexOf(bigText,index+1);
            }
        }catch (Exception e){
            return s;
        }
        return s;
    }
}