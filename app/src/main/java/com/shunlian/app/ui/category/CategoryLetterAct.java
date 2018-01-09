package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ContactsAdapter;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.WaveSideBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CategoryLetterAct extends BaseActivity {

    @BindView(R.id.mtv_reset)
    MyTextView mtv_reset;

    @BindView(R.id.mtv_finish)
    MyTextView mtv_finish;

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;

    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    @BindView(R.id.side_bar)
    WaveSideBar side_bar;

    private List<GetListFilterEntity.Brand.Item> items = new ArrayList<>();

    private ContactsAdapter contactsAdapter;

    private ArrayList<String> letters;
    private List<GetListFilterEntity.Brand> brands;
    @Override
    protected int getLayoutId() {
        return R.layout.act_letter_category;
    }

    public static void startAct(Context context, List<GetListFilterEntity.Brand> brands,ArrayList<String> letters) {
        Intent intent = new Intent(context, CategoryLetterAct.class);
        intent.putStringArrayListExtra("letters", letters);
        intent.putExtra("brands", (Serializable) brands);
        context.startActivity(intent);
    }

    public void dealData(){
        for (int m=0;m<letters.size();m++){
            for (int n=0;n<brands.size();n++){
                if (letters.get(m).equals(brands.get(n).first_letter)){
                    items.addAll(brands.get(n).item_list);
                    break;
                }
            }
            if (m>=letters.size()-1){
                rv_contacts.setLayoutManager(new LinearLayoutManager(this));
                if (contactsAdapter==null){
                    contactsAdapter=new ContactsAdapter(this, false,items);
                }
                rv_contacts.setAdapter(contactsAdapter);
                side_bar.setIndexItems(letters);
                side_bar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
                    @Override
                    public void onSelectIndexItem(String index) {
                        for (int i=0; i<items.size(); i++) {
                            if (items.get(i).first_letter.equals(index)) {
                                ((LinearLayoutManager) rv_contacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                                return;
                            }
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        letters=getIntent().getStringArrayListExtra("letters");
        brands= (List<GetListFilterEntity.Brand>) getIntent().getSerializableExtra("brands");
        mtv_reset.setOnClickListener(this);
        mtv_finish.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        dealData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_reset:
                Constant.BRAND_IDS.clear();
                contactsAdapter.notifyDataSetChanged();
                break;
            case R.id.mtv_finish:
                finish();
                break;
            case R.id.mtv_cancel:
                Constant.BRAND_IDS.clear();
                Constant.BRAND_IDS.addAll(Constant.BRAND_IDSBEFORE);
                finish();
                break;
        }
    }
}
