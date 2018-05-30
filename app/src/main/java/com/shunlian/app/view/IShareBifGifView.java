package com.shunlian.app.view;

import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.bean.PlusMemberEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public interface IShareBifGifView extends IView {

    void getPlusData(PlusDataEntity plusDataEntity);

    void getPlusMember(List<PlusMemberEntity.PlusMember> plusMembers);
}
