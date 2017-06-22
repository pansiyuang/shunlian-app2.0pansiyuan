package com.shunlian.app.presenter;

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

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.UserInfo;
import com.shunlian.app.bean.UserLoginEntity;
import com.shunlian.app.listener.INetDataCallback;

import retrofit2.Call;

/**
 * Created by zhang on 2017/6/20 15 : 23.
 */

public class TestPresenter extends BasePresenter {

    public TestPresenter(){

        Call<BaseEntity<UserLoginEntity>> userLogin1 = getSaveCookieApiService().getUserLogin1(new UserInfo("13007562706", "123456", null));

        getNetData(userLogin1, new INetDataCallback<BaseEntity<UserLoginEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UserLoginEntity> userLoginEntityBaseEntity) {
                System.out.println(userLoginEntityBaseEntity.data.toString());

                myHome();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void myHome() {
        Call<BaseEntity<MyHomeEntity>> baseEntityCall = getAddCookieApiService().getmyHome();
        getNetData(baseEntityCall, new INetDataCallback<BaseEntity<MyHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MyHomeEntity> myHomeEntityBaseEntity) {
                System.out.println(myHomeEntityBaseEntity.data.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
