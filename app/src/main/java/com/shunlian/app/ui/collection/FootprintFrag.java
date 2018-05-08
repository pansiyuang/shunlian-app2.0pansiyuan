package com.shunlian.app.ui.collection;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.FootprintAdapter;
import com.shunlian.app.bean.CalendarEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.presenter.FootPrintPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFootPrintView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.calendar.Calendar;
import com.shunlian.app.widget.calendar.CalendarView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.MyOnClickListener.isFastClick;
import static com.shunlian.app.utils.TransformUtil.expandViewTouchDelegate;

/**
 * Created by Administrator on 2018/1/22.
 * 足迹
 */

public class FootprintFrag extends CollectionFrag implements View.OnClickListener, IFootPrintView, CalendarView.OnMonthChangeListener, CalendarView.OnDateSelectedListener, CalendarView.OnStatusChangeListener, FootprintAdapter.OnChildClickListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.miv_pre)
    MyImageView miv_pre;

    @BindView(R.id.miv_next)
    MyImageView miv_next;

    @BindView(R.id.rl_date_control)
    RelativeLayout rl_date_control;

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.ll_calendar)
    LinearLayout ll_calendar;

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    private FootPrintPresenter printPresenter;
    private List<Calendar> markData = new ArrayList<>();
    private List<FootprintEntity.MarkData> markDataList;
    private List<FootprintEntity.MarkData> delList;
    private List<FootprintEntity.DateInfo> dateInfoList;
    private GridLayoutManager manager;
    private boolean isSelectAll = false;
    private int selectStatus = 0; // 0 全选  1 部分选择 2 全不选
    private FootprintAdapter footprintAdapter;
    private LinearLayout.LayoutParams calendarParams;
    private int hide_space;
    private int mScrolledDistance = 0;
    private boolean mControlsVisible = true;
    private int currentMonth;
    private int currentYear;
    private int currentDay;
    public static Calendar mCurrentCalendar = null;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_footprint, null, false);
    }

    @Override
    protected void initData() {

        hide_space = TransformUtil.dip2px(getActivity(), 105.5f);


        recycler_list.setHasFixedSize(false);
        printPresenter = new FootPrintPresenter(baseContext, this);
        markDataList = new ArrayList<>();
        delList = new ArrayList<>();
        dateInfoList = new ArrayList<>();
        manager = new GridLayoutManager(getContext(), 3);
        recycler_list.setLayoutManager(manager);
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false); //解决刷新item图片闪烁的问题

        tv_date.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月");
        currentDay = calendarView.getCurDay();
        printPresenter.getMarkCalendar();
        printPresenter.getMarklist(String.valueOf(calendarView.getCurYear()), String.valueOf(calendarView.getCurMonth()), false);
        initCalendarView();
    }

    @Override
    protected void initListener() {
        miv_next.setOnClickListener(this);
        miv_pre.setOnClickListener(this);
        miv_more.setOnClickListener(this);

        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnDateSelectedListener(this);
        calendarView.setOnStatusChangeListener(this);

        expandViewTouchDelegate(miv_more, TransformUtil.dip2px(baseActivity, 10f), TransformUtil.dip2px(baseActivity, 10f), TransformUtil.dip2px(baseActivity, 10f), TransformUtil.dip2px(baseActivity, 10f));

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

                int firstVisibleItem = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (firstVisibleItem == 0) {
                    if (!mControlsVisible) {
                        showViews();
                        mControlsVisible = true;
                    }
                } else {
                    if (mScrolledDistance > hide_space && mControlsVisible) {
                        hideViews();
                        mControlsVisible = false;
                        mScrolledDistance = 0;
                    } else if (mScrolledDistance < -hide_space && !mControlsVisible) {
                        showViews();
                        mControlsVisible = true;
                        mScrolledDistance = 0;
                    }
                }
                if ((mControlsVisible && dy > 0) || (!mControlsVisible && dy < 0)) {
                    mScrolledDistance += dy;
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
        super.initListener();
    }

    public void initCalendarView() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        currentMonth = calendar.get(java.util.Calendar.MONTH) + 1;
        currentYear = calendar.get(java.util.Calendar.YEAR);
        if (currentMonth == 1) {
            calendarView.setRange(currentYear - 1, 12, currentYear, 1);
        } else {
            calendarView.setRange(currentYear, currentMonth - 1, currentYear, currentMonth);
        }
        calendarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        calendarView.shrink();
        setShrinkParams();
    }


    private void hideViews() {
        ll_calendar.animate().translationY(-ll_calendar.getHeight() - TransformUtil.dip2px(getActivity(), 15f)).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        ll_calendar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
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
        String ids = getDelIds();
        if (isEmpty(ids)) {
            return;
        }
        String date, month, day;
        if (currentMonth < 10) {
            month = "0" + currentMonth;
        } else {
            month = String.valueOf(currentMonth);
        }
        if (currentDay < 10) {
            day = "0" + currentDay;
        } else {
            day = String.valueOf(currentDay);
        }
        date = currentYear + month + day;
        LogUtil.httpLogW("删除日期：" + date);
        printPresenter.deleteBatch(ids, date);
    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {
        if (isSelectAll) {
            isSelectAll = false;
        } else {
            isSelectAll = true;
        }
        toSelectAll(isSelectAll);
        footprintAdapter.notifyDataSetChanged();
    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {
        LogUtil.httpLogW("operationMange()");
        isSelectAll = false;
        selectStatus = 2;
        toSelectAll(false);
        footprintAdapter.setEditMode(false);
    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {
        selectStatus = 2;
        isSelectAll = false;
        toSelectAll(false);
        ((MyCollectionAct) baseActivity).setManageState(selectStatus);
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        if (!isEmpty(markDataList) && footprintAdapter != null) {
            if (!footprintAdapter.getEditMode()) {
                footprintAdapter.setEditMode(true);
            } else {
                footprintAdapter.setEditMode(false);
            }
            return true;
        }
        return false;
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
            case R.id.miv_more:
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
    public void getMarkList(List<FootprintEntity.MarkData> list, List<FootprintEntity.DateInfo> dateList, int page, int allPage) {
        if (page == 1) {
            markDataList.clear();
            dateInfoList.clear();
            dateInfoList.addAll(dateList);
        }
        if (isSelectAll) {
            for (FootprintEntity.DateInfo dateInfo : dateInfoList) {
                dateInfo.isSelect = true;
            }
        }
        if (!isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                FootprintEntity.MarkData markData = list.get(i);
                for (int j = 0; j < dateInfoList.size(); j++) {
                    FootprintEntity.DateInfo dateInfo = dateInfoList.get(j);
                    if (dateInfo.date.equals(markData.date_normal) && dateInfo.isSelect) {
                        markData.isSelect = true;
                        delList.add(markData);
                        break;
                    }
                }
                markDataList.add(markData);
            }
        }

        if (footprintAdapter == null) {
            footprintAdapter = new FootprintAdapter(baseActivity, markDataList, dateInfoList);
            footprintAdapter.setOnChildClickListener(this);
            recycler_list.setAdapter(footprintAdapter);
        } else {
            footprintAdapter.initData(markDataList, dateInfoList);
            footprintAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void delSuccess(String msg, List<FootprintEntity.DateInfo> dateInfos) {
        delList.clear();
        Iterator markIt = markDataList.iterator();
        while (markIt.hasNext()) {
            FootprintEntity.MarkData markData = (FootprintEntity.MarkData) markIt.next();
            if (markData.isSelect) {
                markIt.remove();
            }
        }

        footprintAdapter.initData(markDataList, dateInfos);
        footprintAdapter.notifyDataSetChanged();
        Common.staticToast(msg);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onMonthChange(int year, int month) {
        if (month == currentMonth && year == currentYear) {
            miv_next.setImageDrawable(getDrawableResouce(R.mipmap.img_shoucang_zuji_right_h));
            miv_pre.setImageDrawable(getDrawableResouce(R.mipmap.img_shoucang_zuji_left_n));
        } else {
            miv_next.setImageDrawable(getDrawableResouce(R.mipmap.img_shoucang_zuji_right_n));
            miv_pre.setImageDrawable(getDrawableResouce(R.mipmap.img_shoucang_zuji_left_h));
        }
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        tv_date.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
        if (isClick) {
            if (isEmpty(calendar.getScheme())) {
                Common.staticToast("当前日期没有足迹");
            } else {
                mCurrentCalendar = calendar;
                printPresenter.initPage();
                currentDay = mCurrentCalendar.getDay();
                printPresenter.getMarklist(String.valueOf(calendar.getYear()), String.valueOf(calendar.getMonth()), String.valueOf(calendar.getDay()), false);
            }
        }
    }

    @Override
    public void OnExpand() {
        setExpandParams();
    }

    @Override
    public void OnShrink() {
        setShrinkParams();
    }

    public void setExpandParams() {
        calendarParams.height = TransformUtil.dip2px(getActivity(), 315f);
        calendarView.setLayoutParams(calendarParams);

        if (rl_date_control.getVisibility() == View.GONE) {
            rl_date_control.setVisibility(View.VISIBLE);
        }
        miv_more.setRotation(180f);
        if (footprintAdapter != null) {
            footprintAdapter.setTitleSpace(390.5f);
        }
    }

    public void setShrinkParams() {
        calendarParams.height = TransformUtil.dip2px(getActivity(), 90f);
        calendarView.setLayoutParams(calendarParams);
        if (rl_date_control.getVisibility() == View.VISIBLE) {
            rl_date_control.setVisibility(View.GONE);
        }
        miv_more.setRotation(0f);
        if (footprintAdapter != null) {
            footprintAdapter.setTitleSpace(105.5f);
        }
    }


    @Override
    public void OnDateSelect(int position, FootprintEntity.DateInfo dateInfo) {
        if (!footprintAdapter.getEditMode()) {
            return;
        }
        if (dateInfo.isSelect) {
            toSelectDate(dateInfo.date, false);
        } else {
            toSelectDate(dateInfo.date, true);
        }
        checkSelctStatus();
        footprintAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemSelect(int position, FootprintEntity.MarkData markData) {
        if (!footprintAdapter.getEditMode()) {
            GoodsDetailAct.startAct(baseActivity, markDataList.get(position).goods_id);
        } else {
            if (markData.isSelect) {
                delItem(markData.id);
            } else {
                selectItem(markData.id);
            }
            checkSelctStatus();
            footprintAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 选择单项Item
     */

    public void selectItem(String markId) {
        String date = "";
        for (int i = 0; i < markDataList.size(); i++) {
            FootprintEntity.MarkData markData = markDataList.get(i);
            if (markData.id.equals(markId)) {
                markData.isSelect = true;
                date = markData.date_normal;
                delList.add(markData);
                break;
            }
        }
        if (!isEmpty(date)) {
            if (checkSelectDate(date)) {
                setDateStatus(date, true);
            }
        }
    }

    /**
     * 取消选择Item
     */

    public void delItem(String markId) {
        String date = "";
        for (Iterator it = markDataList.iterator(); it.hasNext(); ) {
            FootprintEntity.MarkData markData = (FootprintEntity.MarkData) it.next();
            if (markData.id.equals(markId)) {
                markData.isSelect = false;
                delList.remove(markData);
                break;
            }
        }
        if (!isEmpty(date)) {
            setDateStatus(date, false);
        }
    }

    /**
     * 选择/反选当日
     */

    public void toSelectDate(String date, boolean isSelect) {
        for (Iterator it = markDataList.iterator(); it.hasNext(); ) {
            FootprintEntity.MarkData markData = (FootprintEntity.MarkData) it.next();
            if (markData.date_normal.equals(date)) {
                markData.isSelect = isSelect;
                if (isSelect) {
                    delList.add(markData);
                } else {
                    delList.remove(markData);
                }
            }
        }
        setDateStatus(date, isSelect);
    }

    /**
     * 选择/反选所有足迹
     */

    public void toSelectAll(boolean isSelectAll) {
        delList.clear();
        for (Iterator it = markDataList.iterator(); it.hasNext(); ) {
            FootprintEntity.MarkData markData = (FootprintEntity.MarkData) it.next();
            markData.isSelect = isSelectAll;
            if (isSelectAll) {
                delList.add(markData);
            }
        }
        for (FootprintEntity.DateInfo dateInfo : dateInfoList) {
            dateInfo.isSelect = isSelectAll;
        }
    }

    /**
     * 检查当日是否全被选中
     */

    public boolean checkSelectDate(String date) {
        for (int i = 0; i < markDataList.size(); i++) {
            FootprintEntity.MarkData markData = markDataList.get(i);
            if (markData.date_normal.equals(date) && !markData.isSelect) {
                return false;
            }
        }
        return true;
    }

    /**
     * 改变当前日期状态
     */
    public void setDateStatus(String date, boolean isSelect) {
        for (int i = 0; i < dateInfoList.size(); i++) {
            if (dateInfoList.get(i).date.equals(date)) {
                dateInfoList.get(i).isSelect = isSelect;
            }
        }
    }

    /**
     * 检查所选数量改变状态
     */

    public void checkSelctStatus() {
        if (delList.size() == 0) { //全不选
            selectStatus = 2;
            isSelectAll = false;
        } else if (delList.size() == markDataList.size()) {
            selectStatus = 0;
            isSelectAll = true;
        } else {
            selectStatus = 1;
            isSelectAll = false;
        }
        ((MyCollectionAct) baseActivity).setManageState(selectStatus);
    }

    public String getDelIds() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < delList.size(); i++) {
            stringBuffer.append(delList.get(i).id);
            if (i != delList.size() - 1) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public void onDestroy() {
        mCurrentCalendar = null;
        super.onDestroy();
    }
}
