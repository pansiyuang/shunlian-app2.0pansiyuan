package com.shunlian.app.photopick;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by zhanghe on 2018/10/19.
 */

public class ImageVideo implements Comparable<ImageVideo>{

    public String path;
    public String name;
    /**文件创建时间**/
    public long creatTime;
    public boolean isSelect;
    /**视频时长**/
    public long videoDuration;
    /***视频封面**/
    public Bitmap coverBitmap;
    public String coverPath;
    /******图片是否损坏************/
    public boolean isPicDamage;

    public File file;
    /**
     * 服务端url
     */
    public String url;

    @Override
    public String toString() {
        return "ImageVideo{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", creatTime=" + creatTime +
                ", isSelect=" + isSelect +
                ", videoDuration=" + videoDuration +
                ", coverPath='" + coverPath + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ImageVideo o) {
        if (this.creatTime > o.creatTime)
            return -1;
        else if (this.creatTime < o.creatTime){
            return 1;
        }
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
