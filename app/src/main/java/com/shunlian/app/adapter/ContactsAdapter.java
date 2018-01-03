package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.ui.category.LetterCategoryAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class ContactsAdapter extends BaseRecyclerAdapter<Contact> {
    private LetterCategoryAct letterCategoryAct;

    public ContactsAdapter(Context context, boolean isShowFooter, List<Contact> contacts) {
        super(context, false, contacts);
        letterCategoryAct = (LetterCategoryAct) context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contacts, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(final RecyclerView.ViewHolder holder, final int position) {
        final ContactsViewHolder viewHolder = (ContactsViewHolder) holder;
        Contact contact = lists.get(position);
        if (position == 0 || !lists.get(position - 1).getIndex().equals(contact.getIndex())) {
            viewHolder.mtv_index.setVisibility(View.VISIBLE);
            viewHolder.mtv_index.setText(contact.getIndex());
        } else {
            viewHolder.mtv_index.setVisibility(View.GONE);
        }
        if (letterCategoryAct.strings.size() > 0) {
            for (int i = 0; i < letterCategoryAct.strings.size(); i++) {
                if (String.valueOf(position).equals(letterCategoryAct.strings.get(i))) {
                    viewHolder.mtv_name.setTextColor(getColor(R.color.pink_color));
                    LogUtil.augusLogW("yxf000");
                    break;
                } else if (i >= letterCategoryAct.strings.size() - 1) {
                    viewHolder.mtv_name.setTextColor(getColor(R.color.new_text));
                    LogUtil.augusLogW("yxf999");
                    break;
                }
            }
        } else {
            viewHolder.mtv_name.setTextColor(getColor(R.color.new_text));
        }
        viewHolder.mtv_name.setText(contact.getName());
        viewHolder.mtv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickListener.isFastClick()) {
                    return;
                }
                if (letterCategoryAct.strings.size() > 0) {
                    for (int i = 0; i < letterCategoryAct.strings.size(); i++) {
                        if (String.valueOf(position).equals(letterCategoryAct.strings.get(i))) {
                            viewHolder.mtv_name.setTextColor(getColor(R.color.new_text));
                            letterCategoryAct.strings.remove(i);
                            LogUtil.augusLogW("yxf123");
                            break;
                        } else if (i >= letterCategoryAct.strings.size() - 1) {
                            if (letterCategoryAct.strings.size() < 8) {
                                viewHolder.mtv_name.setTextColor(getColor(R.color.pink_color));
                                letterCategoryAct.strings.add(String.valueOf(position));
                                LogUtil.augusLogW("yxf456");
                            } else {
                                Common.staticToast("已达上限8个");
                            }
                            break;
                        }
                    }
                } else {
                    viewHolder.mtv_name.setTextColor(getColor(R.color.pink_color));
                    letterCategoryAct.strings.add(String.valueOf(position));
                    LogUtil.augusLogW("yxf789");
                }
            }
        });
    }


    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public MyTextView mtv_index;
        public MyTextView mtv_name;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            mtv_index = (MyTextView) itemView.findViewById(R.id.mtv_index);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
        }
    }
}