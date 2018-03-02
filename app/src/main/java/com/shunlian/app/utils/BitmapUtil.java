package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import static android.graphics.Bitmap.createBitmap;
import static com.shunlian.mylibrary.ImmersionBar.checkDeviceHasNavigationBar;
import static com.shunlian.mylibrary.ImmersionBar.getNavigationBarHeight;

public class BitmapUtil {
    public static final int MAX_WIDTH = 300;
    public static final int MAX_HEIGHT = 300;
    public static final int MIN_WIDTH = 100;
    public static final int MIN_HEIGHT = 100;

    public static String SAVE_PIC_PATH = Environment.getExternalStorageDirectory().getPath() + "/sldl/" + "shot.png";

    public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 99;
        int newHeight = 99;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        return newBitMap;
    }

    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    public static Bitmap zoomBitmap(Bitmap bitmap, float size) {
        Matrix matrix = new Matrix();
        matrix.postScale(size, size); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 纵向拼接
     * <功能详细描述>
     *
     * @param first
     * @param second
     * @return
     */
    public static Bitmap addBitmap(Activity context, Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height;
        if (checkDeviceHasNavigationBar(context)) { //判断是否包含导航栏
            LogUtil.httpLogW("有导航栏:" + getNavigationBarHeight(context));
            height = first.getHeight() - getNavigationBarHeight(context) + second.getHeight();
        } else {
            LogUtil.httpLogW("无导航栏");
            height = first.getHeight() + second.getHeight();
        }
        Bitmap result = createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        if (checkDeviceHasNavigationBar(context)) {
            canvas.drawBitmap(second, 0, first.getHeight() - 2 - getNavigationBarHeight(context), null);
        } else {
            canvas.drawBitmap(second, 0, first.getHeight() - 2, null);
        }
        return result;
    }

    public static Bitmap createQRImage(String str, Bitmap logoBm, float imgWidth) {
        int width = (int) imgWidth;
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, width, width);
            matrix = deleteWhite(matrix);//删除白边
            width = matrix.getHeight();
            int[] pixels = new int[width * width];
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除白边
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    public static boolean saveImageToAlbumn(Context context, Bitmap bmp) {

        if (!Common.hasSD()) {
            Common.staticToast("没有sd卡");
            return false;
        }
        LogUtil.httpLogW("剩余内存:" + Common.getSDFreeSize());
        if (Common.getSDFreeSize() < 1024 * 5) {
            Common.staticToast( "内存不足");
            return false;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "sldl");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (isSuccess) {
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveImageToAlbumn(Context context, Bitmap bmp, String photoName) {

        if (!Common.hasSD()) {
            Common.staticToast("没有sd卡");
            return false;
        }

        LogUtil.httpLogW("剩余内存:" + Common.getSDFreeSize());
        if (Common.getSDFreeSize() < 1024 * 5) {
            Common.staticToast("内存不足");
            return false;
        }

        File appDir = new File(Environment.getExternalStorageDirectory(), "sldl");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoName + ".png";
        File file = new File(appDir, fileName);
        SAVE_PIC_PATH=file.getAbsolutePath();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (isSuccess) {
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void delImgAlbumn(Context context, String picPath) {
        if (TextUtils.isEmpty(picPath)) {
            return;
        }
        File file = new File(picPath);
        if (file.exists()) {
            file.delete();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }

    /**
     * 画二维码
     *
     * @param context
     * @param bg
     * @param url
     * @param marginTop 二维码距背景高度
     * @param logo      二维码中心logo
     * @param codeWidth 二维码的宽
     * @return
     */
    public static Bitmap drawQRCode(Context context, Bitmap bg, String url, float marginTop, Bitmap logo, int codeWidth) {
        int width = bg.getWidth();
        int height = bg.getHeight();
        Bitmap result = null;
        try {
            result = createBitmap(width, height, Bitmap.Config.ARGB_8888);
            LogUtil.httpLogW("大小:" + TransformUtil.dip2px(context, 73));
            Bitmap QRCode = BitmapUtil.createQRImage(url, logo, codeWidth);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bg, 0, 0, null);
            canvas.drawBitmap(QRCode, bg.getWidth() / 2 - QRCode.getWidth() / 2, marginTop, null);

            QRCode.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
