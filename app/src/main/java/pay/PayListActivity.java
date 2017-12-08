package pay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PayListAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class PayListActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.recy_pay)
    RecyclerView recy_pay;
    private ArrayList<ConfirmOrderEntity.PayTypes> lists;


    public static void startAct(Context context, ArrayList<ConfirmOrderEntity.PayTypes> lists){
        Intent intent = new Intent(context, PayListActivity.class);
        intent.putExtra("list",lists);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        lists = (ArrayList<ConfirmOrderEntity.PayTypes>) getIntent().getSerializableExtra("list");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_pay.setLayoutManager(manager);
        PayListAdapter adapter = new PayListAdapter(this,false,lists);
        recy_pay.setAdapter(adapter);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_close:
                final PromptDialog promptDialog = new PromptDialog(this);
                promptDialog.setSureAndCancleListener("确定要取消支付吗？", "继续支付", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.dismiss();
                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
                break;
        }
    }
}
