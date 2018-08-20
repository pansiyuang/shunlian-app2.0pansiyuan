package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.PersonalDataEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPersonalDataView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chihane.shunlian.BottomDialog;
import chihane.shunlian.ISelectAble;
import chihane.shunlian.Selector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/23.
 */

public class PersonalDataPresenter extends BasePresenter<IPersonalDataView> {
    private List<DistrictAllEntity.Province> provinces;
    private BottomDialog dialog;
    private String one, two;

    public PersonalDataPresenter(Context context, IPersonalDataView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<PersonalDataEntity>> baseEntityCall = getApiService().personalData(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonalDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonalDataEntity> entity) {
                super.onSuccess(entity);
                PersonalDataEntity data = entity.data;
                iView.setAvatar(data.avatar);
                iView.setNickname(data.nickname);
                iView.setSex(data.sex);
                iView.setLocation(data.location, null);
                iView.setTag(data.tag);
                iView.setBirth(data.birth);
                iView.setSignature(data.signature);
            }
        });
    }


    public void setInfo(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().setinfo(getRequestBody(map));
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if ("nickname".equals(key)){
                    iView.setNickname(value);
                }else if ("signature".equals(key)){
                    iView.setSignature(value);
                }
                Common.staticToast(entity.message);
            }
        });

    }

    public void initDistrict() {
        if (provinces == null || provinces.size() <= 0) {
            Map<String, String> map = new HashMap<>();
            sortAndMD5(map);

            Call<BaseEntity<DistrictAllEntity>> baseEntityCall = getApiService().districtAll(map);
            getNetData(true, baseEntityCall,
                    new SimpleNetDataCallback<BaseEntity<DistrictAllEntity>>() {
                        @Override
                        public void onSuccess(BaseEntity<DistrictAllEntity> entity) {
                            super.onSuccess(entity);
                            DistrictAllEntity data = entity.data;
                            if (data != null) {
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

    private void showDialog() {
        Selector selector = new Selector(context, 2);

        selector.setDataProvider((currentDeep, preId, receiver) -> {
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
        });

        selector.setSelectedListener((selectAbles) -> {
            String district_ids = "", district = "";
            for (int m = 0; m < selectAbles.size(); m++) {
                if (m >= selectAbles.size() - 1) {
                    district_ids += selectAbles.get(m).getId();
                    district += selectAbles.get(m).getName();
                    iView.setLocation(district, district_ids);
                    dialog.dismiss();
                } else {
                    district_ids += selectAbles.get(m).getId() + ",";
                    district += selectAbles.get(m).getName() + " ";
                }
            }
        });

        if (dialog == null) {
            dialog = new BottomDialog(context);
        }
        dialog.init(context, selector);
        dialog.show();
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

    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        if (isEmpty(filePath)) {
            return;
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
            if (file == null)continue;
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file[]", file.getName(), requestBody);
            parts.add(part);
        }


        Map<String, String> map = new HashMap<>();
        map.put("path_name", uploadPath);
        sortAndMD5(map);

        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(parts, map);
        getNetData(true, call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                String domain = uploadPicEntity.domain;
                if (!isEmpty(uploadPicEntity.relativePath)) {
                    String s = uploadPicEntity.relativePath.get(0);
                    iView.setRefundPics(s, domain);
                }
            }
        });
    }
}
