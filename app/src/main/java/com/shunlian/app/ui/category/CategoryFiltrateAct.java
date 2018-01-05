package com.shunlian.app.ui.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ContactsAdapter;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.WaveSideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LetterCategoryAct extends BaseActivity {

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

    public List<String> strings=new ArrayList<>();

    private ArrayList<Contact> contacts = new ArrayList<>();

    private ContactsAdapter contactsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_letter_category;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_reset.setOnClickListener(this);
        mtv_finish.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        contacts.addAll(Contact.getEnglishContacts());
        rv_contacts.setLayoutManager(new LinearLayoutManager(this));
        if (contactsAdapter==null){
            contactsAdapter=new ContactsAdapter(this, false,contacts);
        }
        rv_contacts.setAdapter(contactsAdapter);
        side_bar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i=0; i<contacts.size(); i++) {
                    if (contacts.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) rv_contacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.mtv_reset:
                strings.clear();
                contactsAdapter.notifyDataSetChanged();
                break;
            case R.id.mtv_finish:

                break;
            case R.id.mtv_cancel:

                break;
        }
    }
}
