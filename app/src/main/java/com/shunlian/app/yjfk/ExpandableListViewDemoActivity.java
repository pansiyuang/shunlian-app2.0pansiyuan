package com.shunlian.app.yjfk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/19.
 */

public class ExpandableListViewDemoActivity extends BaseActivity {
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;

    private String[] groups = {"A", "B", "C"};

    private String[][] childs = {{"A1", "A2", "A3", "A4"}, {"A1", "A2", "A3", "B4"}, {"A1", "A2", "A3", "C4"}};

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ExpandableListViewDemoActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_expandable;
    }

    @Override
    protected void initData() {
        expandableListView.setAdapter(new MyExpandableAdapter());



    }
    class MyExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return childs[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return childs[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_group, null);
            } else {

            }
            TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
            tv_group.setText(groups[i]);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_child, null);
            }

            ImageView iv_child = (ImageView) convertView.findViewById(R.id.iv_child);
            TextView tv_child = (TextView) convertView.findViewById(R.id.tv_child);

            //iv_child.setImageResource(resId);
            tv_child.setText(childs[groupPosition][childPosition]);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
