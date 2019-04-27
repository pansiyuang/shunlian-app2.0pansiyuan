package com.shunlian.app.demo.demo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/2.
 */

public class DemoImage extends BaseActivity {
    @BindView(R.id.take_photo)
    Button take_photo;
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.choose_from_album)
    Button choose_from_album;
    private static final int REQUEST_CODE_PIC_PHOTO=1;
    private static final int REQUEST_CODE_TAKE_PHOTO=2;
    private String absolutePath1;

    @Override
    protected int getLayoutId() {
        return R.layout.act_imge_pic;
    }

    @Override
    protected void initData() {


        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File photoFile = new File(fileDir, "photo.jpeg");
        absolutePath1 = photoFile.getAbsolutePath();
        Uri uri = Uri.fromFile(photoFile);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,  uri);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);

            }
        });
        choose_from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE_PIC_PHOTO);
            }
        });


    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PIC_PHOTO://手机相册
//                Uri uri = data.getData();
//                try {
//                    File file = new File(new URI(uri.toString()));
//                    absolutePath = file.getAbsolutePath();
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }



                if (!TextUtils.isEmpty(getRealPathFromUri(this,data.getData()))) {

                    Glide.with(this).load(getRealPathFromUri(this,data.getData())).into(picture);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                Toast.makeText(this,"11111",Toast.LENGTH_LONG).show();

                Glide.with(this).load(absolutePath1).into(picture);
    }

    }
}

