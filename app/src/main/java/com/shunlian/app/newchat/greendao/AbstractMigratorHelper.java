package com.shunlian.app.newchat.greendao;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2017/9/21.
 */

public abstract class AbstractMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses);
}
