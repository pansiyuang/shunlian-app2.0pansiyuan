package com.shunlian.app.shunlianyoupin;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shunlian.app.R;

public class BottomBar extends LinearLayout implements OnClickListener {

	private ImageView mHomeIv;
	private TextView mHomeTv;
	private TextView mstegoryTv;
	private ImageView mstegoryIv;
	private ImageView mpersonIv;
	private TextView mpersonTv;
	private IBottomBarClickListener mListener;
	private int mCurrenTabId;

	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setIBottomBarClickListener(IBottomBarClickListener listener) {
		mListener=listener;
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		findViewById(R.id.ll_tab_main_page).setOnClickListener(this);
		findViewById(R.id.ll_tab_main_mystore).setOnClickListener(this);
		findViewById(R.id.ll_tab_person_center).setOnClickListener(this);

		mHomeIv = findViewById(R.id.miv_tab_main);
		mHomeTv =  findViewById(R.id.mtv_tab_main);
		mstegoryIv =  findViewById(R.id.miv_tab_mystore);
		mstegoryTv = findViewById(R.id.mtv_tab_mystore);
		mpersonIv =  findViewById(R.id.miv_tab_person_center);
		mpersonTv =  findViewById(R.id.mtv_tab_person_center);

	}


	@Override
	public void onClick(View v) {
		if (mCurrenTabId==v.getId()) {
			return;
		}

		mHomeIv.setSelected(v.getId() == R.id.ll_tab_main_page);
		mHomeTv.setSelected(v.getId() == R.id.ll_tab_main_page);
		mstegoryIv.setSelected(v.getId() == R.id.ll_tab_main_mystore);
		mstegoryTv.setSelected(v.getId() == R.id.ll_tab_main_mystore);
		mpersonIv.setSelected(v.getId() == R.id.ll_tab_person_center);
		mpersonTv.setSelected(v.getId() == R.id.ll_tab_person_center);
		if (mListener!=null) {
			mListener.onItemClick(v.getId());
			mCurrenTabId=v.getId();
		}
	}

}
