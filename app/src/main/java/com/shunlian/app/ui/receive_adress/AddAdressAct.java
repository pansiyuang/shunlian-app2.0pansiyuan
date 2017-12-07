package com.shunlian.app.ui.receive_adress;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.presenter.AddAddressPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.AddAddressView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class AddAdressAct extends BaseActivity implements View.OnClickListener, AddAddressView {
    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.mtv_locate)
    MyTextView mtv_locate;

    @BindView(R.id.miv_locate)
    MyImageView miv_locate;

    @BindView(R.id.miv_default)
    MyImageView miv_default;

    @BindView(R.id.et_realname)
    EditText et_realname;

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.mtv_save)
    MyTextView mtv_save;

    private AddAddressPresenter addAddressPresenter;
    private String district_ids;
    private boolean isDefault;


    public static void startAct(Context context, ConfirmOrderEntity.Address address) {
        Intent intent = new Intent(context, AddAdressAct.class);
        intent.putExtra("address", address);//地址实体类
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_add_adress;
    }

    @Override
    protected void initData() {
        addAddressPresenter = new AddAddressPresenter(this, this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_address.setOnClickListener(this);
        mtv_locate.setOnClickListener(this);
        miv_locate.setOnClickListener(this);
        miv_default.setOnClickListener(this);
        mtv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                addAddressPresenter.initDistrict();
                break;
            case R.id.mtv_save:
                String isDefaults;
                if (isDefault){
                    isDefaults="1";
                }else {
                    isDefaults="0";
                }
                addAddressPresenter.saveAddress(et_realname.getText().toString(),et_mobile.getText().toString(),et_address.getText().toString(),isDefaults,district_ids);
                break;
            case R.id.miv_default:
                if (isDefault){
                    miv_default.setImageDrawable(getDrawableResouce(R.mipmap.btn_address_setaddress_n));
                    isDefault=false;
                }else {
                    miv_default.setImageDrawable(getDrawableResouce(R.mipmap.btn_address_setaddress_h));
                    isDefault=true;
                }
                break;
            case R.id.mtv_locate:
            case R.id.miv_locate:
                Location location = Common.getGPS(this);
                if (location != null) {
                    addAddressPresenter.initGps(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                }
                break;
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void getGps(DistrictGetlocationEntity districtGetlocationEntity) {
        String district = "";
        district_ids = "";
        for (int m = 0; m < districtGetlocationEntity.district_ids.size(); m++) {
            if (m >= districtGetlocationEntity.district_ids.size() - 1) {
                district_ids += districtGetlocationEntity.district_ids.get(m);
            } else {
                district_ids += districtGetlocationEntity.district_ids.get(m) + ",";
            }
        }
        for (int m = 0; m < districtGetlocationEntity.district_names.size(); m++) {
            if (m >= districtGetlocationEntity.district_names.size() - 1) {
                district += districtGetlocationEntity.district_names.get(m);
                tv_address.setText(district);
            } else {
                district += districtGetlocationEntity.district_names.get(m) + " ";
            }
        }
    }

    @Override
    public void getDistrict(String district, String district_ids) {
        this.district_ids = district_ids;
        tv_address.setText(district);
    }

    @Override
    public void saveAddressCallback() {
        finish();
        AddressListActivity.startAct(this);
    }
}
