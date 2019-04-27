package com.shunlian.app.basementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;

public class BasementMainActivity extends AppCompatActivity {

    private float y;
    private int width;
    private Scroller scroller;
    private BasementView basementView;
    private Button btn1;
    private Button btn2;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, BasementMainActivity.class);
        context.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basementview);
        basementView = findViewById(R.id.bv);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        View basementLayout = basementView.getBasementLayout();
        basementLayout.setBackgroundResource(R.mipmap.base_ic);

        RelativeLayout currentLayout = (RelativeLayout) basementView.getCurrentLayout();
//        currentLayout.setBackgroundResource(R.color.green);

        ListView listView = new ListView(this);
        listView.setAdapter(new MyAdapter(this));
        currentLayout.addView(listView);
        basementView.bindScrollAbleView(listView);

        btn2.setVisibility(View.GONE);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basementView.toB1();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.VISIBLE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basementView.toF1();
                btn2.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
            }
        });

    }


    public class MyAdapter extends BaseAdapter {
        Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = View.inflate(context, R.layout.basemenyview_item, null);
                holder =new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();

            }
            holder.textView.setText(position+"");
            return convertView;
        }
        class ViewHolder {
            TextView textView;
            View view;
            public ViewHolder(View view) {
                this.view = view;
                textView = view.findViewById(R.id.textView);
            }
        }
    }

}
