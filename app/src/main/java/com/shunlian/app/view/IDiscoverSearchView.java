package com.shunlian.app.view;

import com.shunlian.app.bean.TagEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/23.
 */

public interface IDiscoverSearchView extends IView {

    void getTagList(List<TagEntity.Tag> tagList);
}
