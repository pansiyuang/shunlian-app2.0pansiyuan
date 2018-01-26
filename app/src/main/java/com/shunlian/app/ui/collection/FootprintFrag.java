package com.shunlian.app.ui.collection;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FootAdapter;
import com.shunlian.app.adapter.SingleCategoryAdapter;
import com.shunlian.app.bean.CalendarEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.presenter.FootPrintPresenter;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFootPrintView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.calendar.CustomDayView;
import com.shunlian.app.widget.calendar.Utils;
import com.shunlian.app.widget.calendar.component.CalendarAttr;
import com.shunlian.app.widget.calendar.component.CalendarViewAdapter;
import com.shunlian.app.widget.calendar.interf.OnSelectDateListener;
import com.shunlian.app.widget.calendar.model.CalendarDate;
import com.shunlian.app.widget.calendar.view.Calendar;
import com.shunlian.app.widget.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.FastClickListener.isFastClick;

/**
 * Created by Administrator on 2018/1/22.
 * 足迹
 */

public class FootprintFrag extends CollectionFrag implements View.OnClickListener, IFootPrintView {
    @BindView(R.id.calendar_view)
    MonthPager calendar_view;

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

    private FootPrintPresenter printPresenter;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private HashMap<String, String> markData = new HashMap<>();
    private FootAdapter footAdapter;
    private List<FootprintEntity.MarkData> markDataList;
    private GridLayoutManager manager;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_footprint, null, false);
    }

    @Override
    protected void initData() {
        calendar_view.setViewHeight(TransformUtil.dip2px(baseContext, 270));
        recycler_list.setHasFixedSize(false);

        printPresenter = new FootPrintPresenter(baseContext, this);
        initCurrentDate();
        initCalendarView();
        initSelectListener();

        markDataList = new ArrayList<>();
        manager = new GridLayoutManager(getContext(),3);
        recycler_list.setLayoutManager(manager);
        recycler_list.setNestedScrollingEnabled(false);

        printPresenter.getMarkCalendar(String.valueOf(currentDate.getYear()), String.valueOf(currentDate.month));
        printPresenter.getMarklist(String.valueOf(currentDate.getYear()), String.valueOf(currentDate.month), false);
    }

    @Override
    protected void initListener() {
        miv_next.setOnClickListener(this);
        miv_pre.setOnClickListener(this);
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

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tv_date.setText(currentDate.getYear() + "年" + currentDate.month + "月");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(baseContext, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                baseContext,
                onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                recycler_list.scrollToPosition(0);
            }
        });
        initMonthPager();
    }

    private void initSelectListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                calendar_view.selectOtherMonth(offset);
            }
        };
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        calendar_view.setAdapter(calendarAdapter);
        calendar_view.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        calendar_view.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        calendar_view.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    refreshClickDate(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tv_date.setText(date.getYear() + "年" + date.month + "月");
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tv_date.setText(today.getYear() + "年" + today.month + "月");
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
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                    Utils.scrollTo(content, recycler_list, calendar_view.getViewHeight(), 200);
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, recycler_list, calendar_view.getCellHeight(), 200);
                    calendarAdapter.switchToWeek(calendar_view.getRowIndex());
                }
//                calendar_view.setCurrentItem(calendar_view.getCurrentPosition() - 1);
                break;
            case R.id.miv_next:
//                calendar_view.setCurrentItem(calendar_view.getCurrentPosition() + 1);
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                    Utils.scrollTo(content, recycler_list, calendar_view.getViewHeight(), 200);
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, recycler_list, calendar_view.getCellHeight(), 200);
                    calendarAdapter.switchToWeek(calendar_view.getRowIndex());
                }
                break;
        }
    }

    @Override
    public void getCalendarList(List<CalendarEntity> calendarEntityList) {
        if (!isEmpty(calendarEntityList)) {
            for (CalendarEntity calendarEntity : calendarEntityList) {
                if ("1".equals(calendarEntity.has_data)) {
                    markData.put(calendarEntity.getCurrentDate(), calendarEntity.has_data);
                }
            }
            calendarAdapter.setMarkData(markData);
            calendarAdapter.notifyDataChanged();
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
        LogUtil.httpLogW("markDataList:" + markDataList.size());
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
}
