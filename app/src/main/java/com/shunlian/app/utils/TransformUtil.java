package com.shunlian.app.utils;
//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;

import com.shunlian.app.R;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by zhang on 2017/4/14 13 : 06.
 */
public class TransformUtil {

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /**
     * 将字节组转化为Bitmap类型
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(Context ctx, byte[] bytes) {
        if (bytes.length != 0) {
            BitmapFactory.Options opts = new BitmapFactory.Options();

            // 不加载bitmap，只获取图片额外信息
            opts.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);

            // 获得图片的宽高
            int pHeight = opts.outHeight;
            int pWidth = opts.outWidth;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;

            int width = (int) (pWidth / DeviceInfoUtil.getDeviceWidth(ctx) + 0.5f);
            int height = (int) (pHeight /  DeviceInfoUtil.getDeviceHeight(ctx) + 0.5f);
            // 选择框压缩比例最大的进行压缩
            int scale = width > height ? width : height;
            // 设置压缩比例
            opts.inSampleSize = scale;
            // 加载图片
            opts.inJustDecodeBounds = false;
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
            }catch (OutOfMemoryError error){

            }
            return bm;
        }
        return null;
    }


    /**
     * dip转换px
     *
     * @param context
     * @param dpValue
     * @return float
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换dip
     *
     * @param context
     * @param pxValue
     * @return float
     */
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static Bitmap drawable2Bitmap(Context ctx,int id){
        BitmapFactory.Options opts = new BitmapFactory.Options();

        // 不加载bitmap，只获取图片额外信息
        opts.inJustDecodeBounds = true;

        decodeResource(ctx.getResources(),id,opts);

        // 获得图片的宽高
        int pHeight = opts.outHeight;
        int pWidth = opts.outWidth;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        int width = (int) (pWidth / DeviceInfoUtil.getDeviceWidth(ctx) + 0.5f);
        int height = (int) (pHeight /  DeviceInfoUtil.getDeviceHeight(ctx) + 0.5f);
        // 选择框压缩比例最大的进行压缩
        int scale = width > height ? width : height;
        // 设置压缩比例
        opts.inSampleSize = scale;
        // 加载图片
        opts.inJustDecodeBounds = false;
        Bitmap bm = null;
        try {
            bm = decodeResource(ctx.getResources(),id,opts);
        }catch (OutOfMemoryError error){

        }
        return bm;
    }


    /**
     * 在固定大小的ImageView中，保证图片不变形
     * @param bitmap
     * @param imageView
     * @param width
     * @param height
     */
    public static void setBsetBitmap(Bitmap bitmap, ImageView imageView, int width,
                                     int height) {
        //计算最佳缩放倍数,以填充宽高为目标
        float scaleX = (float) width / bitmap.getWidth();
        float scaleY = (float) height / bitmap.getHeight();
        float bestScale = scaleX > scaleY ? scaleX : scaleY;

        float subX = (width - bitmap.getWidth() * bestScale) / 2;
        float subY = (height - bitmap.getHeight() * bestScale) / 2;

        Matrix imgMatrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        //缩放最佳大小
        imgMatrix.postScale(bestScale, bestScale);
        //移动到居中位置显示
        imgMatrix.postTranslate(subX, subY);
        //设置矩阵
        imageView.setImageMatrix(imgMatrix);
        imageView.setImageBitmap(bitmap);
    }

   /* public static float computeSize(Context mContext,float size) {
        SharedPreferences myApp = mContext.getSharedPreferences(Constant.HEADER, Context.MODE_PRIVATE);
        int screenWidth = Integer.valueOf(myApp.getString("deviceWidth","720"));
        int screenHeight = Integer.valueOf(myApp.getString("deviceHeight","1184"));
        float ratioWidth = (float)screenWidth / 480;
        float ratioHeight = (float)screenHeight / 800;
        float ratio = Math.min(ratioWidth, ratioHeight);
        int msize= Math.round(size * ratio);
        return msize;
    }*/

    public static Bitmap convertVIP(Context context,String vip){
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(vip)){
            return bitmap;
        }
        if (vip.equalsIgnoreCase("v0") || "0".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v0);
        }else if (vip.equalsIgnoreCase("v1") || "1".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v1);
        }else if (vip.equalsIgnoreCase("v2") || "2".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v2);
        }else if (vip.equalsIgnoreCase("v3") || "3".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v3);
        }else if (vip.equalsIgnoreCase("v4") || "4".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v4);
        }else if (vip.equalsIgnoreCase("v5") || "5".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v5);
        }else if (vip.equalsIgnoreCase("v6") || "6".equals(vip)){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_login_v6);
        }
        return bitmap;
    }

    /**
     * 扩大控件点击范围
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }


    /**
     * 根据720图计算真实宽高比例
     * @param context
     * @param width
     * @param height
     * @return realWH[0] 宽    realWH[1] 高
     */
    public static int[] countRealWH(Context context,int width,int height){
        int[] realWH = new int[2];
        float wp = width / Constant.DRAWING_WIDTH;
        float real_w = wp * DeviceInfoUtil.getDeviceWidth(context);
        float real_h = real_w * height / width;
        realWH[0] = (int) (real_w + 0.5f);
        if (width == height){
            realWH[1] = (int) (real_w + 0.5f);
        }else {
            realWH[1] = (int) (real_h + 0.5f);
        }
        return realWH;
    }

    /**
     * 计算真实宽
     * @param context
     * @param width 720*1280标注图上给的像素
     * @return
     */
    public static int countRealWidth(Context context,int width){
        float wp = width / Constant.DRAWING_WIDTH;
        float realW = wp * DeviceInfoUtil.getDeviceWidth(context);
        return (int) (realW + 0.5f);
    }

    /**
     * 计算真实高
     * @param context
     * @param height 720*1280标注图上给的像素
     * @return
     */
    public static int countRealHeight(Context context,int height){
        float hp = height / Constant.DRAWING_HEIGHT;
        float realH = hp * DeviceInfoUtil.getDeviceHeight(context);
        return (int) (realH + 0.5f);
    }
    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static byte[] bmpToByteArray(Bitmap var0, boolean var1) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        var0.compress(Bitmap.CompressFormat.PNG, 100, var2);
        if(var1) {
            var0.recycle();
        }

        byte[] var4 = var2.toByteArray();

        try {
            var2.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return var4;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
