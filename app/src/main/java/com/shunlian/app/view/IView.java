package com.shunlian.app.view;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShareInfoParam;

/**
 * Created by zhang on 2017/6/21 14 : 28.
 */

public interface IView {

    /**
     * 显示网络请求失败的界面
     */
    void showFailureView(int request_code);


    /**
     * 显示空数据界面
     */
    void showDataEmptyView(int request_code);

    /**
     * 分享信息
     * @param baseEntity
     */
    default void shareInfo(BaseEntity<ShareInfoParam> baseEntity){}

    default void setAdapter(BaseRecyclerAdapter adapter){}
}
