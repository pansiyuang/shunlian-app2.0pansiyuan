package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by zhanghe on 2018/11/16.
 * 第三版登录 包括（账号密码登录   短信验证登录）
 */
public class New3LoginAct extends BaseActivity {



    public static void startAct(Context context,LoginConfig config){
        context.startActivity(new Intent(context,New3LoginAct.class)
                .putExtra("config",config));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_login_new3;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        LoginConfig mConfig = getIntent().getParcelableExtra("config");
        if (mConfig != null){

            if (!isEmpty(mConfig.status))
            {


            }
            else

                switch (mConfig.login_mode){
                    case PASSWORD_TO_LOGIN:
                        //密码登录

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.frame_rootView,new LoginPwdFrag()).commit();

                        break;
                    case SMS_TO_LOGIN:
                        //短信登录

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.frame_rootView,new LoginMobileFrag()).commit();

                        break;
                }
        }
    }


    /**
     * 登录配置信息
     */
    public static class LoginConfig implements Parcelable {

        //登录状态
        public String status;

        public LOGIN_MODE login_mode;

        public enum LOGIN_MODE{

            /**
             * 密码登录
             */
            PASSWORD_TO_LOGIN,

            /**
             * 短信登录
             */
            SMS_TO_LOGIN
        }

        public LoginConfig() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.status);
            dest.writeInt(this.login_mode == null ? -1 : this.login_mode.ordinal());
        }

        protected LoginConfig(Parcel in) {
            this.status = in.readString();
            int tmpLogin_mode = in.readInt();
            this.login_mode = tmpLogin_mode == -1 ? null : LOGIN_MODE.values()[tmpLogin_mode];
        }

        public static final Creator<LoginConfig> CREATOR = new Creator<LoginConfig>() {
            @Override
            public LoginConfig createFromParcel(Parcel source) {
                return new LoginConfig(source);
            }

            @Override
            public LoginConfig[] newArray(int size) {
                return new LoginConfig[size];
            }
        };
    }
}
