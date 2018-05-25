package com.shunlian.app.view;

import com.shunlian.app.bean.GifProductEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */

public interface IGifBagView extends IView {

    void getGifList(List<GifProductEntity.Product> productList);
}
