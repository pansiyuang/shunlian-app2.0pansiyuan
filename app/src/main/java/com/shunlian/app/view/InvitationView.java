package com.shunlian.app.view;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.InviteLogUserEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface InvitationView extends IView {

    /**
     * 刷新完成
     */
    void refreshFinish(List<InviteLogUserEntity.UserList> userLists, InviteLogUserEntity inviteLogUserEntity,int currentPage);
}
