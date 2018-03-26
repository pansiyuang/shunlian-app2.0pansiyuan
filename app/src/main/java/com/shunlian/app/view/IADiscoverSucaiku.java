package com.shunlian.app.view;

import com.shunlian.app.adapter.DiscoverNewAdapter;
import com.shunlian.app.adapter.DiscoverSucaikuAdapter;
import com.shunlian.app.adapter.TieziCommentAdapter;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IADiscoverSucaiku extends IView {
    void dianZan(DiscoverSucaikuAdapter.SucaikuHolder holder);
    void dianZans(DiscoverNewAdapter.NewHolder holder);
    void dianZanss(TieziCommentAdapter.CommentHolder holder);
}
