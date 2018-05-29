package com.shunlian.app.view;

import com.shunlian.app.bean.InvitationEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public interface IInvitationRecordeView extends IView {

    void getInvitationRecord(int page, int totalPage, List<InvitationEntity.Invitation> invitationList);
}
