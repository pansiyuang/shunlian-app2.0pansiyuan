package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.AddAddressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chihane.shunlian.BottomDialog;
import chihane.shunlian.DataProvider;
import chihane.shunlian.ISelectAble;
import chihane.shunlian.SelectedListener;
import chihane.shunlian.Selector;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class AddAddressPresenter extends BasePresenter<AddAddressView> {
    private List<DistrictAllEntity.Province> provinces;
    private String one, two;
    private BottomDialog dialog;
    private Context context;

    public AddAddressPresenter(Context context, AddAddressView iView) {
        super(context, iView);
        this.context = context;
    }

    @Override
    protected void initApi() {

    }

    public void addressEdit(String address_id,String realname,String mobile,String address,String isdefault,String district_ids){
        Map<String, String> map = new HashMap<>();
        map.put("realname", realname);
        map.put("mobile", mobile);
        map.put("address", address);
        map.put("isdefault", isdefault);
        map.put("district_ids", district_ids);
        map.put("address_id", address_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addressEdit(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.saveAddressCallback();
            }
        });
    }
    public void addressRemove(String address_id){
        Map<String, String> map = new HashMap<>();
        map.put("address_id", address_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delAddress(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.saveAddressCallback();
            }
        });
    }
    public void saveAddress(String realname,String mobile,String address,String isdefault,String district_ids){
        Map<String, String> map = new HashMap<>();
        map.put("realname", realname);
        map.put("mobile", mobile);
        map.put("address", address);
        map.put("isdefault", isdefault);
        map.put("district_ids", district_ids);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addressAdd(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.saveAddressCallback();
            }
        });
    }
    public void initGps(String lng, String lat) {
        Map<String, String> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<DistrictGetlocationEntity>> baseEntityCall = getAddCookieApiService().districtGetlocation(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<DistrictGetlocationEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DistrictGetlocationEntity> entity) {
                super.onSuccess(entity);
                DistrictGetlocationEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("DistrictGetlocationEntity:" + data);
                    iView.getGps(data);
                }
            }
        });
    }

    private void setData(ArrayList<ISelectAble> data, final String name, final int id) {
        data.add(new ISelectAble() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getId() {
                return id;
            }

            @Override
            public Object getArg() {
                return this;
            }
        });

    }

    private ArrayList<ISelectAble> getDatas(int flag, String one, String two) {
        ArrayList<ISelectAble> data = new ArrayList<>();
        switch (flag) {
            case 1:
                for (int j = 0; j < provinces.size(); j++) {
                    if (one.equals(provinces.get(j).id)) {
                        for (int m = 0; m < provinces.get(j).children.size(); m++) {
                            DistrictAllEntity.Province.City city = provinces.get(j).children.get(m);
                            setData(data, city.name, Integer.parseInt(city.id));
                            if (m >= provinces.get(j).children.size() - 1) {
                                return data;
                            }
                        }
                        break;
                    }
                }
                break;
            case 2:
                for (int j = 0; j < provinces.size(); j++) {
                    if (one.equals(provinces.get(j).id)) {
                        for (int m = 0; m < provinces.get(j).children.size(); m++) {
                            if (two.equals(provinces.get(j).children.get(m).id)) {
                                for (int n = 0; n < provinces.get(j).children.get(m).children.size(); n++) {
                                    DistrictAllEntity.Province.City.County county = provinces.get(j).children.get(m).children.get(n);
                                    setData(data, county.name, Integer.parseInt(county.id));
                                    if (n >= provinces.get(j).children.get(m).children.size() - 1) {
                                        return data;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            default:
                for (int j = 0; j < provinces.size(); j++) {
                    setData(data, provinces.get(j).name, Integer.parseInt(provinces.get(j).id));
                    if (j >= provinces.size() - 1) {
                        return data;
                    }
                }
                break;
        }
        return data;
    }

    private void showDialog() {
        Selector selector = new Selector(context, 3);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int preId, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                int flag;
                switch (currentDeep) {
                    case 1:
                        flag = 1;
                        one = String.valueOf(preId);
                        break;
                    case 2:
                        flag = 2;
                        two = String.valueOf(preId);
                        break;
                    default:
                        flag = 0;
                        break;
                }
                receiver.send(getDatas(flag, one, two));
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                String district_ids = "", district = "";
                for (int m = 0; m < selectAbles.size(); m++) {
                    if (m >= selectAbles.size()-1) {
                        district_ids += selectAbles.get(m).getId();
                        district += selectAbles.get(m).getName();
                        iView.getDistrict(district, district_ids);
                        dialog.dismiss();
                    } else {
                        district_ids += selectAbles.get(m).getId() + ",";
                        district += selectAbles.get(m).getName() + " ";
                    }
                }
            }
        });
        if (dialog == null) {
            dialog = new BottomDialog(context);
        }
        dialog.init(context, selector);
        dialog.show();
    }

    public void initDistrict() {
        if (provinces == null || provinces.size() <= 0) {
            Map<String, String> map = new HashMap<>();
            sortAndMD5(map);

            Call<BaseEntity<DistrictAllEntity>> baseEntityCall = getApiService().districtAll(map);
            getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<DistrictAllEntity>>() {
                @Override
                public void onSuccess(BaseEntity<DistrictAllEntity> entity) {
                    super.onSuccess(entity);
                    DistrictAllEntity data = entity.data;
                    if (data != null) {
                        LogUtil.httpLogW("DistrictAllEntity:" + data);
                        if (data.district_all != null && data.district_all.size() > 0) {
                            provinces = data.district_all;
                            showDialog();
                        } else {
                            Common.staticToast("地址库初始失败...");
                        }
                    }
                }
            });
        } else {
            showDialog();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
