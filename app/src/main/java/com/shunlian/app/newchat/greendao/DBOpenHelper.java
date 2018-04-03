package com.shunlian.app.newchat.greendao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shunlian.app.newchat.entity.DaoMaster;
import com.shunlian.app.newchat.entity.MsgInfoDao;

import org.greenrobot.greendao.database.StandardDatabase;

/**
 * Created by Administrator on 2017/9/21.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DaoMaster.createAllTables(new StandardDatabase(db), false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int currentVersion, int lastestVersion) {
        try {
            DBMigrationHelper migratorHelper = new DBMigrationHelper();
            if (currentVersion > lastestVersion) {
                migratorHelper.onUpgrade(db, MsgInfoDao.class); //这边自己通过版本自行判断需要修改的表
            }
        } catch (ClassCastException e) {

        }
    }
}