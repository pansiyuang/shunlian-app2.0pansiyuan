package com.shunlian.app.newchat.greendao;

import com.shunlian.app.newchat.entity.MsgInfo;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2017/9/21.
 */

public class MsgInfoManager extends AbstractDatabaseManager<MsgInfo, Long> {
    @Override
    public AbstractDao<MsgInfo, Long> getAbstractDao() {
        return daoSession.getMsgInfoDao();
    }
}
