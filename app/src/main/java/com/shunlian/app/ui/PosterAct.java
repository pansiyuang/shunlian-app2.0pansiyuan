package com.shunlian.app.ui;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.bean.PosterEntity;
import com.shunlian.app.presenter.PHbCode;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IHbCode;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**R.id.RushBuyCountDownTimerView
 * Created by Administrator on 2019/3/21.
 */

public class PosterAct extends BaseActivity implements IHbCode {
    @BindView(R.id.miv_code)
    MyImageView miv_code;
    @BindView(R.id.miv_code2)
    MyImageView miv_code2;
    @BindView(R.id.miv1)
    MyImageView miv1;
    @BindView(R.id.mtv2)
    MyTextView mtv2;
    private PHbCode pHbCode;


    public static void startAct(Context context) {
        Intent intent = new Intent(context, PosterAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_poster;
    }

    @Override
    protected void initData() {

        pHbCode = new PHbCode(this, this, "1");


    }
    public String Stringinsert(String a,String b,int t){
        return a.substring(0,t)+b+a.substring(t,a.length());
    }
    @Override
    public void setApiData(PosterEntity data) {
        String title = data.title;
        String stringinsert = Stringinsert(title, "·", 4);
        mtv2.setText(stringinsert);
        GlideUtils.getInstance().loadImage(PosterAct.this,miv_code,data.url);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
