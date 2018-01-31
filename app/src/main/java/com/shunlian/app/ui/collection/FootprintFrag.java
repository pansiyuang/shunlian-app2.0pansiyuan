package com.shunlian.app.ui.collection;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FootAdapter;
import com.shunlian.app.bean.CalendarEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.presenter.FootPrintPresenter;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFootPrintView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.calendar.Calendar;
import com.shunlian.app.widget.calendar.CalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.FastClickListener.isFastClick;

/**
 * Created by Administrator on 2018/1/22.
 * 足迹
 */

public class FootprintFrag extends CollectionFrag implements View.OnClickListener, IFootPrintView, CalendarView.OnYearChangeListener, CalendarView.OnDateSelectedListener, CalendarView.OnStatusChangeListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.miv_pre)
    MyImageView miv_pre;

    @BindView(R.id.miv_next)
    MyImageView miv_next;

    @BindView(R.id.content)
    CoordinatorLayout content;

    @BindView(R.id.rl_date_control)
    RelativeLayout rl_date_control;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.miv_calendar_more)
    MyImageView miv_calendar_more;

    private FootPrintPresenter printPresenter;
    private boolean initiated = false;
    private List<Calendar> markData = new ArrayList<>();
    private FootAdapter footAdapter;
    private List<FootprintEntity.MarkData> markDataList;
    private GridLayoutManager manager;
    private CoordinatorLayout.LayoutParams params;
    private RelativeLayout.LayoutParams calendarParams;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_footprint, null, false);
    }

    @Override
    protected void initData() {
        recycler_list.setHasFixedSize(false);
        printPresenter = new FootPrintPresenter(baseContext, this);
        markDataList = new ArrayList<>();
        manager = new GridLayoutManager(getContext(), 3);
        recycler_list.setLayoutManager(manager);

        tv_date.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月");

        printPresenter.getMarkCalendar(String.valueOf(calendarView.getCurYear()), String.valueOf(calendarView.getCurMonth()));
        printPresenter.getMarklist(String.valueOf(calendarView.getCurYear()), String.valueOf(calendarView.getCurMonth()), false);

        params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        calendarParams = new RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        calendarView.shrink();
    }

    @Override
    protected void initListener() {
        miv_next.setOnClickListener(this);
        miv_pre.setOnClickListener(this);
        miv_calendar_more.setOnClickListener(this);

        calendarView.setOnYearChangeListener(this);
        calendarView.setOnDateSelectedListener(this);
        calendarView.setOnStatusChangeListener(this);
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (printPresenter != null) {
                            printPresenter.onRefresh();
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        super.initListener();
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    /**
     * 删除
     */
    @Override
    public void delete() {

    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {

    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {

    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {

    }

    @Override
    public void onClick(View v) {
        if (isFastClick())
            return;
        switch (v.getId()) {
            case R.id.miv_pre:
                calendarView.scrollToPre();
                break;
            case R.id.miv_next:
                calendarView.scrollToNext();
                break;
            case R.id.miv_calendar_more:
                if (calendarView.isExpand()) {
                    calendarView.shrink();
                    rl_date_control.setVisibility(View.GONE);
                } else {
                    calendarView.expand();
                    rl_date_control.setVisibility(View.VISIBLE);
                }
                calendarView.setLayoutParams(calendarParams);
                break;
        }
    }

    @Override
    public void getCalendarList(List<CalendarEntity> calendarEntityList) {
        if (!isEmpty(calendarEntityList)) {
            for (CalendarEntity calendarEntity : calendarEntityList) {
                if ("1".equals(calendarEntity.has_data)) {
                    int year = Integer.valueOf(calendarEntity.year);
                    int month = Integer.valueOf(calendarEntity.month);
                    int day = Integer.valueOf(calendarEntity.date);
                    markData.add(getSchemeCalendar(year, month, day, R.color.pink_color, "标"));
                }
            }
            calendarView.setSchemeDate(markData);
        }
    }

    @Override
    public void getMarkList(List<FootprintEntity.MarkData> list, List<FootprintEntity.DateInfo> dateInfoList, int page, int allPage) {
        if (page == 1) {
            markDataList.clear();
        }
        if (!isEmpty(list)) {
            markDataList.addAll(list);
        }
        if (footAdapter == null) {
            footAdapter = new FootAdapter(getContext(), true, markDataList);
            recycler_list.setAdapter(footAdapter);
            footAdapter.setPageLoading(page, allPage);
            footAdapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (printPresenter != null) {
                        printPresenter.onRefresh();
                    }
                }
            });
        } else {
            footAdapter.setPageLoading(page, allPage);
            if (markDataList.size() <= printPresenter.PAGE_SIZE)
                footAdapter.notifyDataSetChanged();
            else
                footAdapter.notifyItemInserted(printPresenter.PAGE_SIZE);
        }
        footAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        tv_date.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
    }

    @Override
    public void OnExpand() {
        int height = TransformUtil.dip2px(getActivity(), 440.5f);
        params.height = height;
        appBarLayout.setLayoutParams(params);
        calendarParams.height = TransformUtil.dip2px(getActivity(), 350f);
        calendarView.setLayoutParams(calendarParams);
        calendarView.setVisibility(View.GONE);
        rl_date_control.setVisibility(View.VISIBLE);
        miv_calendar_more.setRotation(180f);
    }

    @Override
    public void OnShrink() {
        int height = TransformUtil.dip2px(getActivity(), 130.5f);
        params.height = height;
        appBarLayout.setLayoutParams(params);
        calendarParams.height = TransformUtil.dip2px(getActivity(), 100f);
        calendarView.setLayoutParams(calendarParams);
        rl_date_control.setVisibility(View.GONE);
        miv_calendar_more.setRotation(0f);
    }
}
