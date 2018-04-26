package com.shunlian.app.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.presenter.PersonalDataPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IPersonalDataView;
import com.shunlian.app.widget.BottonDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/23.
 */

public class PersonalDataAct extends BaseActivity implements IPersonalDataView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.llayout_nickname)
    LinearLayout llayout_nickname;

    @BindView(R.id.llayout_sex)
    LinearLayout llayout_sex;

    @BindView(R.id.mtv_sex)
    MyTextView mtv_sex;

    @BindView(R.id.llayout_autograph)
    LinearLayout llayout_autograph;

    @BindView(R.id.llayout_region)
    LinearLayout llayout_region;

    @BindView(R.id.mtv_region)
    MyTextView mtv_region;

    @BindView(R.id.llayout_avatar)
    LinearLayout llayout_avatar;

    @BindView(R.id.miv_avatar)
    MyImageView miv_avatar;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.llayout_interest)
    LinearLayout llayout_interest;

    @BindView(R.id.mtv_interest)
    MyTextView mtv_interest;

    @BindView(R.id.llayout_birthday)
    LinearLayout llayout_birthday;

    @BindView(R.id.mtv_birthday)
    MyTextView mtv_birthday;

    @BindView(R.id.mtv_autograph)
    MyTextView mtv_autograph;

    private BottonDialog dialog;
    private PersonalDataPresenter presenter;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,PersonalDataAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_personal_data;
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_nickname.setOnClickListener(this);
        llayout_sex.setOnClickListener(this);
        llayout_autograph.setOnClickListener(this);
        llayout_region.setOnClickListener(this);
        llayout_interest.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("个人资料");
        gone(mrlayout_toolbar_more);

        presenter = new PersonalDataPresenter(this,this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.llayout_nickname:
                NickNameAct.startAct(this,mtv_nickname.getText().toString());
                break;
            case R.id.llayout_sex:
                if (dialog == null) {
                    dialog = new BottonDialog(this);
                    dialog.setOnClickListener((sex,id)-> {
                        mtv_sex.setText(sex);
                        if (presenter != null){
                            presenter.setInfo("sex",id);
                        }
                    });
                }
                dialog.show();
                break;
            case R.id.llayout_autograph:
                AutographAct.startAct(this,mtv_autograph.getText().toString());
                break;
            case R.id.llayout_region:
                if (presenter != null){
                    presenter.initDistrict();
                }
                break;
            case R.id.llayout_interest:
                SelectLikeAct.startAct(this,mtv_interest.getText().toString());
                break;
        }
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setLocation(String district, String district_ids) {
        mtv_region.setText(district);
        if (!isEmpty(district_ids)){
            if (presenter != null){
                presenter.setInfo("location",district);
            }
        }
    }

    @Override
    public void setAvatar(String avatar) {
        if (!isEmpty(avatar)){
            GlideUtils.getInstance().loadCircleImage(this,miv_avatar,avatar);
        }
    }

    @Override
    public void setSex(String sex) {
        if (!isEmpty(sex)){
            mtv_sex.setText(sex);
        }

    }

    @Override
    public void setNickname(String nickname) {
        if (!isEmpty(nickname)){
            mtv_nickname.setText(nickname);
        }

    }

    /**
     * 设置个性签名
     *
     * @param signature
     */
    @Override
    public void setSignature(String signature) {
        if (!isEmpty(signature)){
            mtv_autograph.setText(signature);
        }

    }

    @Override
    public void setBirth(String birth) {
        if (!isEmpty(birth)){
            mtv_birthday.setText(birth);
        }
    }

    /**
     * 设置标签
     *
     * @param tag
     */
    @Override
    public void setTag(String tag) {
        if (!isEmpty(tag)){
            mtv_interest.setText(tag);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode//昵称
                && requestCode == NickNameAct.REQUEST_CODE){
            String nickname = data.getStringExtra("nickname");
            if (!isEmpty(nickname) && presenter != null){
                mtv_nickname.setText(nickname);
                presenter.setInfo("nickname",nickname);
            }
        }else if (Activity.RESULT_OK == resultCode//个性签名
                && requestCode == AutographAct.REQUEST_CODE){
            String signature = data.getStringExtra("signature");
            if (!isEmpty(signature) && presenter != null){
                mtv_autograph.setText(signature);
                presenter.setInfo("signature",signature);
            }
        }else if (Activity.RESULT_OK == resultCode//兴趣选择
                && requestCode == SelectLikeAct.REQUEST_CODE){
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");

            if (!isEmpty(id) && !isEmpty(name) && presenter != null){
                mtv_interest.setText(name);
                presenter.setInfo("tag",id);
            }
        }
    }
}
