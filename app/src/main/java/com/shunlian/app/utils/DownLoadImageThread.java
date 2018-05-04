package com.shunlian.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;


/**
 * Created by Administrator on 2018/5/2.
 * 下载多张图片
 */
public class DownLoadImageThread extends Thread {

    private Context mContext;
    private ArrayList<String> pics;

    public DownLoadImageThread(Context context,ArrayList<String> data) {
        mContext = context;
        this.pics = data;
    }

    @Override
    public void run() {
        saveSplitePic(pics);
    }

    private void saveSplitePic(ArrayList<String> pics) {
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
}
