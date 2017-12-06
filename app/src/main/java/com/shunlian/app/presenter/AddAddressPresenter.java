package com.shunlian.app.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.AddAddressView;
import com.shunlian.app.view.StoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.DataProvider;
import chihane.jdaddressselector.ISelectAble;
import chihane.jdaddressselector.SelectedListener;
import chihane.jdaddressselector.Selector;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class AddAddressPresenter extends BasePresenter<AddAddressView> {
    private ArrayList<DistrictAllEntity.Province> provinces;
    private String one, two;
    private BottomDialog dialog ;
    private Context context;

    public AddAddressPresenter(Context context, AddAddressView iView) {
        super(context, iView);
        this.context=context;
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<DistrictAllEntity>> baseEntityCall = getApiService().districtAll(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<DistrictAllEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DistrictAllEntity> entity) {
                super.onSuccess(entity);
                DistrictAllEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("DistrictAllEntity:" + data);
                    iView.storeHeader(data.head);
                    iView.storeVoucher(data.voucher);
                    if (data.body != null) {
                        typeOneHandle(data.body);
                    }
                }
            }
        });
    }
    public void setData(ArrayList<ISelectAble> data, final String name, final int id) {
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
                            DistrictAllEntity.Province.City city=provinces.get(j).children.get(m);
                            setData(data,city.name, Integer.parseInt(city.id));
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
                            if (two.equals(provinces.get(j).children.get(m).id)){
                                for (int n = 0; n < provinces.get(j).children.get(m).children.size(); n++){
                                    DistrictAllEntity.Province.City.County county=provinces.get(j).children.get(m).children.get(n);
                                    setData(data,county.name, Integer.parseInt(county.id));
                                    if (n>= provinces.get(j).children.get(m).children.size() - 1) {
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
                    setData(data,provinces.get(j).name, Integer.parseInt(provinces.get(j).id));
                    if (j>=provinces.size()-1){
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
                String result = "";
                for (ISelectAble selectAble : selectAbles) {
                    result += selectAble.getName() + " ";
                }
                dialog.dismiss();
                Common.staticToast(context,result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
        if (dialog==null){
            dialog = new BottomDialog(this);
        }else {
            dialog.init(this, selector);
            dialog.show();
        }

    }
    public void initDistrict(){

    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
