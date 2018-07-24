package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.util.ArrayList;


/**
 * Created by Administrator on 2018/5/2.
 * 下载多张图片
 */
public class DownLoadImageThread extends Thread {

    private Context mContext;
    private ArrayList<String> pics;
    private String pic;
    private MyCallBack myCallBack;

    public DownLoadImageThread(Context context, ArrayList<String> data) {
        mContext = context;
        this.pics = data;
    }

    public DownLoadImageThread(Activity activity, String pic, MyCallBack myCallBack) {
        mContext = activity;
        this.pic = pic;
        this.myCallBack=myCallBack;
    }

    @Override
    public void run() {
        if (!TextUtils.isEmpty(pic)) {
            saveSplitePic(pic);
        } else {
            saveSplitePics(pics);
        }
    }

    private void saveSplitePics(ArrayList<String> pics) {
        for (int i = 0; i < pics.size(); i++) {
            try {
                byte[] b = BitmapUtil.getImage(pics.get(i));
                if (b != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    BitmapUtil.saveImageToAlbumn(mContext, bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSplitePic(String pic) {
        try {
            byte[] b = BitmapUtil.getImage(pic);
            if (b != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                BitmapUtil.saveImageToAlbumn(mContext, bitmap);
            }
            myCallBack.successBack();
        } catch (Exception e) {
            e.printStackTrace();
            myCallBack.errorBack();
        }
    }

    public interface MyCallBack {
        //图文分享这里子类复写的时候一般需要同时另写一个分享的dialog，因为两次上下文不一致
         void successBack();

         void errorBack();
    }
}
